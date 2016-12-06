package com.b2international.library;

import java.io.FileOutputStream;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class SaveLibrary2JSONLHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		String rootAddress = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		String filename = rootAddress + "/library.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
		try {
			ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
			writer.writeValue(new FileOutputStream(filename), Activator.getDefault().getModel());
			System.out.println("We seem to have written to json fine.");
		} catch (Exception e) {
			Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
			MessageDialog dialog = new MessageDialog(shell, "Library Write Failure", null,
				    "Save was unsuccessful.", MessageDialog.ERROR, new String[] { "OK" }, 0);
			dialog.open();
		}
		return null;
	}
}