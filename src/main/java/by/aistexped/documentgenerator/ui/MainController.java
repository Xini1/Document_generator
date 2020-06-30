package by.aistexped.documentgenerator.ui;

import by.aistexped.documentgenerator.exceptions.PropertiesNotFound;
import by.aistexped.documentgenerator.iohandlers.DocxIOHandler;
import by.aistexped.documentgenerator.iohandlers.OrderFillingHandler;
import by.aistexped.documentgenerator.iohandlers.PropertiesHandler;
import by.aistexped.documentgenerator.iohandlers.Property;
import by.aistexped.documentgenerator.transformers.Replacements;
import by.aistexped.documentgenerator.transformers.Replacer;
import by.aistexped.documentgenerator.transformers.TemplateTransformer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import by.aistexped.documentgenerator.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainController {

    @FXML
    private VBox contractTabVBox;

    @FXML
    private GridPane existingContractGridPane;

    @FXML
    private GridPane newContractGridPane;

    @FXML
    private TextField carrierField;

    @FXML
    private TextField carrierVatinField;

    @FXML
    private ComboBox<String> contractOptionsComboBox;

    @FXML
    private TextField contractNumberField;

    @FXML
    private DatePicker contractDatePicker;

    @FXML
    private TextField representedByField;

    @FXML
    private TextField actingOnField;

    @FXML
    private TextArea requisitesField;

    @FXML
    private TextField positionField;

    @FXML
    private TextField representativeShortField;

    @FXML
    private TextField senderField;

    @FXML
    private TextField senderAddressField;

    @FXML
    private ComboBox<String> loadTypeComboBox;

    @FXML
    private TextField senderCustomsField;

    @FXML
    private TextField recipientField;

    @FXML
    private TextField recipientAddressField;

    @FXML
    private ComboBox<String> unloadTypeComboBox;

    @FXML
    private TextField recipientCustomsField;

    @FXML
    private TextField cargoField;

    @FXML
    private TextField cargoWeightField;

    @FXML
    private TextField cargoVolumeField;

    @FXML
    private DatePicker loadDatePicker;

    @FXML
    private DatePicker unloadDatePicker;

    @FXML
    private ComboBox<String> vehicleTypeComboBox;

    @FXML
    private TextField priceField;

    @FXML
    private TextField paymentTermField;

    @FXML
    private TextArea vehicleInfoField;

    @FXML
    private CheckBox saveOrderFillingCheckbox;

    @FXML
    private TextField orderFillingFileNameField;

    @FXML
    private Label orderFillingFileNameLabel;

    @FXML
    private ComboBox<String> customerOptionsComboBox;

    private List<TextInputControl> textFieldsForDefaultInterpretation;
    private List<DatePicker> datePickersForDefaultInterpretation;
    private List<ComboBox<String>> comboBoxesForDefaultInterpretation;

    @FXML
    public void initialize() {
        textFieldsForDefaultInterpretation = new ArrayList<>();
        textFieldsForDefaultInterpretation.add(carrierField);
        textFieldsForDefaultInterpretation.add(carrierVatinField);
        textFieldsForDefaultInterpretation.add(representedByField);
        textFieldsForDefaultInterpretation.add(actingOnField);
        textFieldsForDefaultInterpretation.add(requisitesField);
        textFieldsForDefaultInterpretation.add(positionField);
        textFieldsForDefaultInterpretation.add(representativeShortField);
        textFieldsForDefaultInterpretation.add(representativeShortField);
        textFieldsForDefaultInterpretation.add(senderField);
        textFieldsForDefaultInterpretation.add(senderAddressField);
        textFieldsForDefaultInterpretation.add(senderCustomsField);
        textFieldsForDefaultInterpretation.add(recipientField);
        textFieldsForDefaultInterpretation.add(recipientAddressField);
        textFieldsForDefaultInterpretation.add(recipientCustomsField);
        textFieldsForDefaultInterpretation.add(cargoField);
        textFieldsForDefaultInterpretation.add(cargoWeightField);
        textFieldsForDefaultInterpretation.add(cargoVolumeField);
        textFieldsForDefaultInterpretation.add(priceField);
        textFieldsForDefaultInterpretation.add(paymentTermField);
        textFieldsForDefaultInterpretation.add(vehicleInfoField);

        datePickersForDefaultInterpretation = new ArrayList<>();
        datePickersForDefaultInterpretation.add(loadDatePicker);
        datePickersForDefaultInterpretation.add(unloadDatePicker);

        comboBoxesForDefaultInterpretation = new ArrayList<>();
        comboBoxesForDefaultInterpretation.add(loadTypeComboBox);
        comboBoxesForDefaultInterpretation.add(unloadTypeComboBox);
        comboBoxesForDefaultInterpretation.add(vehicleTypeComboBox);

        List<String> loadUnloadTypes = Arrays.asList("задняя", "боковая", "верхняя");
        loadTypeComboBox.getItems().addAll(loadUnloadTypes);
        unloadTypeComboBox.getItems().addAll(loadUnloadTypes);

        loadTypeComboBox.setValue(loadUnloadTypes.get(0));
        unloadTypeComboBox.setValue(loadUnloadTypes.get(0));

        List<String> vehicleTypes = Arrays.asList("тент", "рефрижератор");
        vehicleTypeComboBox.getItems().addAll(vehicleTypes);
        vehicleTypeComboBox.setValue(vehicleTypes.get(0));

        List<String> contractOptions = Arrays.asList("указать существующий", "создать новый");
        contractOptionsComboBox.getItems().addAll(contractOptions);
        contractOptionsComboBox.setValue(contractOptions.get(0));

        changeContractTabView();
        updateCustomerOptionsComboBox();
        changeOrderFillingFileNameFieldAndLabelVisibility();
    }

    @FXML
    public void generate() {
        PropertiesHandler propertiesHandler;
        try {
            propertiesHandler = new PropertiesHandler();
        } catch (PropertiesNotFound | IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
            return;
        }

        Map<Property, String> properties = propertiesHandler.getProperties();

        Replacements replacements = propertiesHandler.getBasicReplacements();
        fetchData(replacements, properties);

        DirectoryChooser directoryChooser = new DirectoryChooser();
        String defaultSaveDirectory = properties.get(Property.DEFAULT_SAVE_DIRECTORY);

        if (defaultSaveDirectory.isEmpty()) {
            defaultSaveDirectory = ".";
        }

        directoryChooser.setInitialDirectory(new File(defaultSaveDirectory));
        directoryChooser.setTitle("Сохранить");

        File saveDirectory = directoryChooser.showDialog(orderFillingFileNameField.getScene().getWindow());

        if (saveDirectory == null) {
            return;
        }

        createNewOrder(replacements, properties, saveDirectory);

        if (contractOptionsComboBox.getSelectionModel().getSelectedIndex() == 1) {
            createNewContract(replacements, properties, saveDirectory);
        }

        if (saveOrderFillingCheckbox.isSelected()) {
            saveOrderFilling(orderFillingFileNameField.getText(), replacements);
        }

        try {
            propertiesHandler.saveToFile();
        } catch (IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
        }
    }

    @FXML
    public void changeContractTabView() {
        ObservableList<Node> content = contractTabVBox.getChildren();

        switch (contractOptionsComboBox.getSelectionModel().getSelectedIndex()) {
            case 0:
                existingContractGridPane.setVisible(true);
                newContractGridPane.setVisible(false);
                content.remove(existingContractGridPane);
                content.add(1, existingContractGridPane);
                break;
            case 1:
                existingContractGridPane.setVisible(false);
                newContractGridPane.setVisible(true);
                content.remove(newContractGridPane);
                content.add(1, newContractGridPane);
                break;
        }
    }

    @FXML
    public void loadOrderFilling() {
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File("filling_templates");

        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setTitle("Выберите файл с шаблоном наполнения");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Order filling", "*.filling"));

        File file = fileChooser.showOpenDialog(orderFillingFileNameField.getScene().getWindow());

        if (file == null) {
            return;
        }

        OrderFillingHandler orderFillingHandler = new OrderFillingHandler();
        Map<String, String> loadedLabelsWithValues;
        try {
            loadedLabelsWithValues = orderFillingHandler.load(file);
        } catch (IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
            return;
        }

        textFieldsForDefaultInterpretation.forEach(textInputControl -> {
            String value = loadedLabelsWithValues.get(textInputControl.getPromptText());

            if (value != null) {
                textInputControl.setText(value);
            }
        });

        comboBoxesForDefaultInterpretation.forEach(comboBox -> {
            String value = loadedLabelsWithValues.get(comboBox.getPromptText());

            if (value != null) {
                comboBox.setValue(value);
            }
        });

        orderFillingFileNameField.setText(file.getName().replace(".filling", ""));
    }

    @FXML
    public void changeOrderFillingFileNameFieldAndLabelVisibility() {
        orderFillingFileNameField.setVisible(!orderFillingFileNameField.isVisible());
        orderFillingFileNameLabel.setVisible(!orderFillingFileNameLabel.isVisible());
    }

    @FXML
    public void openPropertiesWindow() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/properties.fxml"));
        } catch (IOException e) {
            Logger.getInstance().logException(e);
            return;
        }

        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.initOwner(contractTabVBox.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setTitle("Настройки");

        stage.showAndWait();

        updateCustomerOptionsComboBox();
    }

    private void fetchData(Replacements replacements, Map<Property, String> properties) {
        textFieldsForDefaultInterpretation.forEach(textInputControl ->
                replacements.put(textInputControl.getPromptText(), textInputControl.getText()));

        datePickersForDefaultInterpretation.forEach(datePicker ->
                replacements.put(datePicker.getPromptText(), datePicker.getEditor().getText()));

        comboBoxesForDefaultInterpretation.forEach(stringComboBox ->
                replacements.put(stringComboBox.getPromptText(), stringComboBox.getSelectionModel().getSelectedItem()));

        String contractNumber = "";
        String contractNumberPrefix = "";
        String contractNumberPostfix = "";
        String contractDate = "";

        switch (contractOptionsComboBox.getSelectionModel().getSelectedIndex()) {
            case 0:
                contractNumber = contractNumberField.getText();
                contractDate = contractDatePicker.getEditor().getText();
                break;
            case 1:
                contractNumber = properties.get(Property.NEXT_CONTRACT_NUMBER);
                contractNumberPrefix = properties.get(Property.CONTRACT_NUMBER_PREFIX);
                contractNumberPostfix = properties.get(Property.CONTRACT_NUMBER_POSTFIX);
                contractDate = replacements.get("/--DATE_SHORT--/");
                break;
        }

        replacements.put("/--CONTRACT_NUMBER--/", contractNumber);
        replacements.put("/--CONTRACT_NUMBER_POSTFIX--/", contractNumberPostfix);
        replacements.put("/--CONTRACT_NUMBER_PREFIX--/", contractNumberPrefix);
        replacements.put("/--CONTRACT_DATE--/", contractDate);
    }

    private void createNewOrder(Replacements replacements, Map<Property, String> properties, File saveDirectory) {
        String customer = customerOptionsComboBox.getSelectionModel().getSelectedItem();

        DocxIOHandler orderDocxIOHandler;
        try {
            orderDocxIOHandler = new DocxIOHandler.Builder()
                    .setPathWithCustomerLabel(properties.get(Property.ORDER_PATH))
                    .setCustomerLabel(properties.get(Property.TEMPLATE_CUSTOMER_LABEL))
                    .setCustomer(customer)
                    .build();
        } catch (IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
            return;
        }

        TemplateTransformer orderTemplateTransformer = new TemplateTransformer.Builder()
                .setProperties(properties)
                .setNumberProperty(Property.NEXT_ORDER_NUMBER)
                .setTemplate(properties.get(Property.ORDER_TITLE_TEMPLATE))
                .setDirectory(saveDirectory.toString())
                .setCustomer(customer)
                .setNumberLabel(properties.get(Property.TEMPLATE_NUMBER_LABEL))
                .setCustomerLabel(properties.get(Property.TEMPLATE_CUSTOMER_LABEL))
                .build();

        Replacer orderReplacer = new Replacer(orderDocxIOHandler.getDocument(), properties);

        orderReplacer.replace(replacements);

        try {
            orderDocxIOHandler.save(orderTemplateTransformer.getNextFileName());
        } catch (IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
        }
    }

    private void createNewContract(Replacements replacements, Map<Property, String> properties, File saveDirectory) {
        String customer = customerOptionsComboBox.getSelectionModel().getSelectedItem();

        DocxIOHandler contractDocxIOHandler;
        try {
            contractDocxIOHandler = new DocxIOHandler.Builder()
                    .setPathWithCustomerLabel(properties.get(Property.CONTRACT_PATH))
                    .setCustomerLabel(properties.get(Property.TEMPLATE_CUSTOMER_LABEL))
                    .setCustomer(customerOptionsComboBox.getSelectionModel().getSelectedItem())
                    .build();
        } catch (IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
            return;
        }

        TemplateTransformer contractTemplateTransformer = new TemplateTransformer.Builder()
                .setProperties(properties)
                .setNumberProperty(Property.NEXT_CONTRACT_NUMBER)
                .setDirectory(saveDirectory.toString())
                .setTemplate(properties.get(Property.CONTRACT_TITLE_TEMPLATE))
                .setCustomer(customer)
                .setNumberLabel(properties.get(Property.TEMPLATE_NUMBER_LABEL))
                .setCustomerLabel(properties.get(Property.TEMPLATE_CUSTOMER_LABEL))
                .build();

        Replacer contractReplacer = new Replacer(contractDocxIOHandler.getDocument(), properties);
        contractReplacer.replace(replacements);

        try {
            contractDocxIOHandler.save(contractTemplateTransformer.getNextFileName());
        } catch (IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
        }
    }

    private void saveOrderFilling(String fileName, Replacements replacements) {
        OrderFillingHandler orderFillingHandler = new OrderFillingHandler();
        try {
            orderFillingHandler.save(fileName, replacements);
        } catch (IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
        }
    }

    private void updateCustomerOptionsComboBox() {
        PropertiesHandler propertiesHandler;
        try {
            propertiesHandler = new PropertiesHandler();
        } catch (PropertiesNotFound | IOException e) {
            Logger.getInstance().logException(e);
            showErrorAlertForException(e);
            return;
        }
        Map<Property, String> properties = propertiesHandler.getProperties();

        String[] customerOptions = properties.get(Property.CUSTOMER_LIST).split(",");
        ObservableList<String> items = FXCollections.observableArrayList(customerOptions);

        if (items.equals(customerOptionsComboBox.getItems())) {
            return;
        }

        customerOptionsComboBox.setItems(items);
        customerOptionsComboBox.setValue(customerOptions[0]);
    }

    private void showErrorAlertForException(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.CLOSE);
        alert.showAndWait();
    }
}
