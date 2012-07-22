package com.whitewerx.felixosgiexample;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whitewerx.felixosgiexample.domain.Water;

public class Activator implements BundleActivator {
	
	private static Logger l = LoggerFactory.getLogger(Activator.class);

	public void start(BundleContext context) throws Exception {
		l.info("Start: "+this.getClass().getName());
		l.info("The chemical formula for Water is: " + new Water().chemicalFormula());
	}

	public void stop(BundleContext context) throws Exception {
		l.info("Stop: "+this.getClass().getName());
	}
}
