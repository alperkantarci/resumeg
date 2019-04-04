package com.etp.resumeg.resumeg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.PageSize;
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
	public static final String SRC_HELLO = "pdf/input/hello.pdf";
	public static final String SRC_TEST = "/home/alperk/Downloads/dddd.pdf";
	public static final String SRC_RESUME = "pdf/input/Resume2.pdf";
	public static final String DEST = "pdf/output/parsedStream";
	public static final String OUT_PDF = "pdf/output/testOutput.pdf";
	// Font resources
	public static final String ROBOTO_REGULAR = "resources/fonts/RobotoCondensed-Regular.ttf";
	// New content for pdf
	public static final HashMap<String, String> newContent = new HashMap<String, String>();
	// Font
	public static List<TextRenderInfo> renderInfos = new ArrayList<TextRenderInfo>();
	// GoogleWebFonts
	public static GoogleWebFontService webFontService = null;

	public static void main(String[] args) throws Exception {
//		new App().createDirs();
//		new App().helloWorldExample();
//		new App().parsePdf();
//		new App().getAllPdfObjectsAsStream();

		BasicConfigurator.configure();
		App app = new App();
//		app.manipulatePdf(SRC_RESUME);
		app.drawRectangles(SRC_RESUME, OUT_PDF);
		App.testGoogleWebFonts("Roboto Condensed", "Regular");

//		app.listFonts(SRC_RESUME);

	}

	private void parsePdf() throws IOException {
		PdfReader pdfReader = new PdfReader(SRC_HELLO);
		PdfDocument pdfDoc = new PdfDocument(pdfReader);
//		byte[] streamBytes = pdfDoc.getPage(1).getContentBytes();
//		ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();	
		long startTime = System.currentTimeMillis();
		String currentText = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(1));
		System.out.println(currentText);
		pdfDoc.close();
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("elapsedTime: " + elapsedTime + " ms");
	}

	public void helloWorldExample() throws FileNotFoundException {
//		new App().createDirs();
//		PdfDocument pdf = new PdfDocument(new PdfWriter(DEST));
//		Document document = new Document(pdf);
//		document.add(new Paragraph("Hello World!"));
//		document.add(new Paragraph("Paragraph2"));
//		document.close();
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

	public static PdfDictionary getFontsDictionary(String src) throws IOException {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
		PdfPage firstPage = pdfDoc.getFirstPage();
		PdfResources pdfResources = firstPage.getResources();
		PdfDictionary fontDict = pdfResources.getResource(PdfName.Font);
		return fontDict;

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

	public void drawRectangleOnPdf(PdfDocument pdfDoc, Rectangle rect) {
		PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamAfter(),
				pdfDoc.getFirstPage().getResources(), pdfDoc);
		canvas.saveState();

		canvas.rectangle(new Rectangle(rect));
		canvas.setStrokeColor(ColorConstants.RED);
		canvas.stroke();
		canvas.restoreState();
	}

	/**
	 * @param reText contains "0 0 100 50 re" string from /Contents
	 * @return
	 */
	public Rectangle convertReTagTextToRectangle(String reText) {
		String splittedReLine[] = reText.split(" ");
		// reText example: "0 0 100 200"
		float x1 = Float.valueOf(splittedReLine[0]);
		float y1 = Float.valueOf(splittedReLine[1]);
		float x2 = Float.valueOf(splittedReLine[2]);
		float y2 = Float.valueOf(splittedReLine[3]);
		return new Rectangle(x1, y1, x2, y2);
	}

	public List<Rectangle> parseContentsRectangles(String content) {
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
			drawRectangleOnPdf(pdfDoc, rect);
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

	protected void manipulatePdf(String SRC) throws Exception {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(OUT_PDF));
//		System.out.println("pageWidth:" + pdfDoc.getFirstPage().getPageSize().getWidth());
//		System.out.println("pageHeight:" + pdfDoc.getFirstPage().getPageSize().getHeight());

		PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamAfter(),
				pdfDoc.getFirstPage().getResources(), pdfDoc);
		canvas.saveState();
//		canvas.rectangle(36, 786, 500, 800);
//		canvas.rectangle(43.5, 122.25, 358.5, 561.75);
//		canvas.rectangle(43.5, 122.25, 358.5, 60.75);
//		canvas.rectangle(43.5, 42, 358.5, 77.25);
//		canvas.rectangle(43.195312f, 66.75f, 45.328312f, 1f);
//		canvas.rectangle(89.25f, 792 - 232.5f - 16f, 54f, 16f);

		// fixed relative sizes (y = pageHeight - actualY - actualFontSize)
//		canvas.rectangle(89.25f, 792 - 114f - 16f, 42f, 16f);

		// Stamping content

//		 "EDUCATION" rect, Resume2.pdf
		extractTextFromRectArea(pdfDoc, new Rectangle(89.25f, 232.5f, 54f, 16f), canvas);

////		 "Name" rect, Resume.pdf
//		extractTextFromRectArea(SRC_RESUME, new Rectangle(145.2675f, 708.0f, 116f, 48f), canvas);

//		// "Your" rect, Resume.pdf
//		extractTextFromRectArea(SRC_RESUME, new Rectangle(50.695f, 708.0f, 92f, 48f), canvas);

//		// "Your Name" rect, Resume.pdf
//		extractTextFromRectArea(SRC_RESUME, new Rectangle(50.695f, 708.0f, 185f, 48f), canvas);

		// "Your" rect, Resume.pdf
//		extractTextFromRectArea(SRC_RESUME, new Rectangle(50.695312f, 708.0f, 74.13132f, 48.0f), canvas);

		// Clean content by rectangle (needed jars: cleanup-2.0.3.jar (itext7 pdfSweep
		// addon), commons-imaging-1.0a1.kar)
//				List cleanUpLocations = new ArrayList();
//				cleanUpLocations.add(new PdfCleanUpLocation(1, new Rectangle(89.25f, 792 - 114f - 16f, 42f, 16f)));
//				PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, cleanUpLocations);
//				cleaner.cleanUp();

		canvas.setStrokeColor(ColorConstants.RED);
		canvas.stroke();
		canvas.restoreState();

		changeContentByLocation(pdfDoc);

		pdfDoc.close();
	}

	public static void changeContentByLocation(PdfDocument pdfDocument) throws IOException, GeneralSecurityException {
		PdfDocument pdfDoc = pdfDocument;
//		PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC_RESUME), new PdfWriter(OUT_PDF));

		Document doc = new Document(pdfDoc);
		PageSize ps = pdfDoc.getDefaultPageSize();

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
//			paragraph.setFont(actualRenderInfo.getFont());
			paragraph.setFixedPosition(x1, y1, newTextWidth);
			paragraph.setBorder(new DottedBorder(0.3f));

			doc.add(paragraph);
			item.releaseGraphicsState();

//			testGoogleWebFonts(fontFamily, fontVariant);
		}
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

	public static HashMap<String, String> splitFontName(String baseFont) {
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

	public static void testGoogleWebFonts(String fontFamily, String fontVariant) throws IOException {
		// Creating fonts from google's webfonts api
		System.out.println("\nGoogle fonts:");
		if (webFontService == null) {
			webFontService = new GoogleWebFontService();
		}

		PdfDictionary fontsDict = getFontsDictionary(SRC_RESUME);
//		Iterator it = fontsDict.entrySet().iterator();
//		while(it.hasNext()) {
//			PdfDictionary fontDict = (PdfDictionary) it.next();
//		}

		List<PdfObject> fontObjects = new ArrayList<>();
		for (Entry<PdfName, PdfObject> font : fontsDict.entrySet()) {
			fontObjects.add(font.getValue());
		}
		for (PdfObject fontObj : fontObjects) {
			PdfDictionary fontDict = (PdfDictionary) fontObj;
			HashMap<String, String> fontMap = splitFontName(fontDict.get(PdfName.BaseFont).toString());
//			System.out.println(fontMap.get("Family") + "-" + fontMap.get("Variant"));
			PdfFont font = webFontService.downloadFontByFamily(fontMap.get("Family"), fontMap.get("Variant"));
//			for (Entry<PdfName, PdfObject> item : fontDict.entrySet()) {
//				System.out.println(item.getKey() + ", val:" + item.getValue());
//			}
		}

//		System.out.println("PdfFont:" + webFontService.downloadFontByFamily(fontFamily, fontVariant));
	}

	public void extractTextFromRectArea(PdfDocument pdfDocument, Rectangle rect, PdfCanvas canvas) throws IOException {
		PdfDocument pdfDoc = pdfDocument;
		// PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC_RESUME), new
		// PdfWriter(OUT_PDF));
		StringBuilder strBuilder = new StringBuilder();
		CustomTextRegionEventFilter regionFilter = new CustomTextRegionEventFilter(rect, canvas, strBuilder, pdfDoc,
				renderInfos);
		CustomFilteredEventListener listener = new CustomFilteredEventListener();

		LocationTextExtractionStrategy extractionStrategy = listener
				.attachEventListener(new LocationTextExtractionStrategy(), regionFilter);

//		canvas.fill();

		new PdfCanvasProcessor(listener).processPageContent(pdfDoc.getFirstPage());
		String actualText = extractionStrategy.getResultantText();
		System.out.println("actualText: " + actualText);

		System.out.println("strBuilder:" + strBuilder.toString());
		System.out.println("words.length:" + strBuilder.toString().split(" ").length);

		// print strBuilder which contain content line by line
//		System.out.println("strBuilder.splitted:");
//		for (String item : strBuilder.toString().split(" ")) {
//			System.out.println(item);
//		}

//		pdfDoc.close();
	}
}
