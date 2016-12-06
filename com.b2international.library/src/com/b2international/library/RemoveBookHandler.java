package com.b2international.library;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.b2international.library.Activator;
import com.b2international.library.model.Book;
import com.b2international.library.model.Library;

/**
 * Default handler of the remove book command.
 * 
 * @author Alina
 *
 */
public class RemoveBookHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		if(selection != null && selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.getFirstElement() instanceof Book) {
				Book book = (Book) ssel.getFirstElement();
				Library library = Activator.getDefault().getModel();
				library.removeBook(book);
			}
		}
		return null;
	}
}
