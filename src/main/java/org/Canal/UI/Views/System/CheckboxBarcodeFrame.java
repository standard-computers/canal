package org.Canal.UI.Views.System;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.Canal.UI.Elements.LockeState;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class CheckboxBarcodeFrame extends LockeState {

    private JCheckBox[] checkBoxes;
    private JButton printButton;
    private List<String> selectedValues;

    public CheckboxBarcodeFrame(String[] options) {
        super("Checkbox Barcode Selector", "/CH_BC_PRINTER", false, true, false, true);
        setSize(400, 300);
        setLayout(new BorderLayout());
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new GridLayout(options.length, 1));
        checkBoxes = new JCheckBox[options.length];
        for (int i = 0; i < options.length; i++) {
            checkBoxes[i] = new JCheckBox(options[i]);
            checkboxPanel.add(checkBoxes[i]);
        }
        add(new JScrollPane(checkboxPanel), BorderLayout.CENTER);
        printButton = new JButton("Print to Barcodes (PDF)");
        printButton.addActionListener(_ -> handlePrintAction());
        add(printButton, BorderLayout.SOUTH);
    }

    private void handlePrintAction() {
        selectedValues = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                selectedValues.add(checkBox.getText());
            }
        }

        if (!selectedValues.isEmpty()) {
            try {
                createPdfWithBarcodes(selectedValues);
                JOptionPane.showMessageDialog(this, "PDF with barcodes created successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to create PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No checkboxes selected.");
        }
    }

    private void createPdfWithBarcodes(List<String> selectedValues) throws Exception {
        PDDocument document = new PDDocument();
        Code128Bean barcode128 = new Code128Bean();
        barcode128.setHeight(5f);
        barcode128.setModuleWidth(0.15);
        barcode128.doQuietZone(false);
        for (String value : selectedValues) {
            PDPage page = new PDPage();
            document.addPage(page);
            BufferedImage barcodeImage;
            try (FileOutputStream out = new FileOutputStream("barcode_temp.png")) {
                BitmapCanvasProvider canvasProvider = new BitmapCanvasProvider(out, "image/x-png", 300, BufferedImage.TYPE_BYTE_BINARY, false, 0);
                barcode128.generateBarcode(canvasProvider, value);
                canvasProvider.finish();

                barcodeImage = ImageIO.read(new java.io.File("barcode_temp.png"));
            }
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(BufferedImageToPDImage.convert(document, barcodeImage), 100, 500);
            contentStream.beginText();
            InputStream fontStream = CheckboxBarcodeFrame.class.getResourceAsStream("/VT323-Regular.ttf");
            contentStream.setFont(PDType0Font.load(document, fontStream), 12);
            contentStream.newLineAtOffset(100, 450);
            contentStream.showText("Barcode for: " + value);
            contentStream.endText();
            contentStream.close();
        }
        document.save("selected_barcodes.pdf");
        document.close();
    }

    public static class BufferedImageToPDImage {
        public static org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject convert(PDDocument doc, BufferedImage image) throws Exception {
            return org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory.createFromImage(doc, image);
        }
    }
}