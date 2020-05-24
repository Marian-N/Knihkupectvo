package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import utils.LanguageResource;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Coexisting of Spring and JavFX
 */

@Component
public class SpringFXMLLoader {
    private final ApplicationContext ac;
    private final ResourceBundle rb;
    private LanguageResource lr = LanguageResource.getInstance();

    @Autowired
    public SpringFXMLLoader(ApplicationContext ac, ResourceBundle rb){
        this.rb = rb;
        this.ac = ac;
    }

    /**
     * Setting spring as default loader
     * @param fxmlPath path to fxml file, which i want to load
     */
    public FXMLLoader load(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(ac::getBean);
        loader.setResources(lr.getResources());
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader;
    }


}
