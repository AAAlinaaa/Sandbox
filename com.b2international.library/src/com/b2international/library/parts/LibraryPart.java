package com.b2international.library.parts;

import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

public class LibraryPart {
	
	public LibraryPart() {
		System.out.println("Library Part Constructor called.");
	}
	
	 @PostConstruct
     public void createControls(Composite parent) {
		 
	 }
	

}
