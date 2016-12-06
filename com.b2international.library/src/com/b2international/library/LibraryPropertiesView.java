package com.b2international.library;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;

import com.b2international.library.model.Book;

/**
 * A custom properties view and an extension of the built-in 
 * properties view of the library
 *  
 * @author Alina
 *
 */
public class LibraryPropertiesView extends ViewPart implements ISelectionListener, IPropertySheetPage {

	private TableViewer tableViewer;

	@Override
	public void createPartControl(Composite parent) {
		// 1. Identify the input
		// Library library = Activator.getDefault().getModel();
		// 2. Create the viewer
		tableViewer = new TableViewer(parent, SWT.NONE);
		// 3. Create a Content provider
		// 5. Connect the viewer with the content provider
		tableViewer.setContentProvider(new LibraryContentProvider());
		// 4.Create a Label Provider
		// 5. Connect the viewer with the content provider
		tableViewer.setLabelProvider(new LibraryLabelProvider());

		// Listen to all shared selections
		getSite().getPage().addSelectionListener(this);
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// Listen to selections from other views
		if ((part != this) && (selection instanceof IStructuredSelection)) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.getFirstElement() instanceof Book) {
				tableViewer.setInput(ssel.getFirstElement());
			}
		}
	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(this);
		super.dispose();
	}

	@Override
	public void createControl(Composite parent) {
	}

	@Override
	public Control getControl() {
		return null;
	}

	@Override
	public void setActionBars(IActionBars actionBars) {
	}
}
