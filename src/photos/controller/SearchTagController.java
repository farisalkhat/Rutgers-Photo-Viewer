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
 * This controls the window that is opened upon the user attempting to search their albums by Tag
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class SearchTagController implements Initializable{
	@FXML private Button SearchButton;
	@FXML private Button CreateNewButton;
	@FXML private Button CancelNewButton;
	@FXML private Button CreateButton;
	@FXML private Button CancelButton;

	@FXML private TextField Tag;
	@FXML private TextField AlbumName;
	
	@FXML private TableColumn<CustomImage,ImageView> ImageColumn;
	@FXML private TableColumn<CustomImage,Date> NameColumn;
	@FXML TableView<CustomImage> tableView;

	ObservableList<CustomImage>searchResult;
	List<CustomImage>rtmp;
	Alert error;
	public void start(Stage mainStage) {
		
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb){

	ImageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
       NameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
       tableView.getSelectionModel().select(0);
       //This sets up the tableview. 
	}



	@FXML
	private void SearchAction (ActionEvent event) {
		String tag = Tag.getText();

		if(tag.isEmpty()) {
			error = new Alert(AlertType.INFORMATION);
            error.setTitle("Error!");
            error.setHeaderText("No Date");
            error.setContentText("Please enter a date");
            error.showAndWait();
		}
		
		
		  rtmp = model.user.searchByTag(tag);
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
			Tag.setDisable(true);
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
			    }}
		
			 
		
	}
	@FXML
	private void CancelAction (ActionEvent event) {
		Tag.setDisable(false);
		SearchButton.setDisable(false);
		CreateNewButton.setDisable(false);
		
		CreateButton.setDisable(true);
		CancelButton.setDisable(true);
		AlbumName.setDisable(true);
	}
	
	@FXML
	private void CancelNewAction (ActionEvent event) {
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
