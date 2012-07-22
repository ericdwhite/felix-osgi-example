package com.whitewerx.felixosgiexample.runtime;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Wraps the Felix launcher and sets <code>felix.auto.start.1</code> to a list
 * of assembly URL's based on the Maven Dependencies.
 * 
 * @author ewhite
 */
public class Main {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * An array of partial paths used to remove items from being auto started,
	 * namely those provided by the Felix or this launcher. These can be partial
	 * names or paths.
	 */
	//@fmt:off
	private static final String[] ASSEMBLY_LAUNCH_EXCLUSIONS = new String[] {
		"org.apache.felix.main",
		"org.apache.felix.framework",
		"runtime/target/classes" // This project.
	};
	//@fmt:on

	/**
	 * These are matched against the jar names on the Maven Dependencies path.
	 */
	private static Set<String> assemblyBundleExclusions = new HashSet<String>();

	/**
	 * This starts felix with an <code>felix.auto.start.1</code> property set
	 * from the classpath.
	 * <p>
	 * See: http://felix.apache.org/site/apache-felix-framework-configuration-
	 * properties.html
	 * </p>
	 */
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.launch(args);
	}

	private void launch(String... args) throws Exception {

		determineBundlesToExclude();

		configureFelixAutoStart();

		launchFelix(args);
	}

	private void launchFelix(String... args) throws Exception {
		org.apache.felix.main.Main.main(args);
	}

	/**
	 * Adds the bundles in the <code>"felix.auto.deploy.dir"</code> to the list
	 * of dependencies that should not have assembly URL's.
	 */
	private void determineBundlesToExclude() {
		Properties felixConfig = org.apache.felix.main.Main.loadConfigProperties();
		String autoDeployDir = felixConfig.getProperty("felix.auto.deploy.dir");
		if (autoDeployDir == null)
			autoDeployDir = "bundle";

		File[] jars = new File(autoDeployDir).listFiles();
		for (File jar : jars) {
			assemblyBundleExclusions.add(jar.getName());
		}
	}

	/**
	 * Configures Felix auto start with a list of assemblies built form Maven
	 * dependencies.
	 */
	private void configureFelixAutoStart() {
		StringBuilder b = new StringBuilder();
		for (String assemblyURL : getAssemblyURLs()) {
			b.append(" ");
			b.append(assemblyURL);
		}
		String allAssemblies = b.toString();

		String felixAutoStart = "felix.auto.start.1 = \\" + LINE_SEPARATOR;
		felixAutoStart += allAssemblies.replace(" ", " \\" + LINE_SEPARATOR + "  ");

		info("Felix auto start:" + LINE_SEPARATOR + felixAutoStart);
		info("Actual property:" + LINE_SEPARATOR + "felix.auto.start.1=" + allAssemblies);
		System.setProperty("felix.auto.start.1", allAssemblies);
	}

	private void info(String message) {
		System.out.println("[BOOT] " + message);
	}

	/**
	 * PAX reads assembly URL's derived from the Maven Dependencies.
	 * <p>
	 * See: http://team.ops4j.org/wiki/pages/viewpage.action?pageId=12648754
	 * </p>
	 */
	private List<String> getAssemblyURLs() {
		List<String> assemblies = new ArrayList<String>();
		ClassLoader applicationClassLoader = this.getClass().getClassLoader();
		URL[] urls = ((URLClassLoader) applicationClassLoader).getURLs();

		for (URL url : urls) {
			if (isExcludedUrl(url))
				continue;
			String assembly = "assembly:" + url.getFile();
			assemblies.add(assembly);
		}
		return assemblies;
	}

	/**
	 * Excludes items that are already in the
	 * <code>"felix.auto.deploy.dir"</code> directory, or partials defined for
	 * the launcher (See: {@link #ASSEMBLY_LAUNCH_EXCLUSIONS}.
	 * 
	 * @param url
	 *            of a Maven dependency.
	 * @return true if the dependency should be excluded form the assembly list.
	 */
	private boolean isExcludedUrl(URL url) {
		String urlPath = url.getFile();

		// These could be partials
		for (String excluded : ASSEMBLY_LAUNCH_EXCLUSIONS) {
			if (urlPath.contains(excluded)) {
				info("Excluding from assembly: " + url);
				return true;
			}
		}

		// Compare the bundle auto deploy directory to the Maven dependency
		String jarName = new File(urlPath).getName();
		if (assemblyBundleExclusions.contains(jarName)) {
			info("Excluding from assembly: " + url);
			return true;
		}
		return false;
	}
}
