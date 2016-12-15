package com.b2i.bookshelfrcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.inject.Named;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.b2international.library.model.Book;
import com.b2international.library.model.Library;

/**
 * This is a stub implementation containing e4 LifeCycle annotated methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the
 * <em>org.eclipse.core.runtime.products' extension point</em>) that references
 * this class.
 **/
@SuppressWarnings("restriction")
public class E4LifeCycle {

	@PostContextCreate
	void postContextCreate(IEclipseContext workbenchContext, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		System.out.println("In post Context Create ");
		Library library;
		String rootAddress = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		String filename = rootAddress + "/library.txt";
		ObjectInputStream inputStream;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(new File(filename)));
			library = (Library) inputStream.readObject();
			inputStream.close();
			library.initializeObservers();
		} catch (Exception e) {
			library = new Library("Public Library", new ArrayList<Book>());
			MessageDialog dialog = new MessageDialog(shell, "Library Read In Failure", null,
					"An error occured before or while reading in the library. New Library has been initialized instead.",
					MessageDialog.ERROR, new String[] { "OK" }, 0);
			System.out.println("Error dialog?");
			dialog.open();
		}
		workbenchContext.set("com.b2i.bookshelfrcp.E4LifeCycle.library", library);
	}
	
	@PreSave
	void preSave(IEclipseContext workbenchContext) {
	}

	@ProcessAdditions
	void processAdditions(IEclipseContext workbenchContext) {
	}

	@ProcessRemovals
	void processRemovals(IEclipseContext workbenchContext) {
	}
}
