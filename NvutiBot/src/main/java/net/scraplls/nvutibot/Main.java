package net.scraplls.nvutibot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.scraplls.nvutibot.util.Preferences;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    private static Preferences preferences;

    public static void main(String[] args){
        Application.launch(args);
    }

    private static void setup() throws IOException {
        //Logger setup.
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT [%4$s]: %5$s%6$s%n");
        preferences = new Preferences();
    }

    @Override
    public void start(Stage stage) throws Exception{
        setup();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("views/main_menu.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("NvutiBot v1.0");
        stage.setWidth(800);
        stage.setHeight(550);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest((e) -> System.exit(0));
    }

    public static Preferences getPreferences() {
        return preferences;
    }
}
