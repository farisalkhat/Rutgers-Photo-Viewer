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
 * This controls the display for when a user opens an album
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class photosAlbumController implements Initializable{
	@FXML private Button addButton;
	@FXML private Button deleteButton;
	@FXML private Button copyToButton;
	@FXML private Button moveToButton;
	@FXML private Button captionButton;
	@FXML private Button addTagButton;
	@FXML private Button deleteTagButton;
	@FXML private Button displayButton;
	
	@FXML private Button returnButton;
	
	@FXML private Button addCaptionButton;
	@FXML private Button cancelButton;
	@FXML private Button tagButton;
	@FXML private TextField tagField1;
	@FXML private TextField tagField2;
	@FXML private TextField captionField;
	@FXML private Label textLabel;
	@FXML private Label tagName;
	@FXML private Label tagValue;
	
	
	@FXML private TableColumn<CustomImage,ImageView> photoViewer;
	@FXML private TableColumn<CustomImage,Date> photoName;
	@FXML private TableColumn<CustomImage,ImageView> photoCaptions;

	@FXML TableView<CustomImage> tableView;


	
	Alert error;
	public void start(Stage mainStage) {
		
		
		//ObservableList<albums> albums = FXCollections.observableArrayList();
		//tableView.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)->showDetails(mainStage));
		
	}
	/*Initialize sets up the album photos viewer in the tableView.
	 * It utilizes the global variable loadAlbum. 
	 */
	
	@Override
	public void initialize(URL url, ResourceBundle rb){
		ObservableList<CustomImage> imgList = FXCollections.observableArrayList();
        for(CustomImage image: model.loadAlbum) {
        	imgList.add(image); }
        

       
       photoViewer.setCellValueFactory(new PropertyValueFactory<>("image"));
       photoName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
       photoCaptions.setCellValueFactory(new PropertyValueFactory<>("caption"));
       
       for(CustomImage image: model.loadAlbum) {
    	   System.out.println(image.getfileURI().toString());
       }
       
       
       tableView.setItems(imgList);
       tableView.getSelectionModel().select(0);
       System.out.println("Hello world!!");	
       
	}

	    


	
	/* Action for when the the add button is selected. 
	 * A file chooser appears, where the user can select either .jpg or .png images
	 * After selecting the image, it checks the file URI to see if it exists in the album.
	 * If it does, then an error pops up and returns the GUI to normal.
	 * Otherwise, creates a CustomImage object and adds it into the loadAlbum global variable.
	 */
	
	@FXML
	private void handleAddAction (ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
          
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        Date lastModDate = new Date(file.lastModified());
        String fileName = file.getName();
        URI fileUri = file.toURI();
        boolean check = checkDupe(fileUri);
        if(check==true) {
        	
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("Duplicate");
			error.setContentText("This file already exists in the album.");
			error.showAndWait();

        }
        
        else {
        	if(file.exists()) {
            	System.out.println("Hello world!!@#!@#!");
            }
            
            
            ImageView myImageView = new ImageView();
            ImageView fullSize = new ImageView();
            try {
           
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);        
                myImageView.setImage(image);
                fullSize.setImage(image);

                myImageView.setFitHeight(100);
                myImageView.setFitWidth(100);
                myImageView.setPreserveRatio(true);
                model.loadAlbum.add(new CustomImage(file,lastModDate,fileName,fileUri));
                model.userAlbums.get(model.albumIndex).setImages(model.loadAlbum);
                model.user.setphotoAlbums(model.userAlbums);
				 try {
					model.user.saveUser();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                
            } catch (IOException ex) {
                Logger.getLogger(photosAlbumController.class.getName()).log(Level.SEVERE, null, ex);
            }
    		
            
            
    		
            tableView.setItems(getAlbumImages());
            tableView.getSelectionModel().select(tableView.getItems().size()-1);
    		//CustomImage item = new CustomImage(new ImageView(new Image(new File("farisscary.png").toURI().toString(),100, 100, false, false)));
    	    //selectedAlbum.getImages().add(item);	
    		
    		System.out.println("Hello world!");
        	
        	
        	
        }
        
	}
	
	/* Helper method.
	 * Goes through all the CustomImages in loadAlbum and compares the fileUri. 
	 * Returns true if it matches, false if it doesnt. 
	 */
	
	private boolean checkDupe(URI fileUri) {
        for(CustomImage image: model.loadAlbum) {
   		 if(image.getfileURI().equals(fileUri)) {
   			 return true;
   			 }
   		 }

		return false;
	}
	
	/* Helper method.
	 * Updates the tableView by recreating the ObservableList for it.
	 */
	private ObservableList<CustomImage> getAlbumImages() {
         ObservableList<CustomImage> data = FXCollections.observableArrayList();
         for(CustomImage image: model.loadAlbum) {
    		 data.add(image);}
        return data;
	}
	
	/* Action for when the delete button is selected.
	 * If there are no photos, produces an error and returns GUI to normal.
	 * Otherwise, gets the file URI and goes through the global variable loadAlbum and deletes the CustomImage that matches the fileUri.
	 */
	@FXML
	private void handleDeleteAction (ActionEvent event) {
		if (model.loadAlbum.isEmpty() == true) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("Empty Album!");
			error.setContentText("There are no photos in the album.");
			error.showAndWait();
		}
		else {
			URI fileURI = tableView.getSelectionModel().getSelectedItem().getfileURI();
			int i = 0;
	        for(CustomImage image: model.loadAlbum) {
	   		 if(image.getfileURI().equals(fileURI)) {
	   			model.loadAlbum.remove(i);
	   			 break;
	   			 }
	   		 i++;
	   		 }
	        
            model.userAlbums.get(model.albumIndex).setImages(model.loadAlbum);
            model.user.setphotoAlbums(model.userAlbums);
			 try {
				model.user.saveUser();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
	        tableView.setItems(getAlbumImages());
			
		}
		

		
		System.out.println("Hello world!");
	}
	@FXML
	private void handleCopyAction (ActionEvent event) {
		if(model.loadAlbum.isEmpty()) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("No images");
			error.setContentText("There are no images to be moved.");
			error.showAndWait();
		}


		else {
			model.photoIndex = tableView.getSelectionModel().getSelectedIndex();

		    
			   try {
			         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/copyMenu.fxml"));
			         Scene scene = new Scene(blah);
			         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			         appStage.setScene(scene);
			         appStage.show();
			        
			        
			        
			    } catch(Exception e) {
			        e.printStackTrace();
			    }
		    
		    
		    
		    
		}
	}
	/* Action for the caption button is selected.
	 * If there are no photos, an error pops up and returns the GUI to normal.
	 * Takes the string in the captionField and sets it in the photo, and makes sure that loadAlbum is updated, along with the tableView.
	 * 
	 */
	@FXML
	private void addCaptionCommand (ActionEvent event) {
		String newCaption = captionField.getText();
		
		if(newCaption.isEmpty()==true) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("Empty Album!");
			error.setContentText("There are no photos in the album.");
			error.showAndWait();
		}
		
		else {
			URI fileURI = tableView.getSelectionModel().getSelectedItem().getfileURI();
			Users.setCaptionToImage(fileURI,newCaption);
			
			
			/*
	        for(CustomImage image: model.loadAlbum) {
	   		 if(image.getfileURI().equals(fileURI)) {
	   			model.loadAlbum.get(i).setCaption(newCaption); 
	   			 break;
	   			 }
	   		 i++;
	   		 }
	   		 */
            model.userAlbums.get(model.albumIndex).setImages(model.loadAlbum);
            model.user.setphotoAlbums(model.userAlbums);
			 try {
				model.user.saveUser();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
			tableView.getColumns().get(0).setVisible(false);
			tableView.getColumns().get(0).setVisible(true);
	        tableView.setItems(getAlbumImages());
	        
	        
		}
		cancelButton.setVisible(false);
		cancelButton.setDisable(true);
		captionField.setVisible(false);
		captionField.setDisable(true);
		addCaptionButton.setDisable(true);
		addCaptionButton.setVisible(false);
		textLabel.setVisible(false);
	}
	
	/* Action for when the return button is selected. 
	 * Returns to the previous screen.
	 */
	
	@FXML
	private void handleReturnAction (ActionEvent event) {
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
	@FXML
	private void handleMoveAction (ActionEvent event) {
		
		
		
		if(model.loadAlbum.isEmpty()) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("No images");
			error.setContentText("There are no images to be moved.");
			error.showAndWait();
		}


		else {
			model.photoIndex = tableView.getSelectionModel().getSelectedIndex();

		    
			   try {
			         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/moveMenu.fxml"));
			         Scene scene = new Scene(blah);
			         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			         appStage.setScene(scene);
			         appStage.show();
			        
			        
			        
			    } catch(Exception e) {
			        e.printStackTrace();
			    }
		    
		    
		    
		    
		}
		
		
		
		
	}
	/* Action for then the caption button is selected. 
	 * Sets up the GUI for captioning an image. 
	 */
	@FXML
	private void handleCaptionAction (ActionEvent event) {
		if(model.loadAlbum.isEmpty()) {
			
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("Empty Album");
			error.setContentText("The album is empty.");
			error.showAndWait();
		}
		else {
			cancelButton.setVisible(true);
			cancelButton.setDisable(false);
			captionField.setVisible(true);
			captionField.setDisable(false);
			addCaptionButton.setDisable(false);
			addCaptionButton.setVisible(true);
			
			textLabel.setText("Caption");
			textLabel.setVisible(true);
		}

		
		
	}
	@FXML
	private void handleAddTagAction (ActionEvent event) {
		
		if(model.loadAlbum.isEmpty()) {
			
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("Empty Album");
			error.setContentText("The album is empty.");
			error.showAndWait();
		}
		else {
			cancelButton.setVisible(true);
			tagButton.setVisible(true);
			tagField1.setVisible(true);
			cancelButton.setDisable(false);
			tagButton.setDisable(false);
			tagField1.setDisable(false);
			tagField2.setVisible(true);
			tagField2.setDisable(false);
			tagName.setVisible(true);
			tagValue.setVisible(true);
			
			//textLabel.setText("Tag");
			//textLabel.setVisible(true);
		}

		
		
		
		/*
		addButton.setDisable(true);
		deleteButton.setDisable(true);
		copyToButton.setDisable(true);
		moveToButton.setDisable(true);
		captionButton.setDisable(true);
		addTagButton.setDisable(true);
		deleteTagButton.setDisable(true);
		displayButton.setDisable(true);
		returnButton.setDisable(true);
		*/
		System.out.println("Hello world!");
	}
	
	
	@FXML
	private void addTagCommand (ActionEvent event) {
		
String newName = tagField1.getText();
String newValue = tagField2.getText();
		
		if(newName.isEmpty()==true || newValue.isEmpty()==true) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("Empty Name");
			error.setContentText("There value you entered was nothing.");
			error.showAndWait();
		}
		

		
		
		
		else {
			URI fileURI = tableView.getSelectionModel().getSelectedItem().getfileURI();
			int i = 0;
			boolean nodupe = true;
			Tag t = new Tag(newName,newValue);
			
			
			
		        for(CustomImage image: model.loadAlbum) {
		      		 if(image.getfileURI().equals(fileURI)) {
		      			 for(Tag g: image.gettagsList()) {
		      				 if(g.equals(t)) {
		      					nodupe = false;
		      					error = new Alert(AlertType.INFORMATION);
		      					error.setTitle("Error!");
		      					error.setHeaderText("Duplicate Tag");
		      					error.setContentText("The tag was a dupe bro!");
		      					error.showAndWait();
		      					break;
		      				 }
		      			 }
		      		 }
		      		}
			
			
			
			
			
			
			
			i = 0;
			if(nodupe==true) {
				/*
		        for(CustomImage image: model.loadAlbum) {
			   		 if(image.getfileURI().equals(fileURI)) {
			   			model.loadAlbum.get(i).gettagsList().add(t);
			   			 break;
			   			 }
			   		 i++;
			   		 }
		        
		        */
		        
		        	Users.addTagToImage(fileURI,t);
		    
		            model.userAlbums.get(model.albumIndex).setImages(model.loadAlbum);
		            model.user.setphotoAlbums(model.userAlbums);
					 try {
						model.user.saveUser();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
					tableView.getColumns().get(0).setVisible(false);
					tableView.getColumns().get(0).setVisible(true);
			        tableView.setItems(getAlbumImages());				
			}

		}
		cancelButton.setVisible(false);
		cancelButton.setDisable(true);
		tagField1.setVisible(false);
		tagField1.setDisable(true);
		tagField2.setVisible(false);
		tagField2.setDisable(true);
		addCaptionButton.setDisable(true);
		addCaptionButton.setVisible(false);
		tagName.setVisible(false);
		tagValue.setVisible(false);

		
		tagButton.setVisible(false);
		tagButton.setDisable(true);
		
		textLabel.setVisible(false);


		
	}
	
	
	@FXML
	private void cancelCommand (ActionEvent event) {
		cancelButton.setVisible(false);
		cancelButton.setDisable(true);
		captionField.setVisible(false);
		captionField.setDisable(true);
		addCaptionButton.setDisable(true);
		addCaptionButton.setVisible(false);
		tagButton.setDisable(true);
		tagButton.setVisible(false);
		tagField1.setVisible(false);
		tagField1.setDisable(true);
		tagField2.setVisible(false);
		tagField2.setDisable(true);
		tagName.setVisible(false);
		tagValue.setVisible(false);

		tagName.setVisible(false);
		tagValue.setVisible(false);
		tagField1.setVisible(false);
		tagField1.setDisable(true);
		tagField2.setVisible(false);
		tagField2.setDisable(true);
		
		
		
		textLabel.setVisible(false);
		
	}
	
	
	private boolean checkTag(List<Tag> tagsList,Tag newTag) {
		
		
		return false;
	}
	
	
	
	@FXML
	private void handleDeleteTagAction (ActionEvent event) {
		model.URIdelete = tableView.getSelectionModel().getSelectedItem().getfileURI();
		model.photoIndex = tableView.getSelectionModel().getSelectedIndex();
		if(model.loadAlbum.isEmpty()) {
			
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("Empty Album");
			error.setContentText("The album is empty.");
			error.showAndWait();
		}
		else if(model.loadAlbum.get(model.photoIndex).gettagsList().isEmpty()) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("No tags");
			error.setContentText("There are no tags to be deleted.");
			error.showAndWait();
			
		}
		else {
		    try {
		    	
		        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/photos/view/tagMenu.fxml"));
		        Parent root1 = (Parent) fxmlLoader.load();
		        Stage stage = new Stage();
		        stage.setScene(new Scene(root1));  
		        stage.show();
		        
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
		}		
	}
	
	/* Action for when the display button is selected.
	 * Produces an error if there are no photos. 
	 * Otherwise, it sets the displayImage global variable to whatever number the image highlighted is down the list.
	 * The Display scene is then opened on that image. 
	 */
	@FXML
	private void handleDisplayAction (ActionEvent event) {
		
		if (model.loadAlbum.isEmpty() == true) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("Empty Album!");
			error.setContentText("There are no photos in the album.");
			error.showAndWait();
		}
		else {
			model.displayImage = tableView.getSelectionModel().getSelectedIndex();
		    try {
		        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/photos/view/photosDisplay.fxml"));
		        Parent root1 = (Parent) fxmlLoader.load();
		        Stage stage = new Stage();
		        stage.setScene(new Scene(root1));  
		        stage.show();
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
		}
		
		
		
		
		

		
		System.out.println("Hello world!");
	}










	
}
