package by.aistexped.documentgenerator;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class OrderFillingHandler {

    private List<String> labelsToSearchFor;

    private Logger logger = Logger.getInstance();

    public OrderFillingHandler() {
        logger.logConstructorInvocation(getClass());

        labelsToSearchFor = new ArrayList<>();
        labelsToSearchFor.add("/--SENDER--/");
        labelsToSearchFor.add("/--RECIPIENT--/");
        labelsToSearchFor.add("/--RECIPIENT_ADDRESS--/");
        labelsToSearchFor.add("/--SENDER_ADDRESS--/");
        labelsToSearchFor.add("/--RECIPIENT_CUSTOMS--/");
        labelsToSearchFor.add("/--SENDER_CUSTOMS--/");
        labelsToSearchFor.add("/--LOAD--/");
        labelsToSearchFor.add("/--UNLOAD--/");
        labelsToSearchFor.add("/--CARGO--/");
        labelsToSearchFor.add("/--WEIGHT--/");
        labelsToSearchFor.add("/--VOLUME--/");
        labelsToSearchFor.add("/--LOAD_DATE--/");
        labelsToSearchFor.add("/--UNLOAD_DATE--/");
        labelsToSearchFor.add("/--VEHICLE_TYPE--/");
        labelsToSearchFor.add("/--PRICE--/");
        labelsToSearchFor.add("/--PAYMENT_TERM--/");
    }

    private Map<String, String> fetchRouteCargoPriceLabels(Replacements replacements) {
        logger.logMethodInvocation(getClass(), "fetchRouteCargoPriceLabels", replacements.toString());

        Map<String, String> labelsAndValuesToSave = new HashMap<>();

        for (String label : labelsToSearchFor) {
            String value = replacements.get(label);

            labelsAndValuesToSave.put(label, value);
        }

        logger.logReturnValue(labelsAndValuesToSave);
        return labelsAndValuesToSave;
    }

    public void save(String fileName, Replacements replacements) {
        logger.logMethodInvocation(getClass(), "save", fileName, replacements.toString());

        Map<String, String> labelsAndValuesToSave = fetchRouteCargoPriceLabels(replacements);


        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("filling_templates/" + fileName + ".txt"))) {
            String labelsAndValuesToSaveString = labelsAndValuesToSave.entrySet().stream()
                    .map(entry -> entry.getKey() + '=' + entry.getValue() + ';')
                    .collect(Collectors.joining("\n"));

            writer.write(labelsAndValuesToSaveString);
        } catch (IOException e) {
            logger.logException(e);
        }
    }

    public Map<String, String> load(File file) {
        logger.logMethodInvocation(getClass(), "load");

        Map<String, String> replacements = new HashMap<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String labelAndValue = scanner.nextLine().replace(";", "");
                String[] parts = labelAndValue.split("=");

                String label = parts[0];
                String value = parts.length < 2 ? "" : parts[1];

                replacements.put(label, value);
            }
        } catch (FileNotFoundException e) {
            logger.logException(e);
        }

        logger.logReturnValue(replacements);
        return replacements;
    }
}
