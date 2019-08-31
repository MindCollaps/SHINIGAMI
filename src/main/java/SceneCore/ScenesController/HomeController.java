package SceneCore.ScenesController;

import Engines.Engine;
import SceneCore.MoveListener;
import Utils.dateConfig;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController extends Controller implements Initializable {

    @FXML
    private ProgressIndicator runAnimation;
    @FXML
    private ImageView buttonStartBot;
    @FXML
    private TextArea commandOutputLine;
    @FXML
    private TextField commandInputLine;
    @FXML
    private MenuBar menuBar;

    private double xOffset = 0;
    private double yOffset = 0;


    Thread refreshThread;

    Image startBotState1;
    Image startBotState2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startBotState1 = new Image("icons/startstop.png");
        startBotState2 = new Image("icons/startstop_2.png");
        buttonStartBot.setImage(startBotState1);
        runAnimation.setVisible(false);
        buttonStartBot.setOnMouseClicked(event -> {
            if(engine.getDiscEngine().isBotApplicationRunning()){
                engine.getDiscEngine().shutdownBotApplication();
            } else {
                engine.getDiscEngine().startBotApplication();
            }
        });
    }

    @Override
    public void initController(Engine engine, Stage primaryStage, Scene scene, Stage mainStage) {
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle(engine.getProperties().getBotApplicationName() + " manager");
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setScene(scene);
        primaryStage.getIcons().setAll(new Image("icons/window/programIcon.png"));

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("Close button on view clicked!");
                engine.closeProgram(engine.getProperties().getPlanedProgrammShutdownOnUserAction(), true);
            }
        });
        new MoveListener(menuBar, primaryStage);
        super.initController(engine, primaryStage, scene, mainStage);
    }

    @FXML
    private void onQuitClicked(ActionEvent actionEvent) {
        engine.closeProgram(engine.getProperties().getPlanedProgrammShutdownOnUserAction(), true);
    }

    @FXML
    private void onStartClicked(ActionEvent actionEvent) {
        engine.getDiscEngine().startBotApplication();
    }

    @FXML
    private void onRestartClicked(ActionEvent actionEvent) {
        engine.getDiscEngine().rebootBotApplication();
    }

    @FXML
    private void onPropertiesClicked(ActionEvent actionEvent) {
        engine.getViewEngine().showProperties();
    }

    @FXML
    private void onCommandLineKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleUserCommand();
        }
    }

    @FXML
    private void onStopClicked(ActionEvent actionEvent) {
        engine.getDiscEngine().shutdownBotApplication();
    }

    @FXML
    private void onSendClicked(MouseEvent mouseEvent) {
        handleUserCommand();
    }

    @FXML
    private void onSaveFilesClicked(ActionEvent actionEvent) {
        engine.safeAllFiles();
    }

    @FXML
    private void onReloadFilesClicked(ActionEvent actionEvent) {
        engine.loadAllFiles();
    }

    @FXML
    private void onMindEditorClicked(ActionEvent actionEvent) {
        engine.getViewEngine().showMindEditor();
    }

    private void handleUserCommand() {
        String command = commandInputLine.getText();
        commandInputLine.setText("");
        addCommandLine(command, true);
        engine.getConsoleCommandHandler().handleConsoleCommand(command);
    }

    public void addCommandLine(String text, boolean fromUser) {

        if (fromUser) {

            commandOutputLine.setText(commandOutputLine.getText() + "\n[" + dateConfig.getDateToday() + "] User>> " + text);
        } else {
            commandOutputLine.setText(commandOutputLine.getText() + "\n[" + dateConfig.getDateToday() + "] System>> " + text);
            try {
                if (!refreshThread.isAlive()) {
                    refreshThread = new Thread(new refreshThread());
                    refreshThread.start();
                }
            } catch (Exception e) {
                refreshThread = new Thread(new refreshThread());
                refreshThread.start();
            }
        }
    }

    public void updateStartStopButton(){
        if(!engine.getDiscEngine().isBotApplicationRunning()){
            buttonStartBot.setImage(startBotState1);
            runAnimation.setVisible(false);
        } else {
            buttonStartBot.setImage(startBotState2);
            runAnimation.setVisible(true);
        }
    }

    public void clearCommandLine() {
        commandOutputLine.setText("");
    }

    private class refreshThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            commandOutputLine.setScrollTop(Double.MAX_VALUE);
        }
    }
}
