package com.b2international.library.model;

import java.io.Serializable;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Book object that populates the library model.
 * Has three properties, title, author, and year (the book was first published in).
 * @year	2016
 * @author Alina
 *
 */
public class Book implements IPropertySource, Serializable {
	
	private static final long serialVersionUID = -3327453193093750421L;
	
	@JsonIgnore
	static final protected IPropertyDescriptor[] propDescriptors =
			new IPropertyDescriptor[4];
	private static final String PROP_ID_PUBLISHED = "publicationDate_id";
	private static final String PROP_ID_AUTHOR = "author_id";
	private static final String PROP_ID_BOOK = "book_id";
	private static final String PROP_ID_NULL = "";
	private static final String PROP_DISPLAY_PUBLISHED = "Published";
	private static final String PROP_DISPLAY_AUTHOR = "Author";
	private static final String PROP_DISPLAY_BOOK = "Title";
	private static final String PROP_DISPLAY_NULL = "";
	
	@JsonProperty
	private String title;
	@JsonProperty
	private String author;
	@JsonProperty
	private int year;
	
	public Book(String title, String author, int year) {
		this.title 	= title;
		this.author = author;
		this.year 	= year;
	}

	public Book() {
		title = "";
		author = "";
		year = 0;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public boolean isTheSame(Object obj) {
		if(obj instanceof Book)
		{
			Book book = (Book) obj;
			boolean equals = this.title.equals(book.getTitle());
			equals = equals && this.author.equals(book.author);
			equals = equals && (this.year == book.getYear());
			return equals;
		}
		else
			return false;
	}
	
	@Override
	public String toString() {
		return "Book [ title = " + title + ", author = " + author + ", year = " + year + " ]";
	}

	@Override
	@JsonIgnore
	public Object getEditableValue() {
		return null;
	}

	@JsonIgnore
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		
		PropertyDescriptor pd1 = new PropertyDescriptor(PROP_ID_BOOK, PROP_DISPLAY_BOOK);
		pd1.setCategory("Fixed Value");
		propDescriptors[0] = pd1;
		PropertyDescriptor pd2 = new PropertyDescriptor(PROP_ID_AUTHOR, PROP_DISPLAY_AUTHOR);
		pd2.setCategory("Fixed Value");
		propDescriptors[1] = pd2;
		PropertyDescriptor pd3 = new PropertyDescriptor(PROP_ID_PUBLISHED, PROP_DISPLAY_PUBLISHED);
		pd3.setCategory("Fixed Value");
		propDescriptors[2] = pd3;
		PropertyDescriptor pd4 = new PropertyDescriptor(PROP_ID_NULL, PROP_DISPLAY_NULL);
		pd4.setCategory("Fixed Value");
		propDescriptors[3] = pd4;
		return propDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if(id.equals(PROP_ID_BOOK))
		{
			return getTitle();
		}
		if(id.equals(PROP_ID_AUTHOR))
		{
			return getAuthor();
		}
		if(id.equals(PROP_ID_PUBLISHED))
		{
			return getYear();
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
	}
}