package by.aistexped.documentgenerator;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DocxIOHandler {

    private String path;
    private XWPFDocument document;

    public DocxIOHandler(String path) {
        this.path = path;
        load();
    }

    public XWPFDocument getDocument() {
        return document;
    }

    public void load() {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            document = new XWPFDocument(fileInputStream);
        } catch (IOException e) {
            Logger.getInstance().logException(e);
        }
    }

    public void save(String nameTemplate) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(nameTemplate)) {
            document.write(fileOutputStream);
        } catch (IOException e) {
            Logger.getInstance().logException(e);
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

        public DocxIOHandler build() {
            String path = pathWithCustomerLabel.replace(customerLabel, customer);
            return new DocxIOHandler(path);
        }
    }
}
