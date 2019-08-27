package Threads;

import Engines.Engine;
import javafx.application.Platform;

import java.io.*;

public class SystemOutListenerThread implements Runnable {

    Engine engine;

    public SystemOutListenerThread(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void run() {
        String line = null;

        PipedOutputStream pOut = new PipedOutputStream();
        System.setOut(new PrintStream(pOut));
        PipedInputStream pIn = null;
        try {
            pIn = new PipedInputStream(pOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(pIn));
        while (true) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String finalLine = line;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    engine.getViewEngine().printConsollogOnGui(finalLine);
                }
            });
        }
    }
}
