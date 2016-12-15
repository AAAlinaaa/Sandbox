package com.b2i.bookshelfrcp.handlers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import com.b2international.library.model.Library;

public class SaveHandler {
	
	@CanExecute
	public boolean canExecute(EPartService partService) {
		/*if (partService != null) {
			return !partService.getDirtyParts().isEmpty();
		}*/
		return true;
	}

	/*@Execute
	public void execute(EPartService partService) {
		System.out.println("Execute");
		//partService.saveAll(false);
		//Library library = (Library) partService.getActivePart().getContext().get("com.b2i.bookshelfrcp.E4LifeCycle.library");
		//performSerialSave(?);
	}*/
	
	@Execute
	private void performSerialSave(IEclipseContext ctx) {
		Library library = (Library) ctx.get("com.b2i.bookshelfrcp.E4LifeCycle.library");
		System.out.println("In Perform serial save "+library.toString());
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