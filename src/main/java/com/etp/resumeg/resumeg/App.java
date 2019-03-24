package com.etp.resumeg.resumeg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class App {

//	public static final String DEST = "results/javaone16/hello1.pdf";
	public static final String SRC = "results/javaone16/hello1.pdf";
	public static final String DEST = "results/parse/stream%s";

	public static void main(String[] args) throws IOException {
//		new App().createDirs();
//		new App().helloWorldExample();
//		new App().parsePdf();
		new App().getAllPdfObjectsAsStream();

	}

	private void parsePdf() throws IOException {
		PdfReader pdfReader = new PdfReader(SRC);
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
		new App().createDirs();
		PdfDocument pdf = new PdfDocument(new PdfWriter(DEST));
		Document document = new Document(pdf);
		document.add(new Paragraph("Hello World!"));
		document.add(new Paragraph("Paragraph2"));
		document.close();
	}

	public void getAllPdfObjectsAsStream() throws IOException {
		long startTime = System.currentTimeMillis();
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

		for (int i = 1; i <= pdfDoc.getNumberOfPdfObjects(); i++) {
			PdfObject obj = pdfDoc.getPdfObject(i);
			if (obj != null) {

				System.out.println(obj);
				if (obj.isDictionary() && obj != null) {
//				System.out.println("dictionary obj");
					PdfDictionary dicLevel1 = ((PdfDictionary) obj);
					for (PdfName key : dicLevel1.keySet()) {
						PdfObject value = dicLevel1.get(key);
						System.out.println("\t" + "key: " + key + " value:" + value);
						if (key.toString().equals("/Contents") && value.isStream()) {
							PdfStream stream = ((PdfStream) value);

							byte[] b;
							try {
								b = stream.getBytes();
							} catch (Exception e) {
								b = stream.getBytes(true);
							}
							FileOutputStream fos = new FileOutputStream(String.format(DEST, i));
							fos.write(b);
							fos.flush();
							fos.close();

							System.out.println("\t\t stream: " + stream.getBytes());
							String s = new String(stream.getBytes());
							System.out.println("bytes.toString():" + s);
							
							
//							Pattern pattern = Pattern.compile("BT(.*?)ET", Pattern.MULTILINE);
							Pattern pattern = Pattern.compile("\\(.*\\)Tj", Pattern.MULTILINE);
							
							Matcher matcher = pattern.matcher(s);
							int matches= 0;
							while (matcher.find()) {
								matches++;
								 System.out.println(matcher.group(0));
							}
							System.out.println("matches count: " + matches);
//							System.out.println("regex match: " + s.matches("BT(.*?)ET"));
//						PdfTokenizer tokenizer = new PdfTokenizer();
//						PdfDictionary contentsDic = ((PdfDictionary) value);
//						for (PdfName key2 : contentsDic.keySet()) {
//							PdfObject value2 = contentsDic.get(key2);
//							System.out.println("\t" + "key: " + key2 + " value:" + value2);
//						}
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

	public void createDirs() {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
	}
}
