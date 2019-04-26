package com.etp.resumeg.resumeg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Matrix;
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
    private static final String SRC_RESUME = "pdf/input/Resume2.pdf";
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

    public static void main(String[] args) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC_RESUME), new PdfWriter(OUT_PDF));

        CustomITextExtractionStrategy customStrategy = new CustomITextExtractionStrategy();
        // Extract text content by CustomITextExtractionStrategy class
        PdfTextExtractor.getTextFromPage(pdfDoc.getFirstPage(), customStrategy);

        System.out.println("Template Words:");
        System.out.println("TemplateWords.count:" + customStrategy.getTemplateKeywords().size());
        for (TemplateKeyword templateWords :
                customStrategy.getTemplateKeywords()) {
            System.out.println(templateWords);
        }

//        System.out.println();
//		System.out.println(customStrategy.getWords().size());

//        List<WordTextRenderInfo> captionWords = new ArrayList<>();
//        List<String> googleCaptionWords = new ArrayList<>();
//        googleCaptionWords.add("SKILLS");
//        googleCaptionWords.add("EXPERIENCE");
//        googleCaptionWords.add("EDUCATION");
//        googleCaptionWords.add("AWARDS");

//        int upperCaseCountPerWord = 0;
//        for (WordTextRenderInfo item : customStrategy.getWords()) {
//            System.out.print(item.getContent().size() + " ->");
//            for (TextRenderInfo info : item.getContent()) {
//                System.out.print(info.getText());
//                if (testAllUpperCase(info.getText())) {
//                    upperCaseCountPerWord++;
//                }
//            }
//            if (upperCaseCountPerWord == item.getContent().size() && item.getContent().size() > 1) {
//                if (googleCaptionWords.contains(item.getText())) {
//                    captionWords.add(item);
//                }
//            }
//            upperCaseCountPerWord = 0;
//            System.out.println();
//        }

//        System.out.println("\nCaption words:");
//        for (WordTextRenderInfo item : captionWords) {
//            System.out.print("caption:");
//            for (TextRenderInfo info : item.getContent()) {
//                System.out.print(info.getText());
//            }
//            System.out.println();
//        }

        // SKILLS caption
//        LineSegment skillsSegment = captionWords.get(0).getFirstChar().getBaseline();
//        Vector skillsStartPoint = skillsSegment.getStartPoint();
//        Vector skillsEndPoint = skillsSegment.getEndPoint();
//        float skillsY1 = skillsStartPoint.get(Vector.I2);
//        float skillsX1 = skillsStartPoint.get(Vector.I1);
//        float skillsY2 = skillsEndPoint.get(Vector.I2);

        // EXPERIENCE caption
//        LineSegment experienceSegment = captionWords.get(1).getFirstChar().getBaseline();
//        Vector experienceEndPoint = experienceSegment.getEndPoint();
//        Vector experienceSartPoint = experienceSegment.getStartPoint();
//        float experienceY1 = experienceSartPoint.get(Vector.I2);
//        float experienceY2 = experienceEndPoint.get(Vector.I2);
//        float experienceFontSize = calculateRealFontSize(captionWords.get(1).getFirstChar());

        // EDUCATION caption
//        LineSegment educationSegment = captionWords.get(2).getFirstChar().getBaseline();
//        Vector educationEndPoint = educationSegment.getEndPoint();
//        float educationY2 = educationEndPoint.get(Vector.I2);
//        float educationFontSize = calculateRealFontSize(captionWords.get(2).getFirstChar());
//
//        float paragraphPadding = 10;
//
//        Rectangle rectBetweenSkillsAndExperience = new Rectangle(skillsX1, 792 - skillsY1 + paragraphPadding, skillsX1 + 400f,
//                skillsY1 - experienceY2 - experienceFontSize - 2 * paragraphPadding);
//        Rectangle rectBetweenExperienceAndEducation = new Rectangle(skillsX1, 792 - experienceY1 + paragraphPadding, skillsX1 + 400f,
//                experienceY1 - educationY2 - educationFontSize - 2 * paragraphPadding);
//        drawRectangleOnPdf(pdfDoc, rectBetweenSkillsAndExperience, ColorConstants.RED);
//        drawRectangleOnPdf(pdfDoc, rectBetweenExperienceAndEducation, ColorConstants.RED);
//
//        Rectangle testRect = new Rectangle(skillsX1, 792 - skillsY1 + paragraphPadding, skillsX1 + 400f,
//                2f);
//        Rectangle testRect2 = new Rectangle(skillsX1, skillsY1 - experienceY2 - experienceFontSize - 2 * paragraphPadding, skillsX1 + 400f,
//                10f);
//        drawRectangleOnPdf(pdfDoc, testRect, ColorConstants.BLACK);
//        drawRectangleOnPdf(pdfDoc, testRect2, ColorConstants.BLACK);
//
//        System.out.println("getY:" + rectBetweenSkillsAndExperience.getY());
//        System.out.println("getBottom():" + rectBetweenSkillsAndExperience.getBottom());
//        System.out.println("getTop():" + rectBetweenSkillsAndExperience.getTop());
//        System.out.println("height:" + (skillsY1 - experienceY2 - experienceFontSize - 2 * paragraphPadding));
//        System.out.println("getHeight():" + rectBetweenSkillsAndExperience.getHeight());
//
//
//        System.out.println("leftCorner:" + (792 - skillsY1 + paragraphPadding));
//        System.out.println("skillsY1 - experienceY2:" + (skillsY1 - experienceY2));


//        Rectangle cleanRectBetweenSkillsAndExperience = new Rectangle(skillsX1, experienceY1 + experienceFontSize + paragraphPadding, skillsX1 + 400f,
//                skillsY2 - experienceY2 - experienceFontSize - 2 * paragraphPadding);
//		Rectangle rectBetweenExperienceAndEducation = new Rectangle(skillsX1, 792 - experienceY1 + paragraphPadding, skillsX1 + 400f,
//				experienceY1 - educationY2 - educationFontSize - 2*paragraphPadding);

//        PdfContentCleanService.cleanContentByLocation(pdfDoc, cleanRectBetweenSkillsAndExperience, false);
//		PdfContentCleanService.cleanContentByLocation(pdfDoc, rectBetweenExperienceAndEducation, false);

        pdfDoc.close();

//		List<Rectangle> rectAreas = new ArrayList<>();
//		rectAreas.add(ExampleRectArea.rectangles.get("EDUCATION_Resume2"));

//		PdfContentCleanService.cleanContentByLocation(pdfDoc, rectAreas);
//		PdfContentCleanService.cleanContentByLocation(SRC_RESUME, OUT_PDF,  rectAreas);

//		BasicConfigurator.configure();
//		PdfContentCleanService.cleanContentByLocation(SRC_RESUME, OUT_PDF,  ExampleRectArea.rectangles.get("EDUCATION_Resume2"));
//		PdfContentCleanService.cleanContentByLocation(pdfDoc, ExampleRectArea.rectangles.get("EDUCATION_Resume2"));

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
