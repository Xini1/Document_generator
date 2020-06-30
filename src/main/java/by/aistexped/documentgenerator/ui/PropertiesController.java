package by.aistexped.documentgenerator.ui;

import by.aistexped.documentgenerator.Logger;
import by.aistexped.documentgenerator.exceptions.PropertiesNotFound;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import by.aistexped.documentgenerator.iohandlers.PropertiesHandler;
import by.aistexped.documentgenerator.iohandlers.Property;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
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

    @FXML
    private TextField templateCustomerLabelField;

    @FXML
    private TextField customerListField;

    @FXML
    private TextField defaultSaveDirectoryField;

    private List<TextField> propertiesFields;
    private PropertiesHandler propertiesHandler;
    private Map<Property, String> properties;

    @FXML
    public void initialize() {
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
        propertiesFields.add(templateCustomerLabelField);
        propertiesFields.add(customerListField);
        propertiesFields.add(defaultSaveDirectoryField);

        try {
            propertiesHandler = new PropertiesHandler();
        } catch (PropertiesNotFound | IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
            return;
        }

        properties = propertiesHandler.getProperties();

        fillInFields();
    }

    @FXML
    public void selectDefaultSaveDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));

        File defaultDirectory = directoryChooser.showDialog(defaultSaveDirectoryField.getScene().getWindow());
        defaultSaveDirectoryField.setText(defaultDirectory.toString());
    }

    @FXML
    public void saveChanges() {
        fetchDataFromFields();

        try {
            propertiesHandler.saveToFile();
        } catch (IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
        }

        contractPathField.getScene().getWindow().hide();
    }

    @FXML
    public void cancel() {
        contractPathField.getScene().getWindow().hide();
    }

    private void fillInFields() {
        propertiesFields.forEach(textField -> {
            Property property = Property.valueOf(textField.getPromptText());
            textField.setText(properties.get(property));
        });
    }

    private void fetchDataFromFields() {
        propertiesFields.forEach(textField -> {
            Property property = Property.valueOf(textField.getPromptText());
            properties.put(property, textField.getText());
        });
    }

    private void showErrorAlertForException(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.CLOSE);
        alert.showAndWait();
    }
}
