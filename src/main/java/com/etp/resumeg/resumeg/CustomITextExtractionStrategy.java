package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CustomITextExtractionStrategy implements ITextExtractionStrategy {

    /**
     * The resulting list of items after parsing.
     */
    List<MyItem> items = new ArrayList<MyItem>();

    // Keywords
    private static final List<String> keywords = new ArrayList<>();
    private PdfDocument pdfDoc;

    public CustomITextExtractionStrategy(PdfDocument pdfDocument) {
        pdfDoc = pdfDocument;

        keywords.add("123 your street");
        keywords.add("your city, st 12345");
        keywords.add("(123) 456-7890");
        keywords.add("123.456.7890");
        keywords.add("no_reply@example.com");
        keywords.add("your name");
        keywords.add("creative director");
        keywords.add("skills");
        keywords.add("education");
        keywords.add("experience");
        keywords.add("awards");
        keywords.add("projects");
        keywords.add("languages");
    }

    @Override
    public void eventOccurred(IEventData data, EventType type) {
        // you can first check the type of the event
        if (!type.equals(EventType.RENDER_TEXT))
            return;

        // now it is safe to cast
        TextRenderInfo textRenderInfo = (TextRenderInfo) data;

        System.out.println();
        System.out.println("eventOcurred");
        System.out.println("textRenderInfo.getText(): " + textRenderInfo.getText());
        try {
            byte[] bytes = textRenderInfo.getText().getBytes("US-ASCII");
            System.out.println("textRenderInfoASCII: " + Arrays.toString(bytes));
            if(Arrays.toString(bytes).contains("63")){
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        textRenderInfo.preserveGraphicsState();
        items.add(new TextItem(textRenderInfo, pdfDoc.getFirstPage().getPageSize().getHeight()));
    }

    public List<MyItem> getItems() {
        return items;
    }

    @Override
    public Set<EventType> getSupportedEvents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getResultantText() {
        // TODO Auto-generated method stub
        return null;
    }

}
