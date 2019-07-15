package photos.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.net.URI;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * this is the User object, for methods associated with user-wide actions
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class Users implements Serializable {
	
	
	private String username;
	private List<albums> photoAlbums = new ArrayList<albums>();
	
	
	
	
	public Users(String username) {
		this.username = username;
		this.photoAlbums = new ArrayList<albums>();
	}
	public String getUsername() {
		return username;
	}
	public List<albums> getphotoAlbums(){
		return photoAlbums;
	}
	
	
	public void setphotoAlbums(List<albums> newphotoAlbums){
		photoAlbums = newphotoAlbums;
	}
	
	public void saveUser() throws IOException{
		
		ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream("save/users/" + username + ".dat"));
		oos.writeObject(this);
		oos.close();
	}
	
	public static Users LoadUser(String Tu) throws IOException, ClassNotFoundException {
		
		ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream("save/users/" + Tu + ".dat"));
		Users i = (Users)ois.readObject();
		ois.close();
		return i;
	}
	
	//copies a given image into a given album
	public static boolean copyPhotoTo(CustomImage t, albums d) {
		List<CustomImage> tmp = d.getImages();
		if(tmp.contains(t)){
			return false;
		}
		tmp.add(t);
		
		return true;
	}
	
	//copies a given image into a given album, removes it from current album
	public static boolean movePhotoTo(albums s, CustomImage t, albums d) {
		List<CustomImage> tmp = d.getImages();
		if(tmp.contains(t)){
			return false;
		}
		tmp.add(t);
		tmp = s.getImages();
		tmp.remove(t);
		
		return true;
	}
	
	public List<CustomImage> searchByTag(String s) {
		int filter = checkSearchString(s);
		List<Tag> Ttags = parseSearchString(s, filter);
		List<CustomImage> returnList = new ArrayList<CustomImage>();
		
		List<URI> alreadyAdded = new ArrayList<URI>();
		
		List<albums> albumCheck = this.getphotoAlbums();
		for(int i = 0; i < albumCheck.size();i++) {
			albums tmpAlbum = albumCheck.get(i);
			List<CustomImage> imageCheck = tmpAlbum.getImages();
			for(int j = 0; j < imageCheck.size(); j++) {
				URI tmpURI = imageCheck.get(j).getfileURI();
				if((!(alreadyAdded.contains(tmpURI)))) {
					List<Tag> itags = imageCheck.get(j).gettagsList();
					
					if(filter == 2) {//see if it matches both tags
						for(int k = 0; k < itags.size(); k++) {
							int exit = 0;
							if(itags.get(k).equals(Ttags.get(0))) {
								for(int l = 0; l < itags.size(); l++) {
									if(itags.get(l).equals(Ttags.get(1))) {
										alreadyAdded.add(tmpURI);
										returnList.add(imageCheck.get(j));
										exit=1;
										break;
									}
								}
							}
							if(exit == 1) {break;}
						}
					}else if(filter == 3) {//see if it matches or tag
						int hasTag = 0;
						for(int k = 0; k < itags.size(); k++) {
							if(itags.get(k).equals(Ttags.get(0))) {
								hasTag = 1;
								break;
							}
						}
						if(hasTag == 0) {
							for(int k = 0; k < itags.size(); k++) {
								if(itags.get(k).equals(Ttags.get(1))) {
									hasTag = 1;
									break;
								}
							}
						}
						if(hasTag == 1) {
							alreadyAdded.add(tmpURI);
							returnList.add(imageCheck.get(j));
						}
					}else if(filter == 1) {//see if it matches one tag
						int hasTag = 0;
						for(int k = 0; k < itags.size(); k++) {
							if(itags.get(k).equals(Ttags.get(0))) {
								alreadyAdded.add(tmpURI);
								returnList.add(imageCheck.get(j));
								break;
							}
						}
					}
				}
			}
		}
		return returnList;
	}
	
	//returns 0 if not a proper format, 1 if SINGLE, 2 if AND, 3 if OR
	public static int checkSearchString(String s) {
		int ft = s.indexOf(" AND ");
		if(ft != -1) {//string is in AND format
			int tt = s.indexOf("=");
			if(tt == -1 || tt == 0 || tt == (ft-1)) {return 0;}
			int st = s.indexOf("=",(tt+1));
			if(st == -1 || !(st > (ft+5)) || st == (s.length()-1) ) {return 0;}
			return 2;
		}
		ft = s.indexOf(" OR ");
		if(ft != -1) {//string is in OR format
			int tt = s.indexOf("=");
			if(tt == -1 || tt == 0 || tt == (ft-1)) {return 0;}
			int st = s.indexOf("=",(tt+1));
			if(st == -1 || !(st > (ft+4)) || st == (s.length()-1) ) {return 0;}		
			return 3;
		}
		ft = s.indexOf("=");
		if(ft != -1) {//string is in SINGLE format
			if(ft == 0 || ft == (s.length()-1)) {return 0;}
			int st = s.indexOf("=",(ft+1));
			if(st != -1) {return 0;}
			return 1;
		}
		return 0;
	}
	
	private List<Tag> parseSearchString(String s, int f) {
		List<Tag> returnList = new ArrayList<Tag>();
		
		if(f == 2) {//AND format
			int indexOfFirstEquals = s.indexOf("=");
			int indexOfSecondEquals = s.indexOf("=",(indexOfFirstEquals+1));
			int indexOfAND = s.indexOf(" AND ");
			
			String a = s.substring(0, indexOfFirstEquals);
			String firstName = a.replaceAll("\\s+","");
			a = s.substring((indexOfFirstEquals+1), indexOfAND);
			String firstValue = a.replaceAll("\\s+","");
			a = s.substring((indexOfAND+5), indexOfSecondEquals);
			String secondName = a.replaceAll("\\s+","");
			a = s.substring((indexOfSecondEquals+1),s.length());
			String secondValue = a.replaceAll("\\s+","");
			
			Tag first = new Tag(firstName, firstValue);
			Tag second = new Tag(secondName,secondValue);
			returnList.add(first);
			returnList.add(second);
			
		}else if(f == 3){//OR format
			int indexOfFirstEquals = s.indexOf("=");
			int indexOfSecondEquals = s.indexOf("=",(indexOfFirstEquals+1));
			int indexOfAND = s.indexOf(" AND ");
			
			String a = s.substring(0, indexOfFirstEquals);
			String firstName = a.replaceAll("\\s+","");
			a = s.substring((indexOfFirstEquals+1), indexOfAND);
			String firstValue = a.replaceAll("\\s+","");
			a = s.substring((indexOfAND+4), indexOfSecondEquals);
			String secondName = a.replaceAll("\\s+","");
			a = s.substring((indexOfSecondEquals+1),s.length());
			String secondValue = a.replaceAll("\\s+","");
			
			Tag first = new Tag(firstName, firstValue);
			Tag second = new Tag(secondName,secondValue);
			returnList.add(first);
			returnList.add(second);
		}else if(f == 1){//SINGLE format
			int indexOfFirstEquals = s.indexOf("=");
			
			String a = s.substring(0, indexOfFirstEquals);
			String firstName = a.replaceAll("\\s+","");
			a = s.substring((indexOfFirstEquals+1), s.length());
			String firstValue = a.replaceAll("\\s+","");

			
			Tag first = new Tag(firstName, firstValue);
			returnList.add(first);
		}
		
		return returnList;
	}
	
	
	public static boolean checkDatesFormat(String s, String e) {
		if(s.length() != 10) {
			return false;
		}
		if(!(Character.isDigit(s.charAt(0))) || !(Character.isDigit(s.charAt(1)))){
			return false;
		}
		if(!(s.charAt(2) == '/')){
			return false;
		}
		if(!(Character.isDigit(s.charAt(3))) || !(Character.isDigit(s.charAt(4)))){
			return false;
		}
		if(!(s.charAt(5) == '/')){
			return false;
		}
		if(!(Character.isDigit(s.charAt(6))) || !(Character.isDigit(s.charAt(7))) || !(Character.isDigit(s.charAt(8))) || !(Character.isDigit(s.charAt(9)))){
			return false;
		}
		
		if(e.length() != 10) {
			return false;
		}
		if(!(Character.isDigit(e.charAt(0))) || !(Character.isDigit(e.charAt(1)))){
			return false;
		}
		if(!(e.charAt(2) == '/')){
			return false;
		}
		if(!(Character.isDigit(e.charAt(3))) || !(Character.isDigit(e.charAt(4)))){
			return false;
		}
		if(!(e.charAt(5) == '/')){
			return false;
		}
		if(!(Character.isDigit(e.charAt(6))) || !(Character.isDigit(e.charAt(7))) || !(Character.isDigit(e.charAt(8))) || !(Character.isDigit(e.charAt(9)))){
			return false;
		}
		
		return true;
	}
	
	public List<CustomImage> searchByDates(String s, String e) throws ParseException{
		List<CustomImage> returnList = new ArrayList<CustomImage>();
		
		Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(s);
		Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(e);
		List<URI> alreadyAdded = new ArrayList<URI>();
		
		List<albums> albumCheck = this.getphotoAlbums();
		for(int i = 0; i < albumCheck.size();i++) {
			albums tmpAlbum = albumCheck.get(i);
			List<CustomImage> imageCheck = tmpAlbum.getImages();
			for(int j = 0; j < imageCheck.size(); j++) {
				Date tmpDATE = imageCheck.get(j).getDate();
				URI tmpURI = imageCheck.get(j).getfileURI();
				if((tmpDATE.after(date1)) && (tmpDATE.before(date2)) && (!(alreadyAdded.contains(tmpURI)))) {
					returnList.add(imageCheck.get(j));
					alreadyAdded.add(tmpURI);
				}
			}
		}
		return returnList;
	}
	
	public static void addTagToImage(URI TURI,Tag Tt) {
		for(int i = 0; i < model.userAlbums.size();i++) {
			albums tmpAlbum = model.userAlbums.get(i);
			List<CustomImage> imageCheck = tmpAlbum.getImages();
			for(int j = 0; j < imageCheck.size(); j++ ) {
				URI tmpURI = imageCheck.get(j).getfileURI();
				if(tmpURI.equals(TURI)) {
					List<Tag> tags = imageCheck.get(j).gettagsList();
					boolean exists = true;
					for(int k = 0; k < tags.size(); k++) {
						if(tags.get(k).equals(Tt)) {
							exists = false;
						}
					}
					if(exists) {
						imageCheck.get(j).addTag(Tt);
					}
				}
			}
			
		}
		

	}
	
	public static void removeTagFromImage(URI TURI, Tag Tt) {
		
		for(int i = 0; i < model.userAlbums.size();i++) {
			albums tmpAlbum = model.userAlbums.get(i);
			List<CustomImage> imageCheck = tmpAlbum.getImages();
			for(int j = 0; j < imageCheck.size(); j++ ) {
				URI tmpURI = imageCheck.get(j).getfileURI();
				if(tmpURI.equals(TURI)) {
					imageCheck.get(j).deleteTag(Tt);
				}
			}
			
		}
		

	}
	
	public static void setCaptionToImage(URI TURI, String Tc) {
		boolean checker = true;
		for(int i = 0; i < model.userAlbums.size();i++) {
			albums tmpAlbum = model.userAlbums.get(i);
			List<CustomImage> imageCheck = tmpAlbum.getImages();
			for(int j = 0; j < imageCheck.size(); j++ ) {
				URI tmpURI = imageCheck.get(j).getfileURI();
				if(tmpURI.equals(TURI)) {
					imageCheck.get(j).setCaption(Tc);;
					checker = false;
				}
			}
			
		}
		
	}
}
