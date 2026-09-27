package com.example.chem_project;


import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.*;
import java.net.URISyntaxException;
import java.util.*;


/**
 * Space Invaders style simulation used to collect collision-time data.
 *
 * A player block patrols a fixed region at the bottom of the screen while red blocks fall from
 * random positions at the top. Each time a falling block hits the player, the elapsed time is
 * logged to a raw data file and the round restarts. Press B during a round to return to settings.
 */
public class Game extends Application {
    // Directory that collision times are written to. Override with -Dchem.dataDir=<path>.
    private static final String DATA_DIR = System.getProperty("chem.dataDir",
            "chem project/src/com/example/chem_project/rawData");

    private Stage theStage;
    private Pane parent;
    private Rectangle player;
    private List<Rectangle> enemies;
    private boolean movingRight = true;
    private Label timerLabel;
    private Timeline timeline;
    private int milliSecondsElapsed;
    boolean shouldCreateEnemy = true;
    long lastCircleTime = 0;
    private List<String> dataList;


    double playerSpeed = 0;
    double oppSpeed = 0;


    String playerSpeedSetting = "";


    String oppSpeedSetting = "";


    int setting = 0;
    final long delay = 50_000_000; //50 milliseconds
    private long lastTime = 0;
    int scalingFactor = 1000;
    double playerSpeedSlow = .1;
    double playerSpeedMid = .2;
    double playerSpeedFast = .3;
    double oppSpeedSlow = playerSpeedSlow * scalingFactor;
    double oppSpeedMid = playerSpeedSlow * scalingFactor;
    double oppSpeedFast = playerSpeedSlow * scalingFactor;


    private AnimationTimer timer;


    @Override
    public void start(Stage stage) throws Exception {
        theStage = stage;
        settingScreen();
    }


    private void restartGame() throws URISyntaxException {
        if(setting == 1){
            player.setTranslateX(0);
        }else if(setting == 2) {
            player.setTranslateX(0);
        }else if(setting == 3) {
            player.setTranslateX(0);
        }else if(setting == 4) {
            player.setTranslateX(730);
        }else if(setting == 5) {
            player.setTranslateX(480);
        }


        if (Objects.equals(playerSpeedSetting, "slow")) {
            playerSpeed = playerSpeedSlow;
        }else if (Objects.equals(playerSpeedSetting, "mid")){
            playerSpeed = playerSpeedMid;
        }else if (Objects.equals(playerSpeedSetting, "fast")){
            playerSpeed = playerSpeedFast;
        }


        if(Objects.equals(oppSpeedSetting, "slow")){
            oppSpeed = oppSpeedSlow;
        }else if(Objects.equals(oppSpeedSetting, "mid")){
            oppSpeed = oppSpeedMid;
        }else if(Objects.equals(oppSpeedSetting, "fast")) {
            oppSpeed = oppSpeedFast;
        }
        player.setTranslateY(805);
        movingRight = true;
        milliSecondsElapsed = 0;
        timerLabel.setText("00:00:000");
        enemies.forEach(enemy -> parent.getChildren().remove(enemy));
        enemies.clear();
    }




    private void settingScreen(){
        VBox vBox = new VBox();
        vBox.setPrefWidth(800);
        vBox.setPrefHeight(800);
        Scene scene = new Scene(vBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleGroup playerSpeedToggle = new ToggleGroup();
        ToggleGroup oppSpeedToggle = new ToggleGroup();
        Label gameName = new Label("Chem Project");
        gameName.setStyle("-fx-font-size: 60px");
        Label settings = new Label("Settings");
        settings.setStyle("-fx-font-size: 40px");
        Label speed = new Label("Speed");
        speed.setStyle("-fx-font-size: 40px");
        RadioButton rightQuarterRadio = new RadioButton("Right Quarter Screen");
        RadioButton rightHalfRadio = new RadioButton("Right Half Screen");
        RadioButton quarterRadio = new RadioButton("Left Quarter Screen");
        RadioButton halfRadio = new RadioButton("Left Half Screen");
        RadioButton fullRadio = new RadioButton("Full Screen");

        RadioButton playerSlow = new RadioButton("Player Slow");
        RadioButton playerMid = new RadioButton("Player Mid");
        RadioButton playerFast = new RadioButton("Player Fast");

        RadioButton oppSlow = new RadioButton("Opp Slow");
        RadioButton oppMid = new RadioButton("Opp Mid");
        RadioButton oppFast = new RadioButton("Opp Fast");


        quarterRadio.setToggleGroup(toggleGroup);
        halfRadio.setToggleGroup(toggleGroup);
        fullRadio.setToggleGroup(toggleGroup);
        rightQuarterRadio.setToggleGroup(toggleGroup);
        rightHalfRadio.setToggleGroup(toggleGroup);

        playerSlow.setToggleGroup(playerSpeedToggle);
        playerMid.setToggleGroup(playerSpeedToggle);
        playerFast.setToggleGroup(playerSpeedToggle);

        oppSlow.setToggleGroup(oppSpeedToggle);
        oppMid.setToggleGroup(oppSpeedToggle);
        oppFast.setToggleGroup(oppSpeedToggle);

        quarterRadio.setPadding(new Insets(0,0,0,17));
        Button startBtn = new Button("Start Game");


        startBtn.setOnAction( e->{
            if(!(quarterRadio.isSelected() || halfRadio.isSelected() || fullRadio.isSelected() || rightQuarterRadio.isSelected() || rightHalfRadio.isSelected())){
                System.out.println("select a setting");
            }else if(quarterRadio.isSelected()) {
                setting = 1;
                startGame();
            }else if(halfRadio.isSelected()) {
                setting = 2;
                startGame();
            }else if(fullRadio.isSelected()) {
                setting = 3;
                startGame();
            }else if(rightQuarterRadio.isSelected()) {
                setting = 4;
                startGame();
            }else if(rightHalfRadio.isSelected()) {
                setting = 5;
                startGame();
            }


            if(playerSlow.isSelected()){
                playerSpeedSetting = "slow";
                playerSpeed = playerSpeedSlow;
            }else if(playerMid.isSelected()){
                playerSpeedSetting = "mid";
                playerSpeed = playerSpeedMid;
            }else if(playerFast.isSelected()){
                playerSpeedSetting = "fast";
                playerSpeed = playerSpeedFast;
            }


            if(oppSlow.isSelected()){
                oppSpeedSetting = "slow";
                oppSpeed = oppSpeedSlow;
            }else if(oppMid.isSelected()){
                oppSpeedSetting = "mid";
                oppSpeed = oppSpeedMid;
            }else if(oppFast.isSelected()){
                oppSpeedSetting = "fast";
                oppSpeed = oppSpeedFast;
            }




        });


        vBox.getChildren().addAll(gameName,settings,quarterRadio,halfRadio,
                rightQuarterRadio,rightHalfRadio,fullRadio,speed,playerSlow,playerMid,playerFast,
                oppSlow,oppMid,oppFast,startBtn);


        theStage.setScene(scene);
        theStage.show();
    }


    private void stopGame() {
        if (timeline != null) {
            timeline.stop();
        }


        if (timer != null) {
            timer.stop();
        }
        // Stop other timers if any
        // Clear game state
        milliSecondsElapsed = 0;
        player.setTranslateX(0);
        player.setTranslateY(805);
        movingRight = true;
        enemies.forEach(enemy -> parent.getChildren().remove(enemy));
        enemies.clear();
    }




    private void startGame(){
        parent = new Pane();
        player = new Rectangle(20, 20);
        enemies = new ArrayList<>();
        //dataList = new ArrayList<>();
        timerLabel = new Label("00:00:000");
        timerLabel.setStyle("-fx-font-size: 40px");


        timeline = new Timeline(new KeyFrame(Duration.seconds(.01), e -> {
            milliSecondsElapsed++;
            int minutes = (milliSecondsElapsed / 6000) % 60;
            int seconds = (milliSecondsElapsed / 100) % 60;
            int hundredths = milliSecondsElapsed % 100;
            timerLabel.setText(String.format("%02d:%02d:%02d", minutes, seconds, hundredths));
        }));


        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {


                if (l - lastCircleTime > delay) {
                    shouldCreateEnemy = true;
                    lastCircleTime = l;
                }
                if(setting == 1){
                    if (movingRight) {
                        player.setTranslateX(player.getTranslateX() + playerSpeed);
                        if (player.getTranslateX() >= 245) {
                            player.setTranslateX(245);
                            movingRight = false;
                        }
                    } else {
                        player.setTranslateX(player.getTranslateX() - playerSpeed);
                        if (player.getTranslateX() <= 0) {
                            player.setTranslateX(0);
                            movingRight = true;
                        }
                    }
                }else if(setting == 2){
                    if (movingRight) {
                        player.setTranslateX(player.getTranslateX() + playerSpeed);
                        if (player.getTranslateX() >= 490) {
                            player.setTranslateX(490);
                            movingRight = false;
                        }
                    } else {
                        player.setTranslateX(player.getTranslateX() - playerSpeed);
                        if (player.getTranslateX() <= 0) {
                            player.setTranslateX(0);
                            movingRight = true;
                        }
                    }
                }else if(setting == 3){
                    if (movingRight) {
                        player.setTranslateX(player.getTranslateX() + playerSpeed);
                        if (player.getTranslateX() >= 980) {
                            player.setTranslateX(980);
                            movingRight = false;
                        }
                    } else {
                        player.setTranslateX(player.getTranslateX() - playerSpeed);
                        if (player.getTranslateX() <= 0) {
                            player.setTranslateX(0);
                            movingRight = true;
                        }
                    }
                }else if(setting == 4){
                    if (movingRight) {
                        player.setTranslateX(player.getTranslateX() + playerSpeed);
                        if (player.getTranslateX() >= 980) {
                            player.setTranslateX(980);
                            movingRight = false;
                        }
                    } else {
                        player.setTranslateX(player.getTranslateX() - playerSpeed);
                        if (player.getTranslateX() <= 730) {
                            player.setTranslateX(730);
                            movingRight = true;
                        }
                    }
                }else if(setting == 5){
                    if (movingRight) {
                        player.setTranslateX(player.getTranslateX() + playerSpeed);
                        if (player.getTranslateX() >= 980) {
                            player.setTranslateX(980);
                            movingRight = false;
                        }
                    } else {
                        player.setTranslateX(player.getTranslateX() - playerSpeed);
                        if (player.getTranslateX() <= 480) {
                            player.setTranslateX(480);
                            movingRight = true;
                        }
                    }
                }


                // Handle falling enemies
                if (shouldCreateEnemy && enemies.size() < 5) {
                    // Adjust the range of random values for x-coordinate
                    Rectangle enemy = new Rectangle(new Random().nextInt(1001), 0, 20, 20);
                    enemy.setFill(Color.RED);
                    enemy.setUserData(oppSpeed);
                    enemies.add(enemy);
                    parent.getChildren().add(enemy);
                    shouldCreateEnemy = false;


                }
                try {
                    Iterator<Rectangle> iterator = enemies.iterator();
                    while (iterator.hasNext()) {
                        Rectangle enemy = iterator.next();
                        double speed = (double) enemy.getUserData(); // Get the speed for this circle
                        enemy.setTranslateY(enemy.getTranslateY() + speed);


                        if (enemy.getTranslateY() > 1000) {
                            iterator.remove();
                            parent.getChildren().remove(enemy);
                        } else if (enemy.getBoundsInParent().intersects(player.getBoundsInParent())) {
                            System.out.println(timerLabel.getText());
                            String logFile = logFileForPlayerSpeed(playerSpeedSetting);
                            if (logFile != null) {
                                appendToLogFile(timerLabel.getText(), logFile);
                            }
                            restartGame();
                            break; // restartGame() clears the enemy list, so stop iterating it
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        };


        timer.start();
        parent.setPrefWidth(1000);
        parent.setPrefHeight(1000);
        parent.getChildren().add(player);
        parent.getChildren().add(timerLabel);
        if(setting == 1){
            player.setTranslateX(0);
        }else if(setting == 2) {
            player.setTranslateX(0);
        }else if(setting == 3) {
            player.setTranslateX(0);
        }else if(setting == 4) {
            player.setTranslateX(730);
        }else if(setting == 5) {
            player.setTranslateX(480);
        }


        if (Objects.equals(playerSpeedSetting, "slow")) {
            playerSpeed = playerSpeedSlow;
        }else if (Objects.equals(playerSpeedSetting, "mid")){
            playerSpeed = playerSpeedMid;
        }else if (Objects.equals(playerSpeedSetting, "fast")){
            playerSpeed = playerSpeedFast;
        }


        if(Objects.equals(oppSpeedSetting, "slow")){
            oppSpeed = oppSpeedSlow;
        }else if(Objects.equals(oppSpeedSetting, "mid")){
            oppSpeed = oppSpeedMid;
        }else if(Objects.equals(oppSpeedSetting, "fast")) {
            oppSpeed = oppSpeedFast;
        }
        player.setTranslateY(805);
        timerLabel.setTranslateX(430);
        timerLabel.setTranslateY(50);
        Scene scene = new Scene(parent);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.B){
                    stopGame();
                    settingScreen();
                }
            }
        });
        theStage.setTitle("Chem");
        theStage.setScene(scene);
        theStage.show();
    }
    // Maps the player speed setting to the raw data file that collision times are appended to.
    private static String logFileForPlayerSpeed(String speedSetting) {
        switch (speedSetting) {
            case "slow": return DATA_DIR + File.separator + "pSlow1000x.txt";
            case "mid":  return DATA_DIR + File.separator + "pMid1000x.txt";
            case "fast": return DATA_DIR + File.separator + "pFast1000x.txt";
            default:     return null;
        }
    }

    private void appendToLogFile(String message,String filename) {
        try (FileWriter fw = new FileWriter(filename, true)) {
            fw.write(message);
            fw.write("\n");
        }
        catch(IOException e) {
            System.err.println("Could not write to " + filename + ": " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        launch();
    }
}

