package engines;

import core.ConsoleCommandHandler;
import threads.SystemInListenerThread;
import threads.SystemOutListenerThread;
import utils.FileUtils;
import utils.Properties;
import utils.UtilityBase;
import javafx.stage.Stage;

public class Engine {

    //Other Engine
    private DiscApplicationEngine discEngine;
    private ViewEngine viewEngine;
    private TeleApplicationEngine teleApplicationEngine;

    //Engine stuff
    private Properties properties;
    private FileUtils fileUtils;
    private ConsoleCommandHandler consoleCommandHandler;
    private UtilityBase utilityBase;
    private AiEngine aiEngine;

    boolean viewLoaded = false;

    Thread systemOutListenerThread;

    public void boot(String[] args, Stage mainstage){
        //Setup engine classes
        fileUtils = new FileUtils(this);
        loadProperties();
        consoleCommandHandler = new ConsoleCommandHandler(this);
        aiEngine = new AiEngine(this);
        discEngine = new DiscApplicationEngine(this);
        teleApplicationEngine = new TeleApplicationEngine(this);
        viewEngine = new ViewEngine(this, mainstage);
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
            viewLoaded = true;
            systemOutListenerThread.start();
        } else if (args0.equalsIgnoreCase("view=false")){
            System.out.println("View will not load!");
            viewEngine = null;
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
        discEngine.getFilesHandler().saveAllBotFiles();
        getAiEngine().saveAiMind();
    }

    public void loadAllFiles(){
        loadProperties();
        discEngine.getFilesHandler().loadAllBotFiles();
        getAiEngine().loadAiMind();
    }

    private void loadAllFiles(boolean loadProperties){
        if(loadProperties)loadProperties();
        discEngine.getFilesHandler().loadAllBotFiles();
        getAiEngine().loadAiMind();
    }

    private void safeProperties(){
        try {
            fileUtils.saveOject(fileUtils.getHome() + "/properties.prop", properties);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR IN SAVE OWO");
        }
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

    public DiscApplicationEngine getDiscEngine() {
        return discEngine;
    }

    public ViewEngine getViewEngine() {
        return viewEngine;
    }

    public UtilityBase getUtilityBase() {
        return utilityBase;
    }

    public AiEngine getAiEngine() {
        return aiEngine;
    }

    public TeleApplicationEngine getTeleApplicationEngine() {
        return teleApplicationEngine;
    }

    public void renewProperties(){
        properties = new Properties();
    }

    public boolean isViewLoaded() {
        return viewLoaded;
    }
}
