package com.b2i.bookshelfrcp.parts;

import javax.inject.Inject;
import javax.inject.Named;
import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
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

public class EditorPart {
	
	private Book book;
	@Inject
	public MDirtyable dirty;
	
	@Inject
	public EditorPart(@Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		System.out.println("Editor Part Constructor called.");
		if(object instanceof Book) {
			book = (Book) object;
			System.out.println("Book found :" + book);
		}
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
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
		titleText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
			}
		});

		toolkit.createLabel(sectionClient, "Author ");
		Text authorText = toolkit.createText(sectionClient, book.getAuthor());
		authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		authorText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
			}
		});
		
		toolkit.createLabel(sectionClient, "Year ");
		Text yearText = toolkit.createText(sectionClient, Integer.toString(book.getYear()));
		yearText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		yearText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
			}
		});
		
		section.setClient(sectionClient);
	}
	
	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}