package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
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

    protected static String getFontColor(TextRenderInfo textRenderInfo){
        String fontColor = null;
        if (textRenderInfo.getFillColor() instanceof DeviceRgb) {
            int r = Math.round(textRenderInfo.getFillColor().getColorValue()[0]*255);
            int g = Math.round(textRenderInfo.getFillColor().getColorValue()[1]*255);
            int b = Math.round(textRenderInfo.getFillColor().getColorValue()[2]*255);

            String hex = String.format("#%02x%02x%02x", r, g, b);

//            System.out.println("(DeviceRgb) r: " + r + ", g: " + g + ", b: " + b + ", hex: " + hex);
            fontColor = "(DeviceRgb) r: " + r + ", g: " + g + ", b: " + b + ", hex: " + hex;
        } else if (textRenderInfo.getFillColor() instanceof DeviceGray) {
            int colorValue = Math.round(textRenderInfo.getFillColor().getColorValue()[0]*255);

//            System.out.println("(DeviceGray) colorValue " + colorValue);
            fontColor = "(DeviceGray) colorValue " + colorValue;
        } else if(textRenderInfo.getFillColor() instanceof DeviceCmyk){
            int c = Math.round(textRenderInfo.getFillColor().getColorValue()[0]*255);
            int m = Math.round(textRenderInfo.getFillColor().getColorValue()[1]*255);
            int y = Math.round(textRenderInfo.getFillColor().getColorValue()[2]*255);
            int k = Math.round(textRenderInfo.getFillColor().getColorValue()[3]*255);

            String hex = String.format("#%02x%02x%02x%02x", c, m, y, k);
//            System.out.println("(DeviceCmyk) c: " + c + ", m: " + m + ", y: " + y + ", k: " + k + ", hex: " + hex);
            fontColor = "(DeviceCmyk) c: " + c + ", m: " + m + ", y: " + y + ", k: " + k + ", hex: " + hex;
        }
        return fontColor;
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
