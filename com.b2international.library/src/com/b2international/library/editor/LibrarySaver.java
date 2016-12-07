package com.b2international.library.editor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.b2international.library.Activator;
import com.b2international.library.model.Library;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Library Saver helps perform saves of the current
 * library model to memory.
 * 
 * @author Alina
 */
public class LibrarySaver {

	public static void performSerialSave() {
		Library library = Activator.getDefault().getModel();
		String rootAddress = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		String filename = rootAddress + "/library.txt";
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(filename);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(library);
			fileOutputStream.close();
			objectOutputStream.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public static void performJSONSave() {
		String rootAddress = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		String filename = rootAddress + "/library.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
		try {
			ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
			writer.writeValue(new FileOutputStream(filename), Activator.getDefault().getModel());
		} catch (Exception e) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog dialog = new MessageDialog(shell, "Library Write Failure", null,
				    "Save was unsuccessful.", MessageDialog.ERROR, new String[] { "OK" }, 0);
			dialog.open();
		}
	}
}