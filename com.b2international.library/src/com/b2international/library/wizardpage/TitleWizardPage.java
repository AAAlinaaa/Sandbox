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
 * First page of the add book wizard taking in the title of the book. 
 * 
 * @author Alina
 *
 */
public class TitleWizardPage extends WizardPage {
	private Text titleText;
	private Composite container;
	
	public TitleWizardPage () {
		super("Set Title Page");
		setTitle("Set Title Page");
		setDescription("Please specify the title of the book");
	}
	
	protected TitleWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		Label label = new Label(container, SWT.NONE);
		label.setText("Book title");
		
		titleText = new Text(container,SWT.BORDER | SWT.SINGLE);
		titleText.setText("");
		titleText.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(!titleText.getText().isEmpty()) {
					setPageComplete(true);
					setErrorMessage(null);
				}
				else {
					setErrorMessage("Input can't be empty.");
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		titleText.setLayoutData(gridData);
		
		setControl(container);
		setPageComplete(false);
	}
	
	public String getBookTitle() {
		return titleText.getText();
	}
}