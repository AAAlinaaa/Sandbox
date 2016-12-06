package com.b2international.library.wizardpage;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * Last page of the add book wizard taking in the year the book was published in.
 * 
 * @author Alina
 *
 */
public class YearWizardPage extends WizardPage {
	
	protected boolean validYear;
	
	protected YearWizardPage(String pageName) {
		super(pageName);
		validYear = false;
	}

	private Text yearText;
	private Composite container;

	public YearWizardPage () {
		super("Set Year Page");
		setTitle("Set Year Page");
		setDescription("Please specify the year in which the book was published.");
	}

	protected boolean isValidYear(String input) {
		boolean validYear = false;
		try {
			Integer year = Integer.parseInt(input);
			if(year >= 1500 && year < 2017) {
				validYear = true;
				setErrorMessage(null);
			}
			else {
				setErrorMessage("Valid years range from 1500 to 2016.");
			}
		}
		catch (NumberFormatException e) {
			validYear = false;
			setErrorMessage("Please enter a valid year number.");
		}
		return validYear;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		Label label = new Label(container, SWT.NONE);
		label.setText("Published in");

		yearText = new Text(container, SWT.BORDER | SWT.SINGLE);
		yearText.setText("");
		yearText.setFocus();
		yearText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				String yearInput = yearText.getText();
				if( isValidYear(yearInput) ) {
					validYear = true;
					setPageComplete(true);
				}
				else {
					validYear = false;
					setPageComplete(false);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		yearText.setLayoutData(gridData);

		setControl(container);
		setPageComplete(false);
	}
	
	@Override
	public void setPageComplete(boolean complete) {
		super.setPageComplete(complete);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		yearText.setFocus();
	}

	public String getYear() {
		return yearText.getText();
	}
}