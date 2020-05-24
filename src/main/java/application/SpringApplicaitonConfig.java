package application;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

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
