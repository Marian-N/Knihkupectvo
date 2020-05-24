package utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.util.ResourceBundle;

/**
 * Stores resource bundle for language, which is currently used
 */
public final class LanguageResource {
    private static LanguageResource _instance = null;
    private ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();

    public ObjectProperty<ResourceBundle> resourcesProperty(){
        return resources;
    }
    public final ResourceBundle getResources() {
        return resourcesProperty().get();
    }
    public final void setResources(ResourceBundle resources) {
        resourcesProperty().set(resources);
    }

    public static LanguageResource getInstance(){
        if(_instance == null)
            _instance = new LanguageResource();
        return _instance;
    }

}
