package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.geom.Rectangle;

import java.util.List;

public class Line extends MyItem {

    private String text;

    /**
     * Creates a Line object based on a list of items that have the same
     * offset of their baseline.
     *
     * @param items a list of MyItem objects
     */
    public Line(List<MyItem> items) {
        super();
        rectangle = getItemsRect(items);
        text = getItemsText(items);
        fontSize = getItemFontSize(items);
    }

    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Creates a rectangle that encompasses all the coordinate rectangles
     * of the items that belong to this line.
     *
     * @param items the items that belong to a line
     * @return a rectangle that encompasses all items belonging to a line
     */
    private static Rectangle getItemsRect(List<MyItem> items) {
        float left = Float.MAX_VALUE;
        float right = 0;
        float top = 0;
        float bottom = Float.MAX_VALUE;
        for (MyItem item : items) {
            System.out.println(item.getText() + "->" + item.getRectangle().getLeft() + ", " + item.getRectangle().getRight()
            + ", " + item.getRectangle().getHeight());
            if (item.getRectangle().getLeft() < left)
                left = item.getRectangle().getLeft();
            if (item.getRectangle().getRight() > right)
                right = item.getRectangle().getRight();
            if (item.getRectangle().getTop() > top)
                top = item.getRectangle().getTop();
            if (item.getRectangle().getBottom() < bottom)
                bottom = item.getRectangle().getBottom();
        }
        System.out.println();
        return new Rectangle(left, bottom, right - left, top);
    }

    /**
     * Creates a rectangle that encompasses all the coordinate rectangles
     * of the items that belong to this line.
     *
     * @param items the items that belong to a line
     * @return a rectangle that encompasses all items belonging to a line
     */
    private static String getItemsText(List<MyItem> items) {
        StringBuilder lineText = new StringBuilder();
        for (MyItem item : items) {
            lineText.append(item.getText());
        }
        return lineText.toString();
    }

    private static float getItemFontSize(List<MyItem> items) {
        return items.get(0).getFontSize();
    }
}
