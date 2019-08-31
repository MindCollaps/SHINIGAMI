package SceneCore.ScenesController;

import BotAiCore.Librarys.AiCmdModAnswer;
import BotAiCore.Librarys.AiCmdModification;
import BotAiCore.Librarys.AiCommand;
import Engines.Engine;
import SceneCore.AllertBox;
import SceneCore.MoveListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MindEditorController extends Controller implements Initializable {

    ArrayList<CommandsLine> commandsLines = new ArrayList<>();

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private  MenuBar menuBar;

    @FXML
    private VBox contentVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void initController(Engine engine, Stage primaryStage, Scene scene, Stage mainStage) {
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Mind editor");
        primaryStage.initModality(Modality.NONE);
        primaryStage.setScene(scene);
        primaryStage.getIcons().setAll(new Image("icons/window/mindIcon.png"));
        new MoveListener(menuBar, primaryStage);
        super.initController(engine, primaryStage, scene, mainStage);
    }

    @FXML
    private void onOpenClicked(ActionEvent actionEvent) {
        String path = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(engine.getFileUtils().getHome() + "/bot/mind/"));
        fileChooser.setInitialFileName("mind.exodus");
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
        try {
            engine.getAiEngine().setAiCommands(saveContent());
        } catch (Exception e) {
            return;
        }
    }

    @FXML
    private void onSavedAsClicked(ActionEvent actionEvent) {
        String path = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(engine.getFileUtils().getHome() + "/bot/mind/"));
        fileChooser.setInitialFileName("mind.exodus");
        try {
            path = fileChooser.showSaveDialog(primaryStage).getAbsolutePath();
        } catch (Exception e) {
            new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayMessage("Error", "Please select a valid path!", "ok", "buttonBlue", false);
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
        try {
            engine.getAiEngine().setAiCommands(saveContent());
        } catch (Exception e) {
            return;
        }
        engine.getViewEngine().closeMindEditor();
    }

    @FXML
    private void onSavedWithoutAsClicked(ActionEvent actionEvent) {
        int result = new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayEvenOd("Are you sure you don´t want to save the created files?", "Are u sure about that?", "yes, close", "no, save", false);
        if(result==1){
            onSavedClicked(null);
        }
        engine.getViewEngine().closeMindEditor();
    }

    public void updateContent(ArrayList<AiCommand> listOfCommands) {
        contentVBox.getChildren().clear();
        commandsLines.clear();
        AiCommand current;
        ModsLine modLine;
        AnswersLine answerLine;
        for (int i = 0; i < listOfCommands.size(); i++) {
            CommandsLine commandLine = new CommandsLine();
            current = listOfCommands.get(i);

            commandLine.setTxtCommandInvoke(current.getCommandInvoke());
            try {
                commandLine.setTxtCommandType(current.getCommandType().toString());
            } catch (Exception ignore){
            }

            for (int j = 0; j < current.getHumanSpellingList().size(); j++) {
                if(commandLine.getTxtCommandHumanSpelling().equals("")){
                    commandLine.setTxtCommandHumanSpelling(current.getHumanSpellingList().get(j));
                } else {
                    commandLine.setTxtCommandHumanSpelling(commandLine.getTxtCommandHumanSpelling() + "\n" + current.getHumanSpellingList().get(j));
                }
            }

            for (int j = 0; j < current.getModificators().size(); j++) {
                modLine = new ModsLine(commandLine);
                modLine.setTxtModInvoke(current.getModificators().get(j).getInvoke());
                for (int k = 0; k < current.getModificators().get(j).getHumanSpellingList().size(); k++) {
                    if(modLine.getTxtModHumanSpelling().equalsIgnoreCase("")){
                        modLine.setTxtModHumanSpelling(current.getModificators().get(j).getHumanSpellingList().get(k));
                    } else {
                        modLine.setTxtModHumanSpelling(modLine.getTxtModHumanSpelling() + "\n" + current.getModificators().get(j).getHumanSpellingList().get(k));
                    }
                }

                for (int k = 0; k < current.getModificators().get(j).getAnswers().size(); k++) {
                    answerLine = new AnswersLine(modLine);
                    answerLine.setTxtAnswerEmotes(current.getModificators().get(j).getAnswers().get(k).getEmoteLevel());
                    for (int l = 0; l < current.getModificators().get(j).getAnswers().get(k).getAnswers().size(); l++) {
                        if(answerLine.getTxtAnswers().equalsIgnoreCase("")){
                            answerLine.setTxtAnswers(current.getModificators().get(j).getAnswers().get(k).getAnswers().get(l));
                        } else {
                            answerLine.setTxtAnswers(answerLine.getTxtAnswers() + "\n" + current.getModificators().get(j).getAnswers().get(k).getAnswers().get(l));
                        }
                    }
                    modLine.addAnswer(answerLine);
                }
                commandLine.addMod(modLine);
            }
            commandsLines.add(commandLine);
            contentVBox.getChildren().add(commandLine.getParent());
        }
    }

    public void updateContentFromSource(String path) {
        ArrayList<AiCommand> commands = null;
        try {
            commands = (ArrayList<AiCommand>) engine.getFileUtils().loadObject(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(commands==null){
            new AllertBox(null,Modality.APPLICATION_MODAL, engine).displayMessage("Error", "This file is invalid!", "Ok", "buttonGreen", false);
            System.out.println("This File is invalid");
            return;
        }
        updateContent(commands);
    }

    public ArrayList<AiCommand> saveContent() throws Exception{
        ArrayList<AiCommand> returnValue = new ArrayList<>();

        for (int i = 0; i < commandsLines.size(); i++) {
            AiCommand current = new AiCommand();
            CommandsLine commandLine = commandsLines.get(i);
            current.setCommandInvoke(commandLine.getTxtCommandInvoke());
            String[] commandHuman = commandLine.getTxtCommandHumanSpelling().split("\n");
            ArrayList<String> commandHumanArray = new ArrayList<>();
            commandHumanArray.addAll(Arrays.asList(commandHuman));
            current.setHumanSpellingList(commandHumanArray);
            try {
                current.setCommandType(engine.getUtilityBase().convertStringToCommandType(commandLine.getTxtCommandType()));
            } catch (Exception e) {
                new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayMessage("Error", "The Command Type for command (" + current.getCommandInvoke() + ") is invalid!", "ok", "buttonGreen", false);
                throw new Exception("Command type invalid!");
            }

            for (int j = 0; j < commandLine.getModsLines().size(); j++) {
                ModsLine modLine = commandLine.getModsLines().get(j);
                AiCmdModification modification = new AiCmdModification();
                modification.setInvoke(modLine.getTxtModInvoke());
                ArrayList<String> modificationHumanArray = new ArrayList<>();
                String[] modHuman = modLine.getTxtModHumanSpelling().split("\n");
                modificationHumanArray.addAll(Arrays.asList(modHuman));
                modification.setHumanSpellingList(modificationHumanArray);

                for (int k = 0; k < modLine.getAnswersLines().size(); k++) {
                    AnswersLine answerLine = modLine.getAnswersLines().get(k);
                    AiCmdModAnswer answer = new AiCmdModAnswer();
                    answer.setEmoteLevel(answerLine.getTxtAnswerEmotes());
                    ArrayList<String> answersArray = new ArrayList<>();
                    String[] answers = answerLine.getTxtAnswers().split("\n");
                    answersArray.addAll(Arrays.asList(answers));
                    answer.setAnswers(answersArray);

                    modification.getAnswers().add(answer);
                }
                current.getModificators().add(modification);
            }
            returnValue.add(current);
        }
        return returnValue;
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
        ArrayList<AiCommand> aiCommands = null;
        try {
            aiCommands = saveContent();
        } catch (Exception e) {
            return;
        }
        engine.getFileUtils().saveOject(path, aiCommands);
}

    //Command Line
    private class CommandsLine {

        private ScrollPane scrollPane;
        private AnchorPane mainPane;
        private TextArea txtCommandHumanSpelling;
        private TextField txtCommandInvoke;
        private TextField txtCommandType;
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
                engine.closeProgram(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
            }
            Parent commandLine = root;

            mainPane = new AnchorPane((AnchorPane) commandLine.lookup("#mainPane"));
            txtCommandHumanSpelling = (TextArea) mainPane.lookup("#txtCommandHumanSpelling");
            buttonAddMod = (Button) mainPane.lookup("#buttonGreen");
            txtCommandInvoke = (TextField) mainPane.lookup("#txtCommandInvoke");
            txtCommandType = (TextField) mainPane.lookup("#txtCommandType");
            buttonDeleteCommand = (Button) mainPane.lookup("#buttonRed");
            scrollPane = (ScrollPane) mainPane.lookup("#scrollPane");
            vBoxMods = (VBox)scrollPane.contentProperty().get();

            CommandsLine cmdLine = this;

            buttonDeleteCommand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    commandsLines.remove(cmdLine);
                    contentVBox.getChildren().remove(cmdLine.getParent());
                }
            });

            buttonAddMod.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ModsLine modLine = new ModsLine(cmdLine);
                    addMod(modLine);
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

        public void addMod(ModsLine line){
            vBoxMods.getChildren().add(line.getParent());
            modsLines.add(line);
        }

        public VBox getvBoxMods() {
            return vBoxMods;
        }

        public String getTxtCommandType() {
            return txtCommandType.getText();
        }

        public void setTxtCommandType(String txtCommandType) {
            this.txtCommandType.setText(txtCommandType);
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

        private CommandsLine commandLine;

        private ArrayList<AnswersLine> answersLines;

        public ModsLine(CommandsLine commandLine) {
            this.commandLine = commandLine;
            answersLines = new ArrayList<>();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/mindEditor/mods.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (Exception ex) {
                ex.printStackTrace();
                engine.closeProgram(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
            }
            Parent modsLine = root;

            mainPane = (AnchorPane) modsLine.lookup("#mainPain");
            txtModInvoke = (TextField) mainPane.lookup("#txtModInvoke");
            txtModHumanSpelling = (TextArea) mainPane.lookup("#txtModHumanSpelling");
            buttonAddAnswer = (Button) mainPane.lookup("#buttonGreen");
            buttonDeleteCommand = (Button) mainPane.lookup("#buttonRed");
            scrollPane = (ScrollPane) mainPane.lookup("#scrollPane");
            vBoxAnswers = (VBox) scrollPane.contentProperty().get();

            ModsLine modsLines = this;

            buttonDeleteCommand.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    commandLine.getModsLines().remove(modsLines);
                    commandLine.getvBoxMods().getChildren().remove(getParent());
                }
            });

            buttonAddAnswer.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    AnswersLine answersLine = new AnswersLine(modsLines);
                    addAnswer(answersLine);
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

        public void addAnswer(AnswersLine line){
            vBoxAnswers.getChildren().add(line.getParent());
            answersLines.add(line);
        }

        public VBox getvBoxAnswers() {
            return vBoxAnswers;
        }
    }

    //Answers line
    private class AnswersLine {

        private AnchorPane mainPane;
        private TextField txtAnswerEmotes;
        private TextArea txtAnswers;
        private Button buttonDeleteAnswer;

        private ModsLine answersList;

        public AnswersLine(ModsLine answersList) {
            this.answersList = answersList;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/mindEditor/answers.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (Exception ex) {
                ex.printStackTrace();
                engine.closeProgram(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
            }

            mainPane = (AnchorPane) root.lookup("#mainPane");
            txtAnswerEmotes = (TextField) mainPane.lookup("#txtEmoteLevels");
            txtAnswers = (TextArea) mainPane.lookup("#txtAnswers");
            buttonDeleteAnswer = (Button) mainPane.lookup("#buttonRed");

            AnswersLine answersLine = this;

            buttonDeleteAnswer.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    answersList.getAnswersLines().remove(answersLine);
                    answersList.getvBoxAnswers().getChildren().remove(mainPane);
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
