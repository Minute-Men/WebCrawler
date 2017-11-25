package application;
	
import application.WebCrawler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	public static void main(String[] args) {
		System.out.println( System.getProperties());
		launch(args);
	}
	
	/**
	 * Boilerplate for FXML
	 */
	@Override
	public void start(Stage stage) throws Exception {
		
		// BoilerPlate
		Parent root = FXMLLoader.load(WebCrawler.class.getClassLoader().getResource("Untitled.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
