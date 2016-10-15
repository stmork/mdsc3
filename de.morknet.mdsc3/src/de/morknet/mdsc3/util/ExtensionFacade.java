/*
**
**	$Revision$
**	$Date$
**	$Author$
**	$Id$
**
**	Copyright (C) 2016 Steffen A. Mork
**
**	This program and the accompanying materials are made available under the
**	terms of the Eclipse Public License v1.0.
**
**	The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
**
**
*/

package de.morknet.mdsc3.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.morknet.mdsc3.crypto.SSHA;


public class ExtensionFacade {
	private final static DateFormat fmt  = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private final static DateFormat year = new SimpleDateFormat("yyyy");

	private final static String[] masks =
	{
		"0.0.0.0",
		"128.0.0.0",
		"192.0.0.0",
		"224.0.0.0",
		"240.0.0.0",
		"248.0.0.0",
		"252.0.0.0",
		"254.0.0.0",
		"255.0.0.0",
		"255.128.0.0",
		"255.192.0.0",
		"255.224.0.0",
		"255.240.0.0",
		"255.248.0.0",
		"255.252.0.0",
		"255.254.0.0",
		"255.255.0.0",
		"255.255.128.0",
		"255.255.192.0",
		"255.255.224.0",
		"255.255.240.0",
		"255.255.248.0",
		"255.255.252.0",
		"255.255.254.0",
		"255.255.255.0",
		"255.255.255.128",
		"255.255.255.192",
		"255.255.255.224",
		"255.255.255.240",
		"255.255.255.248",
		"255.255.255.252",
		"255.255.255.254",
		"255.255.255.255"
	};

	/**
	 * This method maps a hostname into an IP address (forward loopup).
	 * @param hostname The hostname to lookup.
	 * @return The resulting IP address.
	 * @throws UnknownHostException 
	 */
	public static String asIpAddress(final String hostname) throws UnknownHostException
	{
		String ip;
		InetAddress ia;

		ia = InetAddress.getByName(hostname);
		ip = ia.getHostAddress();

		return ip;
	}

	/**
	 * This method maps an IP address into a hostname (reverse lookup).
	 * @param ip The IP address of the host.
	 * @return The resulting hostname.
	 * @throws UnknownHostException 
	 */
	public static String asHostname(final String ip) throws UnknownHostException
	{
		return InetAddress.getByName(ip).getHostName();
	}

	/**
	 * This method computes a net mask von the amount of mask bits.
	 * @param bits The net mask bit count
	 * @return The net mask as String.
	 */
	public static String getNetmask(final Integer bits)
	{
		String ip;
		InetAddress ia;

		try
		{
			ia = InetAddress.getByName(masks[bits]);
			ip = ia.getHostAddress();
		}
		catch (UnknownHostException e)
		{
			ip = null;
		}
		return ip;
	}
	
	/**
	 * This method computes the network address from the gateway
	 * address and the networks mask bits. In fact this method
	 * clears all bits of the gateway address left of the mask
	 * bits.
	 *
	 * @param gw The IP address of the gateway.
	 * @param bits The networks mask bits.
	 * @return The network address of the gateway.
	 */
	public static String getNetworkAddress(
		final String gw,
		final Integer bits)
	{
		String nw;
		InetAddress ia;

		try
		{
			InetAddress nm = InetAddress.getByName(masks[bits]);
			InetAddress ip = InetAddress.getByName(gw);

			byte nm_quads[] = nm.getAddress();
			byte ip_quads[] = ip.getAddress();
			byte nw_quads[] = new byte[ip_quads.length];
			for (int i = 0;i < ip_quads.length;i++)
			{
				nw_quads[i] = (byte)(nm_quads[i] & ip_quads[i]);
			}
			ia = InetAddress.getByAddress(nw_quads);
			nw = ia.getHostAddress();
		}
		catch (UnknownHostException e)
		{
			nw = "";
		}
		return nw;
	}

	/**
	 * This method computes the broadcast address from the gateway
	 * address and the networks mask bits. In fact this method
	 * sets all bits of the gateway address left of the mask
	 * bits.
	 *
	 * @param gw The IP address of the gateway.
	 * @param bits The networks mask bits.
	 * @return The broadcast address of the gateway.
	 */
	public static String getBroadcastAddress(
		final String gw,
		final Integer bits)
	{
		String nw;
		InetAddress ia;

		try
		{
			InetAddress nm = InetAddress.getByName(masks[bits]);
			InetAddress ip = InetAddress.getByName(gw);

			byte nm_quads[] = nm.getAddress();
			byte ip_quads[] = ip.getAddress();
			byte nw_quads[] = new byte[ip_quads.length];
			for (int i = 0;i < ip_quads.length;i++)
			{
				nw_quads[i] = (byte)(ip_quads[i] | (nm_quads[i] ^ 0xff));
			}
			ia = InetAddress.getByAddress(nw_quads);
			nw = ia.getHostAddress();
		}
		catch (UnknownHostException e)
		{
			nw = "";
		}
		return nw;
	}

	public static String cryptSSHA(final String credential)
	{
		return SSHA.getLDAPSSHAHash(credential, null);
	}
	
	public static String now()
	{
		return fmt.format(new Date());
	}
	
	public static String year()
	{
		return year.format(new Date());
	}

	public static String leftPad(final String val)
	{
		String whole = "          " + val;
		return whole.substring(whole.length() - 10);
	}
}
