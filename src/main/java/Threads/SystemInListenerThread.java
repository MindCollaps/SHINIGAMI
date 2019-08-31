package Threads;

import Engines.Engine;

import java.util.Scanner;

public class SystemInListenerThread implements Runnable{

    Engine engine;

    public SystemInListenerThread(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void run() {
        String line;
        Scanner scanner = new Scanner(System.in);
        while (true){
            line = scanner.nextLine();
            engine.getConsoleCommandHandler().handleConsoleCommand(line);
        }
    }
}
