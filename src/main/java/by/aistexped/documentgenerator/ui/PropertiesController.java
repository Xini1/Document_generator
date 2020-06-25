package by.aistexped.documentgenerator.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import by.aistexped.documentgenerator.Logger;
import by.aistexped.documentgenerator.PropertiesHandler;
import by.aistexped.documentgenerator.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PropertiesController {

    @FXML
    private TextField contractPathField;

    @FXML
    private TextField contractNumberPrefixField;

    @FXML
    private TextField contractNumberPostfixField;

    @FXML
    private TextField nextContractNumberField;

    @FXML
    private TextField contractTitleTemplateField;

    @FXML
    private TextField orderPathField;

    @FXML
    private TextField orderNumberPrefixField;

    @FXML
    private TextField orderNumberPostfixField;

    @FXML
    private TextField orderContractNumberField;

    @FXML
    private TextField orderTitleTemplateField;

    @FXML
    private TextField templateNumberLabelField;

    private List<TextField> propertiesFields;
    private PropertiesHandler propertiesHandler;
    private Map<Property, String> properties;

    private Logger logger = Logger.getInstance();

    @FXML
    public void initialize() {
        logger.logMethodInvocation(getClass(), "initialize");

        propertiesFields = new ArrayList<>();
        propertiesFields.add(contractPathField);
        propertiesFields.add(contractNumberPrefixField);
        propertiesFields.add(contractNumberPostfixField);
        propertiesFields.add(nextContractNumberField);
        propertiesFields.add(contractTitleTemplateField);
        propertiesFields.add(orderPathField);
        propertiesFields.add(orderNumberPrefixField);
        propertiesFields.add(orderNumberPostfixField);
        propertiesFields.add(orderContractNumberField);
        propertiesFields.add(orderTitleTemplateField);
        propertiesFields.add(templateNumberLabelField);

        propertiesHandler = new PropertiesHandler();
        properties = propertiesHandler.getProperties();

        fillInFields();
    }

    @FXML
    public void saveChanges() {
        logger.logMethodInvocation(getClass(), "saveChanges");

        fetchDataFromFields();

        propertiesHandler.saveToFile();

        contractPathField.getScene().getWindow().hide();
    }

    @FXML
    public void cancel() {
        logger.logMethodInvocation(getClass(), "cancel");

        contractPathField.getScene().getWindow().hide();
    }

    private void fillInFields() {
        logger.logMethodInvocation(getClass(), "fillInFields");

        propertiesFields.forEach(textField -> {
            Property property = Property.valueOf(textField.getPromptText());
            textField.setText(properties.get(property));
        });
    }

    private void fetchDataFromFields() {
        logger.logMethodInvocation(getClass(), "fetchDataFromFields");

        propertiesFields.forEach(textField -> {
            Property property = Property.valueOf(textField.getPromptText());
            properties.put(property, textField.getText());
        });
    }
}
