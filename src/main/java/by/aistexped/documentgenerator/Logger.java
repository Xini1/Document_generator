package by.aistexped.documentgenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Logger {

    private static Logger logger;

    public static final String LOGS_FILE_PATH = "logs.txt";
    public static final String LOGS_DELIMITER = "\n----------------------------------------------------------------\n";

    private Logger() {
    }

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public void logException(Exception exception) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOGS_FILE_PATH, true))) {
            String situation = getTimestamp() + " exception occurred " + exception + ": stack trace:\n";
            situation += Arrays.stream(exception.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n"));
            situation += LOGS_DELIMITER;
            writer.write(situation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy; hh:mm:ss");
        return LocalDateTime.now().format(formatter) + ": ";
    }
}
