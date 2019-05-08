package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class PdfDrawService {

    protected static void drawRectangles(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfPage firstPage = pdfDoc.getFirstPage();
        PdfDictionary dict = firstPage.getPdfObject();
        PdfObject object = dict.get(PdfName.Contents);

        PdfStream stream = (PdfStream) object;
        byte[] data = stream.getBytes();
        String content = new String(data);

        List<Rectangle> contentsRectangle = ParseService.parseContentsRectangles(content);
        for (Rectangle rect : contentsRectangle) {
            drawRectangleOnPdf(pdfDoc, rect, ColorConstants.RED);
        }

        pdfDoc.close();
    }


    /**
     * @param pdfDoc PdfDocument object that you are working on
     * @param rect  Rectangle object that you want to draw
     * @param color Color (com.itextpdf.kernel.colors) object, color of rectangle's border, ex: ColorConstants.RED
     */
    protected static void drawRectangleOnPdf(PdfDocument pdfDoc, Rectangle rect, Color color) {
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamAfter(),
                pdfDoc.getFirstPage().getResources(), pdfDoc);
        canvas.saveState();
        canvas.rectangle(rect);
        canvas.setStrokeColor(color);
        canvas.stroke();
        canvas.restoreState();
    }
}
