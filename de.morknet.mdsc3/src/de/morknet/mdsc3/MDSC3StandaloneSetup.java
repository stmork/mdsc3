/*
 * generated by Xtext
 */
package de.morknet.mdsc3;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class MDSC3StandaloneSetup extends MDSC3StandaloneSetupGenerated{

	public static void doSetup() {
		new MDSC3StandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

