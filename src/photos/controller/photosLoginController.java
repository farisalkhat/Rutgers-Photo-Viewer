package photos.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import photos.model.*;


/**
 * This controls the Login screen
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class photosLoginController{
	
	@FXML TextField loginField;
	@FXML Button loginButton;
	private boolean isThereNewUser = true;
	private static Users newUser; 
	private static int userNum;
	boolean stockexists;
	
	Alert error;
	public void start(Stage primaryStage) throws IOException {
		// TODO Auto-generated method stub
		//model.saveUserDatabase(model.userDatabase);
		
		

		
		

		
		
		
		
		try {
			model.userDatabase = model.LoadUserDatabase();
			
			
			for(Users u: model.userDatabase) {
				if(u.getUsername().equals("stock")) {
					stockexists = true;
					break;
				}
			}
			if(stockexists==false) {
				File folder = new File("save/stock");
				File[] listOfFiles = folder.listFiles();
				Users stock = new Users("stock");
				stock.getphotoAlbums().add(new albums("stock"));
				 for(int i = 0; i < listOfFiles.length; i++) {
					 if (listOfFiles[i].isFile()) {
						 File stockphoto = listOfFiles[i];
						 Date lastModDate = new Date(stockphoto.lastModified());
					     String fileName = stockphoto.getName();
					     URI fileUri = stockphoto.toURI();
					     stock.getphotoAlbums().get(0).getImages().add(new CustomImage(stockphoto,lastModDate,fileName,fileUri));
					 }
				 }
				 
				 model.userDatabase.add(stock);
				 model.saveUserDatabase(model.userDatabase);
				
				try {
					stock.saveUser();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	/*This action is for when the user presses login. It'll look at what's in the loginField and see if the user already exists. 
	 * If he exists, it loads the users albums.
	 * If the user does not exist, creates a new user.
	 * If the user inputs "admin" or "Admin", loads admin screen.
	 */
	@FXML
	private void handleLoginAction(ActionEvent event) throws IOException {
		
		String username = loginField.getText();
		String usernameLowered = loginField.getText().toLowerCase();
		
		
		
		
		if(usernameLowered.equals("admin")) {
			try {
		         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/adminScreen.fxml"));
		         Scene scene = new Scene(blah);
		         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		         appStage.setScene(scene);
		         appStage.show();
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
		}
		
		
		else {
			for(Users user: model.userDatabase)
		     {
				String tmp = user.getUsername();
		         if(tmp.toLowerCase().equals(username))
		         {
		        	 newUser = user;
		        	 isThereNewUser = false;
		        	 break; 
		        	 } 
		         
		     }
		     if (isThereNewUser==true) {
					error = new Alert(AlertType.INFORMATION);
					error.setTitle("Error!");
					error.setHeaderText("User Does not exist!");
					error.setContentText("The entry you've entered does not exist.");
					error.showAndWait();
		    	 
		    	 
		    	 
		    	 /*
		    	 newUser = new Users(username); 
		    	 model.userDatabase.add(newUser);
		    	 System.out.println("It's a new user!!");
		    	 userAlbums =  new ArrayList<albums>();
		         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/photosUserMenu.fxml"));
		         Scene scene = new Scene(blah);
		         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		         appStage.setScene(scene);
		         appStage.show();
		         */
		    	 
		     }
		     else {
		    	 System.out.println("Not a new user!!");
		    	 userNum = findUser(username);
		    	 
		    	try {
					model.user = Users.LoadUser(username);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	model.userAlbums = (ArrayList<albums>) model.user.getphotoAlbums();
		    	 
		         Parent blah = FXMLLoader.load(getClass().getResource("/photos/view/photosUserMenu.fxml"));
		         Scene scene = new Scene(blah);
		         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		         appStage.setScene(scene);
		         appStage.show();
		     }
			
			
			
		}
	     
		
		
		
	}
	
	
	
	/* Helper method
	 * Searches through the userDatabase to see if the user inputted exists.
	 * 
	 */
	private int findUser(String username) {
		int i = 0;
		for(Users user: model.userDatabase) {
			if(user.getUsername().equalsIgnoreCase(username)){
				return i;
			}
			else {
				i++;
			}
		}
		return -1;
	}

	
	   

}
