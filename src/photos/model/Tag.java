package photos.model;

import java.io.*;

/**
 * this is the Tag object, for saving name/value pairs to be used with images
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class Tag implements Serializable{
	private String name;
	private String value;
	
	public Tag(String n, String v) {
		this.name = n;
		this.value = v;
	}
	

	
	public boolean equals(Tag t) {
		if(t.name.equals(this.name) && t.value.equals(this.value)) 
		{return true;}
		return false;
	}
	
	
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	
}
