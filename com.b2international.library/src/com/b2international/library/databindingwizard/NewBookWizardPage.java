package com.b2international.library.databindingwizard;

import java.util.Calendar;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.b2international.library.model.Book;

/**
 * Custom wizard page that adds a user defined book to the library model
 * with the help of databinding. The only page of the wizard.
 * 
 * @author Alina
 *
 */
public class NewBookWizardPage extends WizardPage {

	private Text authorText;
	private Text titleText;
	private Text yearText;
	private Book book;

	protected NewBookWizardPage(String pageName, Book book) {
		super(pageName);
		this.book = book;
		setTitle("Add New Book");
		setDescription("Please specify the properties of the book.");
	}

	@Override
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		DataBindingContext dataBindingContext = new DataBindingContext();
		WizardPageSupport.create(this, dataBindingContext);

		/* Title label and text field */
		Label titleLabel = new Label(container, SWT.NONE);
		titleLabel.setText("Book title: ");
		titleText = new Text(container, SWT.BORDER | SWT.SINGLE);
		titleText.setText("Enter the year the book was published in.");

		GridData titleGridData = new GridData(GridData.FILL_HORIZONTAL);
		titleText.setLayoutData(titleGridData);

		UpdateValueStrategy updateTitleStrategy = new UpdateValueStrategy();
		updateTitleStrategy.setAfterConvertValidator(new TitleValidator());
		IObservableValue<?> observableTitle = PojoProperties.value("title").observe(book);
		dataBindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(titleText), observableTitle,
				updateTitleStrategy, null);

		/* Author label, text field and their binding */
		Label authorLabel = new Label(container, SWT.NONE);
		authorLabel.setText("Book author: ");
		authorText = new Text(container, SWT.BORDER | SWT.SINGLE);
		authorText.setText("");

		GridData authorGridData = new GridData(GridData.FILL_HORIZONTAL);
		authorText.setLayoutData(authorGridData);

		UpdateValueStrategy updateAuthorStrategy = new UpdateValueStrategy();
		updateAuthorStrategy.setAfterConvertValidator(new AuthorValidator());
		IObservableValue<?> observableAuthor = PojoProperties.value("author").observe(book);
		dataBindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(authorText), observableAuthor,
				updateAuthorStrategy, null);

		/* Year label, text field and their binding*/
		Label yearLabel = new Label(container, SWT.NONE);
		yearLabel.setText("Year book was published in: ");
		yearText = new Text(container, SWT.BORDER | SWT.SINGLE);
		yearText.setText("");

		GridData yearGridData = new GridData(GridData.FILL_HORIZONTAL);
		yearText.setLayoutData(yearGridData);

		IObservableValue<?> observableYear = PojoProperties.value("year").observe(book);
		UpdateValueStrategy updateYearStrategy = new UpdateValueStrategy();
		updateYearStrategy.setAfterConvertValidator(new YearValidator());
		updateYearStrategy.setConverter(new YearConverter());
		dataBindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(yearText), observableYear,
				updateYearStrategy, null);

		setControl(container);
		setPageComplete(false);
	}

	private final class YearConverter implements IConverter {

		@Override
		public Object getFromType() {
			return String.class;
		}

		@Override
		public Object getToType() {
			return Integer.class;
		}

		@Override
		public Object convert(Object fromObject) {
			try {
				return Integer.parseInt((String) fromObject);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
	}

	private final class TitleValidator implements IValidator {
		@Override
		public IStatus validate(Object value) {
			String string = (String) value;
			if (string.isEmpty()) {
				return ValidationStatus.error("Please enter a book title.");
			}
			return ValidationStatus.ok();
		}
	}

	private final class AuthorValidator implements IValidator {

		@Override
		public IStatus validate(Object value) {
			String string = (String) value;
			if (string.isEmpty()) {
				return ValidationStatus.error("Please enter an author name.");
			}
			return ValidationStatus.ok();
		}
	}

	private final class YearValidator implements IValidator {
		@Override
		public IStatus validate(Object value) {
			Integer i = (Integer) value;
			Calendar now = Calendar.getInstance();
			int currentYear = now.get(Calendar.YEAR);
			int earliestYear = 1500;
			if (i == null) {
				return ValidationStatus.info("Please enter a value.");
			}
			if (i.intValue() < earliestYear || i.intValue() > currentYear) {
				return ValidationStatus.error("Value must be between " + earliestYear + " and " + currentYear + ".");
			}
			return ValidationStatus.ok();
		}
	}

}