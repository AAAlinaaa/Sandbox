package com.b2international.library.editor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.eclipse.core.resources.ResourcesPlugin;

import com.b2international.library.Activator;
import com.b2international.library.model.Library;

/**
 * Library Saver helps perform saves of the current
 * library model to memory.
 * 
 * @author Alina
 */
public class LibrarySaver {

	public static void performSave() {
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
}