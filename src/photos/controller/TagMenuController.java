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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import photos.model.Tag;
import photos.model.Users;
import photos.model.albums;
import photos.model.model;

/**
 * This controls the sub-menu for when a user deletes tags from an image
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class TagMenuController implements Initializable {

	@FXML private Button cancelButton;
	@FXML private Button deleteButton;
	@FXML private TableView<Tag> tableView;
	@FXML private TableColumn<albums,String> tagName;
	@FXML private TableColumn<albums,String> tagValue;
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
        //tableView.getColumns().addAll(albumName, photoNumbers, earliestImage, latestImage);

			
        
        //Step : 3#  Associate data with columns  
            tagName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tagValue.setCellValueFactory(new PropertyValueFactory<>("value"));
            
        //Step 4: add data inside table
            
           tableView.setItems(getTags());   
           tableView.getSelectionModel().select(0);
           
           //tableView.getItems().add(albuum1);
           System.out.println("WHY WONT YOU WORK TABLEVIEW??");
    }    
	@FXML
	private void deleteAction (ActionEvent event) {
		
		
		
		
		Tag deleteTag = tableView.getSelectionModel().getSelectedItem();
		Users.removeTagFromImage(model.URIdelete,deleteTag);
		
		
		//model.loadAlbum.get(model.photoIndex).gettagsList().remove(deleteTag);
		
		tableView.getColumns().get(0).setVisible(false);
		tableView.getColumns().get(0).setVisible(true);
        tableView.setItems(getTags());
		
        model.userAlbums.get(model.albumIndex).setImages(model.loadAlbum);
        model.user.setphotoAlbums(model.userAlbums);
        try {
			model.user.saveUser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@FXML
	private void cancelAction (ActionEvent event) {
		 Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
	
	private ObservableList<Tag> getTags(){
		 List<Tag> imageTags = model.loadAlbum.get(model.photoIndex).gettagsList();
        ObservableList<Tag> data = FXCollections.observableArrayList();
        
        for(Tag g: imageTags) {
   		 data.add(g);}
        
       System.out.println();
       return data;
        
	}
	
}
