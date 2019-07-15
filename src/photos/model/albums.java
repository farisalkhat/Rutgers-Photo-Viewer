package photos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

/**
 * this is the albums object, for storing the lists of photos and the conglomerations info
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class albums implements Serializable{
	//String albumName;
	
	private String albumName;
	private int numPhotos;
	private Date newestDate, oldestDate;
	private List<CustomImage> images;
	
	
	
	//String earliestDate;
	public albums(String albumName) {
		this.albumName = new String(albumName);
		this.numPhotos = 0;
		this.newestDate = new Date(0);
		this.oldestDate = new Date(0);
		this.images = new ArrayList<CustomImage>();
		
	}
	
	
	public albums(String albumName, int photos, Date newestDate, Date oldestDate) {
		this.albumName = new String(albumName);
		this.numPhotos = photos;
		this.newestDate = newestDate;
		this.oldestDate = oldestDate;
		this.images = new ArrayList<CustomImage>();
		
	}
	public int getPhotos() {
		return numPhotos;
	}
	public List<CustomImage> getImages(){
		return images;
	}

	public void setImages(List<CustomImage> images){
		this.images = images;
	}
	public void setPhotos(int photos) {
		this.numPhotos = photos;
	}
	
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	
	
	
	public Date getNewestDate() {
		return newestDate;
	}
	public void setNewestDate(Date newestDate) {
		this.newestDate = newestDate;
	}

	public Date getOldestDate() {
		return oldestDate;
	}
	public void setOldestDate(Date oldestDate) {
		this.oldestDate = oldestDate;
	}
	
	

}
