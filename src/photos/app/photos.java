package photos.app;
	
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import photos.controller.photosController;
import photos.controller.photosLoginController;
import photos.model.Users;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * this is the main method that begins the application
 * @author Miguel Macaoay (mtm236) and Faris Al-khatahtbeh (fa301)
 *
 */
public class photos extends Application {
	
	
	
	@Override
	public void start(Stage primaryStage) 
	throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/photos/view/photosUserLogin.fxml"));
		VBox root = (VBox)loader.load();
		
		//GridPane root = (GridPane)loader.load();
		photosLoginController loginController;
		photosLoginController listController = loader.getController();
		listController.start(primaryStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("The Offlaner's Photo Album Viewer");
		primaryStage.setResizable(false);  
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);

	}
}




