package by.aistexped.documentgenerator;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DocxIOHandler {

    private String path;
    private XWPFDocument document;

    private Logger logger = Logger.getInstance();

    public DocxIOHandler(String path) {
        logger.logConstructorInvocation(getClass(), path);

        this.path = path;
        load();
    }

    public XWPFDocument getDocument() {
        logger.logMethodInvocation(getClass(), "getDocument");

        logger.logReturnValue(document.getClass().getSimpleName());
        return document;
    }

    public void load() {
        logger.logMethodInvocation(getClass(), "load");

        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            document = new XWPFDocument(fileInputStream);
        } catch (IOException e) {
            logger.logException(e);
        }
    }

    public void save(String nameTemplate) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(nameTemplate)) {
            document.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
