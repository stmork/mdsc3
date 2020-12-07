/*
 * generated by Xtext
 */
package de.morknet.mdsc3.ui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;

import de.morknet.mdsc3.ui.coloring.SyntaxColoringMapper;
import de.morknet.mdsc3.ui.labeling.MDSC3LabelProvider;

/**
 * Use this class to register components to be used within the IDE.
 */
public class MDSC3UiModule extends de.morknet.mdsc3.ui.AbstractMDSC3UiModule
{
	public MDSC3UiModule(AbstractUIPlugin plugin)
	{
		super(plugin);
	}

	// contributed by org.eclipse.xtext.ui.generator.projectWizard.SimpleProjectWizardFragment
	@Override
	public Class<? extends org.eclipse.xtext.ui.wizard.IProjectCreator> bindIProjectCreator()
	{
		return MDSC3ProjectCreator.class;
	}

	@Override
	public Class<? extends ILabelProvider> bindILabelProvider()
	{
		return MDSC3LabelProvider.class;
	}

	public Class<? extends AbstractAntlrTokenToAttributeIdMapper> bindTokenToAttributeIdMapper()
	{
		return SyntaxColoringMapper.class;
	}
}
