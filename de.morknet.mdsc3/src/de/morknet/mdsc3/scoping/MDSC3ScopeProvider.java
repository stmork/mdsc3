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

package de.morknet.mdsc3.scoping;

import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;
import org.eclipse.xtext.scoping.impl.SimpleScope;

import de.morknet.mdsc3.mdsc3.Host;
import de.morknet.mdsc3.mdsc3.NIC;

/**
 * This class contains custom scoping description.
 * 
 * see : http://wiki.eclipse.org/Xtext/Documentation#Scoping
 * on how and when to use it 
 *
 */
public class MDSC3ScopeProvider extends AbstractDeclarativeScopeProvider
{
	public IScope scope_NIC_ifaces(NIC nic, EReference ref)
	{
		final Host host = (Host)nic.eContainer();
		final List<NIC> elements = EcoreUtil2.typeSelect(host.getDevices(), NIC.class);

		elements.remove(nic);

		return new SimpleScope(Scopes.scopedElementsFor(elements));
	}
}
