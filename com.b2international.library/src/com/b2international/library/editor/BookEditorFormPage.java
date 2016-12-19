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
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.b2international.library.Activator;
import com.b2international.library.model.Book;

public class BookEditorFormPage extends FormPage {
/*	
	private final class toYearConverter implements IConverter<String, Integer> {

		@Override
		public Object getFromType() {
			return String.class;
		}

		@Override
		public Object getToType() {
			return Integer.class;
		}

		@Override
		public Integer convert(String fromObject) {
			try {
				return Integer.parseInt(fromObject);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
	}
	
	private final class fromYearConverter implements IConverter<Integer, String> {

		@Override
		public Object getFromType() {
			return Integer.class;
		}

		@Override
		public Object getToType() {
			return String.class;
		}

		@Override
		public String convert(Integer fromObject) {
			Integer year = fromObject;
			return year.toString();
		}
	}
	
	private final class YearValidator implements IValidator<Integer> {
		@Override
		public IStatus validate(Integer year) {
			
			if(noOfYearChanges>0) {
				setDirty(true);
				managedForm.dirtyStateChanged();
				getEditor().editorDirtyStateChanged();
			}
			noOfYearChanges++;
			
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

	private final class AuthorValidator implements IValidator<String>{

		@Override
		public IStatus validate(String value) {
			if(noOfAuthorChanges>0) {
				setDirty(true);
				managedForm.dirtyStateChanged();
				getEditor().editorDirtyStateChanged();
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

	private final class TitleValidator implements IValidator<String> {	

		@Override
		public IStatus validate(String value) {
			if(noOfTitleChanges>0) {
				setDirty(true);
				managedForm.dirtyStateChanged();
				getEditor().editorDirtyStateChanged();
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
*/	
	private Book book;
	private IManagedForm managedForm;
	private Text yearText;
	private Text titleText;
	private Composite sectionClient;
	private Text authorText;
	private int noOfTitleChanges = 0;
	private int noOfAuthorChanges = 0;
	private int noOfYearChanges = 0;
	private AbstractFormPart formPart;
	private Book editorBook;

	public BookEditorFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		formPart = new AbstractFormPart() {};
	}
/*
	public void setDirty(boolean dirty) {
		managedForm.dirtyStateChanged();
		formPart.markDirty();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {			
		try {
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
		return managedForm.isDirty();
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		Activator.getDefault().getModel().updatetBook(book, editorBook);
		managedForm.commit(true);
	}
	
	@Override
	public void setFocus() {
		titleText.setFocus();
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		this.managedForm = managedForm;
		formPart.initialize(managedForm);
		managedForm.addPart(formPart);
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
		editorBook = new Book(book.getTitle(), book.getAuthor(), book.getYear());
		FormToolkit toolkit = new FormToolkit(parentComposite.getDisplay());
		Section section = toolkit.createSection(parentComposite, Section.TITLE_BAR | Section.FOCUS_TITLE);
		section.setLayoutData(GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).create());
		
		section.setText("Change the Book's Attributes");
		sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		
		toolkit.createLabel(sectionClient, "Title ", SWT.FILL);
		titleText = toolkit.createText(sectionClient, editorBook.getTitle()); 
		titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		toolkit.createLabel(sectionClient, "Author ");
		authorText = toolkit.createText(sectionClient, editorBook.getAuthor());
		authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		toolkit.createLabel(sectionClient, "Year ");
		String value = String.valueOf(editorBook.getYear());
		yearText = toolkit.createText(sectionClient, value);
		yearText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		section.setClient(sectionClient);
		DataBindingContext dataBindingContext = new DataBindingContext();
		
		IObservableValue<String> observableTitle = PojoProperties.value("title", String.class).observe(editorBook);
		UpdateValueStrategy<String,String> updateTitleStrategy = new UpdateValueStrategy<String,String>();
		updateTitleStrategy.setAfterConvertValidator(new TitleValidator());
		dataBindingContext.bindValue(WidgetProperties.textText(SWT.Modify).observe(titleText), observableTitle,
				updateTitleStrategy, null);
		
		IObservableValue<String> observableAuthor = PojoProperties.value("author", String.class).observe(editorBook);
		UpdateValueStrategy<String, String> updateAuthorStrategy = new UpdateValueStrategy<String, String>();
		updateAuthorStrategy.setAfterConvertValidator(new AuthorValidator());
		dataBindingContext.bindValue(WidgetProperties.textText(SWT.Modify).observe(authorText), observableAuthor,
				updateAuthorStrategy, null);
		
		IObservableValue<Integer> observableYear = PojoProperties.value("year", Integer.class).observe(editorBook);
		UpdateValueStrategy<String, Integer> updateYearStrategy = new UpdateValueStrategy<String, Integer>();
		updateYearStrategy.setAfterConvertValidator(new YearValidator());
		
		updateYearStrategy.setConverter(new toYearConverter());
		UpdateValueStrategy<Integer, String> updateYearFieldStrategy = new UpdateValueStrategy<Integer, String>();
		updateYearFieldStrategy.setConverter(new fromYearConverter());
		
		dataBindingContext.bindValue(WidgetProperties.textText(SWT.Modify).observe(yearText), observableYear,
				updateYearStrategy, updateYearFieldStrategy);
	}
*/
}