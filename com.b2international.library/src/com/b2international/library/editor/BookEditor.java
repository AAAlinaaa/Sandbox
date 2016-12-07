package com.b2international.library.editor;

import java.util.Calendar;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.internal.forms.widgets.FormHeading;
import org.eclipse.ui.part.EditorPart;

import com.b2international.library.Activator;
import com.b2international.library.model.Book;

/***
 * Editor to change properties of a Book.
 * 
 * @author Alina
 *
 */
public class BookEditor extends EditorPart {

	private Book book;
	private boolean dirty;
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		Activator.getDefault().getModel().notifyObservers();
		LibrarySaver.performSerialSave();
	}

	@Override
	public void doSaveAs() {
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if( input instanceof BookEditorInput ) {
			book = ((BookEditorInput)input).getBook();
			setSite(site);
            setInput(input);
            setPartName("Book Properties Editor");
		}
		else {
			throw new PartInitException("Incorrect input type.");
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void setFocus() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		
		FormToolkit toolkit = new FormToolkit(container.getDisplay());

		Section section = toolkit.createSection(container, Section.DESCRIPTION | Section.TITLE_BAR);
		section.setLayoutData(GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).create());
		section.setText("Book Editor");
		section.setDescription("Change the book's attributes.");
		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		
		toolkit.createLabel(sectionClient, "Title ", SWT.FILL);
		Text titleText = toolkit.createText(sectionClient, book.getTitle()); 
		titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		toolkit.createLabel(sectionClient, "Author ");
		Text authorText = toolkit.createText(sectionClient, book.getAuthor());
		authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		toolkit.createLabel(sectionClient, "Year ");
		Text yearText = toolkit.createText(sectionClient, Integer.toString(book.getYear()));
		yearText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		section.setClient(sectionClient);
		DataBindingContext dataBindingContext = new DataBindingContext();
		
		IObservableValue<?> observableTitle = PojoProperties.value("title").observe(book);
		UpdateValueStrategy updateTitleStrategy = new UpdateValueStrategy();
		updateTitleStrategy.setAfterConvertValidator(new TitleValidator());
		dataBindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(titleText), observableTitle,
				updateTitleStrategy, null);
		
		IObservableValue<?> observableAuthor = PojoProperties.value("author").observe(book);
		UpdateValueStrategy updateAuthorStrategy = new UpdateValueStrategy();
		updateAuthorStrategy.setAfterConvertValidator(new AuthorValidator());
		dataBindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(authorText), observableAuthor,
				updateAuthorStrategy, null);
		
		IObservableValue<?> observableYear = PojoProperties.value("year").observe(book);
		UpdateValueStrategy updateYearStrategy = new UpdateValueStrategy();
		updateYearStrategy.setAfterConvertValidator(new YearValidator());
		updateYearStrategy.setConverter(new YearConverter());
		dataBindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(yearText), observableYear,
				updateYearStrategy, null);
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
			String title = (String) value;
			if (title.isEmpty()) {
				return ValidationStatus.error("Please enter a book title.");
			}
			setDirty(true);
			firePropertyChange(IEditorPart.PROP_DIRTY);
			return ValidationStatus.ok();
		}
	}

	private final class AuthorValidator implements IValidator {

		@Override
		public IStatus validate(Object value) {
			String author = (String) value;
			if (author.isEmpty()) {
				return ValidationStatus.error("Please enter an author name.");
			}
			setDirty(true);
			firePropertyChange(IEditorPart.PROP_DIRTY);
			return ValidationStatus.ok();
		}
	}

	private final class YearValidator implements IValidator {
		@Override
		public IStatus validate(Object value) {
			Integer year = (Integer) value;
			Calendar now = Calendar.getInstance();
			int currentYear = now.get(Calendar.YEAR);
			int earliestYear = 1500;
			if (year == null) {
				return ValidationStatus.info("Please enter a value.");
			}
			if (year.intValue() < earliestYear || year.intValue() > currentYear) {
				return ValidationStatus.error("Value must be between " + earliestYear + " and " + currentYear + ".");
			}
			
			setDirty(true);
			firePropertyChange(IEditorPart.PROP_DIRTY);
			return ValidationStatus.ok();
		}
	}

}