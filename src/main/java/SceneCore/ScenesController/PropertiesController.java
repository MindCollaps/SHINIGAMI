package SceneCore.ScenesController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesController extends Controller implements Initializable {

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
