package com.b2international.library.model;

/**
 * Interface to implement for classes wishing to observe the
 * library model. 
 * 
 * @author Alina
 *
 */
public interface LibraryObserver {
	
	/*Called when subject in an Observer pattern 
	 * has been changed. Updates its own structure. */
	void update();
}