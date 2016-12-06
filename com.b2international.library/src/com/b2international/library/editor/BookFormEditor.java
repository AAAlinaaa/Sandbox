package com.b2international.library.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

import com.b2international.library.Activator;

public class BookFormEditor extends FormEditor{

	private static final String id = "BOOK_FORM_EDITOR";
	private boolean dirty = false;

	@Override
	protected void addPages() {
		IFormPage bookEditorPage = new BookEditorFormPage(this, id, getTitle());
		try {
			addPage(bookEditorPage);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	public void setDirty() {
		dirty = true;
	}
	
	@Override
	public boolean isDirty() {
			return dirty;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		Activator.getDefault().getModel().notifyObservers();
		LibrarySaver.performSave();
		dirty = false;
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