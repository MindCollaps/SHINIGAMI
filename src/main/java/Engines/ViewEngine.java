package Engines;

import SceneCore.AllertBox;
import SceneCore.ScenesController.HomeController;
import SceneCore.ScenesController.MindEditorController;
import SceneCore.ScenesController.PropertiesController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

public class ViewEngine {

    Engine engine;

    //JavaFX stuff
    private Stage primaryStage;
    private HomeController homeController;
    private PropertiesController propertiesController;
    private MindEditorController mindEditorController;

    private AllertBox propertiesAllertBox;

    boolean viewLoaded = false;

    public ViewEngine(Engine engine, Stage primaryStage) {
        this.engine = engine;
        this.primaryStage = primaryStage;
    }

    public void boot(){
        setupView();
    }

    //Implements everything what we need to see a window and start the window!
    private void setupView(){
        loadScenes();
        primaryStage.setScene(homeController.getScene());
        primaryStage.setTitle(engine.getProperties().getBotApplicationName() + " manager");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        //primaryStage.getIcons().setAll(new Image(""));
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("Close button on view clicked!");
                engine.closeProgramm(engine.getProperties().getPlanedProgrammShutdownOnUserAction(), true);
            }
        });

        primaryStage.show();
        viewLoaded = true;
    }

    private void loadScenes(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/home.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
            engine.closeProgramm(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
        }
        homeController = loader.getController();
        Scene scene = new Scene(root);

        homeController.initController(engine, primaryStage, scene);

        loader = new FXMLLoader(getClass().getResource("/Scenes/properties.fxml"));
        try {
            root = loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
            engine.closeProgramm(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
        }
        propertiesController = loader.getController();
        scene = new Scene(root);
        propertiesController.initController(engine,primaryStage,scene);

        loader = new FXMLLoader(getClass().getResource("/Scenes/mindEditor.fxml"));
        try {
            root = loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
            engine.closeProgramm(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
        }
        mindEditorController = loader.getController();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        mindEditorController.initController(engine,new Stage(),scene);
    }

    public void printConsollogOnGui(String text){
        homeController.addCommandLine(text, false);
    }

    public void clearConsollogOnGui(){
        homeController.clearCommandLine();
    }

    public void displayMessageAllertBox(String title, String message){
        if(viewLoaded){
            new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayMessage(title, message, "ok", "buttonBlue", false);
        }
    }

    public void showProperties(){
        primaryStage.hide();
        propertiesController.updatePropertiesWindow();
        propertiesAllertBox = new AllertBox(propertiesController.getScene(), Modality.APPLICATION_MODAL, engine);
        propertiesAllertBox.displayFromRoot("Properties", false);
    }

    public void showMindEditor(){
        primaryStage.hide();
        mindEditorController.updateContent(engine.getBotEngine().getAiEngine().getAiCommands());
        mindEditorController.getPrimaryStage().show();
    }

    public void closeMindEditor(){
        mindEditorController.getPrimaryStage().close();
        primaryStage.show();
    }

    public void closeProperties(){
        propertiesAllertBox.close();
        primaryStage.show();
    }

    public boolean isViewLoaded() {
        return viewLoaded;
    }

}
