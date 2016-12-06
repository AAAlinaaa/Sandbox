package com.b2international.library.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Library model class, contains a collection of book objects.
 * @year	2016
 * @author Alina
 */
public class Library implements Serializable {
	
	private static final long serialVersionUID = -3323023825598260699L;
	@JsonProperty
	private String name;
	@JsonProperty
	private List<Book> books;
	@JsonIgnore
	private transient List<LibraryObserver> observers;
	
	public Library() {
	}
	
	public Library(String name, ArrayList<Book> books) {
		this.name = name;
		this.books = books;
		observers = new ArrayList<LibraryObserver>();
	}
	
	public void initializeObservers() {
		observers = new ArrayList<LibraryObserver>();
	}
	
	public void addBook(Book book) {
		books.add(book);
		notifyObservers();
	}
	
	public void removeBook(Book book) {
		books.remove(book);
		notifyObservers();
	}
	
	public void notifyObservers() {
		for(LibraryObserver observer: observers) {
			observer.update();
		}
	}
	
	public void addObserver(LibraryObserver observer) {
		observers.add(observer);
	}

	public List<Book> getBooks() {
		return books;
	}
	
	public void setBooks(ArrayList<Book> books) {
		this.books = books; 
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean hasBooks() {
		return !books.isEmpty();
	}

	public void removeObserver(LibraryObserver observer) {
		observers.remove(observer);
	}
}