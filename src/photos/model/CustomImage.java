package photos.model;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Calendar;
import java.util.Date;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * this is the Image object, for storing the image itself and any data associated with it (like tags)
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class CustomImage implements Serializable {

    private File image;
    private Date date; //this needs to be changed to something better, probably involving the java.util.calendar thing
	private List<Tag> tagsList = new ArrayList<Tag>();
	private String fileName;
	private URI fileURI;
	private String caption;
    

    public CustomImage(File img,Date date,String fileName, URI fileURI) {
        this.image = img;
        this.date = date;
        this.fileName = fileName;
        this.fileURI = fileURI;
    }

    /*public void setImage(File value) {
        image = value;
    }*/
    
    public ImageView getImage() {
    	ImageView myImageView = new ImageView();
        BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(image);
			Image image2 = SwingFXUtils.toFXImage(bufferedImage, null);        
	        myImageView.setImage(image2);
            myImageView.setFitHeight(100);
            myImageView.setFitWidth(100);
            myImageView.setPreserveRatio(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return myImageView;
    }
    public Date getDate() {
        return date;
    }
    
	void addTag(Tag t) {
		tagsList.add(t);
	}
	void deleteTag(Tag t) {
		tagsList.remove(t);
	}
	public List<Tag> gettagsList(){
		return tagsList;
	}
	public String getFileName() {
		return fileName;
	}
	public URI getfileURI() {
		return fileURI;
	}
	public String getCaption() {
		return caption;
	}
	void setCaption(String newCaption) {
		caption = newCaption;
	}
}