package net.scraplls.nvutibot.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import net.scraplls.nvutibot.model.NvutiBot;
import net.scraplls.nvutibot.model.Strategy;
import net.scraplls.nvutibot.model.impl.MartingaleStrategy;
import net.scraplls.nvutibot.util.RollType;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public AnchorPane top_pane;
    public Label logo_label;
    public MenuBar menu_bar;
    public Menu game_menu;
    public MenuItem settings_menu_item;
    public Menu help_menu;
    public MenuItem about_menu_item;
    public TextField login_field;
    public CheckBox remember_box;
    public AnchorPane stat_pane;
    public TextField bet_field;
    public TextField win_field;
    public ChoiceBox<String> strategy_choice;
    public Button action_button;
    public PasswordField pass_field;
    public ChoiceBox<String>  button_choice;
    public Label wins_label;
    public Label loses_label;
    public Label profit_label;

    private boolean started;
    private NvutiBot nvutiBot;
    private Thread botThread;
    private Timeline timeline;
    private File userdata;

    public void initialize(URL location, ResourceBundle resources) {
        setupFields();
        setupBoxes();
        setupButtons();
    }

    private void setupFields(){
        bet_field.setText("1");
        win_field.setText("50");
        userdata = new File("userdata");
        if(userdata.length() != 0){
            try(BufferedReader reader = new BufferedReader(new FileReader(userdata))) {
                String[] data = reader.readLine().split(":");
                if(data.length != 0){
                    login_field.setText(data[0]);
                    pass_field.setText(data[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupBoxes() {
        strategy_choice.getItems().addAll("Martingale");
        strategy_choice.setValue("Martingale");
//        strategy_choice.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
//            switch (newValue.intValue()){
//                case 0:
//                    break;
//            }
//        });
        button_choice.getItems().addAll("Random", "More", "Less");
        button_choice.setValue("Random");
    }

    private void setupButtons() {
        action_button.setOnMouseClicked(event -> {
            if (!started) {
                started = true;
                nvutiBot = new NvutiBot(
                        Integer.parseInt(bet_field.getText()),
                        Integer.parseInt(win_field.getText()),
                        200,
                        512);
                String strategyChoiceValue = strategy_choice.getValue().toUpperCase();
                String buttonChoiceValue = button_choice.getValue().toUpperCase();
                Strategy strategy = null;
                RollType rollType = null;
                switch (strategyChoiceValue){
                    case "MARTINGALE":
                        strategy = new MartingaleStrategy(nvutiBot);
                        break;
                }
                switch (buttonChoiceValue){
                    case "RANDOM":
                        rollType = RollType.RANDOM;
                        break;
                    case "MORE":
                        rollType = RollType.MORE;
                        break;
                    case "LESS":
                        rollType = RollType.LESS;
                        break;
                }
                Strategy finalStrategy = strategy;
                RollType finalRollType = rollType;
                botThread = new Thread(() -> {
                    try {
                        nvutiBot.launch();
                        Thread.sleep(5000L);
                        nvutiBot.login(login_field.getText(), pass_field.getText());
                        nvutiBot.setBet(Integer.parseInt(bet_field.getText()));
                        nvutiBot.setChance(Integer.parseInt(win_field.getText()));
                        Objects.requireNonNull(finalStrategy).play(finalRollType);
                        Platform.runLater(this::end);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        nvutiBot.end();
                    }
                });
                botThread.start();
                stat_pane.setDisable(false);
                wins_label.setTextFill(Color.GREEN);
                loses_label.setTextFill(Color.DARKRED);
                timeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
                    wins_label.setText(String.valueOf(nvutiBot.getWins()));
                    loses_label.setText(String.valueOf(nvutiBot.getLoses()));
                    if(nvutiBot.getOverallProfit() >= 0){
                        profit_label.setTextFill(Color.GREEN);
                    } else {
                        profit_label.setTextFill(Color.DARKRED);
                    }
                    profit_label.setText(String.valueOf(nvutiBot.getOverallProfit()));
                }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
                try(FileWriter writer = new FileWriter(userdata)) {
                    if(remember_box.isSelected()){
                        writer.write(String.format("%s:%s", login_field.getText(), pass_field.getText()));
                    } else {
                        writer.write("");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                action_button.setText("Stop");
            } else {
                end();
            }
        });
    }

    private void end() {
        started = false;
        nvutiBot.end();
        nvutiBot = null;
        botThread.stop();
        timeline.stop();
        timeline = null;
        stat_pane.setDisable(true);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        action_button.setText("Start");
    }


}
