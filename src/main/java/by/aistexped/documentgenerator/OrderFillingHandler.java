package by.aistexped.documentgenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class OrderFillingHandler {

    private List<String> labelsToSearchFor;

    public OrderFillingHandler() {
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
        labelsToSearchFor.add("/--VEHICLE_TYPE--/");
        labelsToSearchFor.add("/--PRICE--/");
        labelsToSearchFor.add("/--PAYMENT_TERM--/");
    }

    private Map<String, String> fetchRouteCargoPriceLabels(Replacements replacements) {
        Map<String, String> labelsAndValuesToSave = new HashMap<>();

        for (String label : labelsToSearchFor) {
            String value = replacements.get(label);

            labelsAndValuesToSave.put(label, value);
        }

        return labelsAndValuesToSave;
    }

    public void save(String fileName, Replacements replacements) {
        Map<String, String> labelsAndValuesToSave = fetchRouteCargoPriceLabels(replacements);
        File destination = new File("filling_templates/" + fileName + ".filling");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(destination), StandardCharsets.UTF_8))) {

            String labelsAndValuesToSaveString = labelsAndValuesToSave.entrySet().stream()
                    .map(entry -> entry.getKey() + '=' + entry.getValue() + ';')
                    .collect(Collectors.joining("\n"));

            writer.write(labelsAndValuesToSaveString);
        } catch (IOException e) {
            Logger.getInstance().logException(e);
        }
    }

    public Map<String, String> load(File file) {
        Map<String, String> replacements = new HashMap<>();

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String labelAndValue = scanner.nextLine().replace(";", "");
                String[] parts = labelAndValue.split("=");

                String label = parts[0];
                String value = parts.length < 2 ? "" : parts[1];

                replacements.put(label, value);
            }
        } catch (IOException e) {
            Logger.getInstance().logException(e);
        }

        return replacements;
    }
}
