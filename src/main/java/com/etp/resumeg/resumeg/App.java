package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class App {

    public App() {
        newContent.put("123 your street", "124 MY STREET");
        newContent.put("your city, st 12345", "MY CITY, ST 54321");
        newContent.put("(123) 456-7890", "(553) 654-0987");
        newContent.put("no_reply@example.com", "TEST@TEST.COM");
    }

    // Paths
    private static final String SRC_HELLO = "pdf/input/hello.pdf";
    //	public static final String SRC_TEST = "/home/alperk/Downloads/dddd.pdf";
    private static final String SRC_RESUME = "pdf/input/Resume4_1col.pdf";
    private static final String DEST = "pdf/output/parsedStream";
    private static final String OUT_PDF = "pdf/output/testOutput.pdf";
    // Font resources
    private static final String ROBOTO_REGULAR = "resources/fonts/RobotoCondensed-Regular.ttf";
    // New content for pdf
    private static final HashMap<String, String> newContent = new HashMap<>();
    // Font
    private static List<TextRenderInfo> renderInfos = new ArrayList<>();
    // GoogleWebFonts
    private static GoogleWebFontService webFontService = null;

    private static final List<TemplateKeyword> templateKeywordList = new ArrayList<>();
    private static List<TemplateKeyword> templateKeywords = new ArrayList<>();

//    private static HashMap<String, List<TextRenderInfo>> templateKeywords = new HashMap<>();
//        private static HashMap<String, HashMap<String, List<TextRenderInfo>>> templateKeywords = new HashMap<>();


    public static void main(String[] args) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC_RESUME), new PdfWriter(OUT_PDF));

//        CustomITextExtractionStrategy customStrategy = new CustomITextExtractionStrategy(pdfDoc);
//        PdfTextExtractor.getTextFromPage(pdfDoc.getFirstPage(), customStrategy);

        pdfDoc.getFirstPage().getPageSize().getX();
        App app = new App();
        List<MyItem> items = app.getContentItems(pdfDoc);
        Collections.sort(items);
        List<Line> lines = app.getLines(items);
        List<Structure> structures = app.getStructures(lines);


        System.out.println("items.size():" + items.size());
        System.out.println("lines.size():" + lines.size());
        System.out.println("structures.size():" + structures.size());

        System.out.println();
//        for (Line lineItem : lines) {
//            if (!lineItem.getText().equals(" ")) {
//                System.out.println();
//                System.out.println(lineItem.getRealRectangle());
//                System.out.println("lineItem.getText():" + lineItem.getText());
//                System.out.println("lineItem.getX():" + lineItem.getRealRectangle().getX());
//                System.out.println("lineItem.getLeft():" + lineItem.getRealRectangle().getLeft());
//                System.out.println("lineItem.getY():" + lineItem.getRealRectangle().getY());
//                System.out.println("lineItem.getBottom():" + lineItem.getRealRectangle().getBottom());
//                System.out.println("lineItem.getRight():" + lineItem.getRealRectangle().getRight());
//                System.out.println("lineItem.getTop():" + lineItem.getRealRectangle().getTop());
//                System.out.println("lineItem.getHeight():" + lineItem.getRealRectangle().getHeight());
//                System.out.println("lineItem.getWidth():" + lineItem.getRealRectangle().getWidth());
//                System.out.println("lineItem.fontSize():" + lineItem.getFontSize());
//
//                PdfDrawService.drawRectangleOnPdf(pdfDoc, lineItem.getDrawableRectangle(), ColorConstants.RED);
//            }
//        }

        for (Structure structure : structures) {
            if (!structure.getText().equals(" ")) {
                PdfDrawService.drawRectangleOnPdf(pdfDoc, structure.getDrawableRectangle(), ColorConstants.RED);
            }
        }
        pdfDoc.close();
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

    /**
     * Combines lines into structures
     *
     * @param lines a list of lines
     * @return list of structures
     */
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

    /**
     * Checks if 2 items are on the same line.
     *
     * @param i1 first item
     * @param i2 second item
     * @return true if items are on the same line, otherwise false
     */
    static boolean areOnSameLine(MyItem i1, MyItem i2) {
        return Math.abs(i1.getLL().getY() - i2.getLL().getY()) <= MyItem.itemPositionTolerance;
    }

    static boolean areInSameStructure(Line i1, Line i2) {
        float i2Bottom = i2.getRealRectangle().getBottom();
        float i1Bottom = i1.getRealRectangle().getBottom();

        // sadece structure ozelligi bozan durumlarda false dondur
        // 14.0 degeri iki line arasindaki max vertical bosluk, o degeri gecince 2 line ayni structure'a ait olmuyor.
        if (i1Bottom - i2Bottom > 20.99) {
            return false;
        }

        return true;
    }

    private void createDirs() {
//		File file = new File(DEST);
//		file.getParentFile().mkdirs();
    }

    private static float calculateRealFontSize(TextRenderInfo renderInfo) {
        CanvasGraphicsState canvasGs = renderInfo.getGraphicsState();
        Matrix textToUserSpaceTransformMatrix = canvasGs.getCtm();
        float transformedFontSize = new Vector(0, canvasGs.getFontSize(), 0).cross(textToUserSpaceTransformMatrix)
                .length();
        return (float) Math.ceil(transformedFontSize);
    }

    public static void downloadPdfFontsFromGoogleWebFontsApi() throws IOException {
        System.out.print("downloadPdfFontsFromGoogleWebFontsApi(): ");
        // Creating fonts from google's webfonts api
        if (webFontService == null) {
            try {
                webFontService = new GoogleWebFontService();

                // List of fontObjects that pdf contains
                List<PdfObject> fontObjects = PdfFontService.getFontsList(SRC_RESUME);

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
