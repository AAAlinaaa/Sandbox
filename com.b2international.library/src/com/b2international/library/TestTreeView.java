package com.b2international.library;

import com.b2international.library.model.Book;
import com.b2international.library.model.Library;
import com.b2international.library.model.LibraryObserver;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

/***
 * A View using an SWT Tree and no Viewer.
 * 
 * @author Alina
 *
 */
public class TestTreeView extends ViewPart implements ISelectionProvider, LibraryObserver {

	private Tree tree;
	private Image image;
	private Library library;
	private IStructuredSelection selection;
	private List<ISelectionChangedListener> listeners;

	@Override
	public void createPartControl(Composite parent) {
		tree = new Tree(parent, SWT.NONE);
		listeners = new ArrayList<ISelectionChangedListener>();

		drawTree();			/*Draw the library tree with in its initial state*/
		MenuManager menuManager = new MenuManager();   /* Create a menu manager */
		Menu menu = menuManager.createContextMenu(tree.getParent()); /* Create context menu */
		tree.setMenu(menu);		/* Set the menu on the SWT widget */
		getSite().registerContextMenu(menuManager, this); /* Register the menu with the framework. */

		library = Activator.getDefault().getModel();	
		library.addObserver(this);		/*Make this View an observer of the library*/

		getSite().setSelectionProvider(this);	/*Share selection with other, potentially listening views*/

		/* Have the tree listen for selections and propagate them to anyone listening to this view */
		tree.addSelectionListener(new SelectionListener() {	
			@Override
			public void widgetSelected(SelectionEvent e) {				
				if(e.item instanceof TreeItem) {
					TreeItem treeItem = (TreeItem)e.item;
					Book selectedBook = (Book)treeItem.getData();

					setSelection(new StructuredSelection(selectedBook));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}
		});
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		return selection;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		if(listeners != null) {
			listeners.remove(listener);
		}
	}

	@Override
	public void setSelection(ISelection selection) {
		if(selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection)selection; 
			for (ISelectionChangedListener iSelectionChangedListener : listeners) {
				iSelectionChangedListener.selectionChanged(new SelectionChangedEvent(this, selection));
			}
		}
	}

	public void setFocus() {
	}

	public void dispose() {
		tree.dispose();
		image.dispose();
		listeners.clear();
		library.removeObserver(this);
		super.dispose();
	}

	public void update() {
		drawTree();
	}

	private void drawTree() {
		/*Library is populated in Activator*/
		Library library = Activator.getDefault().getModel();
		List<Book> books = library.getBooks();
		ImageDescriptor descriptor = 
				Activator.getDefault().getImageRegistry().getDescriptor(
						Activator.BOOK_IMAGE_KEY);
		image = descriptor.createImage();
		tree.removeAll();
		for (Book book : books) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(book.getTitle());
			item.setData(book);
			item.setImage(image);
		}
	}

}