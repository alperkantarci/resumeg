package com.etp.resumeg.resumeg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.pdfcleanup.PdfCleanUpTool;

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

		System.out.println();
//		System.out.println(customStrategy.getWords().size());

		List<WordTextRenderInfo> captionWords = new ArrayList<>();
		List<String> googleCaptionWords = new ArrayList<>();
		googleCaptionWords.add("SKILLS");
		googleCaptionWords.add("EXPERIENCE");
		googleCaptionWords.add("EDUCATION");
		googleCaptionWords.add("AWARDS");

		int upperCaseCountPerWord = 0;
		for (WordTextRenderInfo item : customStrategy.getWords()) {
			System.out.print(item.getContent().size() + " ->");
			for (TextRenderInfo info : item.getContent()) {
				System.out.print(info.getText());
				if (testAllUpperCase(info.getText())) {
					upperCaseCountPerWord++;
				}
			}
			if (upperCaseCountPerWord == item.getContent().size() && item.getContent().size() > 1) {
				if (googleCaptionWords.contains(item.getText())) {
					captionWords.add(item);
				}
			}
			upperCaseCountPerWord = 0;
			System.out.println();
		}

		System.out.println("\nCaption words:");
		for (WordTextRenderInfo item : captionWords) {
			System.out.print("caption:");
			for (TextRenderInfo info : item.getContent()) {
				System.out.print(info.getText());
			}
			System.out.println();
		}

		// SKILLS caption
		LineSegment skillsSegment = captionWords.get(0).getFirstChar().getBaseline();
		Vector skillsStartPoint = skillsSegment.getStartPoint();
		Vector skillsEndPoint = skillsSegment.getEndPoint();
		float skillsY1 = skillsStartPoint.get(Vector.I2);
		float skillsX1 = skillsStartPoint.get(Vector.I1);
		float skillsY2 = skillsEndPoint.get(Vector.I2);

		// EXPERIENCE caption
		LineSegment experienceSegment = captionWords.get(1).getFirstChar().getBaseline();
		Vector experienceEndPoint = experienceSegment.getEndPoint();
		Vector experienceSartPoint = experienceSegment.getStartPoint();
		float experienceY1 = experienceSartPoint.get(Vector.I2);
		float experienceY2 = experienceEndPoint.get(Vector.I2);
		float experienceFontSize = calculateRealFontSize(captionWords.get(1).getFirstChar());

		// EDUCATION caption
		LineSegment educationSegment = captionWords.get(2).getFirstChar().getBaseline();
		Vector educationEndPoint = educationSegment.getEndPoint();
		float educationY2 = educationEndPoint.get(Vector.I2);
		float educationFontSize = calculateRealFontSize(captionWords.get(2).getFirstChar());
		
		float paragraphPadding = 10;

		Rectangle rectBetweenSkillsAndExperience = new Rectangle(skillsX1, 792 - skillsY1 + paragraphPadding, skillsX1 + 400f,
				skillsY1 - experienceY2 - experienceFontSize - 2*paragraphPadding);
		Rectangle rectBetweenExperienceAndEducation = new Rectangle(skillsX1, 792 - experienceY1 + paragraphPadding, skillsX1 + 400f,
				experienceY1 - educationY2 - educationFontSize - 2*paragraphPadding);
		drawRectangleOnPdf(pdfDoc, rectBetweenSkillsAndExperience, ColorConstants.RED);
		drawRectangleOnPdf(pdfDoc, rectBetweenExperienceAndEducation, ColorConstants.RED);
		
		Rectangle testRect = new Rectangle(skillsX1, 792 - skillsY1 + paragraphPadding, skillsX1 + 400f,
				2f);
		Rectangle testRect2 = new Rectangle(skillsX1, skillsY1 - experienceY2 - experienceFontSize - 2*paragraphPadding, skillsX1 + 400f,
				10f);
		drawRectangleOnPdf(pdfDoc, testRect, ColorConstants.BLACK);
		drawRectangleOnPdf(pdfDoc, testRect2, ColorConstants.BLACK);
		
		System.out.println("getY:" + rectBetweenSkillsAndExperience.getY());
		System.out.println("getBottom():" + rectBetweenSkillsAndExperience.getBottom());
		System.out.println("getTop():" + rectBetweenSkillsAndExperience.getTop());
		System.out.println("height:" + (skillsY1 - experienceY2 - experienceFontSize - 2*paragraphPadding));
		System.out.println("getHeight():" + rectBetweenSkillsAndExperience.getHeight());
		
		
		System.out.println("leftCorner:" + (792 - skillsY1 + paragraphPadding));
		System.out.println("skillsY1 - experienceY2:" + (skillsY1 - experienceY2));
		
		
		Rectangle cleanRectBetweenSkillsAndExperience = new Rectangle(skillsX1, experienceY1 + experienceFontSize + paragraphPadding, skillsX1 + 400f,
				skillsY2 - experienceY2 - experienceFontSize - 2*paragraphPadding);
//		Rectangle rectBetweenExperienceAndEducation = new Rectangle(skillsX1, 792 - experienceY1 + paragraphPadding, skillsX1 + 400f,
//				experienceY1 - educationY2 - educationFontSize - 2*paragraphPadding);
		
		PdfContentCleanService.cleanContentByLocation(pdfDoc, cleanRectBetweenSkillsAndExperience, false);
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

	private static boolean testAllUpperCase(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= 97 && c <= 122) {
				return false;
			}
		}
		// str.charAt(index)
		return true;
	}

	public void helloWorldExample() throws FileNotFoundException {
		new App().createDirs();
		PdfDocument pdf = new PdfDocument(new PdfWriter(DEST));
		Document document = new Document(pdf);
		document.add(new Paragraph("Hello World!"));
		document.add(new Paragraph("Paragraph2"));
		document.close();
	}

	public void parse(String src, String dest) throws IOException {
//        PdfReader reader = new PdfReader(src);
//        PdfObject obj;
//        for (int i = 1; i <= reader.getLastXref(); i++) {
//            obj = reader.getPdfObject(i);
//            if (obj != null && obj.isStream()) {
//                PRStream stream = (PRStream)obj;
//                byte[] b;
//                try {
//                    b = PdfReader.getStreamBytes(stream);
//                }
//                catch(UnsupportedPdfException e) {
//                    b = PdfReader.getStreamBytesRaw(stream);
//                }
//                FileOutputStream fos = new FileOutputStream(String.format(dest, i));
//                fos.write(b);
//                fos.flush();
//                fos.close();
//            }
//        }
	}

	private static List<PdfObject> getFontsList(String src) throws IOException {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
		PdfPage firstPage = pdfDoc.getFirstPage();
		PdfResources pdfResources = firstPage.getResources();
		PdfDictionary fontsDict = pdfResources.getResource(PdfName.Font);

		List<PdfObject> fontObjects = new ArrayList<>();
		for (Entry<PdfName, PdfObject> font : fontsDict.entrySet()) {
			fontObjects.add(font.getValue());
		}
		pdfDoc.close();
		return fontObjects;
	}

	public void listFonts(String src) throws IOException {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
		PdfPage firstPage = pdfDoc.getFirstPage();
		PdfResources pdfResources = firstPage.getResources();
		PdfDictionary fontDict = pdfResources.getResource(PdfName.Font);

		for (Entry<PdfName, PdfObject> item : fontDict.entrySet()) {
			System.out.println(item.getKey() + ", " + item.getValue());
		}
		pdfDoc.close();
	}

	private static void drawRectangleOnPdf(PdfDocument pdfDoc, Rectangle rect, Color color) {
		PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamAfter(),
				pdfDoc.getFirstPage().getResources(), pdfDoc);
		canvas.saveState();

		canvas.rectangle(new Rectangle(rect));
		canvas.setStrokeColor(color);
		canvas.stroke();
		canvas.restoreState();
	}

	/**
	 * @param reText contains "0 0 100 50 re" string from /Contents
	 * @return
	 */
	private Rectangle convertReTagTextToRectangle(String reText) {
		String splittedReLine[] = reText.split(" ");
		// reText example: "0 0 100 200"
		float x1 = Float.valueOf(splittedReLine[0]);
		float y1 = Float.valueOf(splittedReLine[1]);
		float x2 = Float.valueOf(splittedReLine[2]);
		float y2 = Float.valueOf(splittedReLine[3]);
		return new Rectangle(x1, y1, x2, y2);
	}

	private List<Rectangle> parseContentsRectangles(String content) {
		List<Rectangle> contentsRectangle = new ArrayList<>();
		/**
		 * split content line by line and filter which contains "re" tag
		 */
		for (String item : content.split("\n")) {
			if (item.contains("re")) {
				Rectangle rect = convertReTagTextToRectangle(item);
				contentsRectangle.add(rect);
			}
		}
		return contentsRectangle;
	}

	public void drawRectangles(String src, String dest) throws IOException {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
		PdfPage firstPage = pdfDoc.getFirstPage();
		PdfDictionary dict = firstPage.getPdfObject();
		PdfObject object = dict.get(PdfName.Contents);

		PdfStream stream = (PdfStream) object;
		byte[] data = stream.getBytes();
		String content = new String(data);

		List<Rectangle> contentsRectangle = parseContentsRectangles(content);
		for (Rectangle rect : contentsRectangle) {
			drawRectangleOnPdf(pdfDoc, rect, ColorConstants.RED);
		}

		pdfDoc.close();
	}

	public void getAllPdfObjectsAsStream() throws IOException {
		long startTime = System.currentTimeMillis();
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC_HELLO));

		for (int i = 1; i <= pdfDoc.getNumberOfPdfObjects(); i++) {
			PdfObject obj = pdfDoc.getPdfObject(i);
			if (obj != null) {

				System.out.println(obj);
				if (obj.isDictionary() && obj != null) {
					PdfDictionary dicLevel1 = ((PdfDictionary) obj);
					for (PdfName key : dicLevel1.keySet()) {
						PdfObject value = dicLevel1.get(key);
						System.out.println("\t" + "key: " + key + " value:" + value);
						if (key.toString().equals("/Contents") && value.isStream()) {
							PdfStream stream = ((PdfStream) value);

//							byte[] b;
//							try {
//								b = stream.getBytes();
//							} catch (Exception e) {
//								b = stream.getBytes(true);
//							}
//							FileOutputStream fos = new FileOutputStream(String.format(DEST, i));
//							fos.write(b);
//							fos.flush();
//							fos.close();

							System.out.println("\t\t stream: " + stream.getBytes());
							String s = new String(stream.getBytes());
							System.out.println("bytes.toString():" + s);

							// Get just tj (actual text) part from stream.getBytes();
							ParseActualTextFromStreamBytes(stream);

							Pattern pattern = Pattern.compile("\\(.*\\)Tj", Pattern.MULTILINE);

							Matcher matcher = pattern.matcher(s);
							int matches = 0;
							while (matcher.find()) {
								matches++;
								System.out.println(matcher.group(0));
							}
							System.out.println("matches count: " + matches);
						}
					}
				}
			}
		}
		pdfDoc.close();
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("elapsedTime: " + elapsedTime + " ms");
	}

	public static void changeContentByLocation(String SRC) throws IOException, GeneralSecurityException {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

		// PdfDocument pdfDoc = pdfDocument;
//		PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC_RESUME), new PdfWriter(OUT_PDF));

		Document doc = new Document(pdfDoc);
//		PageSize ps = pdfDoc.getDefaultPageSize();

		Rectangle rect;
		float transformedFontSize = 0;

		float x1 = 0;
		float x2 = 0;
		float y1 = 0;
		float y2 = 0;

		// First clean all texts by location
		for (TextRenderInfo item : renderInfos) {
			TextRenderInfo textRenderInfo = item;

			System.out.println("realText:" + textRenderInfo.getText());
			x1 = textRenderInfo.getBaseline().getStartPoint().get(Vector.I1);
			x2 = textRenderInfo.getBaseline().getEndPoint().get(Vector.I1);
			y1 = textRenderInfo.getBaseline().getStartPoint().get(Vector.I2);
			y2 = textRenderInfo.getBaseline().getEndPoint().get(Vector.I2);

			// real Font size calculation
			CanvasGraphicsState canvasGs = item.getGraphicsState();
			Matrix textToUserSpaceTransformMatrix = canvasGs.getCtm();
			transformedFontSize = new Vector(0, canvasGs.getFontSize(), 0).cross(textToUserSpaceTransformMatrix)
					.length();

			rect = new Rectangle(x1, y1 + 1, x2, transformedFontSize);

			System.out.println("x1:" + x1 + " y1:" + y1 + ", x2:" + x2 + ", fontSize:" + transformedFontSize);
			System.out.println("pageHeight:" + pdfDoc.getFirstPage().getPageSize().getHeight());

			// first clean the content
			List cleanUpLocations = new ArrayList();
			cleanUpLocations.add(new PdfCleanUpLocation(1, rect));
			PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, cleanUpLocations);
			cleaner.cleanUp();
		}

		// Then fill with new content
		for (TextRenderInfo item : renderInfos) {
			x1 = item.getBaseline().getStartPoint().get(Vector.I1);
			x2 = item.getBaseline().getEndPoint().get(Vector.I1);
			y1 = item.getBaseline().getStartPoint().get(Vector.I2);
			y2 = item.getBaseline().getEndPoint().get(Vector.I2);

			float charWidth = (x2 - x1) / item.getText().length();
			float wordWidth = x2 - x1;
			System.out.println("charWidth:" + charWidth + ", wordWidth:" + wordWidth);

			// real Font size calculation
			CanvasGraphicsState canvasGs = item.getGraphicsState();
			Matrix textToUserSpaceTransformMatrix = canvasGs.getCtm();
			transformedFontSize = new Vector(0, canvasGs.getFontSize(), 0).cross(textToUserSpaceTransformMatrix)
					.length();

			rect = new Rectangle(x1, y1, x2, transformedFontSize);

			// Create paragraph
			String newText = newContent.get(item.getText().toLowerCase());
			// rectangle's x2 value
			float newTextWidth = charWidth * newText.length() + 0.5f;
			Paragraph paragraph = new Paragraph(newText);

			// Font splitting by 'camel case' and '-'
			String[] dashSplittedFontName = item.getFont().getFontProgram().getFontNames().getFontName().split("-");
			String[] camelCaseFontFamilySplitted = dashSplittedFontName[0]
					.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
			String fontFamily = camelCaseFontFamilySplitted[0] + " " + camelCaseFontFamilySplitted[1];
			String fontVariant = dashSplittedFontName[1];
			System.out.println("Font family:" + fontFamily + ", variant:" + fontVariant);

			// Creating fonts from StandardFonts in itext's library
//			PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);

			// Creating fonts from .ttf files
			FontProgram fontProgram = FontProgramFactory.createFont(ROBOTO_REGULAR);
			PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.WINANSI, true);

			paragraph.setFontSize(transformedFontSize);
			// sets leading equal to fontsize
			paragraph.setMultipliedLeading(0.6f);
			paragraph.setFontColor(item.getStrokeColor());
			paragraph.setFont(font);
			paragraph.setFixedPosition(x1, y1, newTextWidth);
			paragraph.setBorder(new DottedBorder(0.3f));

			doc.add(paragraph);
			item.releaseGraphicsState();
		}
		doc.close();
	}

	private void ParseActualTextFromStreamBytes(PdfStream stream) throws FileNotFoundException, IOException {
		PdfTokenizer tokenizer = new PdfTokenizer(
				new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(stream.getBytes())));

		PrintWriter out = new PrintWriter(new FileOutputStream(DEST));

		while (tokenizer.nextToken()) {
			if (tokenizer.getTokenType() == PdfTokenizer.TokenType.String) {
				out.println(tokenizer.getStringValue());
			}
		}
		out.flush();
		out.close();
		tokenizer.close();
	}

	public void extractText(String src, String dest) throws IOException {
//		PrintWriter out = new PrintWriter(new FileOutputStream(dest));
//		PdfReader reader = new PdfReader(src);
//		RenderListener listener = new MyTextRenderListener(out);
//		PdfTextExtractor.
//		PdfDocumentContentParser processor = new PdfDocumentContentParser(listener);
////		PdfDictionary pageDic = reader.getPageN(1);
////		PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
//		processor.pr
//		processor.processContent(ContentByteUtils.getContentBytesForPage(reader, 1), resourcesDic);
//		out.flush();
//		out.close();
//		reader.close();
	}

	public void createDirs() {
//		File file = new File(DEST);
//		file.getParentFile().mkdirs();
	}

	private static HashMap<String, String> splitFontName(String baseFont) {
		HashMap<String, String> fontMap = new HashMap<String, String>();

		// Font splitting by 'camel case' and '-'
		String[] dashSplittedFontName = baseFont.split("-");
		String fontFamily = "";
		String fontVariant = "";
		if (dashSplittedFontName.length > 1) {
			String[] camelCaseFontFamilySplitted = dashSplittedFontName[0].replace("/", "")
					.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
			if (camelCaseFontFamilySplitted.length > 1) {
				for (String item : camelCaseFontFamilySplitted) {
					fontFamily += item + " ";
				}
				fontFamily = fontFamily.trim();
			} else {
				fontFamily = camelCaseFontFamilySplitted[0];
			}
			fontVariant = dashSplittedFontName[1];
		} else {
			fontFamily = dashSplittedFontName[0];
		}

		fontMap.put("Family", fontFamily);
		fontMap.put("Variant", fontVariant);

		return fontMap;
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
				List<PdfObject> fontObjects = getFontsList(SRC_RESUME);

				// Downloaded fonts
				List<PdfFont> downloadedFonts = new ArrayList<>();
//				int countDownloadedFonts = 0;
				// Loop through fontObjects and download every font from google web font api
				for (PdfObject fontObj : fontObjects) {
					PdfDictionary fontDict = (PdfDictionary) fontObj;
					// Split originial pdf font name to be able to download from google api
					// Google font names are like: fontFamily: Roboto Condensed, fontVariant:
					// regular
					HashMap<String, String> font = splitFontName(fontDict.get(PdfName.BaseFont).toString());
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

	public static void extractTextFromRectArea(String SRC, Rectangle rect) throws IOException {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));
		StringBuilder strBuilder = new StringBuilder();
		CustomTextRegionEventFilter regionFilter = new CustomTextRegionEventFilter(rect);
		FilteredEventListener listener = new FilteredEventListener();

		LocationTextExtractionStrategy extractionStrategy = listener
				.attachEventListener(new LocationTextExtractionStrategy(), regionFilter);

		new PdfCanvasProcessor(listener).processPageContent(pdfDoc.getFirstPage());
		String actualText = extractionStrategy.getResultantText();
		System.out.println("actualText: " + actualText);

		pdfDoc.close();
	}
}
