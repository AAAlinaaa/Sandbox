package com.b2international.library;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.b2international.library.model.Library;

/**
 * Default handler of the save library command.
 * 
 * @author Alina
 *
 */
public class SaveLibraryHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Library library = Activator.getDefault().getModel();
		String rootAddress = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		String filename = rootAddress + "/library.txt";
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
			objectOutputStream.writeObject(library);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
}