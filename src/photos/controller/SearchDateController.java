package photos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

import photos.model.*;

/**
 * This controls the window that is opened upon the user attempting to search their albums by date
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class SearchDateController implements Initializable{
	@FXML private Button SearchButton;
	@FXML private Button CreateNewButton;
	@FXML private Button CancelNewButton;
	@FXML private Button CreateButton;
	@FXML private Button CancelButton;

	@FXML private TextField Date1;
	@FXML private TextField Date2;
	@FXML private TextField AlbumName;
	
	@FXML private TableColumn<CustomImage,ImageView> ImageColumn;
	@FXML private TableColumn<CustomImage,Date> NameColumn;
	@FXML TableView<CustomImage> tableView;
	List<CustomImage> rtmp = new ArrayList<CustomImage>();
	ObservableList<CustomImage>searchResult;
	
	Alert error;
	public void start(Stage mainStage) {
		
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb){
       Date1.setPromptText("dd/MM/yyy");
       Date2.setPromptText("dd/MM/yyy");
		ImageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
       NameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
       tableView.getSelectionModel().select(0);
       //This sets up the tableview. 
	}



	@FXML
	private void SearchAction (ActionEvent event) {
		String date1 = Date1.getText();
		String date2 = Date2.getText();
		
		if(date1.isEmpty() || date2.isEmpty()) {
			error = new Alert(AlertType.INFORMATION);
            error.setTitle("Error!");
            error.setHeaderText("No Date");
            error.setContentText("Please enter a date");
            error.showAndWait();
		}
		//Pressing the search button causes this command to run. You want to find all images in all albums that are within the date range.
		//Also make sure the user inputs correct date format. 
		if(!(Users.checkDatesFormat(date1,date2))) {
			error = new Alert(AlertType.INFORMATION);
            error.setTitle("Error!");
            error.setHeaderText("Incorrect Date Format!");
            error.setContentText("Please enter a date of the form dd/MM/yyyy");
            error.showAndWait();
		}
		//Check dates across all albums. 
		//You should use a helper method () to create a list of CustomImages based on search result. 
		
		try {
			rtmp = model.user.searchByDates(date1,date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Do this to update the tableview to show all the images from the search results:
		searchResult = getAlbumImages(rtmp);
		tableView.setItems(searchResult); 
		//Create error(?) if no search results
		if(searchResult.isEmpty()) {
			error = new Alert(AlertType.INFORMATION);
            error.setTitle("Error!");
            error.setHeaderText("Empty Search");
            error.setContentText("No Results Found");
            error.showAndWait();
            
            CreateNewButton.setDisable(true);
		}
		else {
			CreateNewButton.setDisable(false);
			
		}

		
	}
	@FXML
	private void CreateNewAction (ActionEvent event) {
		if(rtmp.isEmpty()){
			error = new Alert(AlertType.INFORMATION);
            error.setTitle("Error!");
            error.setHeaderText("Empty Search");
            error.setContentText("How did we get here??");
            error.showAndWait();
			
			//Just create an error here if the search result was empty. 	
		}
		else {
			//This sets up the GUI for creating a new album.
			Date1.setDisable(true);
			Date2.setDisable(true);
			SearchButton.setDisable(true);
			CreateNewButton.setDisable(true);	
			
			CreateButton.setDisable(false);
			CancelButton.setDisable(false);
			AlbumName.setDisable(false);
			
		}
		
	}
	@FXML
	private void CreateAction (ActionEvent event) {
		String albumname = AlbumName.getText();
		
		String albumNameUn = AlbumName.getText();
		String albumName = AlbumName.getText().toLowerCase();
		boolean match = checkMatch(albumName,model.userAlbums);
		
		if (match == true) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("Duplicate entry!");
			error.setContentText("The entry you've entered is a duplicate. Therefore, your input has been cancelled.");
			error.showAndWait();
		}
		
		else {
			albums newAlbum = new albums(albumname);
			newAlbum.setImages(rtmp);
			model.userAlbums.add(newAlbum);
			model.user.setphotoAlbums(model.userAlbums);
			 try {
				model.user.saveUser();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 
			    try {
			         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/photosUserMenu.fxml"));
			         Scene scene = new Scene(blah);
			         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			         appStage.setScene(scene);
			         appStage.show();
			    } catch(Exception e) {
			        e.printStackTrace();
			    }
			 
			 
			 
			 
			 
			 
			 
			 
			 
		}
		
		
		
		
		
		//Check if album name exists, and return error if it does.
		//Otherwise, use that new album you filtered and throw it in there.
		//To save the album in the user, do this:
		//1. Create a new album to contain the search results
		//2. Add the photos from the search results to the new album
		//3. Do this:
		/*
		 * 	model.userAlbums.add(arg0);
	    	model.user.setphotoAlbums(model.userAlbums);
	        try {
				model.user.saveUser();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 * 
		 * The above code adds your new album(arg0) to the global variable holding the user's albums. You then reset the albums for the user.
		 * The user is then saved, and then youre done!
		 * You should just close the window now, copypaste from the cancelnewaction event.
		 */

	}
	@FXML
	private void CancelAction (ActionEvent event) {
		//Don't need to do anything here.
		Date1.setDisable(false);
		Date2.setDisable(false);
		SearchButton.setDisable(false);
		CreateNewButton.setDisable(false);
		
		CreateButton.setDisable(true);
		CancelButton.setDisable(true);
		AlbumName.setDisable(true);
	}
	
	@FXML
	private void CancelNewAction (ActionEvent event) {
		//Dont need to do anything here. 
	    try {
	         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/photosUserMenu.fxml"));
	         Scene scene = new Scene(blah);
	         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	         appStage.setScene(scene);
	         appStage.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	private ObservableList<CustomImage> getAlbumImages(List<CustomImage> t) {
         ObservableList<CustomImage> data = FXCollections.observableArrayList();
         for(CustomImage image: t) {
    		 data.add(image);}
         //This is an example of how to add customimages to an observablelist.
         //model.loadAlbum is a global variable for an album that was opened up.
         //OBVIOUSLY we dont need model.loadAlbum, so switch that up according to what you need.
         //I'd assume you need a double for loop to through the customimage lists of all the albums
         //And check if the dates match. You're going to need an additional for loop 
         //to make sure they're not duplicates. I'd assume you did that already with your methods, but I write it here anyway. 
        return data;
	}
	
	

	

	


	private boolean checkMatch(String albumName,ArrayList<albums>albums) {
   	 	for(albums album: albums) {
   	 		if(albumName.equals(album.getAlbumName().toLowerCase())) {
   	 			return true;
   	 		}
   	 	} 
		return false;
	}




	/*	ObservableList<CustomImage> imgList = FXCollections.observableArrayList();
	for(CustomImage image: model.loadAlbum) {
	imgList.add(image); }
		tableView.setItems(imgList);

 * 
 */





	
}
