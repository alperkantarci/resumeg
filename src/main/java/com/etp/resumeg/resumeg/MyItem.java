package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;

public class MyItem implements Comparable<MyItem>  {

    /**
     * If we want to compare item positions we should add some tolerance.
     */
    public static final float itemPositionTolerance = 3f;
//    public static final float itemPositionTolerance = 70f;

    protected float pageHeight;

    /** Rectangle that defines the original coordinates of an item. */
    protected Rectangle realRectangle;
    /** Rectangle that defines the drawing coordinates of an item. */
    protected Rectangle drawableRectangle;

    protected String text;
    protected float fontSize;
    protected PdfFont font;
    protected TextRenderInfo textRenderInfo;

    public MyItem() {
    }

    public float getPageHeight() {
        return pageHeight;
    }

    public String getText() {
        return text;
    }

    public PdfFont getFont() {
        return font;
    }

    public TextRenderInfo getTextRenderInfo() {
        return textRenderInfo;
    }

    public Rectangle getRealRectangle() {
        return realRectangle;
    }

    public float getFontSize(){
        return fontSize;
    }

    public Rectangle getDrawableRectangle() {
        return drawableRectangle;
    }

    /**
     * Gets the lower left corner of the item.
     * For image items it returns lower left corner of bounding box.
     * For text items it returns start point of a baseline.
     * @return point of the lower left corner.
     */
    public Point getLL() {
        return new Point(getRealRectangle().getLeft(), getRealRectangle().getBottom());
    }

    @Override
    public int compareTo(MyItem o) {
        double left = getLL().getX();
        double bottom = getLL().getY();
        double oLeft = o.getLL().getX();
        double oBottom = o.getLL().getY();
        if (bottom - oBottom > itemPositionTolerance)
            return -1;
        else if (oBottom - bottom > itemPositionTolerance)
            return 1;
        else
            return Double.compare(left, oLeft);
    }
}
