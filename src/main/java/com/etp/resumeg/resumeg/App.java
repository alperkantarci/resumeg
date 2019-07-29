package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.filespec.PdfDictionaryFS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*;

public class App {

    public App() {
        newContent.put("123 your street", "124 MY STREET");
        newContent.put("your city, st 12345", "MY CITY, ST 54321");
        newContent.put("(123) 456-7890", "(553) 654-0987");
        newContent.put("no_reply@example.com", "TEST@TEST.COM");
    }

    // New content for pdf
    private static final HashMap<String, String> newContent = new HashMap<>();

    // GoogleWebFonts
    private static GoogleWebFontService webFontService = null;

    public static void main(String[] args) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(EnvVariable.SRC_RESUME), new PdfWriter(EnvVariable.OUT_PDF));
        pdfDoc.getFirstPage().getPageSize().getX();

        App app = new App();

        List<MyItem> items = app.getContentItems(pdfDoc);
        Collections.sort(items);

        List<Line> lines = app.getLines(items);
//        List<Structure> structures = app.getStructures(lines);

        System.out.println("items.size():" + items.size());
        System.out.println("lines.size():" + lines.size());
//        System.out.println("structures.size():" + structures.size());

        removeEmptyLines(lines);

        HashMap<Double, List<Line>> multipleColumnLines = splitColumnLinesByX(lines);

        System.out.println("twoColumnLines.size(): " + multipleColumnLines.size());

//        for (Map.Entry<Double, List<Line>> entry : multipleColumnLines.entrySet()
//        ) {
//            System.out.println("key: " + entry.getKey() + " -> " + entry.getValue().size());
//            for (Line line :
//                    entry.getValue()) {
////                System.out.println("line: " + line.getText());
//            }
//        }

        HashMap<Double, List<Structure>> multipleColumnStructures = new HashMap<>();

        for (Map.Entry<Double, List<Line>> entry : multipleColumnLines.entrySet()
        ) {
            List<Structure> structures = app.getStructures(entry.getValue());

            System.out.println("structures.size(): " + structures.size());

            for (Structure structure :
                    structures) {
                System.out.println("structure.text: " + structure.getText());
            }
//            if (!multipleColumnStructures.containsKey(entry.getKey())) {
//                multipleColumnStructures.put(entry.getKey(), app.getStructures(entry.getValue()));
//            }
        }

        System.out.println("multipleColumnStructures.size(): " + multipleColumnStructures.size());

        for (Map.Entry<Double, List<Structure>> entry : multipleColumnStructures.entrySet()
        ) {
            System.out.println("key: " + entry.getKey());
            for (Structure structure: entry.getValue()
                 ) {
                System.out.println("structure.text: " + structure.getText());
            }
        }


        System.out.println();

        showPdfStructure(pdfDoc, items);
        showPdfStructure(pdfDoc, lines);
//        showPdfStructure(pdfDoc, structures);

        pdfDoc.close();
    }

    public static HashMap<Double, List<Line>> splitColumnLinesByX(List<Line> lines) {
        HashMap<Double, List<Line>> multipleColumnLines = new HashMap<>();
        for (Line line :
                lines) {
            double lineX = Math.round(line.getLL().getX() * 100.0) / 100.0;
//            System.out.println("line.x: " + lineX);
//            System.out.println("line: " + line.getText());

            if (!multipleColumnLines.containsKey(lineX)) {

//                // berkayCv bug fix
//                double maxDistance = 0.0;
//                double globalXTolerance = 10.0;
//
//
//                for (Map.Entry<Double, List<Line>> entry : multipleColumnLines.entrySet()
//                ) {
//                    maxDistance = Math.max(Math.abs(entry.getKey() - globalXTolerance), maxDistance);
//                }
//
//                System.out.println("maxDistance: " + maxDistance);
//
//                if(maxDistance >= globalXTolerance){
//                    List<Line> oneColumnLines = new ArrayList<>();
//                    multipleColumnLines.put(lineX, oneColumnLines);
//                    multipleColumnLines.get(lineX).add(line);
//                }

                List<Line> oneColumnLines = new ArrayList<>();
                multipleColumnLines.put(lineX, oneColumnLines);
            }
            multipleColumnLines.get(lineX).add(line);


//            if (lineX == 50.7) {
//                System.out.println("txt: " + line.getText());
//            }

//            if(lineX == 409.2){
//                System.out.println("txt: " + line.getText());
//            }
        }

        return multipleColumnLines;
    }

    public static void removeEmptyLines(List<Line> lines) {
        List<Line> toRemove = new ArrayList<>();

        try {
            for (Line line :
                    lines) {
                byte[] bytes = line.getText().getBytes("US-ASCII");

                // [63] -> ascii value of empty string
                if (Arrays.toString(bytes).equals("[63]")) {
                    toRemove.add(line);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        lines.removeAll(toRemove);
    }

    public static <T extends MyItem> void showPdfStructure(PdfDocument pdfDoc, List<T> items) {
        Color rectColor = ColorConstants.MAGENTA;

        if (items.get(0).getClass().getSimpleName().equals("Line")) {
            rectColor = ColorConstants.RED;
        } else if (items.get(0).getClass().getSimpleName().equals("Structure")) {
            rectColor = ColorConstants.BLACK;
        }

        for (MyItem item : items) {
            if (!item.getText().equals(" ")) {
                PdfDrawService.drawRectangleOnPdf(pdfDoc, item.getDrawableRectangle(), rectColor);
            }
        }
    }

    public List<MyItem> getContentItems(PdfDocument pdfDocument) throws IOException {
        CustomITextExtractionStrategy customStrategy = new CustomITextExtractionStrategy(pdfDocument);
        // Extract text content by CustomITextExtractionStrategy class
        PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), customStrategy);
        return customStrategy.getItems();
    }

    public List<Line> getLines(List<MyItem> items) {
        List<Line> lines = new ArrayList<>();
        List<MyItem> line = new ArrayList<>();
        for (MyItem item : items) {
            if (line.isEmpty()) {
                line.add(item);
                continue;
            }
            if (areOnSameLine(line.get(line.size() - 1), item)) {
                line.add(item);
            } else {
                lines.add(new Line(line));
                line = new ArrayList<>();
                line.add(item);
            }
        }
        if (!line.isEmpty())
            lines.add(new Line(line));
        return lines;
    }

    public List<Structure> getStructures(List<Line> lines) {
        List<Structure> structures = new ArrayList<>();
        List<MyItem> structure = new ArrayList<>();
        for (Line line : lines) {
            if (structure.isEmpty()) {
                structure.add(line);
                continue;
            }
            if (areInSameStructure((Line) structure.get(structure.size() - 1), line)) {
                structure.add(line);
            } else {
                structures.add(new Structure(structure));
                structure = new ArrayList<>();
                structure.add(line);
            }
        }
        if (!structure.isEmpty())
            structures.add(new Structure(structure));
        return structures;
    }

    static boolean areOnSameLine(MyItem i1, MyItem i2) {

        DecimalFormat f = new DecimalFormat("##.00");

//        System.out.println("i1.text:" + i1.getText() + "; x:" + f.format(i1.getLL().getX()) + "; y:" + f.format(i1.getLL().getY()));
//        System.out.println("i2.text:" + i2.getText() + "; x:" + f.format(i2.getLL().getX()) + "; y:" + f.format(i2.getLL().getY()));

        return ((Math.abs(i1.getLL().getY() - i2.getLL().getY()) <= MyItem.itemPositionTolerance)
                && (Math.abs(i1.getLL().getX() - i2.getLL().getX()) <= 40f));


//        return (Math.abs(i1.getLL().getY() - i2.getLL().getY()) <= MyItem.itemPositionTolerance);
    }

    static boolean areInSameStructure(Line i1, Line i2) {
        float i2Bottom = i2.getRealRectangle().getBottom();
        float i1Bottom = i1.getRealRectangle().getBottom();

//        System.out.println("line1:" + i1.getText());
//        System.out.println("line2:" + i2.getText());
//
//        System.out.println("i1Bottom:" + i1Bottom + ", i2Bottom:" + i2Bottom);

        if ((i1Bottom - i2Bottom > 30)) {
            return false;
        }

        return true;
    }

    public static void downloadPdfFontsFromGoogleWebFontsApi() throws IOException {
        System.out.print("downloadPdfFontsFromGoogleWebFontsApi(): ");
        // Creating fonts from google's webfonts api
        if (webFontService == null) {
            try {
                webFontService = new GoogleWebFontService();

                // List of fontObjects that pdf contains
                List<PdfObject> fontObjects = PdfFontService.getFontsList(EnvVariable.SRC_RESUME);

                // Downloaded fonts
                List<PdfFont> downloadedFonts = new ArrayList<>();
//				int countDownloadedFonts = 0;
                // Loop through fontObjects and download every font from google web font api
                for (PdfObject fontObj : fontObjects) {
                    PdfDictionary fontDict = (PdfDictionary) fontObj;
                    // Split originial pdf font name to be able to download from google api
                    // Google font names are like: fontFamily: Roboto Condensed, fontVariant:
                    // regular
                    HashMap<String, String> font = ParseService.splitFontName(fontDict.get(PdfName.BaseFont).toString());
                    // Download font and create PdfFont object
                    PdfFont pdfFont = webFontService.downloadFontByFamily(font.get("Family"), font.get("Variant"));
                    if (pdfFont != null) {
                        downloadedFonts.add(pdfFont);
//						countDownloadedFonts++;
                    }
                }

                System.out.println(downloadedFonts.size() + " fonts were downloaded successfully.");
            } catch (Exception ex) {
//				ex.printStackTrace();
                if (ex.getMessage().contains("No subject alternative DNS name matching")) {
                    System.out.println("Check your internet connection !");
                }
            }

        }

    }
}
