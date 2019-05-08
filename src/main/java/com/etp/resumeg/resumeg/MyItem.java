package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;

public class MyItem implements Comparable<MyItem>  {

    /**
     * If we want to compare item positions we should add some tolerance.
     */
    public static final float itemPositionTolerance = 3f;

    /** Rectangle that defines the coordinates of an item. */
    protected Rectangle rectangle;

    protected String text;
    protected float fontSize;

    public MyItem() {
    }

    public String getText() {
        return text;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public float getFontSize(){
        return fontSize;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Gets the lower left corner of the item.
     * For image items it returns lower left corner of bounding box.
     * For text items it returns start point of a baseline.
     * @return point of the lower left corner.
     */
    public Point getLL() {
        return new Point(getRectangle().getLeft(), getRectangle().getBottom());
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
