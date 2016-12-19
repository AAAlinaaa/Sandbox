package com.b2i.bookshelfrcp.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.b2international.library.LibraryContentProvider;
import com.b2international.library.LibraryLabelProvider;
import com.b2international.library.model.Book;

public class PropertiesPart {
	private Book book;
	private TableViewer tableViewer;
	
	public PropertiesPart() {
	}
	
	@Inject 
	public void showSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		if(object instanceof Book) {
			book = (Book) object;
			tableViewer.setInput(book);
		}
	}
	
	class BookContentProvider implements IStructuredContentProvider {
		
		public Object[] getElements(Object inputElement) {
			if(inputElement instanceof Book)
			{
				Book book = (Book) inputElement;
				String[] contents = new String[3];
				contents[0] = book.getTitle();
				contents[1] = book.getAuthor();
				contents[2] = Integer.toString(book.getYear());
			}
		    return null;
		  }
		  public void dispose() {
		  }
		  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		  }
	}
	
	@PostConstruct
	public void createComposite(Composite parent , ESelectionService selectionService) {
		tableViewer = new TableViewer(parent, SWT.NONE);
		
		tableViewer.setContentProvider(new LibraryContentProvider());
		tableViewer.setLabelProvider(new LibraryLabelProvider());
		
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
	}
}