package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.geom.Rectangle;

import java.util.List;

public class Structure extends Line {
    /**
     * Creates a Structure object based on a list of lines that belong
     * together in the same structure.
     *
     * @param items a list of MyItem objects
     */
    public Structure(List<MyItem> items) {
        super(items);
    }

    @Override
    public Rectangle getDrawableRectangle() {
        Line firstLine = (Line) getItems().get(0);
        Line lastLine = (Line) getItems().get(getItems().size() - 1);

        float structureHeight;
        if (getItems().size() > 1) {
            structureHeight = firstLine.getRealRectangle().getBottom() - lastLine.getRealRectangle().getBottom() + getItemFontSize();
        } else {
            structureHeight = getItemFontSize();
        }

        float structureLeft = firstLine.getRealRectangle().getLeft();
        float structureBottom = lastLine.getRealRectangle().getBottom();
        float y0 = pageHeight - structureBottom - structureHeight;
        float structureWidth = getRealRectangle().getRight() - getRealRectangle().getLeft();

        Rectangle realRectangle = new Rectangle(structureLeft, y0, structureWidth, structureHeight);
        return realRectangle;
    }
}