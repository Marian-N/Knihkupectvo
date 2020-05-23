package application;

import gui.usermain.UserMainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Customer;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.Objects;

/**
 * Switching scenes
 */
public class StageManager {
    private final Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;

    public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage){
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    }


    public void  switchScene(final FxmlView view) throws IOException {
        FXMLLoader viewLoader = loadView(view.getFxmlFile());
        Parent viewRoot = null;
        try {
            viewRoot = viewLoader.load();
        } catch (Exception exception) {
            System.out.println("Unable to load FXML view  " + view.getFxmlFile() + exception);
        }
        show(viewRoot, view.getTitle());
    }

    public void  switchScene(final FxmlView view, Customer customer) throws IOException {
        FXMLLoader viewLoader = loadView(view.getFxmlFile());
        Parent viewRoot = null; //= viewLoader.load();
        try {
            viewRoot = viewLoader.load();
        } catch (Exception exception) {
            System.out.println("Unable to load FXML view  " + view.getFxmlFile() + exception);
        }
        UserMainController umc = viewLoader.<UserMainController>getController();
        umc.initData(customer);

//        Parent viewRoot = viewLoader.load();
        show(viewRoot, view.getTitle());
    }

    private void show(final Parent root, String title){
        Scene scene = prepareScene(root);
//        System.out.println("NAME: " + root.getId());
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Scene prepareScene(Parent root){
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(root);
        }
        scene.setRoot(root);
        return scene;
    }

    private FXMLLoader loadView(String fxmlPath) throws IOException {
        FXMLLoader loader = null;
        loader = springFXMLLoader.load(fxmlPath);
        return loader;
    }
}
