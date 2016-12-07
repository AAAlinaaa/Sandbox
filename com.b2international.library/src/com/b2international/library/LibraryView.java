package com.b2international.library;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.b2international.library.editor.BookEditorInput;
import com.b2international.library.model.Book;
import com.b2international.library.model.Library;
import com.b2international.library.model.LibraryObserver;

/**
 * A View for the Library model using a TreeViewer
 * @author Alina
 *
 */
public class LibraryView extends ViewPart implements LibraryObserver, IDoubleClickListener {

	//private static final String ID_OF_THE_EDITOR = "com.b2international.library.bookeditor";
	private static final String ID_OF_THE_EDITOR = "com.b2international.library.bookeditorwithform";
	private TreeViewer treeViewer;
	private Library library;
	
	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		// Identify the input
		library = Activator.getDefault().getModel();
	}
	
	public void createPartControl(Composite parent) {
		// Create the viewer
		treeViewer = new TreeViewer(parent, SWT.NONE);
		// Create a Content provider
		LibraryContentProvider libraryContentProvider = new LibraryContentProvider();
		// Connect the viewer with the content provider
		treeViewer.setContentProvider(libraryContentProvider);
		// Create a Label Provider and connect the viewer to it
		treeViewer.setLabelProvider(new LibraryLabelProvider());
		// Create a menu manager and create context menu
        MenuManager menuManager = new MenuManager();
        Menu menu = menuManager.createContextMenu(treeViewer.getTree());
        // set the menu on the SWT widget
        treeViewer.getTree().setMenu(menu);
        // register the menu with the framework
        getSite().registerContextMenu(menuManager, treeViewer);
		//Share selection with other, potentially listening views
		getSite().setSelectionProvider(treeViewer);
		treeViewer.addDoubleClickListener(this);
		library.addObserver(this); 
		
		// Define input model to the viewer
		treeViewer.setInput(library);
	}

	public void setFocus() {
	}

	@Override
	public void update() {
		treeViewer.refresh();
	}
	
	@Override
	public void dispose() {
		library.removeObserver(this);
		super.dispose();
	}

	/**
	 * Handles a double click event on a book and 
	 * opens corresponding book editor
	 */
	@Override
	public void doubleClick(DoubleClickEvent event) {
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.getFirstElement() instanceof Book) {
				Book book = (Book) ssel.getFirstElement();
				BookEditorInput input = new BookEditorInput(book);
				try 
				{
					if(getViewSite().getPage().getActiveEditor()==null) {
						getViewSite().getPage().openEditor(input, ID_OF_THE_EDITOR);
					}
					else {
						getViewSite().getPage().getActiveEditor().setFocus();
					}
				} 
				catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}