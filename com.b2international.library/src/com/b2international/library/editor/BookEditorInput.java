package com.b2international.library.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.b2international.library.Activator;
import com.b2international.library.model.Book;

/**
 * Class to set input of BookEditor.
 * @author Alina
 *
 */
public class BookEditorInput implements IEditorInput {

	 private Book book;

     public BookEditorInput(Book book) {
             this.book = book;
     }
	
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getDefault().getImageRegistry().getDescriptor(
				Activator.WIZARD_IMAGE_KEY);
	}

	@Override
	public String getName() {
		return getBook().getTitle();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return null;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}