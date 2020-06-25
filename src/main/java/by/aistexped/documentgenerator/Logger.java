package by.aistexped.documentgenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static Logger logger;

    private StringBuilder builder;
    private DateTimeFormatter formatter;

    public static final String LOGS_FILE_PATH = "logs.txt";

    private Logger() {
        builder = new StringBuilder();
        formatter=DateTimeFormatter.ofPattern("dd.MM.yyyy; hh:mm:ss");
    }

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public void logStart() {
        appendTimestamp();
        builder.append("program started\n");
    }

    public void logExit() {
        appendTimestamp();
        builder.append("program closed\n");
        saveToFile();
    }

    public void logMethodInvocation(Class<?> clazz, String methodName, String... parameters) {
        appendTimestamp();
        builder.append("in class '").append(clazz.getName())
                .append("' invoked method '").append(methodName)
                .append("' with parameters: [");
        String parametersString = String.join(", ", parameters);
        builder.append(parametersString).append("]\n");
    }

    public void logReturnValue(String returnValue) {
        appendTimestamp();
        builder.append("method returned ").append(returnValue).append('\n');
    }

    public void logReturnValue(Object returnObject) {
        logReturnValue(returnObject.toString());
    }

    public void logReturnValue(boolean returnBoolean) {
        logReturnValue(returnBoolean ? "true" : "false");
    }

    public void logConstructorInvocation(Class<?> clazz, String... parameters) {
        appendTimestamp();
        builder.append("invoked constructor of class '").append(clazz.getName())
                .append("' with parameters: [");
        String parametersString = String.join(", ", parameters);
        builder.append(parametersString).append("]\n");
    }

    public void logException(Exception e) {
        appendTimestamp();
        builder.append("throw exception ").append(e);
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOGS_FILE_PATH, true))) {
            builder.append('\n')
                    .append("----------------------------------------------------------------------------------------")
                    .append('\n').append('\n');
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendTimestamp() {
        builder.append(LocalDateTime.now().format(formatter)).append(": ");
    }
}
