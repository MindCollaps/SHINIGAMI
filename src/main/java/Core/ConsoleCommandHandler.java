package Core;

import Engines.Engine;
import SceneCore.AllertBox;
import javafx.stage.Modality;

public class ConsoleCommandHandler {

    Engine engine;

    public ConsoleCommandHandler(Engine engine) {
        this.engine = engine;
    }

    public void handleConsoleCommand(String[] command) {
        String args0;
        try {
            args0 = command[0];
        } catch (Exception e) {
            return;
        }
        switch (args0.toLowerCase()) {
            case "reloadall":
                engine.loadAllFiles();
                engine.getBotEngine().reboot();
                break;
            case "reloaddata":
                engine.loadAllFiles();
                break;
            case "debugai":
                if (engine.getBotEngine().isDebugAi()) {
                    engine.getBotEngine().setDebugAi(false);
                } else {
                    engine.getBotEngine().setDebugAi(true);
                }
                System.out.println("Debug AI is now " + engine.getBotEngine().isDebugAi());
                break;

            case "debug":
                if(engine.getProperties().isDebug()){
                    engine.getProperties().setDebug(false);
                } else {
                    engine.getProperties().setDebug(true);
                }
                System.out.println("Debug is now " + engine.getProperties().isDebug());
                break;

            case "clear":
                engine.getViewEngine().clearConsollogOnGui();
                break;

            case "startbot":
                engine.getBotEngine().startBotApplication();
                break;

            case "stopbot":
                engine.getBotEngine().shutdown();
                break;

            case "restartbot":
                engine.getBotEngine().reboot();
                break;

            case "help":
                System.out.println("reloadAll - Reload the complete bot\nstartBot - starts the bot...UwU\nreloadData -reload onely the properties and minds\ndebugai - toggle the function: shows the output of the ai in the console\nclear - clears the console\nrestartBot - restarts the bot\nstopBot - stops the bot");
                break;
            default:
                System.out.println("unknown command! Use \"help\" to help...yourself :D");
                break;
        }
    }
}
