package com.b2international.library;

import org.eclipse.core.commands.*;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.b2international.library.model.Book;
import com.b2international.library.model.Library;

/**
 * Handler for adding a book with an input dialog.
 * 
 * @author Alina
 *
 */
public class AddBookHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		InputBookDialog inputBookDialog = new InputBookDialog(shell);
		int returnCode = inputBookDialog.open();
		
		if(returnCode == Dialog.OK) {
			Book book = inputBookDialog.getBook();
			Library library = Activator.getDefault().getModel();
			library.addBook(book);
		}
		return null;
	}
}