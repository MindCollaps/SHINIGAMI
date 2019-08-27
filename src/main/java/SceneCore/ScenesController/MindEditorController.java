package SceneCore.ScenesController;

import BotAiCore.Librarys.AiCommand;
import SceneCore.AllertBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MindEditorController extends Controller implements Initializable {

    ArrayList<CommandsLine> commandsLines = new ArrayList<>();

    @FXML
    private VBox contentVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void onOpenClicked(ActionEvent actionEvent) {
        String path = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(engine.getFileUtils().home + "/bot/mind/"));
        fileChooser.setInitialFileName("mind.exodus");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("exodus"));
        try {
            path = fileChooser.showOpenDialog(primaryStage).getAbsolutePath();
        } catch (Exception e) {
            new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayMessage("Error", "Please select a validt path!", "ok", "buttonBlue", false);
            return;
        }
        updateContentFromSource(path);
    }

    @FXML
    private void onSavedClicked(ActionEvent actionEvent) {
    }

    @FXML
    private void onSavedAsClicked(ActionEvent actionEvent) {
        String path = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(engine.getFileUtils().home + "/bot/mind/"));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("exodus"));
        fileChooser.setInitialFileName("mind.exodus");
        try {
            path = fileChooser.showSaveDialog(primaryStage).getAbsolutePath();
        } catch (Exception e) {
            new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayMessage("Error", "Please select a validt path!", "ok", "buttonBlue", false);
            return;
        }
        saveContentAs(path);
    }

    @FXML
    private void onAddCommandClicked(MouseEvent mouseEvent) {
        CommandsLine line = new CommandsLine();
        commandsLines.add(line);
        contentVBox.getChildren().add(line.getParent());
    }

    @FXML
    private void onCloseClicked(MouseEvent mouseEvent) {
        engine.getViewEngine().closeMindEditor();
    }

    public void updateContent() {
        AiCommand current;
        CommandsLine commandLine = new CommandsLine();
        ModsLine modLine;
        AnswersLine answerLine;
        for (int i = 0; i < engine.getBotEngine().getAiEngine().getAiCommands().size(); i++) {
            current = engine.getBotEngine().getAiEngine().getAiCommands().get(i);
            commandLine.setTxtCommandInvoke(current.getCommandInvoke());

            for (int j = 0; j < current.getHumanSpellingList().size(); j++) {
                if(commandLine.getTxtCommandHumanSpelling().equals("")){
                    commandLine.setTxtCommandHumanSpelling(current.getHumanSpellingList().get(j));
                } else {
                    commandLine.setTxtCommandHumanSpelling(commandLine.getTxtCommandHumanSpelling() + "\n" + current.getHumanSpellingList().get(j));
                }
            }

            for (int j = 0; j < current.getModificators().size(); j++) {
                modLine = new ModsLine(commandLine.vBoxMods);
                modLine.setTxtModInvoke(current.getModificators().get(j).getInvoke());
                for (int k = 0; k < current.getModificators().get(j).getAnswers().size(); k++) {
                    if(modLine.getTxtModHumanSpelling().equalsIgnoreCase("")){
                        modLine.setTxtModHumanSpelling(current.getModificators().get(j).getHumanSpellingList().get(k));
                    } else {
                        modLine.setTxtModHumanSpelling(modLine.getTxtModHumanSpelling() + "\n" + current.getModificators().get(j).getHumanSpellingList().get(k));
                    }
                }

                for (int k = 0; k < current.getModificators().get(j).getAnswers().size(); k++) {
                    answerLine = new AnswersLine(modLine.vBoxAnswers);
                    answerLine.setTxtAnswerEmotes(current.getModificators().get(j).getAnswers().get(k).getEmoteLevel());
                    for (int l = 0; l < current.getModificators().get(j).getAnswers().get(k).getAnswers().size(); l++) {
                        if(answerLine.getTxtAnswers().equalsIgnoreCase("")){
                            answerLine.setTxtAnswers(current.getModificators().get(j).getAnswers().get(k).getAnswers().get(l));
                        } else {
                            answerLine.setTxtAnswers(answerLine.getTxtAnswers() + "\n" + current.getModificators().get(j).getAnswers().get(k).getAnswers().get(l));
                        }
                    }
                }
            }
            commandsLines.add(commandLine);
        }
    }

    public void updateContentFromSource(String path) {

    }

    public void saveContent() {
        AiCommand current = new AiCommand();

        CommandsLine commandLine;
        ModsLine modLine;
        AnswersLine answerLine;

        for (int i = 0; i < commandsLines.size(); i++) {
            commandLine = commandsLines.get(i);
            current.setCommandInvoke(commandLine.getTxtCommandInvoke());

            //TODO:weiter geths lol
        }
    }

    public ArrayList<String> convertStringToArray(String convert){
        int nextInt = 0;
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < convert.length(); i++) {
            if (convert.getBytes()[i]=='\n'){
                nextInt = i +1;
                arrayList.add(convert.substring(nextInt, i));
            }
        }
        return arrayList;
    }

    public void saveContentAs(String path) {

    }

    //Command Line
    private class CommandsLine {

        private ScrollPane scrollPane;
        private AnchorPane mainPane;
        private TextArea txtCommandHumanSpelling;
        private TextField txtCommandInvoke;
        private Button buttonAddMod;
        private Button buttonDeleteCommand;
        private VBox vBoxMods;

        private ArrayList<ModsLine> modsLines;

        CommandsLine() {

            modsLines = new ArrayList<>();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/mindEditor/command.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (Exception ex) {
                ex.printStackTrace();
                engine.closeProgramm(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
            }
            Parent commandLine = root;

            mainPane = new AnchorPane((AnchorPane) commandLine.lookup("#mainPane"));
            txtCommandHumanSpelling = (TextArea) mainPane.lookup("#txtCommandHumanSpelling");
            buttonAddMod = (Button) mainPane.lookup("#buttonGreen");
            txtCommandInvoke = (TextField) mainPane.lookup("txtCommandInvoke");
            buttonDeleteCommand = (Button) mainPane.lookup("#buttonRed");
            scrollPane = (ScrollPane) mainPane.lookup("#scrollPane");
            vBoxMods = (VBox)scrollPane.contentProperty().get();

            CommandsLine cmdLine = this;

            buttonDeleteCommand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    contentVBox.getChildren().remove(cmdLine.getParent());
                }
            });

            buttonAddMod.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ModsLine modLine = new ModsLine(vBoxMods);
                    vBoxMods.getChildren().add(modLine.getParent());
                    modsLines.add(modLine);
                }
            });
        }

        public String getTxtCommandHumanSpelling() {
            return txtCommandHumanSpelling.getText();
        }

        public String getTxtCommandInvoke() {
            return txtCommandInvoke.getText();
        }

        public ArrayList<ModsLine> getModsLines() {
            return modsLines;
        }

        public void setTxtCommandHumanSpelling(String txtCommandHumanSpelling) {
            this.txtCommandHumanSpelling.setText(txtCommandHumanSpelling);
        }

        public void setTxtCommandInvoke(String txtCommandInvoke) {
            this.txtCommandInvoke.setText(txtCommandInvoke);
        }

        public Parent getParent(){
            return mainPane;
        }


    }

    //Mods line
    private class ModsLine {

        private ScrollPane scrollPane;
        private AnchorPane mainPane;
        private TextArea txtModHumanSpelling;
        private TextField txtModInvoke;
        private Button buttonAddAnswer;
        private Button buttonDeleteCommand;
        private VBox vBoxAnswers;

        private VBox commandLine;

        private ArrayList<AnswersLine> answersLines;

        public ModsLine(VBox commandLine) {
            this.commandLine = commandLine;
            answersLines = new ArrayList<>();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/mindEditor/mods.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (Exception ex) {
                ex.printStackTrace();
                engine.closeProgramm(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
            }
            Parent modsLine = root;

            mainPane = (AnchorPane) modsLine.lookup("#mainPain");
            txtModInvoke = (TextField) mainPane.lookup("#txtModInvoke");
            txtModHumanSpelling = (TextArea) mainPane.lookup("#txtModHumanSpelling");
            buttonAddAnswer = (Button) mainPane.lookup("#buttonGreen");
            buttonDeleteCommand = (Button) mainPane.lookup("#buttonRed");
            scrollPane = (ScrollPane) mainPane.lookup("#scrollPane");
            vBoxAnswers = (VBox) scrollPane.contentProperty().get();

            buttonDeleteCommand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    commandLine.getChildren().remove(getParent());
                }
            });

            buttonAddAnswer.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    AnswersLine answersLine = new AnswersLine(vBoxAnswers);
                    vBoxAnswers.getChildren().add(answersLine.getParent());
                    answersLines.add(answersLine);
                }
            });
        }

        public String getTxtModHumanSpelling() {
            return txtModHumanSpelling.getText();
        }

        public String getTxtModInvoke() {
            return txtModInvoke.getText();
        }

        public ArrayList<AnswersLine> getAnswersLines() {
            return answersLines;
        }

        public void setTxtModHumanSpelling(String txtModHumanSpelling) {
            this.txtModHumanSpelling.setText(txtModHumanSpelling);
        }

        public void setTxtModInvoke(String txtModInvoke) {
            this.txtModInvoke.setText(txtModInvoke);
        }

        public Parent getParent(){
            return this.mainPane;
        }
    }

    //Answers line
    private class AnswersLine {

        private AnchorPane mainPane;
        private TextArea txtAnswerEmotes;
        private TextField txtAnswers;
        private Button buttonDeleteAnswer;

        private VBox answersList;

        public AnswersLine(VBox answersList) {
            this.answersList = answersList;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/mindEditor/answers.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (Exception ex) {
                ex.printStackTrace();
                engine.closeProgramm(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
            }

            mainPane = (AnchorPane) root.lookup("#mainPane");
            txtAnswerEmotes = (TextArea) mainPane.lookup("#txtEmoteLevels");
            txtAnswers = (TextField) mainPane.lookup("#txtAnswers");
            buttonDeleteAnswer = (Button) mainPane.lookup("#buttonRed");

            buttonDeleteAnswer.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    answersList.getChildren().remove(mainPane);
                }
            });
        }

        public Parent getParent(){
            return mainPane;
        }

        public String getTxtAnswerEmotes() {
            return txtAnswerEmotes.getText();
        }

        public String getTxtAnswers() {
            return txtAnswers.getText();
        }

        public void setTxtAnswerEmotes(String txtAnswerEmotes) {
            this.txtAnswerEmotes.setText(txtAnswerEmotes);
        }

        public void setTxtAnswers(String txtAnswers) {
            this.txtAnswers.setText(txtAnswers);
        }
    }
}
