package com.etp.resumeg.resumeg;

import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseService {
    /**
     * @param reText contains "0 0 100 50 re" string from /Contents
     * @return
     */
    protected static Rectangle convertReTagTextToRectangle(String reText) {
        String splittedReLine[] = reText.split(" ");
        // reText example: "0 0 100 200"
        float x1 = Float.valueOf(splittedReLine[0]);
        float y1 = Float.valueOf(splittedReLine[1]);
        float x2 = Float.valueOf(splittedReLine[2]);
        float y2 = Float.valueOf(splittedReLine[3]);
        return new Rectangle(x1, y1, x2, y2);
    }

    protected static List<Rectangle> parseContentsRectangles(String content) {
        List<Rectangle> contentsRectangle = new ArrayList<>();

//         split content line by line and filter which contains "re" tag
        for (String item : content.split("\n")) {
            if (item.contains("re")) {
                Rectangle rect = convertReTagTextToRectangle(item);
                contentsRectangle.add(rect);
            }
        }
        return contentsRectangle;
    }

    private void ParseActualTextFromStreamBytes(String DEST, PdfStream stream) throws FileNotFoundException, IOException {
        PdfTokenizer tokenizer = new PdfTokenizer(
                new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(stream.getBytes())));

        PrintWriter out = new PrintWriter(new FileOutputStream(DEST));

        while (tokenizer.nextToken()) {
            if (tokenizer.getTokenType() == PdfTokenizer.TokenType.String) {
                out.println(tokenizer.getStringValue());
            }
        }
        out.flush();
        out.close();
        tokenizer.close();
    }

    protected static HashMap<String, String> splitFontName(String baseFont) {
        HashMap<String, String> fontMap = new HashMap<String, String>();

        // Font splitting by 'camel case' and '-'
        String[] dashSplittedFontName = baseFont.split("-");
        String fontFamily = "";
        String fontVariant = "";
        if (dashSplittedFontName.length > 1) {
            String[] camelCaseFontFamilySplitted = dashSplittedFontName[0].replace("/", "")
                    .split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
            if (camelCaseFontFamilySplitted.length > 1) {
                for (String item : camelCaseFontFamilySplitted) {
                    fontFamily += item + " ";
                }
                fontFamily = fontFamily.trim();
            } else {
                fontFamily = camelCaseFontFamilySplitted[0];
            }
            fontVariant = dashSplittedFontName[1];
        } else {
            fontFamily = dashSplittedFontName[0];
        }

        fontMap.put("Family", fontFamily);
        fontMap.put("Variant", fontVariant);

        return fontMap;
    }

    protected static void extractTextFromRectArea(String SRC, Rectangle rect) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));
        StringBuilder strBuilder = new StringBuilder();
        CustomTextRegionEventFilter regionFilter = new CustomTextRegionEventFilter(rect);
        FilteredEventListener listener = new FilteredEventListener();

        LocationTextExtractionStrategy extractionStrategy = listener
                .attachEventListener(new LocationTextExtractionStrategy(), regionFilter);

        new PdfCanvasProcessor(listener).processPageContent(pdfDoc.getFirstPage());
        String actualText = extractionStrategy.getResultantText();
        System.out.println("actualText: " + actualText);

        pdfDoc.close();
    }

    protected static void parse(String src, String dest) throws IOException {
//        PdfReader reader = new PdfReader(src);
//        PdfObject obj;
//        for (int i = 1; i <= reader.getLastXref(); i++) {
//            obj = reader.getPdfObject(i);
//            if (obj != null && obj.isStream()) {
//                PRStream stream = (PRStream)obj;
//                byte[] b;
//                try {
//                    b = PdfReader.getStreamBytes(stream);
//                }
//                catch(UnsupportedPdfException e) {
//                    b = PdfReader.getStreamBytesRaw(stream);
//                }
//                FileOutputStream fos = new FileOutputStream(String.format(dest, i));
//                fos.write(b);
//                fos.flush();
//                fos.close();
//            }
//        }
    }
}
