package photos.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import photos.model.*;

/**
 * This controls the user sub-system upon the user logging in
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class photosController implements Initializable{
	@FXML TableView<albums> tableView;
	@FXML private Button Delete;
	@FXML private Button Rename;
	@FXML private Button Create;
	@FXML private Button Open;
	@FXML private Button Logout;
	
	@FXML private TextField albumField;
	@FXML private TextField renameField;
	@FXML private Button createButton;
	@FXML private Button createCancel;
	@FXML private Button renameButton;	@FXML private Button deleteButton;
	@FXML private Button SearchTag;
	@FXML private Button SearchDate;
	
	

	ObservableList<albums> data = FXCollections.observableArrayList();
	
	@FXML private TableColumn<albums,String> albumName;
	@FXML private TableColumn<albums,String> photoNumbers;
	@FXML private TableColumn<albums,String> earliestImage;
	@FXML private TableColumn<albums,String> latestImage;
	Alert error;
	public void start(Stage mainStage) {
		
		
		//ObservableList<albums> albums = FXCollections.observableArrayList();
		//tableView.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)->showDetails(mainStage));
		
	}
	/* Runs the setProperties() method, which updates all of the user album's following variables: # of photos, earliest/latest date of an image.
	 * Sets up all the tableView columns by pulling the information from all of the users albums.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
        //tableView.getColumns().addAll(albumName, photoNumbers, earliestImage, latestImage);

			setProperties();
        
        //Step : 3#  Associate data with columns  
            albumName.setCellValueFactory(new PropertyValueFactory<>("albumName"));
            photoNumbers.setCellValueFactory(new PropertyValueFactory<>("photos"));
            earliestImage.setCellValueFactory(new PropertyValueFactory<>("oldestDate"));
            latestImage.setCellValueFactory(new PropertyValueFactory<>("newestDate"));
        //Step 4: add data inside table
            
           tableView.setItems(getAlbums());   
           tableView.getSelectionModel().select(0);
           
           //tableView.getItems().add(albuum1);
           System.out.println("WHY WONT YOU WORK TABLEVIEW??");
    }    
	
	
	/* Helper method. 
	 * Goes through all of the albums of the user currently logged in, and does the following:
	 * Goes through all the photos in each album, recording the earliest date, latest date, and total number of photos.
	 * At the end of the for loop, updates that album so it remembers this information. 
	 */
	
	private void setProperties() {
		
		int i=0;
		Date oldestDate = new Date(0);
		Date newestDate = new Date(0);
		
		
		for (albums album: model.userAlbums) {
			List<Date> listOfDates = new ArrayList<Date>();

			
			
			for(CustomImage images : album.getImages()) {
					listOfDates.add(images.getDate());
			}
			
			if(listOfDates.isEmpty()==false) {
				newestDate = Collections.max(listOfDates);
				oldestDate = Collections.min(listOfDates);
					
				album.setOldestDate(oldestDate);
				album.setNewestDate(newestDate);
				album.setPhotos(album.getImages().size());
			}

			
			
			}
		
		
		 model.user.setphotoAlbums(model.userAlbums);
		 try {
			model.user.saveUser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	
		
		
		}
		


	
	
	/* This action is for when the user selects the CREATE button.
	 * When selecting the create button, disables various GUI elements and enables others.
	 */
	
	@FXML
	private void handleCreateAction(ActionEvent event) {
		
			buttonDisables();
			albumField.setVisible(true);
			albumField.setDisable(false);
			createButton.setVisible(true);
			createButton.setDisable(false);
			createCancel.setVisible(true);
			createCancel.setDisable(false);	
			
		
	}
	
	/* This action is for when the user presses the CREATE button that appears after pressing the initial CREATE button.
	 * Takes the input the user provided, and goes through all the albums they have created.
	 * If the album name already exists, an error appears and the GUI is returned to normal.
	 * Otherwise, the album is added to the user and it updates the tableView for the album to appear.
	 */
	
	@FXML
	private void handleCreateAlbum(ActionEvent event) {
		
			String albumNameUn = albumField.getText();
			String albumName = albumField.getText().toLowerCase();
			boolean match = checkMatch(albumName,model.userAlbums);
			
			if (match == true) {
				error = new Alert(AlertType.INFORMATION);
				error.setTitle("Error!");
				error.setHeaderText("Duplicate entry!");
				error.setContentText("The entry you've entered is a duplicate. Therefore, your input has been cancelled.");
				error.showAndWait();
				cancelCommands();
				albumField.setVisible(false);
				albumField.setDisable(true);
				createButton.setVisible(false);
				createButton.setDisable(true);
				
			}
			
			else {
				
				model.userAlbums.add( new albums(albumNameUn));
				model.user.setphotoAlbums(model.userAlbums);
				 try {
					model.user.saveUser();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				tableView.setItems(getAlbums());
				cancelCommands();
				albumField.setVisible(false);
				albumField.setDisable(true);
				createButton.setVisible(false);
				createButton.setDisable(true);
				tableView.getSelectionModel().select(tableView.getItems().size()-1);
			}
			
		

	}
	
	/* This action is for after the user selects the create button.
	 * Pressing this cancel button will return the GUI to normal.
	 */
	
	@FXML
	private void handleCancelAction(ActionEvent event) {
		
			cancelCommands();
			if(albumField.isVisible()==true) {
				albumField.setVisible(false);
				albumField.setDisable(true);				
			}
			if(createButton.isVisible()==true) {
				createButton.setVisible(false);
				createButton.setDisable(true);				
			}
			if(renameField.isVisible()==true) {
				renameField.setVisible(false);
				renameField.setDisable(true);	
			}
			if(renameButton.isVisible()==true) {
				renameButton.setVisible(false);
				renameButton.setDisable(true);	
			}

			if(deleteButton.isDisable()==false) {
				deleteButton.setDisable(false);
			}
			if(deleteButton.isVisible()==true) {
				deleteButton.setVisible(false);
			}
		
		
	}
	
	
	/* This action is for when the user selects the RENAME button.
	 * Upon selecting it, it selects the highlighted album and sets up the GUI for the user to rename the album.
	 * If the user has no albums, an error pops up and returns the GUI to normal.
	 */
	@FXML
	private void handleRenameAction(ActionEvent event) {
		
		if (model.userAlbums.isEmpty() == true) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("No item selected!");
			error.setContentText("There is no item selected. Try again.");
			error.showAndWait();
			cancelCommands();
			renameField.setVisible(false);
			renameField.setDisable(true);
			renameButton.setVisible(false);
			renameButton.setDisable(true);
		}
		else {
			buttonDisables();
			renameField.setVisible(true);
			renameField.setDisable(false);
			renameButton.setVisible(true);
			renameButton.setDisable(false);
			createCancel.setVisible(true);
			createCancel.setDisable(false);				
		}

		
		
		
	}
	
	/* This action is for when the user presses the rename button that appears after the initial rename button.
	 * Takes the input from the textField that appeared, and sees if the album name exists.
	 * If it doesn't, renames the album and updates the tableView.
	 * Otherwise, produces an error and returns the GUI back to normal.
	 */
	
	
	@FXML
	private void handleRenameAlbum(ActionEvent event) {
			String albumNameUn = renameField.getText();
			String albumName = renameField.getText().toLowerCase();
			boolean match = checkMatch(albumName,model.userAlbums);
			
			
			
			if (match == true) {
				error = new Alert(AlertType.INFORMATION);
				error.setTitle("Error!");
				error.setHeaderText("Duplicate entry!");
				error.setContentText("The entry you've entered is a duplicate. Therefore, your input has been cancelled.");
				error.showAndWait();
				cancelCommands();
				renameField.setVisible(false);
				renameField.setDisable(true);
				renameButton.setVisible(false);
				renameButton.setDisable(true);
				
			}
			
			else {
				SimpleStringProperty lmao = new SimpleStringProperty(albumNameUn);
				String name2beChanged = tableView.getSelectionModel().getSelectedItem().getAlbumName();
				updateUserAlbums(name2beChanged, model.userAlbums, albumNameUn);
				
				 model.user.setphotoAlbums(model.userAlbums);
				 try {
					model.user.saveUser();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				tableView.getColumns().get(0).setVisible(false);
				tableView.getColumns().get(0).setVisible(true);
				tableView.setItems(getAlbums());
			
				tableView.getSelectionModel().select(tableView.getSelectionModel().getSelectedIndex());
				
				cancelCommands();
				renameField.setVisible(false);
				renameField.setDisable(true);
				renameButton.setVisible(false);
				renameButton.setDisable(true);
				
				

			}
			
		
	}
	
	
	
	
	
	

	
	/* This action is for when the user selects the delete button after the initial delete button was selected.
	 * Upon pressing it, the album currently highlighted is deleted.
	 * 
	 */
	
	

	@FXML
	private void handleDeleteAlbum(ActionEvent event) {
		albums name2beChanged = tableView.getSelectionModel().getSelectedItem();
		deleteAlbum(model.userAlbums,name2beChanged);
		
		
		 model.user.setphotoAlbums(model.userAlbums);
		 try {
			model.user.saveUser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		tableView.setItems(getAlbums());
		cancelCommands();
		deleteButton.setVisible(false);
		deleteButton.setDisable(true);
	}
	/* Helper method. 
	 * Goes into the user's albums and removes the one that the user selected to delete. 
	 */
	
	private void deleteAlbum(ArrayList<albums> userAlbums, albums name2beChanged) {
		userAlbums.remove(name2beChanged);
		 model.user.setphotoAlbums(model.userAlbums);
		 try {
			model.user.saveUser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleSearchTag(ActionEvent event) {	
	    try {
	         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/SearchTagMenu.fxml"));
	         Scene scene = new Scene(blah);
	         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	         appStage.setScene(scene);
	         appStage.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	private void handleSearchDate(ActionEvent event) {	
	    try {
	         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/SearchDateMenu.fxml"));
	         Scene scene = new Scene(blah);
	         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	         appStage.setScene(scene);
	         appStage.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	/* This action is for when the user selects the delete button.
	 * If there are no albums, an error pops up and returns the GUI to normal.
	 * Otherwise, the GUI is set up for deletion.
	 */
	@FXML
	private void handleDeleteAction(ActionEvent event) {		
		if (model.userAlbums.isEmpty() == true) {
		error = new Alert(AlertType.INFORMATION);
		error.setTitle("Error!");
		error.setHeaderText("No item selected!");
		error.setContentText("There is no item selected. Try again.");
		error.showAndWait();
		cancelCommands();
		renameField.setVisible(false);
		renameField.setDisable(true);
		renameButton.setVisible(false);
		renameButton.setDisable(true);
	}
		else {
			buttonDisables();	
			deleteButton.setVisible(true);
			deleteButton.setDisable(false);
			createCancel.setVisible(true);
			createCancel.setDisable(false);		
			
			
		}
		
	}
	
	/* This action is for when the open button is selected. 
	 * Produces an error if no albums exist when selected.
	 * Otherwise, we set the global variable loadAlbums to the album that was selected.
	 * A window opens to view images, loading the information from the loadAlbums global variable.
	 */
	
	@FXML
	private void handleOpenAction(ActionEvent event) throws IOException {
		if (model.userAlbums.isEmpty() == true) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("No item selected!");
			error.setContentText("There is no item selected. Try again.");
			error.showAndWait();
			
		}
		
		else {
			
			
			//Image image = new Image(new File("farisscary.png").toURI().toString(),100, 100, false, false);
		    //CustomImage item_2 = new CustomImage(new ImageView(image));
			
			model.albumIndex = tableView.getSelectionModel().getSelectedIndex();
			 
			model.loadAlbum = model.userAlbums.get(model.albumIndex).getImages();
			
			
			
			
			//CustomImage item = new CustomImage(new ImageView(new Image(new File("farisscary.png").toURI().toString(),100, 100, false, false)));
		    //selectedAlbum.getImages().add(item);
		    
		    
		   
			
			
			
			Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/photosAlbumMenu.fxml"));
		    Scene scene = new Scene(blah);
		    Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    appStage.setScene(scene);
		    appStage.show();	
		}
	    

	}
	
	
	
	@FXML
	private void handleSearchAction(ActionEvent event) {
		buttonDisables();
	}
	
	/* This action is for when the user selects the logout button.
	 * Before logging out, we first look through the global list of users, userDatabase.
	 * We find the user we have been modifying their albums for, and update their albums with the global variable userAlbums. 
	 * Then, we are returned to the login screen.
	 */
	@FXML
	private void handleLogoutAction(ActionEvent event) {
		try {
			model.saveUserDatabase(model.userDatabase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   try {
		         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/photosUserLogin.fxml"));
		         Scene scene = new Scene(blah);
		         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		         appStage.setScene(scene);
		         appStage.show();
		        
		        
		        
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
		
		
	}

	
	
	/* Helper method to disable buttons.
	 * 
	 */
	private void buttonDisables() {
		tableView.setDisable(true);
		
		Rename.setDisable(true);
		Delete.setDisable(true);
		SearchDate.setDisable(true);
		SearchTag.setDisable(true);
		Create.setDisable(true);
		Open.setDisable(true);
		Logout.setDisable(true);

		
		//Rename.setVisible(false);
		//Delete.setVisible(false);
		//Search.setVisible(false);
		//Create.setVisible(false);
		//Open.setVisible(false);
		
	
	}
	/* Helper method to enable buttons.
	 * 
	 */
	private void cancelCommands() {
		tableView.setDisable(false);
		
		Rename.setDisable(false);
		Delete.setDisable(false);
		SearchDate.setDisable(false);
		SearchTag.setDisable(false);
		Create.setDisable(false);
		Open.setDisable(false);
		Logout.setDisable(false);
		
		//Rename.setVisible(true);
		//Delete.setVisible(true);
		//Search.setVisible(true);
		//Create.setVisible(true);
		//Open.setVisible(true);
		
		
		if(createCancel.isVisible()==true) {
			createCancel.setVisible(false);
			createCancel.setDisable(true);			
		}
		
		

		
	}
	/* Helper method
	 * Gets the albums from the global variable userAlbums and creates an ObservableList for the tableView to display.
	 */
	
	private ObservableList<albums> getAlbums(){
		 ArrayList<albums> userAlbums = model.userAlbums;
         ObservableList<albums> data = FXCollections.observableArrayList();
         
         for(albums album: userAlbums) {
    		 data.add(album);}
         
        System.out.println();
        return data;
         
	}
	
	/* Helper method for renaming albums, updating the global variable.
	 */
	
	private void updateUserAlbums(String name2beChanged, ArrayList<albums> userAlbums, String albumName) {
		for (albums album: userAlbums) {
			if (album.getAlbumName().toLowerCase().equals(name2beChanged.toLowerCase())){
				album.setAlbumName(albumName);
			}
		}
	}

	/* Helper method for finding an album name match.
	 * 
	 */
	
	private boolean checkMatch(String albumName,ArrayList<albums>albums) {
   	 	for(albums album: albums) {
   	 		if(albumName.equals(album.getAlbumName().toLowerCase())) {
   	 			return true;
   	 		}
   	 	} 
		return false;
	}
}
