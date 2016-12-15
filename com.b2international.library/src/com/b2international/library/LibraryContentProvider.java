package com.b2international.library;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.b2international.library.model.Book;
import com.b2international.library.model.Library;

/**
 * Content Provider for the various views displaying the library model.
 * 
 * @author Alina
 *
 */
public class LibraryContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof Library) {
			Library library = (Library) inputElement;
		}
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Library) {
			return ((Library) parentElement).getBooks().toArray();
		}
		if(parentElement instanceof Book) {
			Book book = (Book) parentElement;
			Object [] ans = new Object[2];
			ans[0] = book.getAuthor();
			ans[1] = book.getYear();
			return ans;
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof Library) {
			Library library = (Library) element;
			return library.hasBooks();
		}
		if(element instanceof Book) {
			return true;
		}
		return false;
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
