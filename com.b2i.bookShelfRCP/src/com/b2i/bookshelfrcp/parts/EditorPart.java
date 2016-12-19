package com.b2i.bookshelfrcp.parts;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.b2international.library.model.Book;
import com.b2international.library.model.Library;

public class EditorPart {
	
	private Book book;
	@Inject
	public MDirtyable dirty;
	private Text titleText;
	private Text authorText;
	private Text yearText;
	private Book editorBook;
	@Inject IEclipseContext workbenchContext;
	
	@Inject
	public EditorPart(@Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		System.out.println("Editor Part Constructor called.");
		if(object instanceof Book) {
			book = (Book) object;
			System.out.println("Book found :" + book);
		}
	}
	
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
		public Integer convert(Object fromObject) {
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
		public String convert(Object fromObject) {
			Integer year = (Integer) fromObject;
			return year.toString();
		}
	}
	
	private final class YearValidator implements IValidator {
		@Override
		public IStatus validate(Object value) {
			
			Calendar now = Calendar.getInstance();
			int currentYear = now.get(Calendar.YEAR);
			int earliestYear = 1500;
			Integer year = (Integer) value;
			if (year == null) {
				return ValidationStatus.info("Please enter a value.");
			}
			if (year.intValue() < earliestYear || year.intValue() > currentYear) {
				return ValidationStatus.error("Value must be between " + earliestYear + " and " + currentYear + ".");
			}
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
			
			return ValidationStatus.ok();
		}
	}

	private final class TitleValidator implements IValidator {	
		@Override
		public IStatus validate(Object value) {
			
			String title = (String) value;
			if (title.isEmpty()) {
				return ValidationStatus.error("Please enter a book title.");
			}
			
			return ValidationStatus.ok();
		}
	}
	
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		editorBook = new Book(book.getTitle(), book.getAuthor(), book.getYear());
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
		titleText = toolkit.createText(sectionClient, book.getTitle()); 
		titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		titleText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
				System.out.println("In modify text listener");
				System.out.println(editorBook);
			}
		});

		toolkit.createLabel(sectionClient, "Author ");
		authorText = toolkit.createText(sectionClient, book.getAuthor());
		authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		authorText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
			}
		});
		
		toolkit.createLabel(sectionClient, "Year ");
		yearText = toolkit.createText(sectionClient, Integer.toString(book.getYear()));
		yearText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		yearText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
			}
		});
		
		section.setClient(sectionClient);
		
		DataBindingContext dataBindingContext = new DataBindingContext();
		
		IObservableValue<?> observableTitle = PojoProperties.value("title").observe(editorBook);
		UpdateValueStrategy updateTitleStrategy = new UpdateValueStrategy();
		updateTitleStrategy.setAfterConvertValidator(new TitleValidator());
		dataBindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(titleText), observableTitle,
				updateTitleStrategy, new UpdateValueStrategy());
		
		IObservableValue<?> observableAuthor = PojoProperties.value("author").observe(editorBook);
		UpdateValueStrategy updateAuthorStrategy = new UpdateValueStrategy();
		updateAuthorStrategy.setAfterConvertValidator(new AuthorValidator());
		dataBindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(authorText), observableAuthor,
				updateAuthorStrategy, null);
		
		IObservableValue<?> observableYear = PojoProperties.value("year").observe(editorBook);
		UpdateValueStrategy updateYearStrategy = new UpdateValueStrategy();
		updateYearStrategy.setAfterConvertValidator(new YearValidator());
		
		updateYearStrategy.setConverter(new toYearConverter());
		UpdateValueStrategy updateYearFieldStrategy = new UpdateValueStrategy();
		updateYearFieldStrategy.setConverter(new fromYearConverter());
		
		dataBindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(yearText), observableYear,
				updateYearStrategy, updateYearFieldStrategy);
		
		dirty.setDirty(false);
	}
	
	@Persist
	public void save() {
		dirty.setDirty(false);
		Library library = (Library) workbenchContext.get("com.b2i.bookshelfrcp.E4LifeCycle.library");
		library.updatetBook(book, editorBook);
	}
}