package utils;

import engines.Engine;

import java.io.*;

public class FileUtils {

    Engine engine;

    public FileUtils(Engine engine) {
        this.engine = engine;
    }

    public String home = System.getProperty("user.dir");

    public String getHome() {
        return home + "/botmanagerhome";
    }

    public Object loadObject(String path) throws Exception {
        String filePath = new File(path).getAbsolutePath();
        File file = new File(filePath);
        System.out.println("\n[File loader] Loading Object Flile: " + filePath);
        FileInputStream stream = null;
        ObjectInputStream objStream = null;
        Object obj = null;

        if (!file.exists()) {
            System.out.println("The File was never created!");
            throw new Exception("File was never created!");
        }


        stream = new FileInputStream(file);


        objStream = new ObjectInputStream(stream);

        obj = objStream.readObject();

        objStream.close();

        stream.close();


        return obj;
    }

    public void saveOject(String path, Object obj) throws Exception {
        String filePath = new File(path).getAbsolutePath();
        File file = new File(filePath);
        System.out.println("\n[File loader] Save Object File: " + filePath);
        FileOutputStream stream = null;
        ObjectOutputStream objStream = null;

        createFileRootAndFile(file);


        stream = new FileOutputStream(file);


        objStream = new ObjectOutputStream(stream);

        objStream.writeObject(obj);
        objStream.flush();

        objStream.close();

        stream.close();

    }

    public String loadReadebleTxtFile(String path) {
        String filePath = new File(path).getAbsolutePath();
        engine.getUtilityBase().printDebug("\n[File loader] Read Object File: \" + filePath");
        File file = new File(filePath);
        createFileRootAndFile(file);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        FileReader reader = null;
        BufferedReader bufferReader = null;
        String txt = "";
        String add = "";

        try {
            reader = new FileReader(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bufferReader = new BufferedReader(reader);
        try {
            for (int i = 0; bufferReader.read() >= i; i++) {
                add = bufferReader.readLine();
                txt += add;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (txt.isEmpty()) {
            engine.getUtilityBase().printDebug("\n[File Loader] readed empty file");
        }
        try {
            bufferReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return txt;
    }

    public void saveReadbleFile(String txt, String path) {
        String filePath = new File(path).getAbsolutePath();
        engine.getUtilityBase().printDebug("\n[File loader] Save Readable Flile: " + filePath);
        File file = new File(filePath);
        FileWriter fw = null;

        createFileRootAndFile(file);
        try {
            file.createNewFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        createFileRootAndFile(file);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            fw = new FileWriter(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            fw.write(txt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createFileRootAndFile(File file) {
        String pas = file.getAbsolutePath().replace("\\", "/");
        String[] path = pas.split("/");
        String pat = path[0];
        for (int i = 1; i < path.length - 1; i++) {
            pat = pat + "/" + path[i];
        }
        File dir = new File(pat);
        if (!dir.mkdirs()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                engine.getUtilityBase().printDebug("[file utils create File Dirs] cant create file dirs");
            }
        }
        createFiles(file);
    }

    private void createFiles(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                engine.getUtilityBase().printDebug("[file utils create File Dirs] cant create file dirs");
            }
        }
    }
}
