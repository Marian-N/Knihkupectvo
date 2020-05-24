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
import utils.LanguageResource;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


@SpringBootApplication
@ComponentScan(basePackageClasses={LoginController.class, UserRegisterController.class, AdminMainController.class, StageManager.class,
                Customer.class, UserMainController.class, javafx.stage.Stage.class})
public class Main extends Application {

    protected ConfigurableApplicationContext springContext;
    protected StageManager stageManager = null;
    private LanguageResource lr = LanguageResource.getInstance();

    @Override
    public void init() throws IOException {
        Locale locale = new Locale("en", "US");
        lr.setResources(ResourceBundle.getBundle("Lang", locale));
        springContext = bootstrapSpringApplicationContext();
    }

    @Override
    public void start(Stage stage) throws Exception{
        stageManager = springContext.getBean(StageManager.class, stage);
        displayInitialScene();
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
