package de.morknet.mdsc3.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandFacade;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.OutputImpl;
import org.eclipse.xtend.type.impl.java.JavaBeansMetaModel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.morknet.mdsc3.ui.wizard.MDSC3ProjectInfo;

public class MDSC3ProjectCreator extends org.eclipse.xtext.ui.wizard.AbstractPluginProjectCreator {

	protected static final String DSL_GENERATOR_PROJECT_NAME = "de.morknet.mdsc3";

	protected static final String MODEL_ROOT = "model";
	protected static final String WORKFLOW_ROOT = "workflow";
	protected static final String GEN_ROOT = "generated";
	protected final List<String> SRC_FOLDER_LIST = ImmutableList.of(MODEL_ROOT, WORKFLOW_ROOT, GEN_ROOT);

	@Override
	protected MDSC3ProjectInfo getProjectInfo() {
		return (MDSC3ProjectInfo) super.getProjectInfo();
	}
	
	@Override
	protected String getModelFolderName() {
		return MODEL_ROOT;
	}
	
	@Override
	protected List<String> getAllFolders() {
        return SRC_FOLDER_LIST;
    }

    @Override
	protected List<String> getRequiredBundles() {
		List<String> result = Lists.newArrayList(super.getRequiredBundles());
		result.add(DSL_GENERATOR_PROJECT_NAME);
		return result;
	}

	@Override
	protected void enhanceProject(final IProject project, final IProgressMonitor monitor) throws CoreException
	{
		OutputImpl output = new OutputImpl();
		output.addOutlet(new Outlet(false, getEncoding(), null, true, project.getLocation().makeAbsolute().toOSString()));

		XpandExecutionContextImpl execCtx = new XpandExecutionContextImpl(output, null);
		execCtx.getResourceManager().setFileEncoding("UTF-8");
		execCtx.registerMetaModel(new JavaBeansMetaModel());

		XpandFacade facade = XpandFacade.create(execCtx);
		facade.evaluate("de::morknet::mdsc3::ui::wizard::MDSC3NewProject::main", getProjectInfo());

		final IJavaProject javaProject = JavaCore.create(project);

		ArrayList<IClasspathEntry> newEntryList = new ArrayList<IClasspathEntry>();

		/*
		 * Set the JVM Version to 1.7
		 */
		final IClasspathEntry jvmVersion = JavaCore.newContainerEntry(
				new Path("org.eclipse.jdt.launching.JRE_CONTAINER"));
		newEntryList.add(jvmVersion);

		/*
		/*
		 * Add the required plugins from MANIFEST.MF to the classpath
		 */
		final IClasspathEntry requiredPlugins = JavaCore.newContainerEntry(
				new Path("org.eclipse.pde.core.requiredPlugins"));
		newEntryList.add(requiredPlugins);

		/*
		 * Add source folders.
		 */
		for(String folder : SRC_FOLDER_LIST)
		{
			IClasspathEntry srcfolder = JavaCore.newSourceEntry(javaProject.getPath().append(folder), null);
			newEntryList.add(srcfolder);
		}

		/*
		 * Convert to array.
		 */
		IClasspathEntry [] newEntries = new IClasspathEntry[newEntryList.size()];
		newEntryList.toArray(newEntries);
		javaProject.setRawClasspath(newEntries, monitor);

		project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}
}
