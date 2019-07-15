package photos.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import photos.model.CustomImage;
import photos.model.model;

/**
 * This controls the display pop-up window for an album
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class photosDisplayController implements Initializable{

	int currentImage = model.displayImage;
	@FXML private Button prevButton;
	@FXML private Button nextButton;
	@FXML private ImageView featured;
	ObservableList<CustomImage> imageList = getAlbumImages();
	

	public void start(Stage mainStage) {
		ObservableList<CustomImage> imageList = getAlbumImages();
        Image image = new Image(imageList.get(0).getfileURI().toString());
        featured.setImage(image);
		//featured = imageList.get(0).getImage();
        setValidButtons();
	}
	/* When the scene is started, it gathers the list of album images and puts it into an observable list to be displayed on tableview.
	 * It then sets the ImageView featured to whatever image was selected when the display button was pressed.
	 *  setValidButtons() is then run to determine if the buttons should be highlighted.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb){
		ObservableList<CustomImage> imageList = getAlbumImages();
        Image image = new Image(imageList.get(currentImage).getfileURI().toString());
        featured.setImage(image);	
		//featured = imageList.get(0).getImage();
        setValidButtons();
        
	}
	/* Helper method for disabling/enabling buttons.
	 * 
	 */
	private void setValidButtons() {
		if (currentImage-1<0){
			prevButton.setDisable(true);
		}
		else {
			prevButton.setDisable(false);
		}
		if(currentImage+1>imageList.size()-1) {
			nextButton.setDisable(true);
		}
		else {
			nextButton.setDisable(false);
		}
	}

	/* Action for when the the next button is pressed.
	 * Updates the display to the next image, and then checks if the buttons need to be enabled/disabled.
	 * 
	 */
	@FXML
	private void nextImage (ActionEvent event) {
		currentImage++;
		ObservableList<CustomImage> imageList = getAlbumImages();
        Image image = new Image(imageList.get(currentImage).getfileURI().toString());
        featured.setImage(image);	
		setValidButtons();
	}
	/* Action for when the the prev button is pressed.
	 * Updates the display to the previous image, and then checks if the buttons need to be enabled/disabled.
	 * 
	 */
	@FXML
	private void previousImage (ActionEvent event) {
		currentImage--;
		ObservableList<CustomImage> imageList = getAlbumImages();
        Image image = new Image(imageList.get(currentImage).getfileURI().toString());
        featured.setImage(image);	
		setValidButtons();
	}
	/* Helper method
	 * Gets the list of images for the display in observablelist format in order to be displayed on the tableView.
	 */
	
	private ObservableList<CustomImage> getAlbumImages() {
        ObservableList<CustomImage> data = FXCollections.observableArrayList();
        for(CustomImage image: model.loadAlbum) {
   		 data.add(image);}
       return data;
	}
	
	
	
	
	
}
