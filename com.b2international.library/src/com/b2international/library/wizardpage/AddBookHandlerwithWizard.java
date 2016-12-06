package com.b2international.library.wizardpage;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.b2international.library.Activator;
import com.b2international.library.model.Book;
import com.b2international.library.model.Library;

/**
 * Handler for adding a book with the help of a multipage wizard.
 * 
 * @author Alina
 *
 */
public class AddBookHandlerwithWizard extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		AddBookWizard addBookWizard = new AddBookWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, addBookWizard);
		int returnCode = wizardDialog.open();
		if(returnCode == Dialog.OK) {
			Book book = addBookWizard.getBook();
			Library library = Activator.getDefault().getModel();
			library.addBook(book);
		}
		return null;
	}

}
