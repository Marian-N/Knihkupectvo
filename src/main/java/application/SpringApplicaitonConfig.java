package application;

import gui.login.LoginController;
import javafx.stage.Stage;
import model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import utils.LanguageResource;

import java.util.ResourceBundle;

@Configuration
public class SpringApplicaitonConfig {
    @Autowired SpringFXMLLoader springFXMLLoader;
    @Bean
    public ResourceBundle rb(){
        return ResourceBundle.getBundle("Lang");
    }

    @Bean
    @Lazy
    public StageManager stageManager(Stage stage){
        return new StageManager(springFXMLLoader, stage);
    }

}
