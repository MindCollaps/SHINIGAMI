package SceneCore.ScenesController;

import Utils.dateConfig;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
        menuBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        menuBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    @FXML
    private void onQuitClicked(ActionEvent actionEvent) {
        engine.closeProgramm(engine.getProperties().getPlanedProgrammShutdownOnUserAction(), true);
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
