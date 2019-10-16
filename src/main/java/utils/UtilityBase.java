package utils;

import BotAiCore.librarys.AiCommand;
import engines.Engine;

import java.awt.*;

public class UtilityBase {

     Engine engine;

    public UtilityBase(Engine engine) {
        this.engine = engine;
    }

    public Color convertStringToColor(String scolor) throws Exception{
        switch (scolor) {
            case "black":
                return Color.BLACK;
            case "blue":
                return Color.BLUE;
            case "cyan":
                return Color.CYAN;
            case "dark_gray":
                return Color.DARK_GRAY;
            case "gray":
                return Color.GRAY;
            case "green":
                return Color.GREEN;
            case "light_gray":
                return Color.LIGHT_GRAY;
            case "magenta":
                return Color.MAGENTA;
            case "orange":
                return Color.ORANGE;
            case "pink":
                return Color.PINK;
            case "red":
                return Color.RED;
            case "white":
                return Color.WHITE;
            case "yellow":
                return Color.YELLOW;
        }
        throw new Exception("Color doesn't exist");
    }

    public AiCommand.commandType convertStringToCommandType(String s) throws Exception{
        switch (s.toLowerCase()){
            case "discord":
                return AiCommand.commandType.DISCORD;

            case "telegram":
                return AiCommand.commandType.TELEGRAM;

            case "all":
                return AiCommand.commandType.ALL;

        }
        throw new Exception("Color doesn't exist");
    }

    public void printDebug(String message){
        if(engine.getProperties().isDebug()){
            System.out.println(message);
        }
    }
}
