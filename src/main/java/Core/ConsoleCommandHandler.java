package Core;

import Engines.Engine;

public class ConsoleCommandHandler {

    Engine engine;

    public ConsoleCommandHandler(Engine engine) {
        this.engine = engine;
    }

    public void handleConsoleCommand(String command) {
        String args0;
        try {
            args0 = command.split(" ")[0];
        } catch (Exception e) {
            return;
        }
        switch (args0.toLowerCase()) {
            case "reloadall":
                engine.loadAllFiles();
                engine.getDiscEngine().rebootBotApplication();
                break;
            case "reloaddata":
                engine.loadAllFiles();
                break;
            case "debugai":
                if (engine.getDiscEngine().isDebugAi()) {
                    engine.getDiscEngine().setDebugAi(false);
                } else {
                    engine.getDiscEngine().setDebugAi(true);
                }
                System.out.println("Debug AI is now " + engine.getDiscEngine().isDebugAi());
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
                engine.getDiscEngine().startBotApplication();
                break;

            case "stopbot":
                engine.getDiscEngine().shutdownBotApplication();
                break;

            case "restartbot":
                engine.getDiscEngine().rebootBotApplication();
                break;

            case "shini":
                System.out.println(engine.getAiEngine().runAi(command, true));
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
