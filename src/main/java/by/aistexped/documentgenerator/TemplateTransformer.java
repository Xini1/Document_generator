package by.aistexped.documentgenerator;

import java.util.Map;

public class TemplateTransformer {

    private Map<Property, String> properties;
    private Property numberProperty;

    private String template;
    private String numberLabel;

    private Logger logger = Logger.getInstance();

    public TemplateTransformer() {
        logger.logConstructorInvocation(getClass());
    }

    public void setProperties(Map<Property, String> properties) {
        logger.logMethodInvocation(getClass(), "setProperties", properties.toString());

        this.properties = properties;
    }

    public void setNumberProperty(Property numberProperty) {
        logger.logMethodInvocation(getClass(), "setNumberProperty", numberProperty.toString());

        this.numberProperty = numberProperty;
    }

    public void setTemplate(String template) {
        logger.logMethodInvocation(getClass(), "setTemplate", template);

        this.template = template;
    }

    public void setNumberLabel(String numberLabel) {
        logger.logMethodInvocation(getClass(), "setNumberLabel", numberLabel);

        this.numberLabel = numberLabel;
    }

    public String getNextFileName() {
        logger.logMethodInvocation(getClass(), "getNextFileName");

        int number = Integer.parseInt(properties.get(numberProperty));
        String name = template.replace(numberLabel, String.valueOf(number)) + ".docx";
        number++;
        properties.put(numberProperty, String.valueOf(number));

        logger.logReturnValue(name);
        return name;
    }

    @Override
    public String toString() {
        return "by.aistexped.documentgenerator.TemplateTransformer{" +
                "properties=" + properties +
                ", numberProperty=" + numberProperty +
                ", template='" + template + '\'' +
                ", numberLabel='" + numberLabel + '\'' +
                '}';
    }

    public static class Builder {
        private Map<Property, String> properties;
        private Property numberProperty;

        private String template;
        private String numberLabel;

        private Logger logger = Logger.getInstance();

        public Builder() {
            logger.logConstructorInvocation(getClass());
        }

        public Builder setProperties(Map<Property, String> properties) {
            logger.logMethodInvocation(getClass(), "setProperties", properties.toString());

            this.properties = properties;
            return this;
        }

        public Builder setNumberProperty(Property numberProperty) {
            logger.logMethodInvocation(getClass(), "setNumberProperty", numberProperty.toString());

            this.numberProperty = numberProperty;
            return this;
        }

        public Builder setTemplate(String template) {
            logger.logMethodInvocation(getClass(), "setTemplate", template);

            this.template = template;
            return this;
        }

        public Builder setNumberLabel(String numberLabel) {
            logger.logMethodInvocation(getClass(), "setNumberLabel", numberLabel);

            this.numberLabel = numberLabel;
            return this;
        }

        public TemplateTransformer build() {
            logger.logMethodInvocation(getClass(), "build");

            TemplateTransformer templateTransformer = new TemplateTransformer();
            templateTransformer.setProperties(properties);
            templateTransformer.setNumberProperty(numberProperty);
            templateTransformer.setTemplate(template);
            templateTransformer.setNumberLabel(numberLabel);

            logger.logReturnValue(templateTransformer);
            return templateTransformer;
        }
    }
}
