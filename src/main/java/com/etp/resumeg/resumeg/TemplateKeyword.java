package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;

public class TemplateKeyword {

    private CanvasGraphicsState graphicsState;
    private PdfFont font;
    private String text;
    private Float realFontSize = null;

    // locations
    private float left;
    private float bottom;
    private float width;

    public TemplateKeyword() {
    }

    @Override
    public String toString() {
        String fontName = font != null ? font.getFontProgram().getFontNames().getFontName() : "not found";
        return "txt:" + text + "(fnt:" + fontName + ")" + " => left:" + left + ", bottom:" + bottom + ", width:" + width;
    }

    public Float getRealFontSize() {
        if(realFontSize == null){
            // real Font size calculation
            Matrix textToUserSpaceTransformMatrix = getGraphicsState().getCtm();
            realFontSize = new Vector(0, getGraphicsState().getFontSize(), 0).cross(textToUserSpaceTransformMatrix)
                    .length();
        }

        return realFontSize;
    }

    public CanvasGraphicsState getGraphicsState() {
        return graphicsState;
    }

    public void setGraphicsState(CanvasGraphicsState graphicsState) {
        this.graphicsState = graphicsState;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public PdfFont getFont() {
        return font;
    }

    public void setFont(PdfFont font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
