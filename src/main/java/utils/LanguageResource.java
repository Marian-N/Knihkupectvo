package utils;

import controller.AuthorBookController;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public final class LanguageResource {
    private static LanguageResource _instance = null;
    private Locale locale;

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
    public final void setLocale(Locale locale){
        this.locale = locale;
    }
    public final Locale getLocale(){
        return locale;
    }

    public static LanguageResource getInstance(){

        if(_instance == null)
            _instance = new LanguageResource();
        return _instance;
    }

}
