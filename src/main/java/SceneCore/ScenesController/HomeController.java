package SceneCore.ScenesController;

import Engines.Engine;
import SceneCore.MoveListener;
import Utils.dateConfig;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
    private TextArea commandOutputLine;
    @FXML
    private TextField commandInputLine;
    @FXML
    private MenuBar menuBar;

    private double xOffset = 0;
    private double yOffset = 0;

    Thread refreshThread;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void initController(Engine engine, Stage primaryStage, Scene scene) {
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle(engine.getProperties().getBotApplicationName() + " manager");
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setScene(scene);
        primaryStage.getIcons().setAll(new Image("Scenes/icons/programIcon.jpg"));

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("Close button on view clicked!");
                engine.closeProgram(engine.getProperties().getPlanedProgrammShutdownOnUserAction(), true);
            }
        });
        new MoveListener(menuBar, primaryStage);
        super.initController(engine, primaryStage, scene);
    }

    @FXML
    private void onQuitClicked(ActionEvent actionEvent) {
        engine.closeProgram(engine.getProperties().getPlanedProgrammShutdownOnUserAction(), true);
    }

    @FXML
    private void onStartClicked(ActionEvent actionEvent) {
        engine.getBotEngine().startBotApplication();
    }

    @FXML
    private void onRestartClicked(ActionEvent actionEvent) {
        engine.getBotEngine().reboot();
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
        engine.getBotEngine().shutdown();
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
        engine.getConsoleCommandHandler().handleConsoleCommand(command.split(" "));
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
