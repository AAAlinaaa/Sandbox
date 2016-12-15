 package com.b2i.bookshelfrcp.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;

import com.b2international.library.model.Book;
import com.b2international.library.model.Library;

public class RemoveBookHandler {

	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object, 
			IEclipseContext workbenchContext) {
		System.out.println("In Remove Handler        Execute called.");
		System.out.println(object);
		if (object instanceof Book) {
			Book book = (Book) object;
			Library library = (Library) workbenchContext.get("com.b2i.bookshelfrcp.E4LifeCycle.library");
			library.removeBook(book);
		}
	}

}