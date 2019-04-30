package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfFontService {

    protected void listFonts(String src) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        PdfPage firstPage = pdfDoc.getFirstPage();
        PdfResources pdfResources = firstPage.getResources();
        PdfDictionary fontDict = pdfResources.getResource(PdfName.Font);

        for (Map.Entry<PdfName, PdfObject> item : fontDict.entrySet()) {
            System.out.println(item.getKey() + ", " + item.getValue());
        }
        pdfDoc.close();
    }

    protected static List<PdfObject> getFontsList(String src) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        PdfPage firstPage = pdfDoc.getFirstPage();
        PdfResources pdfResources = firstPage.getResources();
        PdfDictionary fontsDict = pdfResources.getResource(PdfName.Font);

        List<PdfObject> fontObjects = new ArrayList<>();
        for (Map.Entry<PdfName, PdfObject> font : fontsDict.entrySet()) {
            fontObjects.add(font.getValue());
        }
        pdfDoc.close();
        return fontObjects;
    }

    protected static float calculateRealFontSize(CanvasGraphicsState canvasGraphicsState){
        // real Font size calculation
        Matrix textToUserSpaceTransformMatrix = canvasGraphicsState.getCtm();
        float transformedFontSize = new Vector(0, canvasGraphicsState.getFontSize(), 0).cross(textToUserSpaceTransformMatrix)
                .length();

        return transformedFontSize;
    }
}
