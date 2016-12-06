package com.b2international.library.databindingwizard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Handler for the add book with databinding command.
 * 
 * @author Alina
 *
 */
public class NewAddBookHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		AddBookWizard addBookWizard = new AddBookWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, addBookWizard);
		wizardDialog.open();
		return null;
	}

}