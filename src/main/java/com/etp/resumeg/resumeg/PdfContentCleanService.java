package com.etp.resumeg.resumeg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.pdfcleanup.PdfCleanUpTool;

public class PdfContentCleanService {

	public static void cleanContentByLocation(PdfDocument pdfDoc, List<Rectangle> rectLocations) throws IOException {
		// Clean content
		List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<>();

		for (Rectangle rect : rectLocations) {
			cleanUpLocations.add(new PdfCleanUpLocation(1, rect));
		}

		PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, cleanUpLocations);
		cleaner.cleanUp();

		pdfDoc.close();
	}

	public static void cleanContentByLocation(PdfDocument pdfDoc, Rectangle rectLocation, boolean closePdfDoc) throws IOException {
		// Clean content
		List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<>();
		cleanUpLocations.add(new PdfCleanUpLocation(1, rectLocation));
		PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, cleanUpLocations);
		cleaner.cleanUp();

		if(closePdfDoc)
		pdfDoc.close();
	}

	public static void cleanContentByLocation(String SRC, String OUT, Rectangle rectLocation) throws IOException {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(OUT));

		// Clean content
		List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<>();
		cleanUpLocations.add(new PdfCleanUpLocation(1, rectLocation));
		PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, cleanUpLocations);
		cleaner.cleanUp();

		pdfDoc.close();
	}

	public static void cleanContentByLocation(String SRC, String OUT, List<Rectangle> rectLocations) throws IOException {
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(OUT));

		// Clean content
		List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<>();
		
		for (Rectangle rect : rectLocations) {
			cleanUpLocations.add(new PdfCleanUpLocation(1, rect));
		}
		
		PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, cleanUpLocations);
		cleaner.cleanUp();

		pdfDoc.close();
	}

}
