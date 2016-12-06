package com.b2international.library;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.b2international.library.model.Book;

/**
 * Custom Dialog to take in information necessary to 
 * create a new book object.
 * 
 * @author Alina
 *
 */
public class InputBookDialog extends Dialog {
	private String dialogTitle;

	/** Book object created in case of successful input */
	private Book book;

	private Text titleText;
	private Text authorText;
	private Text yearText;
	
	/** Error message label widget. */
	private Text errorMessageText;
	private boolean validTitle;
	private boolean validAuthor;
	private boolean validYear;
	
	/** Modify Listeners **/
	private ModifyListener titleModifyListener;
	private ModifyListener authorModifyListener;
	private ModifyListener yearModifyListener;

	public InputBookDialog(Shell shell) {
		super(shell);
		dialogTitle = "Add New Book";
		validTitle = false;
		validAuthor = false;
		validYear = false;
		setTitleModifyListener();
		setAuthorModifyListener();
		setYearModifyListener();
	}

	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout gl = new GridLayout(2, false);
		composite.setLayout(gl);
		Label titleLabel = new Label(composite, SWT.NONE);
		titleLabel.setText("Title:");

		titleText = new Text(composite, SWT.BORDER);
		GridData gridDataText = new GridData();
		gridDataText.horizontalAlignment = SWT.FILL;
		gridDataText.widthHint = 300;
		gridDataText.grabExcessHorizontalSpace = true;
		titleText.setLayoutData(gridDataText);
		titleText.addModifyListener(titleModifyListener);

		Label authorLabel = new Label(composite, SWT.NONE);
		authorLabel.setText("Author:");

		authorText = new Text(composite, SWT.BORDER);
		GridData gridDataAuthor = new GridData();
		gridDataAuthor.horizontalAlignment = SWT.FILL;
		gridDataAuthor.widthHint = 300;
		gridDataAuthor.grabExcessHorizontalSpace = true;
		authorText.setLayoutData(gridDataAuthor);
		authorText.addModifyListener(authorModifyListener);

		Label yearLabel = new Label(composite, SWT.NONE);
		yearLabel.setText("Published In:");

		yearText = new Text(composite, SWT.BORDER);
		GridData gridDataYear = new GridData();
		gridDataYear.horizontalAlignment = SWT.FILL;
		gridDataYear.widthHint = 300;
		gridDataYear.grabExcessHorizontalSpace = true;
		yearText.setLayoutData(gridDataYear);
		yearText.addModifyListener(yearModifyListener);
		
		/*Empty Label to push error message into column 2*/
		new Label(composite, SWT.NONE);
		
		errorMessageText = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
		errorMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		setErrorMessage(null,0);

		return composite;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (dialogTitle != null) {
			shell.setText(dialogTitle);
		}
	}
	
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}
	
	public void setErrorMessage(String errorMessage, int id) {
		if(errorMessage == null) {
			errorMessageText.setText("\n");
			switch(id) {
			case 1: validTitle 	= true; break;
			case 2: validAuthor = true; break;
			case 3: validYear	= true; break;
			}
		}
		else {
			errorMessageText.setEnabled(true);
			errorMessageText.setVisible(true);
			errorMessageText.getParent().update();
			errorMessageText.setText(errorMessage);
			switch(id) {
			case 1: validTitle 	= false; break;
			case 2: validAuthor = false; break;
			case 3: validYear	= false; break;
			}
		}
		
		Control button = getButton(IDialogConstants.OK_ID);
		boolean validInput = validTitle && validAuthor && validYear;
		if (button != null) {
			button.setEnabled(validInput);
		}
	}

	@Override
	protected void okPressed() {
		/* Create Book Here */
		String title = titleText.getText();
		String author = authorText.getText();
		String year = yearText.getText();
		int publishedIn = Integer.parseInt(year);
		book = new Book(title, author, publishedIn);
		super.okPressed();
	}
	
	protected void setTitleModifyListener() {
		titleModifyListener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				String input = titleText.getText();
				if (input.isEmpty()) {
					setErrorMessage("A book must have a title.",1);
				}
				else {
					setErrorMessage(null,1);
				}
			}
		};
	}
	
	protected void setAuthorModifyListener() {
		authorModifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {

				String input = authorText.getText();
				if (input.isEmpty()) {
					setErrorMessage("An author must be specified.",2);
				}
				else {
					setErrorMessage(null,2);
				}
			}
		};
	}
	
	protected void setYearModifyListener() {
		yearModifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				
				String input = yearText.getText();
				if (input.isEmpty()) {
					setErrorMessage("Input can't be empty.",3);
				}
				if(!input.isEmpty()) {
					boolean numerical = true;
					for (int i = 0; i < input.length(); i++) {
						if(!Character.isDigit(input.charAt(i))) {
							numerical = false;
							i = input.length();
						}
					}
					if(numerical) {
						int year = Integer.parseInt(input);
						if(year<1500) {
							setErrorMessage("No books from before the Age of Discovery.",3);
						}
						else if(year>2016) {
							setErrorMessage("No books from the future.",3);
						}
						else {
							setErrorMessage(null,3);
						}
					}
					else {
						setErrorMessage("Input isn't numerical.",3);
					}
				}
			}
		};
	}

	public Book getBook() {
		return book;
	}
}