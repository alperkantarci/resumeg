package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.colors.*;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.*;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;

import java.lang.reflect.Field;

public class TextItem extends MyItem {

    /**
     * Position of the baseline of the text.
     */
    private float baseline;

    public TextItem(TextRenderInfo textRenderInfo, float pageHeight) {
        this.pageHeight = pageHeight;
        this.textRenderInfo = textRenderInfo;
        baseline = textRenderInfo.getBaseline().getStartPoint().get(1);
        realRectangle = getRectangle(textRenderInfo);
        drawableRectangle = getDrawableRectangle();
        text = textRenderInfo.getText();
        fontSize = getFontSize(textRenderInfo);
        font = getFont(textRenderInfo);
    }

    @Override
    public Point getLL() {
        return new Point(getRealRectangle().getLeft(), baseline);
    }

    static float getFontSize(TextRenderInfo textRenderInfo) {
        Matrix textToUserSpaceTransformMatrix = textRenderInfo.getGraphicsState().getCtm();
        float fontSize = new Vector(0, textRenderInfo.getGraphicsState().getFontSize(), 0).cross(textToUserSpaceTransformMatrix)
                .length();
        return fontSize;
    }

    static PdfFont getFont(TextRenderInfo textRenderInfo) {
        return textRenderInfo.getFont();
    }

    static Rectangle getRectangle(TextRenderInfo textRenderInfo) {
//        System.out.println("textItemText:" + textRenderInfo.getText());
        LineSegment descentLine = textRenderInfo.getDescentLine();
        LineSegment ascentLine = textRenderInfo.getAscentLine();
        float x0 = descentLine.getStartPoint().get(0);
        float x1 = descentLine.getEndPoint().get(0);
//        float y0 = descentLine.getStartPoint().get(1);
        float y0 = textRenderInfo.getBaseline().getStartPoint().get(Vector.I2);
        float y1 = ascentLine.getEndPoint().get(1);
        return new Rectangle(x0, y0, x1 - x0, y1);
    }

    public Rectangle getDrawableRectangle() {
        if (realRectangle != null) {
            float x0 = getRealRectangle().getLeft();
            float y0 = pageHeight - getRealRectangle().getBottom() - getFontSize();
            float width = getRealRectangle().getRight() - getRealRectangle().getLeft();

            Rectangle drawableRect = new Rectangle(x0, y0, width, getFontSize());
            return drawableRect;
        }
        return null;
    }

}
