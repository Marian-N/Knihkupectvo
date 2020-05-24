package application;

import gui.usermain.UserMainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Customer;

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


    public void  switchScene(final FxmlView view){
        FXMLLoader viewLoader = loadView(view.getFxmlFile());
        Parent viewRoot = null;
        try {
            viewRoot = viewLoader.load();
        } catch (Exception exception) {
            System.out.println("Unable to load FXML view  " + view.getFxmlFile() + exception);
        }
        show(viewRoot, view.getTitle());
    }

    /**
     * Switching scenes in which i need to pass logged user
     * @param customer logged user
     */
    public void  switchScene(final FxmlView view, Customer customer){
        FXMLLoader viewLoader = loadView(view.getFxmlFile());
        Parent viewRoot = null; //= viewLoader.load();
        try {
            viewRoot = viewLoader.load();
        } catch (Exception exception) {
            System.out.println("Unable to load FXML view  " + view.getFxmlFile() + exception);
        }
        UserMainController umc = viewLoader.<UserMainController>getController();
        umc.initData(customer);
        show(viewRoot, view.getTitle());
    }

    private void show(final Parent root, String title){
        Scene scene = prepareScene(root);
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

    private FXMLLoader loadView(String fxmlPath){
        FXMLLoader loader = null;
        loader = springFXMLLoader.load(fxmlPath);
        return loader;
    }
}
