package SceneCore.ScenesController;

import Engines.Engine;
import SceneCore.MoveListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesController extends Controller implements Initializable {

    @FXML
    public AnchorPane mainPane;
    @FXML
    private CheckBox cBShowToken;
    @FXML
    private TextField txtBotVersion;
    @FXML
    private TextField txtBotCommandPrefix;
    @FXML
    private TextField txtBotDefaultYTPlaylist;
    @FXML
    private TextField txtBotToken;
    @FXML
    private TextField txtBotGame;
    @FXML
    private TextField txtBotName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void initController(Engine engine, Stage primaryStage, Scene scene, Stage mainStage) {
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Properties");
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setScene(scene);
        primaryStage.getIcons().setAll(new Image("icons/window/propsIcon.png"));
        new MoveListener(mainPane, primaryStage);
        super.initController(engine, primaryStage, scene, mainStage);
    }

    @FXML
    private void onSaveClicked(MouseEvent mouseEvent) {
        if (!txtBotVersion.getText().equalsIgnoreCase("")) {
            engine.getProperties().setBotApplicationVersion(txtBotVersion.getText());
        }
        if (!txtBotGame.getText().equalsIgnoreCase("")) {
            engine.getProperties().setBotApplicationGame(txtBotGame.getText());
        }
        if (!txtBotName.getText().equalsIgnoreCase("")) {
            engine.getProperties().setBotApplicationName(txtBotName.getText());
        }
        if (!txtBotDefaultYTPlaylist.getText().equalsIgnoreCase("")) {
            engine.getProperties().setCommandMusicDefaultYTPlaylist(txtBotDefaultYTPlaylist.getText());
        }
        if (!txtBotCommandPrefix.getText().equalsIgnoreCase("")) {
            engine.getProperties().setBotApplicationPrefix(txtBotCommandPrefix.getText());
        }
        if (!txtBotToken.getText().equalsIgnoreCase("")) {
            engine.getProperties().setBotApplicationToken(txtBotToken.getText());
        }
        primaryStage.setTitle(engine.getProperties().getBotApplicationName() + " manager");
        engine.getViewEngine().closeProperties();
    }

    @FXML
    private void onShowTokenPressed(MouseEvent mouseEvent) {
        updateTokenField();
    }

    @FXML
    private void onCloseClicked(MouseEvent mouseEvent) {
        engine.getViewEngine().closeProperties();
    }

    private void updateTokenField() {
        if (cBShowToken.isSelected()) {
            txtBotToken.setText(engine.getProperties().getBotApplicationToken());
        } else {
            txtBotToken.setText("");
        }
    }

    public void updatePropertiesWindow() {
        updateTokenField();
        txtBotCommandPrefix.setText(engine.getProperties().getBotApplicationPrefix());
        txtBotDefaultYTPlaylist.setText(engine.getProperties().getCommandMusicDefaultYTPlaylist());
        txtBotName.setText(engine.getProperties().getBotApplicationName());
        txtBotGame.setText(engine.getProperties().getBotApplicationGame());
        txtBotVersion.setText(engine.getProperties().getBotApplicationVersion());
    }
}
