package com.b2international.library;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.b2international.library.model.Book;
import com.b2international.library.model.Library;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Alina
 *
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.b2international.library"; //$NON-NLS-1$
	public static final String BOOK_IMAGE_KEY = "BOOK_IMAGE_KEY";
	public static final String AUTHOR_IMAGE_KEY = "AUTHOR_IMAGE_KEY";
	public static final String WIZARD_IMAGE_KEY = "WIZARD_IMAGE_KEY";

	private static Activator plugin; // The shared instance
	private Library library;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		ImageDescriptor bookImageDescriptor = imageDescriptorFromPlugin(PLUGIN_ID, "/icons/book-open.png");
		reg.put(BOOK_IMAGE_KEY, bookImageDescriptor);
		ImageDescriptor authorImageDescriptor = imageDescriptorFromPlugin(PLUGIN_ID, "/icons/user-black-female.png");
		reg.put(AUTHOR_IMAGE_KEY, authorImageDescriptor);
		ImageDescriptor wizardImageDescriptor = imageDescriptorFromPlugin(PLUGIN_ID, "/icons/bluebookbanner.png");
		reg.put(WIZARD_IMAGE_KEY, wizardImageDescriptor);
	}

	/*
	 * Attempts to read in library object from json file. If file is not found
	 * or can't be parsed a new library object is created. (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		String rootAddress = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		String filename = rootAddress + "/library.json";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			library = objectMapper.readValue(new File(filename), Library.class);
			library.initializeObservers();
		} catch (FileNotFoundException e) {
			library = new Library("Public Library", new ArrayList<Book>());
		} catch (Exception e) {
			library = new Library("Public Library", new ArrayList<Book>());
			Shell shell = this.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog dialog = new MessageDialog(shell, "Library Read In Failure", null,
					"An error occured before or while reading in the library. New Library has been initialized instead.",
					MessageDialog.ERROR, new String[] { "OK" }, 0);
			dialog.open();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public Library getModel() {
		return library;
	}
}