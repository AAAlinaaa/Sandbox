package com.b2international.library.wizardpage;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;

import com.b2international.library.Activator;
import com.b2international.library.model.Book;

/**
 * Multipage wizard that adds a book to the library.
 * 
 * @author Alina
 *
 */
public class AddBookWizard extends Wizard {
	
	protected TitleWizardPage titleWizardPage;
	protected AuthorWizardPage authorWizardPage;
	protected YearWizardPage yearWizardPage;
	protected Book book;
	private ImageDescriptor imageDescriptor;
	
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
		titleWizardPage = new TitleWizardPage();
		authorWizardPage = new AuthorWizardPage();
		yearWizardPage = new YearWizardPage();
		addPage(titleWizardPage);
		addPage(authorWizardPage);
		addPage(yearWizardPage);
	}

	@Override
	public boolean performFinish() {
		String title = titleWizardPage.getBookTitle();
		String author = authorWizardPage.getAuthor();
		int year = Integer.parseInt(yearWizardPage.getYear());
		book = new Book(title, author, year);
		return true;
	}

	public Book getBook() {
		return book;
	}
}