package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.geom.Rectangle;

import java.util.List;

public class Line extends MyItem {

    private List<MyItem> items;
    private String text;

    /**
     * Creates a Line object based on a list of items that have the same
     * offset of their baseline.
     *
     * @param items a list of MyItem objects
     */
    public Line(List<MyItem> items) {
        super();
        this.items = items;
        realRectangle = getItemsRect(items);
        drawableRectangle = getDrawableRectangle();
        text = getItemsText(items);
        fontSize = getItemFontSize(items);
        pageHeight = items.get(0).getPageHeight();
    }

    public List<MyItem> getItems() {
        return items;
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

    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Creates a realRectangle that encompasses all the coordinate rectangles
     * of the items that belong to this line.
     *
     * @param items the items that belong to a line
     * @return a realRectangle that encompasses all items belonging to a line
     */
    private static Rectangle getItemsRect(List<MyItem> items) {
        float left = Float.MAX_VALUE;
        float right = 0;
        float top = 0;
        float bottom = Float.MAX_VALUE;
        for (MyItem item : items) {
//            System.out.println(item.getText() + "-> left:" + item.getRealRectangle().getLeft() + ", right:" + item.getRealRectangle().getRight()
//            + ", top:" + item.getRealRectangle().getTop());
            if (item.getRealRectangle().getLeft() < left)
                left = item.getRealRectangle().getLeft();
            if (item.getRealRectangle().getRight() > right)
                right = item.getRealRectangle().getRight();
            if (item.getRealRectangle().getTop() > top)
                top = item.getRealRectangle().getTop();
            if (item.getRealRectangle().getBottom() < bottom)
                bottom = item.getRealRectangle().getBottom();
        }
//        System.out.println();
        return new Rectangle(left, bottom, right - left, top);
    }

    /**
     * Creates a realRectangle that encompasses all the coordinate rectangles
     * of the items that belong to this line.
     *
     * @param items the items that belong to a line
     * @return a realRectangle that encompasses all items belonging to a line
     */
    private static String getItemsText(List<MyItem> items) {
//        System.out.println("line.getItems():" + items.size());
        StringBuilder lineText = new StringBuilder();
        for (MyItem item : items) {
            lineText.append(item.getText());
        }
//        System.out.println("lineText:" + lineText);
        return lineText.toString();
    }

    private static float getItemFontSize(List<MyItem> items) {
        return items.get(0).getFontSize();
    }
}
