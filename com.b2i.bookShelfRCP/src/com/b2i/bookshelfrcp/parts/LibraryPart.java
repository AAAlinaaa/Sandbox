package com.b2i.bookshelfrcp.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.b2international.library.LibraryContentProvider;
import com.b2international.library.LibraryLabelProvider;
import com.b2international.library.model.Book;
import com.b2international.library.model.Library;
import com.b2international.library.model.LibraryObserver;

public class LibraryPart implements LibraryObserver, IDoubleClickListener {
	@Inject @Named("com.b2i.bookshelfrcp.E4LifeCycle.library")
	private Library library;
	@Inject IEclipseContext context;
	@Inject EPartService partService;
	@Inject MApplication app;
	@Inject EModelService modelService;
	private TreeViewer treeViewer;
	
	/*@Inject
	private MDirtyable dirty;*/
	
	public LibraryPart() {
		System.out.println("In Library Part Constructor.");
	}
	
	@PostConstruct
	public void createComposite(Composite parent , EMenuService menuservice, ESelectionService selectionService, IEclipseContext context) {
		System.out.println("In Create Composite");
		parent.setLayout(new GridLayout(1, false));
		
		treeViewer = new TreeViewer(parent, SWT.NONE);
		treeViewer.setContentProvider(new LibraryContentProvider());		
		treeViewer.setLabelProvider(new LibraryLabelProvider());
		treeViewer.setInput(library);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		/*Register the removebook popup menu with the menuservice */
		menuservice.registerContextMenu(treeViewer.getControl(), "com.b2i.bookshelfrcp.popupmenu.removebook");
		
		/*Make Tree Viewer give its selections to the selection service*/
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				selectionService.setSelection(selection.getFirstElement());
				if(selection.getFirstElement() instanceof Book) {
					context.set("myactivePartId", "com.b2i.bookshelfrcp.parts.librarypart");
				}
				else {
					context.remove("myactivePartId");
				}
			}
		});
				
		library.addObserver(this);
		
		treeViewer.addDoubleClickListener(this);
	}

	@Override
	public void update() {
		treeViewer.refresh();
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {		
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.getFirstElement() instanceof Book) {
				Book book = (Book) ssel.getFirstElement();
				System.out.println("Clicked on: "+ book);
				
				MPart part = partService.createPart("com.b2i.bookshelfrcp.partdescriptor.bookeditorpart");
				//MPart part = partService.createPart("com.b2i.bookshelfrcp.partdescriptor.bookeditorform");
				MPartStack editorStack = (MPartStack)modelService.find("com.b2i.bookshelfrcp.partstack.editorstack", app);
				//MPartStack editorStack = (MPartStack)modelService.find("com.b2i.bookShelfRCP.partstack.sample", app);
				
				editorStack.getChildren().add(part);
				partService.showPart(part, PartState.ACTIVATE);
			}
		}
	}

}