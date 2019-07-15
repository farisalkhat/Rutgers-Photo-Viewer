package photos.controller;

import java.io.IOException;
import java.net.URI;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import photos.model.Users;
import photos.model.albums;
import javafx.scene.control.Alert;

import photos.model.*;

/**
 * This controls the admin sub-system upon the user logging in
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class AdminController implements Initializable{

	@FXML private Button addButton;
	@FXML private Button deleteButton;
	@FXML private Button logoutButton;
	@FXML private TableView<Users> tableView;
	@FXML private TableColumn<Users,String> tableColumn;
	
	Alert error;
	@FXML private Button add2Button;
	@FXML private TextField userField;
	@FXML private Button cancelButton;
	
	@FXML private Button deleteUserButton;
	
	private ObservableList<Users>Users;
	
	/* initialize sets up what gets seen in the tableview. 
	 * The table column takes the username string from all the users in the ObservableList<Users> and places it in the table.
	 * If the list is not empty, it selects the first user. 
	 */
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		tableColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableView.setItems(getUsers());   
        tableView.getSelectionModel().select(0);
		
		
		
	}
	
	
	/* This event is for when the user presses the "Add User" button. Sets up the GUI to add a user.
	 * Disables everything and enables the userField and Add and Cancel buttons.
	 */
	
	@FXML
	private void addAction(ActionEvent event) {
		
		addButton.setDisable(true);
		deleteButton.setDisable(true);
		logoutButton.setDisable(true);
		tableView.setDisable(true);
		
		add2Button.setDisable(false);
		userField.setDisable(false);
		cancelButton.setDisable(false);
	}
	
	/* Event for when the admin presses the Delete button.
	 * Upon pressing it, deletes the user highlighted in the tableView.
	 * Produces an error if the tableView is empty.
	 */
	
	@FXML
	private void deleteAction(ActionEvent event) {
		
		if(model.userDatabase.isEmpty()) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("No users");
			error.setContentText("There are no users.");
			error.showAndWait();
			
		}
		else if(tableView.getSelectionModel().getSelectedItem().getUsername().equalsIgnoreCase("stock")){
		

			
				error = new Alert(AlertType.INFORMATION);
				error.setTitle("Error!");
				error.setHeaderText("Cannot delete Stock");
				error.setContentText("You cannot delete stock.");
				error.showAndWait();

		}
		
		else {

			
			String deleteUser = tableView.getSelectionModel().getSelectedItem().getUsername();
			int i = 0;
			for(Users user: model.userDatabase) {
				if(deleteUser.equals(user.getUsername())) {
					model.userDatabase.remove(i);
					break;
				}
				i++;
			}
			
			try {
				model.saveUserDatabase(model.userDatabase);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			tableView.getColumns().get(0).setVisible(false);
			tableView.getColumns().get(0).setVisible(true);
			tableView.setItems(getUsers());   
			tableView.getSelectionModel().select(0);
		}
		
		

		
		
		
		
		
	}
	
	
	
	
	
	/* Event for when the user presses the Logout button.
	 * Upon pressing it, returns the user to the login screen. 
	 */
	
	
	
	
	@FXML
	private void logoutAction(ActionEvent event) {
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
	
	
	
	/* cancelAction is for the cancel button that appears after pressing the add user button.
	 * Upon pressing the cancel button, returns the GUI to before the add user button was pressed.
	 */
	
	
	@FXML
	private void cancelAction(ActionEvent event) {
		addButton.setDisable(false);
		deleteButton.setDisable(false);
		logoutButton.setDisable(false);
		tableView.setDisable(false);
		
		add2Button.setDisable(true);
		userField.setDisable(true);
		cancelButton.setDisable(true);
	}
	
	/* add2Action is for the add button that is enabled after pressing the add user button.
	 * Upon pressing it, takes the input from the userField, and sees if the user exists.
	 * If the username already exists, reveals an error and returns the GUI to normal.
	 * Otherwise, adds the user to the tableView and returns the GUI to normal.
	 */
	
	@FXML
	private void add2Action(ActionEvent event) {
		
		String newUser = userField.getText();
		
		if(newUser.isEmpty()==true || newUser.equals("admin")|| newUser.equals("Admin")) {
			error = new Alert(AlertType.INFORMATION);
			error.setTitle("Error!");
			error.setHeaderText("Invalid input!");
			error.setContentText("The name you entered is invalid.");
			error.showAndWait();
			
			addButton.setDisable(false);
			deleteButton.setDisable(false);
			logoutButton.setDisable(false);
			tableView.setDisable(false);
			
			add2Button.setDisable(true);
			userField.setDisable(true);
			cancelButton.setDisable(true);
		}
		
		else {
			for(Users user:model.userDatabase) {
				if (user.getUsername().equals(newUser)) {
					error = new Alert(AlertType.INFORMATION);
					error.setTitle("Error!");
					error.setHeaderText("Duplicate input");
					error.setContentText("You cannot have duplicate usernames.");
					error.showAndWait();
					
					addButton.setDisable(false);
					deleteButton.setDisable(false);
					logoutButton.setDisable(false);
					tableView.setDisable(false);
					
					add2Button.setDisable(true);
					userField.setDisable(true);
					cancelButton.setDisable(true);
					
					return;
				}
			}
			
			
			Users user = new Users(newUser);
			model.userDatabase.add(user);
			
			
			try {
				model.saveUserDatabase(model.userDatabase);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				user.saveUser();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			/*
			try {
				model.saveUserDatabase(model.userDatabase);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			

			tableView.setItems(getUsers());   
			tableView.getSelectionModel().select(tableView.getItems().size()-1);
			
			
			addButton.setDisable(false);
			deleteButton.setDisable(false);
			logoutButton.setDisable(false);
			tableView.setDisable(false);
			
			add2Button.setDisable(true);
			userField.setDisable(true);
			cancelButton.setDisable(true);
			
		
	        
	        
		}
		
	}
	
	/* Helper method
	 * Produces an obersableList containing all the user objects from the user database.
	 */

	private ObservableList<Users> getUsers() {
		Users = FXCollections.observableArrayList();
		for(Users user: model.userDatabase) {
			Users.add(user);
		}
		
		return Users;
	}
	
	
	
}
