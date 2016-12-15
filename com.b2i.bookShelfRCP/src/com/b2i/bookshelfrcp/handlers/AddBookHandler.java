 
package com.b2i.bookshelfrcp.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import com.b2international.library.InputBookDialog;
import com.b2international.library.model.Book;
import com.b2international.library.model.Library;

public class AddBookHandler {
	
	@Execute
	public void execute(IEclipseContext workbenchContext, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		System.out.println("In Add Book Handler.");
		InputBookDialog inputBookDialog = new InputBookDialog(shell);
		int returnCode = inputBookDialog.open();
		if(returnCode == Dialog.OK) {
			Book book = inputBookDialog.getBook();
			Library library = (Library) workbenchContext.get("com.b2i.bookshelfrcp.E4LifeCycle.library");
			library.addBook(book);
		}
	}	
}