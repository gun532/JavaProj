//package BL;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.logging.*;
//
//public class GlobalLogger {
//    static private FileHandler fileTxt;
//    static private MyFormatter formatterTxt;
//
//    static public void setup() {
//
//        try {
//            // get the global logger to configure it
//            Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
//
//            // suppress the logging output to the console
//            Logger rootLogger = Logger.getLogger("");
//            Handler[] handlers = rootLogger.getHandlers();
//            if (handlers[0] instanceof ConsoleHandler) {
//                rootLogger.removeHandler(handlers[0]);
//            }
//
//            logger.setLevel(Level.INFO);
//            fileTxt = new FileHandler("logger.log", true);
//            // create a TXT formatter
//            SimpleFormatter simpleFormatter = new SimpleFormatter();
//            //formatterTxt = new MyFormatter();
//            //fileTxt.setFormatter(formatterTxt);
//            fileTxt.setFormatter(simpleFormatter);
//            logger.addHandler(fileTxt);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}

package BL;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GlobalLogger {
    public Logger logger;
    FileHandler fileHandler;

    public GlobalLogger(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            fileHandler = new FileHandler(fileName,true);
            logger = Logger.getLogger("Project");
            logger.addHandler(fileHandler);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
