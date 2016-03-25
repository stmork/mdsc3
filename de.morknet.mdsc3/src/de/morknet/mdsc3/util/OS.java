package de.morknet.mdsc3.util;

public class OS {
	private final static int    UBUNTU_LEN = "Ubuntu_".length();

	public static double getUbuntuVersion(String os)
	{
		return Double.valueOf(os.substring(UBUNTU_LEN).replace('_', '.'));
	}
}
