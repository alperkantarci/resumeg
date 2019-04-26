package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.font.PdfFont;

public class TemplateKeyword {

    private PdfFont font;
    private String text;

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
