package com.b2international.library.editor;

import java.util.Calendar;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.b2international.library.model.Book;

public class BookEditorFormPage extends FormPage {
	
	private final class toYearConverter implements IConverter {

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
	
	private final class fromYearConverter implements IConverter {

		@Override
		public Object getFromType() {
			return Integer.class;
		}

		@Override
		public Object getToType() {
			return String.class;
		}

		@Override
		public Object convert(Object fromObject) {
			Integer year = (Integer)fromObject;
			return year.toString();
		}
	}
	
	private final class YearValidator implements IValidator {
		@Override
		public IStatus validate(Object value) {
			
			if(noOfYearChanges>0) {
				setDirty(true);
				getEditor().editorDirtyStateChanged();
			}
			noOfYearChanges++;
			
			Integer year = (Integer) value;
			Calendar now = Calendar.getInstance();
			int currentYear = now.get(Calendar.YEAR);
			int earliestYear = 1500;
			if (year == null) {
				managedForm.getMessageManager().addMessage(0, "The year the book was published in must be entered.", null, IMessageProvider.ERROR, yearText);
				return ValidationStatus.info("Please enter a value.");
			}
			if (year.intValue() < earliestYear || year.intValue() > currentYear) {
				managedForm.getMessageManager().addMessage(0, "Year must be between the 14th century and the present.", null, IMessageProvider.ERROR, yearText);
				return ValidationStatus.error("Value must be between " + earliestYear + " and " + currentYear + ".");
			}
			managedForm.getMessageManager().removeMessages(yearText);
			return ValidationStatus.ok();
		}
	}

	private final class AuthorValidator implements IValidator {

		@Override
		public IStatus validate(Object value) {
			if(noOfAuthorChanges>0) {
				setDirty(true);
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
			noOfAuthorChanges++;
			
			String author = (String) value;
			if (author.isEmpty()) {
				managedForm.getMessageManager().addMessage(0, "Book author can't be a null string.", null, IMessageProvider.ERROR, authorText);
				return ValidationStatus.error("Please enter an author name.");
			}
			
			managedForm.getMessageManager().removeMessages(authorText);
			return ValidationStatus.ok();
		}
	}

	private final class TitleValidator implements IValidator {	

		@Override
		public IStatus validate(Object value) {
			if(noOfTitleChanges>0) {
				setDirty(true);
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
			noOfTitleChanges++;
			
			String title = (String) value;
			if (title.isEmpty()) {
				managedForm.getMessageManager().addMessage(0, "Book title can't be empty.", null, IMessageProvider.ERROR, titleText);
				return ValidationStatus.error("Please enter a book title.");
			}
			
			managedForm.getMessageManager().removeMessages(titleText);
			return ValidationStatus.ok();
		}
	}
	
	private Book book;
	private IManagedForm managedForm;
	private Text yearText;
	private Text titleText;
	private Composite sectionClient;
	private Text authorText;
	private int noOfTitleChanges = 0;
	private int noOfAuthorChanges = 0;
	private int noOfYearChanges = 0;
	private boolean dirty;

	public BookEditorFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		this.getEditor().editorDirtyStateChanged();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {			
		try {
			dirty = false;
			book = ((BookEditorInput)input).getBook();
			setSite(site);
			setInput(input);
			setPartName("Book Properties Editor");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		this.managedForm = managedForm;
	
		super.createFormContent(managedForm);
		Composite body = managedForm.getForm().getBody();
		GridLayout gl = new GridLayout(2, true);
		gl.verticalSpacing = 20;
		
		Form form = this.getManagedForm().getForm().getForm(); 
		form.setText(this.getPartName());
		this.getEditor().getToolkit().decorateFormHeading(form);
		
		Composite parentComposite = this.getEditor().getToolkit().createComposite(body, SWT.NONE);
		GridDataFactory.swtDefaults().span(2, 1).align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(parentComposite); 
		parentComposite.setLayout(gl);
		
		createContents(parentComposite);
		
		body.setLayout(gl);
		getEditor().getToolkit().adapt(parentComposite);
	}

	private void createContents(Composite parentComposite) {
		
		FormToolkit toolkit = new FormToolkit(parentComposite.getDisplay());
		Section section = toolkit.createSection(parentComposite, Section.DESCRIPTION | Section.TITLE_BAR);
		section.setLayoutData(GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).create());
		
		section.setDescription("Change the book's attributes.");
		sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		
		toolkit.createLabel(sectionClient, "Title ", SWT.FILL);
		titleText = toolkit.createText(sectionClient, book.getTitle()); 
		titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		toolkit.createLabel(sectionClient, "Author ");
		authorText = toolkit.createText(sectionClient, book.getAuthor());
		authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		toolkit.createLabel(sectionClient, "Year ");
		String value = String.valueOf(book.getYear());
		yearText = toolkit.createText(sectionClient, value);
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
		
		updateYearStrategy.setConverter(new toYearConverter());
		UpdateValueStrategy updateYearFieldStrategy = new UpdateValueStrategy();
		updateYearFieldStrategy.setConverter(new fromYearConverter());
		
		dataBindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(yearText), observableYear,
				updateYearStrategy, updateYearFieldStrategy);
	}

}