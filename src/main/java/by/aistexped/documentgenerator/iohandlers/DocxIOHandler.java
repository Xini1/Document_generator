package by.aistexped.documentgenerator.iohandlers;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DocxIOHandler {

    private String path;
    private XWPFDocument document;

    public DocxIOHandler(String path) throws IOException {
        this.path = path;
        load();
    }

    public XWPFDocument getDocument() {
        return document;
    }

    public void load() throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            document = new XWPFDocument(fileInputStream);
        }
    }

    public void save(String nameTemplate) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(nameTemplate)) {
            document.write(fileOutputStream);
        }
    }

    public static class Builder {

        private String pathWithCustomerLabel;
        private String customerLabel;
        private String customer;

        public Builder setPathWithCustomerLabel(String pathWithCustomerLabel) {
            this.pathWithCustomerLabel = pathWithCustomerLabel;
            return this;
        }

        public Builder setCustomerLabel(String customerLabel) {
            this.customerLabel = customerLabel;
            return this;
        }

        public Builder setCustomer(String customer) {
            this.customer = customer;
            return this;
        }

        public DocxIOHandler build() throws IOException {
            String path = pathWithCustomerLabel.replace(customerLabel, customer);
            return new DocxIOHandler(path);
        }
    }
}
