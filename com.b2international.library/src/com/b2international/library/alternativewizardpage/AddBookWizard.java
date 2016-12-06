package com.b2international.library.alternativewizardpage;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;

import com.b2international.library.Activator;
import com.b2international.library.model.Book;

/**
 * Single page wizard for adding a book.
 * 
 * @author Alina
 *
 */
public class AddBookWizard extends Wizard {
	
	protected Book book;
	protected ImageDescriptor imageDescriptor;
	protected NewBookWizardPage newBookWizardPage;
	
	public AddBookWizard() {
		super();
		imageDescriptor = 
				Activator.getDefault().getImageRegistry().getDescriptor(
						Activator.WIZARD_IMAGE_KEY);
		setWindowTitle("Add a new book to the library");
		setDefaultPageImageDescriptor(imageDescriptor);
	}
	
	@Override
	public void addPages() {
		newBookWizardPage = new NewBookWizardPage("Book Fields Entry Page");
		addPage(newBookWizardPage);
	}

	@Override
	public boolean performFinish() {
		String title = newBookWizardPage.getBookTitle();
		String author = newBookWizardPage.getAuthor();
		int year = newBookWizardPage.getYear();
		book = new Book(title, author, year);
		return true;
	}

	public Book getBook() {
		return book;
	}
}