package com.b2international.library;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import com.b2international.library.model.Book;
import com.b2international.library.model.Library;

/**
 * Label provider for the library model.
 * 
 * @author Alina
 *
 */
public class LibraryLabelProvider implements ILabelProvider {
	private Image bookImage;
	private Image authorImage;

	public LibraryLabelProvider() {
		ImageDescriptor bookDescriptor = Activator.getDefault().getImageRegistry().getDescriptor(Activator.BOOK_IMAGE_KEY);
		bookImage = bookDescriptor.createImage();
		ImageDescriptor authorDescriptor = Activator.getDefault().getImageRegistry().getDescriptor(Activator.AUTHOR_IMAGE_KEY);
		authorImage = authorDescriptor.createImage();
	}

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public void dispose() {
		if(bookImage !=null) {
			bookImage.dispose();
		}
		if(authorImage !=null) {
			authorImage.dispose();
		}
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public Image getImage(Object element) {
		if(element instanceof Book) {
			return bookImage;
		}
		if(element instanceof String) {
			return authorImage;
		}
		return null; 
	}

	@Override
	public String getText(Object element) {

		if( element instanceof Library ) {
			return ((Library)element).getName();
		}
		if( element instanceof Book ) {
			return ((Book)element).getTitle();
		}
		else return element.toString();
	}

}
