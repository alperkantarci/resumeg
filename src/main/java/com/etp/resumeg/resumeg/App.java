package com.etp.resumeg.resumeg;

import java.io.IOException;
import java.util.*;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;

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
    private static final String SRC_RESUME = "pdf/input/Resume2_1col.pdf";
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

        System.out.println("pdfDoc.getX():" + pdfDoc.getFirstPage().getPageSize().getX());
        System.out.println("pdfDoc.getY():" + pdfDoc.getFirstPage().getPageSize().getY());
        System.out.println("pdfDoc.getWidth():" + pdfDoc.getFirstPage().getPageSize().getWidth());
        System.out.println("pdfDoc.getHeight():" + pdfDoc.getFirstPage().getPageSize().getHeight());
        System.out.println("pdfDoc.getPageSize():" + pdfDoc.getFirstPage().getPageSize());
        System.out.println();

        pdfDoc.getFirstPage().getPageSize().getX();
        App app = new App();
        List<MyItem> items = app.getContentItems(pdfDoc);
        Collections.sort(items);
        List<Line> lines = app.getLines(items);

        System.out.println("items.size():" + items.size());
        System.out.println("lines.size():" + lines.size());

        System.out.println();
        for (Line lineItem : lines) {
            if (!lineItem.getText().equals(" ")) {
                System.out.println();
                System.out.println(lineItem.getRealRectangle());
                System.out.println("lineItem.getText():" + lineItem.getText());
                System.out.println("lineItem.getX():" + lineItem.getRealRectangle().getX());
                System.out.println("lineItem.getLeft():" + lineItem.getRealRectangle().getLeft());
                System.out.println("lineItem.getY():" + lineItem.getRealRectangle().getY());
                System.out.println("lineItem.getBottom():" + lineItem.getRealRectangle().getBottom());
                System.out.println("lineItem.getRight():" + lineItem.getRealRectangle().getRight());
                System.out.println("lineItem.getTop():" + lineItem.getRealRectangle().getTop());
                System.out.println("lineItem.getHeight():" + lineItem.getRealRectangle().getHeight());
                System.out.println("lineItem.getWidth():" + lineItem.getRealRectangle().getWidth());
                System.out.println("lineItem.fontSize():" + lineItem.getFontSize());

                PdfDrawService.drawRectangleOnPdf(pdfDoc, lineItem.getDrawableRectangle(), ColorConstants.RED);
            }
        }

//        System.out.println();
//        System.out.println("pdf contains " + customStrategy.getTemplateKeywords().size() + " different types");
//        System.out.println();
//
//        // templateKeywords splitted by new line char and grouped by uuid keys
//        System.out.println("templateKeywords.size:" + customStrategy.getTemplateKeywords().size());
//        for (Map.Entry<String, List<TextRenderInfo>> templateWords :
//                customStrategy.getTemplateKeywords().entrySet()) {
//            System.out.println("key:" + templateWords.getKey() + ", value.size:" + templateWords.getValue().size());
//
//            int index = 0;
//            TemplateKeyword templateKeyword = new TemplateKeyword();
//            StringBuilder stringBuilder = new StringBuilder();
//            for (TextRenderInfo textRenderInfo :
//                    templateWords.getValue()) {
//
//                stringBuilder.append(textRenderInfo.getText());
//
//                // firstChar of a word
//                if (index == 0) {
//                    float left = textRenderInfo.getBaseline().getStartPoint().get(Vector.I1);
//                    float bottom = textRenderInfo.getBaseline().getStartPoint().get(Vector.I2);
//
//                    templateKeyword.setLeft(left);
//                    templateKeyword.setBottom(bottom);
//
//                    templateKeyword.setGraphicsState(textRenderInfo.getGraphicsState());
//                }
//
//                // lastChar of a word
//                if (index == templateWords.getValue().size() - 1) {
//                    System.out.println("templateWords.getRight():" + textRenderInfo.getBaseline().getEndPoint().get(Vector.I1));
//                    System.out.println("left:" + templateKeyword.getLeft());
//
//                    float right = textRenderInfo.getBaseline().getEndPoint().get(Vector.I1);
//                    float width = right - templateKeyword.getLeft();
//                    System.out.println("width:" + width);
//
//                    templateKeyword.setWidth(width);
//                    templateKeyword.setText(stringBuilder.toString());
//                    templateKeywords.add(templateKeyword);
//                }
//
//                System.out.print("\"" + textRenderInfo.getText() + "\"");
//
//                index++;
//            }
//            System.out.println();
//            System.out.println("=======================");
//        }
//
//        // sort lines by top to bottom
//        Collections.sort(templateKeywords);
//
//        System.out.println("TEMPLATEKEYWORDS LIST:");
//        for (TemplateKeyword templateKeyword :
//                templateKeywords) {
//            System.out.println("templateKeyword.getText():" + templateKeyword.getText());
//
//            Rectangle realRectangle = new Rectangle(templateKeyword.getLeft(), 792 - templateKeyword.getBottom() - templateKeyword.getRealFontSize(), templateKeyword.getWidth(), templateKeyword.getRealFontSize());
//            PdfDrawService.drawRectangleOnPdf(pdfDoc, realRectangle, ColorConstants.RED);
//        }
//
//        System.out.println("TEMPLATEKEYWORDS LIST:");
//        List<List<TemplateKeyword>> structures = new ArrayList<>();
//        List<TemplateKeyword> structure = new ArrayList<>();
//        for (TemplateKeyword templateKeyword :
//                templateKeywords) {
//            System.out.println("templateKeyword.getText():" + templateKeyword.getText());
//
//            if (structure.isEmpty()) {
//                structure.add(templateKeyword);
//                continue;
//            }
//            if (areInSameStructure(structure.get(structure.size() - 1), templateKeyword)) {
//                structure.add(templateKeyword);
//            } else {
//                structures.add(new ArrayList<>(structure));
//                structure = new ArrayList<>();
//                structure.add(templateKeyword);
//            }
//
////            Rectangle realRectangle = new Rectangle(templateKeyword.getLeft(), 792 - templateKeyword.getBottom() - templateKeyword.getRealFontSize(), templateKeyword.getWidth(), templateKeyword.getRealFontSize());
////            PdfDrawService.drawRectangleOnPdf(pdfDoc, realRectangle, ColorConstants.RED);
//        }
//        if (!structure.isEmpty()) {
//            structures.add(new ArrayList<>(structure));
//        }
//
//
//        System.out.println("Structures.size():" + structures.size());
//
//        for (List<TemplateKeyword> structureItem : structures) {
//            System.out.println("structure item:");
//            System.out.println("structureItem.size():" + structureItem.size());
//
//            float lineSpace = ((float)((structureItem.size()-1) * 13.25));
//
//
////            float structureHeight = (structureItem.size() * structureItem.get(0).getRealFontSize());
//
//            float structureHeight;
//            if(structureItem.size() > 1){
//                structureHeight = structureItem.get(0).getBottom() - structureItem.get(structureItem.size() - 1).getBottom() + structureItem.get(0).getRealFontSize();
//            }else{
//                structureHeight = structureItem.get(0).getRealFontSize();
//            }
//
//            float structureLeft = structureItem.get(0).getLeft();
//            float structureBottom = structureItem.get(structureItem.size() - 1).getBottom();
//            float structureWidth = 0;
//
//            Rectangle realRectangle = new Rectangle(structureLeft, 792 - structureBottom - structureHeight, 0, structureHeight);
//
//            for (TemplateKeyword templateKeyword : structureItem) {
//                System.out.println(templateKeyword.getText());
//
//                if (structureWidth <= templateKeyword.getWidth()) {
//                    structureWidth = templateKeyword.getWidth();
//                }
//            }
//
//            realRectangle.setWidth(structureWidth);
//            PdfDrawService.drawRectangleOnPdf(pdfDoc, realRectangle, ColorConstants.RED);
//        }

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
     * Checks if 2 items are on the same line.
     *
     * @param i1 first item
     * @param i2 second item
     * @return true if items are on the same line, otherwise false
     */
    static boolean areOnSameLine(MyItem i1, MyItem i2) {
        return Math.abs(i1.getLL().getY() - i2.getLL().getY()) <= MyItem.itemPositionTolerance;
    }

    static boolean areInSameStructure(TemplateKeyword i1, TemplateKeyword i2) {

        System.out.println();
        System.out.println("i1:" + i1.getText());
        System.out.println("i2:" + i2.getText());
        System.out.println("i1 color:" + i1.getGraphicsState().getStrokeColor().getColorValue()[0]);
        System.out.println("i2 color:" + i2.getGraphicsState().getStrokeColor().getColorValue()[0]);
        System.out.println("i1 bottom:" + (792 - i1.getBottom()));
        System.out.println("i2 bottom:" + (792 - i2.getBottom()));
        System.out.println("i1 realFontSize:" + i1.getRealFontSize());


        float i2Bottom = 792 - i2.getBottom();
        float i1Bottom = 792 - i1.getBottom();

        System.out.println("i2Bottom - i1Bottom:" + (i2Bottom - i1Bottom));

        // sadece structure ozelligi bozan durumlarda false dondur
        // 14.0 degeri iki line arasindaki max vertical bosluk, o degeri gecince 2 line ayni structure'a ait olmuyor.
        if (i2Bottom - i1Bottom > 18.75) {
            return false;
        }

//        if (i2Bottom - i1Bottom >= 13.0) {
//            return false;
//        }

//        if(i1.getGraphicsState().getStrokeColor().getColorValue()[0] != i2.getGraphicsState().getStrokeColor().getColorValue()[0]){
//            return false;
//        }
//        else if(i2.getLeft() - i1.getLeft() >= 3.0 && i2.getBottom() - i1.getBottom() >= 3.0){
//            return false;
//        }
        return true;

//        if (!i1.getColor().equals(i2.getColor()))
//            return false;
//        else if (i2.getRealRectangle().getLeft() - i1.getRealRectangle().getLeft() >= MyItem.itemPositionTolerance)
//            return false;
//        return true;
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
