package by.aistexped.documentgenerator.iohandlers;

import by.aistexped.documentgenerator.Logger;
import by.aistexped.documentgenerator.exceptions.PropertiesNotFound;
import by.aistexped.documentgenerator.transformers.Replacements;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PropertiesHandler {

    private Map<Property, String> properties;

    public static final String PROPERTIES_FILE_PATH = "properties.txt";

    public PropertiesHandler() throws PropertiesNotFound, IOException {
        File propertiesFile = new File(PROPERTIES_FILE_PATH);

        if (!propertiesFile.exists()) {
            createEmptyPropertiesFile();
            PropertiesNotFound exception = new PropertiesNotFound("properties.txt not found.");
            Logger.getInstance().logException(exception);
            throw exception;
        }

        loadPropertiesFromFile(propertiesFile);

        if (!validate()) {
            PropertiesNotFound exception = new PropertiesNotFound("Missing some properties values.");
            Logger.getInstance().logException(exception);
            throw exception;
        }
    }

    private void loadPropertiesFromFile(File propertiesFile) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(propertiesFile)) {
            StringBuilder builder = new StringBuilder();

            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }

            String propertiesFileContent = builder.toString();
            properties = parseProperties(propertiesFileContent);
        }
    }

    public Map<Property, String> getProperties() {
        return properties;
    }

    public Replacements getBasicReplacements() {
        Replacements replacements = new Replacements();
        addDateInfo(replacements);
        addOrderInfo(replacements);

        return replacements;
    }

    private void addDateInfo(Replacements replacements) {
        LocalDate date = LocalDate.now();

        String month = "";
        switch (date.getMonth()) {
            case JANUARY:
                month = "января";
                break;
            case FEBRUARY:
                month = "февраля";
                break;
            case MARCH:
                month = "марта";
                break;
            case APRIL:
                month = "апреля";
                break;
            case MAY:
                month = "мая";
                break;
            case JUNE:
                month = "июня";
                break;
            case JULY:
                month = "июля";
                break;
            case AUGUST:
                month = "августа";
                break;
            case SEPTEMBER:
                month = "сентября";
                break;
            case OCTOBER:
                month = "октября";
                break;
            case NOVEMBER:
                month = "ноября";
                break;
            case DECEMBER:
                month = "декабря";
                break;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("«dd» " + month + " yyyy");
        String dateFull = date.format(formatter) + " года";

        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateShort = date.format(formatter);

        replacements.put("/--DATE_FULL--/", dateFull);
        replacements.put("/--DATE_SHORT--/", dateShort);
    }

    private void addOrderInfo(Replacements replacements) {
        String orderNumber = properties.get(Property.NEXT_ORDER_NUMBER);
        String orderNumberPrefix = properties.get(Property.ORDER_NUMBER_PREFIX);
        String orderNumberPostfix = properties.get(Property.ORDER_NUMBER_POSTFIX);

        replacements.put("/--ORDER_NUMBER--/", orderNumber);
        replacements.put("/--ORDER_NUMBER_POSTFIX--/", orderNumberPostfix);
        replacements.put("/--ORDER_NUMBER_PREFIX--/", orderNumberPrefix);
    }

    public void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROPERTIES_FILE_PATH))) {
            String propertiesString = properties.entrySet().stream()
                    .map(entry -> entry.getKey().toString() + '=' + entry.getValue() + ";")
                    .collect(Collectors.joining("\n"));
            writer.write(propertiesString);
        }
    }

    private Map<Property, String> parseProperties(String propertiesFileContent) {
        Map<Property, String> parsedProperties = new HashMap<>();
        String[] propertiesWithValues = propertiesFileContent.split(";");

        for (String propertyWithValue : propertiesWithValues) {
            String[] parts = propertyWithValue.split("=");

            Property property = Property.valueOf(parts[0]);
            String value = parts.length < 2 ? "" : parts[1];
            
            parsedProperties.put(property, value);
        }

        return parsedProperties;
    }

    private boolean validate() {
        for (Property property : Property.values()) {
            if (properties.get(property) == null) {
                return false;
            }
        }

        return true;
    }

    private void createEmptyPropertiesFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROPERTIES_FILE_PATH))) {
            String propertiesString = Arrays.stream(Property.values())
                    .map(property -> property.toString() + "=;")
                    .collect(Collectors.joining("\n"));
            writer.write(propertiesString);
        }
    }
}
