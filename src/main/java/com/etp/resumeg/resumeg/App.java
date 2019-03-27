package com.etp.resumeg.resumeg;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.pdfcleanup.PdfCleanUpTool;

public class App {

	public static final String SRC_HELLO = "pdf/input/hello.pdf";
//	public static final String SRC_RESUME = "pdf/input/Resume.pdf";
	public static final String SRC_RESUME = "pdf/input/Resume2.pdf";
	public static final String DEST = "pdf/output/parsedStream";
	public static final String OUT_PDF = "pdf/output/testOutput.pdf";

	public static void main(String[] args) throws Exception {
//		new App().createDirs();
//		new App().helloWorldExample();
//		new App().parsePdf();
//		new App().getAllPdfObjectsAsStream();

		new App().manipulatePdf();
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
		System.out.println("pageWidth:" + pdfDoc.getFirstPage().getPageSize().getWidth());
		System.out.println("pageHeight:" + pdfDoc.getFirstPage().getPageSize().getHeight());

		PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamAfter(),
				pdfDoc.getFirstPage().getResources(), pdfDoc);
		canvas.saveState();
//		canvas.rectangle(36, 786, 500, 800);
//		canvas.rectangle(43.5, 122.25, 358.5, 561.75);
//		canvas.rectangle(43.5, 122.25, 358.5, 60.75);
//		canvas.rectangle(43.5, 42, 358.5, 77.25);
//		canvas.rectangle(43.195312f, 66.75f, 45.328312f, 1f);
//		canvas.rectangle(89.25f, 792 - 232.5f - 16f, 54f, 16f);
		
		//fixed relative sizes (y = pageHeight - actualY - actualFontSize)
//		canvas.rectangle(89.25f, 792 - 114f - 16f, 42f, 16f);
		
		// Clean content by rectangle (needed jars: cleanup-2.0.3.jar (itext7 pdfSweep addon), commons-imaging-1.0a1.kar)
		List cleanUpLocations = new ArrayList();
		cleanUpLocations.add(new PdfCleanUpLocation(1, new Rectangle(89.25f, 792 - 114f - 16f, 42f, 16f)));
		PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, cleanUpLocations);
		cleaner.cleanUp();
		
		// Stamping content
		

//		 "EDUCATION" rect, Resume2.pdf
		extractTextFromRectArea(SRC_RESUME, new Rectangle(89.25f, 232.5f, 54f, 16f), canvas);

////		 "Name" rect, Resume.pdf
//		extractTextFromRectArea(SRC_RESUME, new Rectangle(145.2675f, 708.0f, 116f, 48f), canvas);

//		// "Your" rect, Resume.pdf
//		extractTextFromRectArea(SRC_RESUME, new Rectangle(50.695f, 708.0f, 92f, 48f), canvas);

//		// "Your Name" rect, Resume.pdf
//		extractTextFromRectArea(SRC_RESUME, new Rectangle(50.695f, 708.0f, 185f, 48f), canvas);

		// "Your" rect, Resume.pdf
//		extractTextFromRectArea(SRC_RESUME, new Rectangle(50.695312f, 708.0f, 74.13132f, 48.0f), canvas);

		canvas.setStrokeColor(ColorConstants.RED);
		canvas.stroke();
		canvas.restoreState();
		pdfDoc.close();
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

	public void extractTextFromRectArea(String src, Rectangle rect, PdfCanvas canvas) throws IOException {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
		StringBuilder strBuilder = new StringBuilder();
		CustomTextRegionEventFilter regionFilter = new CustomTextRegionEventFilter(rect, canvas, strBuilder);
		CustomFilteredEventListener listener = new CustomFilteredEventListener();

		LocationTextExtractionStrategy extractionStrategy = listener
				.attachEventListener(new LocationTextExtractionStrategy(), regionFilter);

//		System.out.println(PdfTextExtractor.getTextFromPage(pdfDoc.getFirstPage(), extractionStrategy));
//		new PdfCanvasProcessor(listener).processPageContent(pdfDoc.getFirstPage());

//		canvas.fill();

		new PdfCanvasProcessor(listener).processPageContent(pdfDoc.getFirstPage());
		String actualText = extractionStrategy.getResultantText();
		System.out.println("actualText: " + actualText);
		
		System.out.println("strBuilder:" + strBuilder.toString());
		System.out.println("words.length:" + strBuilder.toString().split(" ").length);
		
		System.out.println("strBuilder.splitted:");
		for(String item : strBuilder.toString().split(" ")) {
			System.out.println(item);
		}
		
		pdfDoc.close();
	}
}
