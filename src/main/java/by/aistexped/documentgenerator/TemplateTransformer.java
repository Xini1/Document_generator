package by.aistexped.documentgenerator;

import java.util.Map;

public class TemplateTransformer {

    private Map<Property, String> properties;
    private Property numberProperty;

    private String template;
    private String customer;
    private String numberLabel;
    private String customerLabel;

    public void setProperties(Map<Property, String> properties) {
        this.properties = properties;
    }

    public void setNumberProperty(Property numberProperty) {
        this.numberProperty = numberProperty;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setNumberLabel(String numberLabel) {
        this.numberLabel = numberLabel;
    }

    public void setCustomerLabel(String customerLabel) {
        this.customerLabel = customerLabel;
    }

    public String getNextFileName() {
        int number = Integer.parseInt(properties.get(numberProperty));
        String name = template.replace(numberLabel, String.valueOf(number));
        name = name.replace(customerLabel, customer);
        name += ".docx";
        number++;
        properties.put(numberProperty, String.valueOf(number));

        return name;
    }

    public static class Builder {
        private Map<Property, String> properties;
        private Property numberProperty;

        private String template;
        private String customer;
        private String numberLabel;
        private String customerLabel;

        public Builder setProperties(Map<Property, String> properties) {
            this.properties = properties;
            return this;
        }

        public Builder setNumberProperty(Property numberProperty) {
            this.numberProperty = numberProperty;
            return this;
        }

        public Builder setTemplate(String template) {
            this.template = template;
            return this;
        }

        public Builder setCustomer(String customer) {
            this.customer = customer;
            return this;
        }

        public Builder setNumberLabel(String numberLabel) {
            this.numberLabel = numberLabel;
            return this;
        }

        public Builder setCustomerLabel(String customerLabel) {
            this.customerLabel = customerLabel;
            return this;
        }

        public TemplateTransformer build() {
            TemplateTransformer templateTransformer = new TemplateTransformer();
            templateTransformer.setProperties(properties);
            templateTransformer.setNumberProperty(numberProperty);
            templateTransformer.setTemplate(template);
            templateTransformer.setCustomer(customer);
            templateTransformer.setNumberLabel(numberLabel);
            templateTransformer.setCustomerLabel(customerLabel);

            return templateTransformer;
        }
    }
}
