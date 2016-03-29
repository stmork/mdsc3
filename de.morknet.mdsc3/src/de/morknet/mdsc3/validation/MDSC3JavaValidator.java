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

package de.morknet.mdsc3.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;

import de.morknet.mdsc3.mdsc3.APC;
import de.morknet.mdsc3.mdsc3.CPU;
import de.morknet.mdsc3.mdsc3.Controller;
import de.morknet.mdsc3.mdsc3.Host;
import de.morknet.mdsc3.mdsc3.HostDevice;
import de.morknet.mdsc3.mdsc3.HostService;
import de.morknet.mdsc3.mdsc3.IPMI;
import de.morknet.mdsc3.mdsc3.JK;
import de.morknet.mdsc3.mdsc3.Machine;
import de.morknet.mdsc3.mdsc3.Mail;
import de.morknet.mdsc3.mdsc3.Mdsc3Package;
import de.morknet.mdsc3.mdsc3.NIC;
import de.morknet.mdsc3.mdsc3.Network;
import de.morknet.mdsc3.mdsc3.OS;
import de.morknet.mdsc3.mdsc3.Postfix;
import de.morknet.mdsc3.mdsc3.SMART;
import de.morknet.mdsc3.mdsc3.TWI;
import de.morknet.mdsc3.mdsc3.VM;
import de.morknet.mdsc3.util.ExtensionFacade;

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
public class MDSC3JavaValidator extends de.morknet.mdsc3.validation.AbstractMDSC3JavaValidator {

	@Check(CheckType.FAST)
	public void fastCheckCpu(final CPU cpu)
	{
		if (cpu.getCores() <= 0)
		{
			error("Auf " + getMachine(cpu).getName() + " sind nur CPU-Mengen größer 0 erlaubt!", Mdsc3Package.Literals.CPU__CORES);
		}
	}

	@Check(CheckType.FAST)
	public void fastCheckNetwork(final Network network)
	{
		if (network.getBits() < 0)
		{
			error(String.format("Die Maskenbreite im Netzwerk %s muss posititv sein!", network.getName()),
					Mdsc3Package.Literals.NETWORK__BITS);
		}
		if (network.getBits() > 32)
		{
			error(String.format("Die Maskenbreite im Netzwerk %s muss kleiner 32 bits sein sein!", network.getName()),
					Mdsc3Package.Literals.NETWORK__BITS);
		}
	}

	@Check(CheckType.FAST)
	public void fastCheckHost(final Host host)
	{
		if (host.getParent() != null)
		{
			int index = 0;

			for (HostService service :host.getServices())
			{
				if (service instanceof SMART)
				{
					warning("Der virtuelle Gast " + host.getName() + " braucht kein SMART!",
							Mdsc3Package.Literals.HOST__SERVICES, index);
				}
				else if (service instanceof VM)
				{
					error("Ein virtualisierter Rechner (" + host.getName() + ") kann nicht gleichzeitig Wirt sein!",
							Mdsc3Package.Literals.HOST__SERVICES, index);
				}
				index++;
			}
		}
	}

	@Check(CheckType.FAST)
	public void fastCheckJk(final JK jk)
	{
		final String context = jk.getMountpoint();

		if (!context.startsWith("/"))
		{
			error("Der Mountpoint '" + context + "' muss mit '/' beginnen!", Mdsc3Package.Literals.JK__MOUNTPOINT);
		}
	}
	
	@Check(CheckType.FAST)
	public void fastCheckNic(final NIC nic)
	{
		final String hostname = getMachine(nic).getName();

		if (nic.isBonded())
		{
			final int mode = nic.getMode();
			
			if ((mode < 0) || (mode > 6))
			{
				error("Host " + getMachine(nic).getName() + ": Die Bonding-Modes müssen zwischen 0 und 6 liegen!", Mdsc3Package.Literals.NIC__MODE);
			}
			
			if (nic.isBridged())
			{
				error("Host " + hostname + ": Bonds können nicht gleichzeigt gebridged sein!",
						Mdsc3Package.Literals.NIC__BONDED);
				error("Host " + hostname + ": Bonds können nicht gleichzeigt gebridged sein!",
						Mdsc3Package.Literals.NIC__BRIDGED);
			}
		}

		if (nic.getNetwork() != null)
		{
			if((nic.getIpaddr() == null) && (!nic.isDhcp()))
			{
				 error("Host " + hostname + ": Wenn kein DHCP verwendet wird, muss eine IP-Adresse definiert sein!",
						 Mdsc3Package.Literals.NIC__IPADDR);
			}
			if (nic.getIpaddr() != null)
			{
				final Network network   = nic.getNetwork();
				final String nicNetAddr = ExtensionFacade.getNetworkAddress(network.getAddress(), network.getBits());
				final String ipNetAddr  = ExtensionFacade.getNetworkAddress(nic.getIpaddr(), network.getBits());

				if (!nicNetAddr.equals(ipNetAddr))
				{
					error(String.format("Host %s: Netzwerk %s passt nicht zu IP-Adresse %s!", hostname, network.getName(),nic.getIpaddr()),
							Mdsc3Package.Literals.NIC__NETWORK);
					error(String.format("Host %s: IP-Adresse %s passt nicht zu Netzwerk %s!", hostname, nic.getIpaddr(), network.getName()),
							Mdsc3Package.Literals.NIC__IPADDR);
				}
			}
		}
	}
	
	@Check(CheckType.FAST)
	public void fastCheckIpmi(final IPMI ipmi)
	{
		final Network network   = ipmi.getNetwork();
		final String nicNetAddr = ExtensionFacade.getNetworkAddress(network.getAddress(), network.getBits());
		final String ipNetAddr  = ExtensionFacade.getNetworkAddress(ipmi.getIpaddr(), network.getBits());

		if (!nicNetAddr.equals(ipNetAddr))
		{
			final String hostname = getMachine(ipmi).getName();

			error(String.format("Host %s: Netzwerk %s passt nicht zu IP-Adresse %s!", hostname, network.getName(),ipmi.getIpaddr()),
					Mdsc3Package.Literals.IPMI__NETWORK);
			error(String.format("Host %s: IP-Adresse %s passt nicht zu Netzwerk %s!", hostname, ipmi.getIpaddr(), network.getName()),
					Mdsc3Package.Literals.IPMI__IPADDR);
		}
	}

	@Check
	public void checkHost(final Host host)
	{
		final Host parent = host.getParent();
		final List<IPMI> ipmiList = getDevices(host, IPMI.class);

		if (parent != null)
		{
			if (!isVirtualServer(parent))
			{
				error("Der Wirt " + parent.getName() + " muss Virtualisierungs-Software haben!",
						Mdsc3Package.Literals.HOST__PARENT);
			}

			for (HostDevice device : getDevices(host, Controller.class))
			{
				warning("Der virtuelle Host " + host.getName() + " braucht keinen HD-Controller!", device, 
						Mdsc3Package.Literals.HOST__DEVICES, 0);
			}

			for (IPMI ipmi : ipmiList)
			{
				error("Gast " + host.getName() + " kann keine IPMI interfaces besitzen!", ipmi,
						Mdsc3Package.Literals.HOST__DEVICES, 0);
			}
		}

		if (getConnectedNICs(host).size() == 0)
		{
			error("Mindestens eine Netzwerkkarte muss für " + host.getName() + " konfiguriert sein!",
					Mdsc3Package.Literals.HOST__DEVICES);
		}

		if ((host.getName().compareToIgnoreCase("localhost") != 0) && (host.getParent() == null) && (host.getOs() != OS.DUMMY))
		{
			if (!hasDevice(host, CPU.class))
			{
				warning("Eine CPU muss für " + host.getName() + " konfiguriert sein!",
						Mdsc3Package.Literals.HOST__DEVICES);
			}
		}

		if (ipmiList.size() > 1)
		{
			for (IPMI ipmi : ipmiList)
			{
				error("Host " + host.getName() + " kann nicht mehrere IPMI-Interfaces besitzen!", ipmi,
						Mdsc3Package.Literals.HOST__DEVICES, 0);
			}
		}

		for (TWI twi : getDevices(host, TWI.class))
		{
			if (!hasService(host, Mail.class))
			{
				warning("Host " + host.getName() + " mit 3ware-Controller sollte einen Mailserver haben", twi, 
						Mdsc3Package.Literals.HOST__DEVICES, 0);
			}
		}
	}

	@Check
	public void checkApc(final APC apc)
	{
		final Host host   = getHost(apc);
		final Host parent = host.getParent();

		if (parent != null)
		{
			if (apc.getHost() == null)
			{
				error("Gast " + host.getName() + " kann keine UPS steuern!", Mdsc3Package.Literals.APC__HOST);
			}
			final APC parentApc = getService(parent, APC.class);
			Host parentApcHost = parentApc.getHost();
			if (parentApcHost == null)
			{
				parentApcHost = parent;
			}

			if (apc.getHost() != parentApcHost)
			{
				error("Wirt " + parent.getName() + " und Gast " + host.getName() + " müssen dieselbe UPS benutzen!", Mdsc3Package.Literals.APC__HOST);
			}
			else
			{
				if ((parentApc.getDelay() - apc.getDelay()) < 120)
				{
					warning("UPS an Wirt " + parent.getName() + " muss mindestens 120s länger aktiv bleiben als Gast " + host.getName() + "!",
							Mdsc3Package.Literals.APC__DELAY);
				}
			}
		}

		final Host apcMaster = apc.getHost();

		if (apcMaster != null)
		{
			final APC apcController = getService(apcMaster, APC.class);
			
			if (apcController == null)
			{
				error("APC-Slave " + host.getName() + " hat den APC-Master " + apcMaster.getName() + " konfiguriert, der nicht an eine APC angeschlossen ist!",
						Mdsc3Package.Literals.APC__HOST); 
			}
			else if (apcController.getHost() != null)
			{
				error("APC-Slave " + host.getName() + " darf nicht auf anderen APC-Slave " + apcMaster.getName() + " konfiguriert werden!",
						Mdsc3Package.Literals.APC__HOST); 
			}
		}
	}

	@Check
	public void checkPostfix(final Postfix postfix)
	{
		final Host relay = postfix.getRelay();

		if ((relay != null) && (!hasService(relay, Mail.class)))
		{
			error("Der auf " + getHost(postfix).getName() + " definierte Relay-Server " + relay.getName() + " sollte ein Mailserver sein!",
					Mdsc3Package.Literals.POSTFIX__RELAY);
		}
	}

	private static <T extends HostService> T getService(final Host host, final Class<T> cls)
	{
		final List<T> services = EcoreUtil2.typeSelect(host.getServices(), cls);
		
		return services.isEmpty() ? null : services.get(0);
	}

	private static <T extends HostService> boolean hasService(final Host host, final Class<T> cls)
	{
		return !EcoreUtil2.typeSelect(host.getServices(), cls).isEmpty();
	}

	private final static Map<Host, List<NIC> > nicCache = new HashMap<Host, List<NIC> >();

	private static List<NIC> getConnectedNICs(final Host host)
	{
		List<NIC> nics = nicCache.get(host);

		if (nics == null)
		{
			nics = new ArrayList<NIC>();
			for(NIC nic : getDevices(host, NIC.class))
			{
				if (nic.getNetwork() != null)
				{
					nics.add(nic);
				}
			}
			nicCache.put(host, nics);
		}
		return nics;
	}

	private static <T extends HostDevice> List<T> getDevices(final Host host, final Class<T> cls)
	{
		return EcoreUtil2.typeSelect(host.getDevices(), cls);
	}

	private static <T extends HostDevice> boolean hasDevice(final Host host, final Class<T> cls)
	{
		return !EcoreUtil2.typeSelect(host.getDevices(), cls).isEmpty();
	}

	private static boolean isVirtualServer(final Host host)
	{
		return hasService(host, VM.class);
	}

	private static Machine getMachine(final HostDevice device)
	{
		return (Machine)device.eContainer();
	}

	private static Host getHost(final HostService service)
	{
		return (Host)service.eContainer();
	}
}
