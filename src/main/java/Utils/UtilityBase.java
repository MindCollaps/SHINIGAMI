package Utils;

import Engines.Engine;

import java.awt.*;

public class UtilityBase {

     Engine engine;

    public UtilityBase(Engine engine) {
        this.engine = engine;
    }

    public Color convertStringToColor(String scolor) throws Exception{
        System.out.println("Convert " + scolor + " to Color!");
        Color color;
        switch (scolor) {
            case "black":
                color = Color.BLACK;
                break;
            case "blue":
                color = Color.BLUE;
                break;
            case "cyan":
                color = Color.CYAN;
                break;
            case "dark_gray":
                color = Color.DARK_GRAY;
                break;
            case "gray":
                color = Color.GRAY;
                break;
            case "green":
                color = Color.GREEN;
                break;
            case "light_gray":
                color = Color.LIGHT_GRAY;
                break;
            case "magenta":
                color = Color.MAGENTA;
                break;
            case "orange":
                color = Color.ORANGE;
                break;
            case "pink":
                color = Color.PINK;
                break;
            case "red":
                color = Color.RED;
                break;
            case "white":
                color = Color.WHITE;
                break;
            case "yellow":
                color = Color.YELLOW;
                break;
            default:
                throw new Exception("Color doesn't exist");
        }
        return color;
    }

    public void printDebug(String message){
        if(engine.getProperties().isDebug()){
            System.out.println(message);
        }
    }
}
