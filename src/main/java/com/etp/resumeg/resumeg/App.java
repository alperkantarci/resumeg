package com.etp.resumeg.resumeg;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.kernel.colors.Color;
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
    private static final String SRC_RESUME = "pdf/input/Resume.pdf";
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

        CustomITextExtractionStrategy customStrategy = new CustomITextExtractionStrategy(pdfDoc);
        // Extract text content by CustomITextExtractionStrategy class
        PdfTextExtractor.getTextFromPage(pdfDoc.getFirstPage(), customStrategy);

        System.out.println();
        System.out.println("pdf contains " + customStrategy.getTemplateKeywords().size() + " different types");
        System.out.println();

        // templateKeywords splitted by new line char and grouped by uuid keys
        System.out.println("templateKeywords.size:" + customStrategy.getTemplateKeywords().size());
        for (Map.Entry<String, List<TextRenderInfo>> templateWords :
                customStrategy.getTemplateKeywords().entrySet()) {
            System.out.println("key:" + templateWords.getKey() + ", value.size:" + templateWords.getValue().size());

            int index = 0;
            TemplateKeyword templateKeyword = new TemplateKeyword();
            StringBuilder stringBuilder = new StringBuilder();
            for (TextRenderInfo textRenderInfo :
                    templateWords.getValue()) {

                stringBuilder.append(textRenderInfo.getText());

                // firstChar of a word
                if (index == 0) {
                    float left = textRenderInfo.getBaseline().getStartPoint().get(Vector.I1);
                    float bottom = textRenderInfo.getBaseline().getStartPoint().get(Vector.I2);

                    templateKeyword.setLeft(left);
                    templateKeyword.setBottom(bottom);

                    templateKeyword.setGraphicsState(textRenderInfo.getGraphicsState());
                }

                // lastChar of a word
                if (index == templateWords.getValue().size() - 1) {
                    float right = textRenderInfo.getBaseline().getEndPoint().get(Vector.I1);
                    float width = right - templateKeyword.getLeft();

                    templateKeyword.setWidth(width);
                    templateKeyword.setText(stringBuilder.toString());
                    templateKeywords.add(templateKeyword);
                }

                System.out.print("\"" + textRenderInfo.getText() + "\"");

                index++;
            }
            System.out.println();
            System.out.println("=======================");
        }

        System.out.println("TEMPLATEKEYWORDS LIST:");
        for (TemplateKeyword templateKeyword :
                templateKeywords) {
            System.out.println("templateKeyword.getText():" + templateKeyword.getText());

            Rectangle rectangle = new Rectangle(templateKeyword.getLeft(), 792 - templateKeyword.getBottom() - templateKeyword.getRealFontSize(), templateKeyword.getWidth(), templateKeyword.getRealFontSize());
            PdfDrawService.drawRectangleOnPdf(pdfDoc, rectangle, ColorConstants.RED);
        }


        pdfDoc.close();
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
