package photos.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import photos.model.CustomImage;
import photos.model.Tag;
import photos.model.albums;
import photos.model.model;

/**
 * This controls the move-to action performed by the user on an image
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class MoveMenuController implements Initializable {

	@FXML private Button cancelButton;
	@FXML private Button moveButton;
	@FXML private TableView<albums> tableView;
	@FXML private TableColumn<albums,String> albumName;
	Alert error;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {

            albumName.setCellValueFactory(new PropertyValueFactory<>("albumName"));

           tableView.setItems(getAlbums());   
           tableView.getSelectionModel().select(0);

    }    
	@FXML
	private void moveAction (ActionEvent event) {
		
		int albumIndex = tableView.getSelectionModel().getSelectedIndex();
		boolean duplicate = false;
		
		for(CustomImage t: model.userAlbums.get(albumIndex).getImages()) {
			if(t.getfileURI().equals(model.loadAlbum.get(model.photoIndex).getfileURI())) {
				error = new Alert(AlertType.INFORMATION);
				error.setTitle("Error!");
				error.setHeaderText("Image Duplicate!");
				error.setContentText("Image already in album!");
				error.showAndWait();
				duplicate = true;
				break;
				
			}
		}
		
		if(duplicate==false) {
			model.userAlbums.get(albumIndex).getImages().add(model.loadAlbum.get(model.photoIndex));
			model.loadAlbum.remove(model.photoIndex);
			model.userAlbums.get(model.albumIndex).setImages(model.loadAlbum);
	        model.user.setphotoAlbums(model.userAlbums);
	        try {
				model.user.saveUser();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			   try {
			         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/photosAlbumMenu.fxml"));
			         Scene scene = new Scene(blah);
			         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			         appStage.setScene(scene);
			         appStage.show();
			        
			        
			        
			    } catch(Exception e) {
			        e.printStackTrace();
			    }
		}
		
	}
	@FXML
	private void cancelAction (ActionEvent event) {
		   try {
		         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/photosAlbumMenu.fxml"));
		         Scene scene = new Scene(blah);
		         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		         appStage.setScene(scene);
		         appStage.show();
		        
		        
		        
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
	}
	
	private ObservableList<albums> getAlbums(){
		 ArrayList<albums> userAlbums = model.userAlbums;
        ObservableList<albums> data = FXCollections.observableArrayList();
        
        for(albums album: userAlbums) {
   		 data.add(album);}
        
       System.out.println();
       return data;
        
	}
	
	
	
	
}
