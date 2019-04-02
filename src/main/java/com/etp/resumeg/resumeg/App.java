package com.etp.resumeg.resumeg;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.sasl.AuthenticationException;

import org.apache.log4j.BasicConfigurator;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.webfonts.Webfonts;
import com.google.api.services.webfonts.Webfonts.Builder;
import com.google.api.services.webfonts.Webfonts.WebfontsOperations;
import com.google.api.services.webfonts.WebfontsRequest;
import com.google.api.services.webfonts.WebfontsRequestInitializer;
import com.google.api.services.webfonts.model.Webfont;
import com.google.api.services.webfonts.model.WebfontList;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
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
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.ParagraphRenderer;
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
//	public static final String SRC_RESUME = "pdf/input/Resume.pdf";
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
		new App().manipulatePdf();
//		new App().replaceContentStream(SRC_RESUME, OUT_PDF);

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

	public void replaceContentStream(String src, String dest) throws IOException {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
		PdfPage firstPage = pdfDoc.getFirstPage();
		PdfDictionary dict = firstPage.getPdfObject();
		PdfObject object = dict.get(PdfName.Contents);

		if (object instanceof PdfStream) {
			PdfStream stream = (PdfStream) object;
			byte[] data = stream.getBytes();
			System.out.println(new String(data));
			stream.setData(new String(data).replace("<027A>", "(HELLO WORLD)").getBytes("UTF-8"));
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

	protected void manipulatePdf() throws Exception {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC_RESUME), new PdfWriter(OUT_PDF));
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

		int i = 0;

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

//			System.out.println("Font:");
//			for (String w : item.getFont().getFontProgram().getFontNames().getFontName().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
//		        System.out.println(w);
//		    }
//			String[] fontCamelCaseSplit = item.getFont().getFontProgram().getFontNames().getFontName().replace("/([a-z])([A-Z])/g", "$1 $2");
//			String fontFamily = 
//			System.out.println("fontFamily:" + item.getFont().getFontProgram().getFontNames().getFullName()[0][3]);
			String[] dashSplittedFontName = item.getFont().getFontProgram().getFontNames().getFontName().split("-");
			String[] camelCaseFontFamilySplitted = dashSplittedFontName[0]
					.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
			String fontFamily = camelCaseFontFamilySplitted[0] + " " + camelCaseFontFamilySplitted[1];
			String fontVariant = dashSplittedFontName[1];
			System.out.println("Font family:" + fontFamily + ", variant:" + fontVariant);

			// Creating fonts from StandardFonts in itext's library
//				PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);

			// Creating fonts from .ttf files
			FontProgram fontProgram = FontProgramFactory.createFont(ROBOTO_REGULAR);
			PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.WINANSI, true);

			paragraph.setFontSize(transformedFontSize);
			// sets leading equal to fontsize
			paragraph.setMultipliedLeading(0.6f);
			paragraph.setFontColor(item.getStrokeColor());
			paragraph.setFont(font);
//				paragraph.setFont(actualRenderInfo.getFont());
			paragraph.setFixedPosition(x1, y1, newTextWidth);
			paragraph.setBorder(new DottedBorder(0.3f));

			doc.add(paragraph);
			item.releaseGraphicsState();

			testGoogleWebFonts(fontFamily, fontVariant);
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

	public static void testGoogleWebFonts(String fontFamily, String fontVariant) throws IOException {
		// Creating fonts from google's webfonts api
		System.out.println("\nGoogle fonts:");
		if (webFontService == null) {
			webFontService = new GoogleWebFontService();
		}

		System.out.println("FontProgram:" + webFontService.downloadFontByFamily(fontFamily, fontVariant));
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
