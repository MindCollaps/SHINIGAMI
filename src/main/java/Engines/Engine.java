package Engines;

import Core.ConsoleCommandHandler;
import Threads.SystemInListenerThread;
import Threads.SystemOutListenerThread;
import Utils.FileUtils;
import Utils.Properties;
import Utils.UtilityBase;
import javafx.stage.Stage;

public class Engine {

    //Other Engine
    BotApplicationEngine botEngine;
    ViewEngine viewEngine;

    //Engine stuff
    private Properties properties;
    private FileUtils fileUtils;
    ConsoleCommandHandler consoleCommandHandler;
    UtilityBase utilityBase;

    Thread systemOutListenerThread;

    public void boot(String[] args){
        //Setup engine classes
        fileUtils = new FileUtils(this);
        loadProperties();
        consoleCommandHandler = new ConsoleCommandHandler(this);
        botEngine = new BotApplicationEngine(this);
        viewEngine = new ViewEngine(this);
        utilityBase = new UtilityBase(this);
        loadAllFiles(false);
        systemOutListenerThread = new Thread(new SystemOutListenerThread(this));

        String args0 = "";
        try {
            args0 = args[0];
        } catch (Exception ignored) {
        }

        if(args0.equals("")){
            viewEngine.boot();
            systemOutListenerThread.start();
        } else if (args0.equalsIgnoreCase("view=false")){
            System.out.println("View will not load!");
        } else {
            System.out.println(args0 + " is an unknown start argument!");
        }
        new Thread(new SystemInListenerThread(this)).start();
    }

    public void closeProgram(int shutdownCode, boolean safe){
        if(safe)safeAllFiles();
        if(viewEngine.isViewLoaded()){
            viewEngine.closeProperties();
            viewEngine.closeMindEditor();
            viewEngine.closeHome();
        }
        System.exit(shutdownCode);
    }

    public void safeAllFiles(){
        safeProperties();
        botEngine.getFilesHandler().saveAllBotFiles();
        botEngine.getAiEngine().saveAiMind();
    }

    public void loadAllFiles(){
        loadProperties();
        botEngine.getFilesHandler().loadAllBotFiles();
        botEngine.getAiEngine().loadAiMind();
    }

    private void loadAllFiles(boolean loadProperties){
        if(loadProperties)loadProperties();
        botEngine.getFilesHandler().loadAllBotFiles();
        botEngine.getAiEngine().loadAiMind();
    }

    private void safeProperties(){
        fileUtils.saveOject(fileUtils.getHome() + "/properties.prop", properties);
    }

    private void loadProperties(){
        try {
            properties = (Properties) fileUtils.loadObject(fileUtils.getHome() + "/properties.prop");
        } catch (Exception e) {
            properties = new Properties();
        }
    }

    public Properties getProperties(){
        return this.properties;
    }

    public ConsoleCommandHandler getConsoleCommandHandler() {
        return consoleCommandHandler;
    }

    public FileUtils getFileUtils() {
        return fileUtils;
    }

    public BotApplicationEngine getBotEngine() {
        return botEngine;
    }

    public ViewEngine getViewEngine() {
        return viewEngine;
    }

    public UtilityBase getUtilityBase() {
        return utilityBase;
    }
}
