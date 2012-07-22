# Working with Exploded Bundles

This project enable you to develop in Eclipse OSGi bundles without needing to redeploy
the bundle in the OSGi container as a jar.

This is based on a blog entry by Maksim Sorokin.
  http://maksim.sorokin.dk/it/2011/07/19/maven-apache-felix-easy-development-and-debugging-with-eclipse/


# Packaging and Testing

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
