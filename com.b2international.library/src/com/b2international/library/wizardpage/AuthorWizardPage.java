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
 * Second page of the add book wizard taking in the author of the book.
 * 
 * @author Alina
 *
 */
public class AuthorWizardPage extends WizardPage {
	private Text authorText;
	private Composite container;
	
	public AuthorWizardPage () {
		super("Set Author Page");
		setTitle("Set Author Page");
		setDescription("Please specify the author of the book");
	}
	
	protected AuthorWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		Label label = new Label(container, SWT.NONE);
		label.setText("Book author");
		
		authorText = new Text(container, SWT.BORDER | SWT.SINGLE);
		authorText.setText("");
		authorText.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(!authorText.getText().isEmpty()) {
					setErrorMessage(null);
					setPageComplete(true);
				}
				else {
					setErrorMessage("Input can't be empty.");
					setPageComplete(false);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		authorText.setLayoutData(gridData);
        
        setControl(container);
        setPageComplete(false);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		authorText.setFocus();
	}
	
	public String getAuthor() {
		return authorText.getText();
	}
}