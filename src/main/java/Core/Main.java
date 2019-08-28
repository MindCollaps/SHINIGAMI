package Core;

import Engines.Engine;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public Engine engine;
    static String[] argis;

    public static void main(String[] args){
        argis = args;
        launch("");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        engine = new Engine();
        engine.boot(argis, primaryStage);
    }
}
