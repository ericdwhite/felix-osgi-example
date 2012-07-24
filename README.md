# Working with Exploded Bundles

This project enables you to develop OSGi bundles in Eclipse without needing to redeploy
the bundle in the OSGi container as a jar.

This is based on a blog entry by Maksim Sorokin.
  http://maksim.sorokin.dk/it/2011/07/19/maven-apache-felix-easy-development-and-debugging-with-eclipse/

There are two key parts:
 * A Java main 'com.whitewerx.felixosgiexample.runtime.Main'
 * A Maven property 'auto.start.excludeGroupIds'

## Java main 'com.whitewerx.felixosgiexample.runtime.Main'

This is a wrapper around the normal Felix main 'org.apache.felix.main.Main.main'. It configures
one property 'felix.auto.start.1' with a list of PAX URL assembly paths built up from the 
Maven Dependencies as seen in Eclipse.  Note, however this list doesn't include any
bundles discovered in the 'felix.auto.deploy.dir' (typically $CWD/bundle).

The PAX URL assembly allows the bundle to be read from 'target/classes' instead of needing it
to be packaged up a as jar.

## Maven property 'auto.start.excludeGroupIds'

Normally it is desirable to have all the true third party jars copied to the 'felix.auto.deploy.dir'.
This property controls which artefacts from the dependency list are EXCLUDED from being copied to the
'felix.auto.deploy.dir'.

In this sample project it is set to

  <properties>
    <auto.start.excludeGroupIds>com.whitewerx.felix-osgi-example.lib</auto.start.excludeGroupIds>
  </properties>

This means that any artifact with the 'com.whitewerx.felix-osgi-example.lib' will not be copied into
the 'felix.auto.deploy.dir' and is thus a target for a PAX assembly URL.

## Felix configuration

There is one property to be configured.

  org.osgi.framework.storage.clean=onFirstInit

See resources for a list of Felix configuration properties.

# Launching

Included in the sample is an Eclipse launcher 'felix-osgi-example-runtime.launch'.  This can be used
to run the sample once a 'maven install' build has occurred at the top level.

The key properties of the launcher are:

  Main: com.whitewerx.felixosgiexample.runtime.Main
  VM Arguments: -Djava.protocol.handler.pkgs=org.ops4j.pax.url -Dfelix.config.properties=file:conf/config.properties
  Working Directory: ${workspace_loc:felix-osgi-example.runtime/target/felix}

The most important is '-Djava.protocol.handler.pkgs=org.ops4j.pax.url' which in conjunction with a Bundle Activator
included in 'pax-url-assembly' jar tells Felix how to read bundles from assembly URLs.

Launching from Eclipse looks like:

  [BOOT] Excluding from assembly: file:~/github/felix-osgi-example/runtime/target/classes/
  [BOOT] Excluding from assembly: file:~/.m2/repository/org/osgi/org.osgi.core/4.3.0/org.osgi.core-4.3.0.jar
  [BOOT] Excluding from assembly: file:~/.m2/repository/org/apache/felix/org.apache.felix.main/4.0.3/org.apache.felix.main-4.0.3.jar
  …
  [BOOT] Excluding from assembly: file:~/.m2/repository/ch/qos/logback/logback-classic/1.0.6/logback-classic-1.0.6.jar
  [BOOT] Excluding from assembly: file:~/.m2/repository/ch/qos/logback/logback-core/1.0.6/logback-core-1.0.6.jar
  [BOOT] Felix auto start:
  felix.auto.start.1 = \
   \
    assembly:~/github/felix-osgi-example/lib/target/classes/
  [BOOT] Actual property:
  felix.auto.start.1= assembly:~/github/felix-osgi-example/lib/target/classes/
  08:04:37.136 [FelixStartLevel] INFO  c.w.felixosgiexample.Activator - Start: com.whitewerx.felixosgiexample.Activator
  08:04:37.140 [FelixStartLevel] INFO  c.w.felixosgiexample.Activator - The chemical formula for Water is: H2O
  08:04:37.385 [FelixStartLevel] DEBUG o.o.p.u.c.handler.HandlerActivator - Handler for protocols [assembly, assemblyref] started
  ____________________________
  Welcome to Apache Felix Gogo
  
  g!

Note the PAX assembly URL for felix-osgi-example/lib!

# Packaging

If the auto.start.excludeGroupIds is left blank then all the bundles required by the
runtime will be copied to the bundle folder.  This means enables Felix to be launched
directly and all the bundles resolved.

Notice the BundleActivator start/stop calls for the felix-osgi-example.lib bundle.

  felix-osgi-example $ mvn clean install -Dauto.start.excludeGroupIds=

  felix-osgi-example $ cd runtime/target/felix
  felix $ java -jar lib/org.apache.felix.main-4.0.3.jar org.apache.felix.main.Main

    20:25:10.682 [FelixStartLevel] INFO  c.w.felixosgiexample.Activator - Start: com.whitewerx.felixosgiexample.Activator
    20:25:10.687 [FelixStartLevel] INFO  c.w.felixosgiexample.Activator - The chemical formula for Water is: H2O
    20:25:10.938 [FelixStartLevel] DEBUG o.o.p.u.c.handler.HandlerActivator - Handler for protocols [assembly, assemblyref] started
    ____________________________
    Welcome to Apache Felix Gogo
    g! 
    g! ^D 
    g! gosh: stopping framework
    20:25:39.853 [FelixStartLevel] DEBUG o.o.p.u.c.handler.HandlerActivator - Handler for protocols [assembly, assemblyref] stopped
    20:25:39.858 [FelixStartLevel] INFO  c.w.felixosgiexample.Activator - Stop: com.whitewerx.felixosgiexample.Activator

  felix$

# Resources

 * http://felix.apache.org/site/apache-felix-framework-configuration-properties.html
 * http://maksim.sorokin.dk/it/2011/07/19/maven-apache-felix-easy-development-and-debugging-with-eclipse/
 * http://team.ops4j.org/wiki/pages/viewpage.action?pageId=12648754
