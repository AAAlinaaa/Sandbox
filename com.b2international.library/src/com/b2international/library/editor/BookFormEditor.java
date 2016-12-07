package com.b2international.library.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

import com.b2international.library.Activator;

public class BookFormEditor extends FormEditor{

	private static final String id = "BOOK_FORM_EDITOR";
	private IFormPage bookEditorPage;

	@Override
	protected void addPages() {
		bookEditorPage = new BookEditorFormPage(this, id, getTitle());
		try {
			addPage(bookEditorPage);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	public void doSave(IProgressMonitor monitor) {
		Activator.getDefault().getModel().notifyObservers();
		LibrarySaver.performSave();
		bookEditorPage.doSave(monitor);
		editorDirtyStateChanged();
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}