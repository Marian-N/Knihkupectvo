package application;

import gui.adminmain.AdminMainController;
import gui.login.LoginController;
import gui.login.userregister.UserRegisterController;
import gui.usermain.UserMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;


@SpringBootApplication
@ComponentScan(basePackageClasses={LoginController.class, UserRegisterController.class, AdminMainController.class, StageManager.class,
                Customer.class, UserMainController.class, javafx.stage.Stage.class})
public class Main extends Application {

    protected ConfigurableApplicationContext springContext;
    protected StageManager stageManager = null;
//    private Parent root;

    @Override
    public void init() throws IOException {
        springContext = bootstrapSpringApplicationContext();
//        springContext = SpringApplication.run(Main.class);
//        FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlView.LOGIN.getFxmlFile()));
//        loader.setControllerFactory(springContext::getBean);
//        root = loader.load();
    }

    @Override
    public void start(Stage stage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("/gui/login/login.fxml"));
//        primaryStage.setTitle("Knihkupectvo");
//        primaryStage.setScene(new Scene(root, 900, 500));
//        primaryStage.setResizable(false);
//        primaryStage.show();

        stageManager = springContext.getBean(StageManager.class, stage);
        displayInitialScene();
//        stage.setScene(new Scene(root));
//        stage.show();

    }
    @Override
    public void stop(){
        springContext.close();
    }

    public static void main(String[] args){
        Application.launch(args);
    }

    protected void displayInitialScene() throws IOException {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    private ConfigurableApplicationContext bootstrapSpringApplicationContext(){
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        String[] args = getParameters().getRaw().stream().toArray(String[]::new);
        //builder.headless(false);
        return builder.run(args);
    }
}
