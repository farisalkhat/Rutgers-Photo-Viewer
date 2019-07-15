package photos.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


/**
 * this is the model object, for manipulating the list of all users
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class model {
	
	public static List<Users>userDatabase = new ArrayList<Users>();

	public static int userNum;
	
	public static Users user;	//Logged in user
	
	public static ArrayList<albums> userAlbums = new ArrayList<albums>(); //User's list of albums.
	
	public static List<CustomImage> loadAlbum = new ArrayList<CustomImage>(); 
	//Selected album's list of images. 
	
	public static int displayImage = 0;
	public static int albumIndex;
	
	public static int photoIndex =0;
	
	public static int imageIndex;
	
	public static URI URIdelete;
	
	public static void saveUserDatabase(List<Users> TuL) throws IOException{

		ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream("save/userDatabase" + ".dat"));
		oos.writeObject(TuL);
		oos.close();
	}

	public static List<Users> LoadUserDatabase() throws IOException, ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream("save/userDatabase" + ".dat"));
		List<Users> i = (ArrayList)ois.readObject();
		ois.close();
		return i;
	}

	





}