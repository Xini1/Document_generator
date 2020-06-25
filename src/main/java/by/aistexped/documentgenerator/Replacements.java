package by.aistexped.documentgenerator;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.HashMap;
import java.util.Map;

public class Replacements {

    private static class Replacement {

        private String label;
        private String data;

        public Replacement(String label) {
            this.label = label;
            data = "";
        }

        public String getLabel() {
            return label;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public void apply(XWPFParagraph paragraph) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(run.getTextPosition());

                if (text == null || !text.contains(label)) {
                    continue;
                }

                text = text.replaceAll(label, data);

                run.setText(text, 0);
            }
        }
    }

    private static class ReplacementWithCarriageReturn extends Replacement {

        public ReplacementWithCarriageReturn(String label) {
            super(label);
        }

        @Override
        public void apply(XWPFParagraph paragraph) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(run.getTextPosition());

                if (text == null || !text.contains(getLabel())) {
                    continue;
                }

                String[] parts = getData().split("\n");

                text = text.replaceAll(getLabel(), parts[0]);

                run.setText(text, 0);

                for (int i = 1; i < parts.length; i++) {
                    run.addBreak();
                    run.setText(parts[i]);
                }
            }
        }
    }

    private Map<String, Replacement> replacementsForLabels;

    public Replacements() {
        replacementsForLabels = new HashMap<>();
    }

    public void put(String label, String data) {
        Replacement replacementObject;

        if (data.contains("\n")) {
            replacementObject = new ReplacementWithCarriageReturn(label);
        } else {
            replacementObject = new Replacement(label);
        }

        replacementObject.setData(data);
        replacementsForLabels.put(label, replacementObject);
    }

    public String get(String label) {
        Replacement replacement = replacementsForLabels.get(label);

        if (replacement == null) {
            return "";
        }

        return replacement.getData();
    }

    public void apply(XWPFParagraph paragraph) {
        for (Replacement replacement : replacementsForLabels.values()) {
            replacement.apply(paragraph);
        }
    }
}
