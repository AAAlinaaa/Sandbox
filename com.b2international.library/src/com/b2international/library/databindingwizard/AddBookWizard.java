package com.b2international.library.databindingwizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;

import com.b2international.library.Activator;
import com.b2international.library.model.Book;
import com.b2international.library.model.Library;

/**
 * Single page wizard that adds a book to the library with the help
 * of databinding.
 * @author Alina
 *
 */
public class AddBookWizard extends Wizard {

	protected Book book;
	protected ImageDescriptor imageDescriptor;
	protected NewBookWizardPage newBookWizardPage;

	public AddBookWizard() {
		super();
		book = new Book();
		imageDescriptor = Activator.getDefault().getImageRegistry().getDescriptor(Activator.WIZARD_IMAGE_KEY);
		setWindowTitle("Add a new book to the library");
		setDefaultPageImageDescriptor(imageDescriptor);
	}

	@Override
	public void addPages() {
		newBookWizardPage = new NewBookWizardPage("Book Fields Entry Page", book);
		addPage(newBookWizardPage);
	}

	@Override
	public boolean performFinish() {
		Library library = Activator.getDefault().getModel();
		library.addBook(book);
		return true;
	}
}