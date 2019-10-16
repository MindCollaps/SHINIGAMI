package threads;

import engines.Engine;

import java.io.*;
import java.util.ArrayList;

public class SystemOutListenerThread implements Runnable {

    Engine engine;
    ArrayList<Thread> threads = new ArrayList<>();

    public SystemOutListenerThread(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void run() {
        new Thread(new ThreadLoader()).start();
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

            threads.add(new Thread(new Runnable() {
                String print = finalLine;
                @Override
                public void run() {
                    threads.remove(this);
                    try {
                        Thread.sleep(10 * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    engine.getViewEngine().printConsollogOnGui(print);
                }
            }));
        }
    }

    private class ThreadLoader implements Runnable{

        @Override
        public void run() {
            int rise = 0;
            int size = 0;
            boolean done = true;
            while (true){
                try {
                    Thread.sleep(10*10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(done)size=threads.size();
                if(size>0){
                    try {
                        threads.get(rise).start();
                    } catch (Exception ignored){
                    }
                    rise++;
                    size--;
                    done = false;
                } else {
                    rise = 0;
                    done = true;
                }
            }
        }
    }
}

