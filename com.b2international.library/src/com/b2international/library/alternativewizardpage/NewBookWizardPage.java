package com.b2international.library.alternativewizardpage;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.b2international.library.model.Book;

/**
 * The only page of the single page wizard for adding books.
 * 
 * @author Alina
 *
 */
public class NewBookWizardPage extends WizardPage {

	private KeyListener titleKeyListener;
	private KeyListener authorKeyListener;
	private KeyListener intKeyListener;
	private boolean isValidYear;
	private boolean isValidTitle;
	private boolean isValidAuthor;
	private Text authorText;
	private Text titleText;
	private Text yearText;
	private Book book;

	protected NewBookWizardPage(String pageName) {
		super(pageName);
		book = new Book();
		setTitle("Add New Book");
		setDescription("Please specify the properties of the book");
		setTitleKeyListener();
		setAuthorKeyListener();
		setIntKeyListener();
		isValidTitle = false;
		isValidAuthor = false;
		isValidYear = false;
	}

	@Override
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;

		/*Title label and text field*/
		Label titleLabel = new Label(container, SWT.NONE);
		titleLabel.setText("Book title: ");
		titleText = new Text(container,SWT.BORDER | SWT.SINGLE);
		titleText.setText("");
		titleText.addKeyListener(titleKeyListener);
		GridData titleGridData = new GridData(GridData.FILL_HORIZONTAL);
		titleText.setLayoutData(titleGridData);

		/*Author label and text field*/
		Label authorLabel = new Label(container, SWT.NONE);
		authorLabel.setText("Book author: ");
		authorText = new Text(container, SWT.BORDER | SWT.SINGLE);
		authorText.setText("");
		authorText.addKeyListener(authorKeyListener);
		GridData authorGridData = new GridData(GridData.FILL_HORIZONTAL);
		authorText.setLayoutData(authorGridData);

		/*Year label and text field*/
		Label yearLabel = new Label(container, SWT.NONE);
		yearLabel.setText("Year book was published in: ");
		yearText = new Text(container, SWT.BORDER | SWT.SINGLE);
		yearText.setText("");
		yearText.addKeyListener(intKeyListener);
		GridData yearGridData = new GridData(GridData.FILL_HORIZONTAL);
		yearText.setLayoutData(yearGridData);

		setControl(container);
		setPageComplete(false);
	}

	public String getBookTitle() {
		return book.getTitle();
	}

	public String getAuthor() {
		return book.getAuthor();
	}

	public int getYear() {
		return book.getYear();
	}

	protected void setTitleKeyListener() {
		titleKeyListener = new KeyListener() {	
			@Override
			public void keyReleased(KeyEvent e) {
				if(!titleText.getText().isEmpty()){
					isValidTitle = true;
					setErrorMessage(null);
					upDateValidity();
				}
				else {
					isValidTitle = false;
					setErrorMessage("Book title can't be an empty string.");
					setPageComplete(false);
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {}
		};
	}

	protected void setAuthorKeyListener() {
		authorKeyListener = new KeyListener() {	
			@Override
			public void keyReleased(KeyEvent e) {
				if(!authorText.getText().isEmpty()) {
					isValidAuthor = true;
					setErrorMessage(null);
					upDateValidity();
				}
				else {
					isValidAuthor = false;
					setErrorMessage("The author of the book must be specified.");
					setPageComplete(false);
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {}
		};
	}

	protected void setIntKeyListener() {
		intKeyListener = new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!isValidYear(yearText.getText())) {
					setPageComplete(false);
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
			}
		};
	}

	@SuppressWarnings("deprecation")
	protected boolean isValidYear(String input) {
		Date today = Calendar.getInstance().getTime();
		int currentYear = today.getYear()+1900;
		try {
			Integer year = Integer.parseInt(input);
			if(year >= 1500 && year < currentYear) {
				isValidYear = true;
				setErrorMessage(null);
				upDateValidity();
			}
			else {
				isValidYear = false;
				setErrorMessage("Valid years range from 1500 to " + 
						currentYear + ".");
				setPageComplete(false);
			}
		}
		catch (NumberFormatException e) {
			isValidYear = false;
			setErrorMessage("Please enter a valid year number.");
			setPageComplete(false);
		}
		return isValidYear;
	}

	protected void upDateValidity() {
		if(isValidAuthor && isValidTitle && isValidYear) {
			String title = titleText.getText();
			String author = authorText.getText();
			int year = Integer.parseInt(yearText.getText());
			book = new Book(title, author, year);
			setErrorMessage(null);
			setPageComplete(true);
		}
		else {
			if( !isValidAuthor ) {
				setErrorMessage("The author of the book must be specified.");
			}
			else if( !isValidTitle ) {
				setErrorMessage("Book title can't be an empty string.");
			}
			else {
				isValidYear(yearText.getText());
			}
		}
	}
}