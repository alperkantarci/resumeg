package com.etp.resumeg.resumeg;

import java.util.*;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.CharacterRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

public class CustomITextExtractionStrategy implements ITextExtractionStrategy {

    // Keywords
    private static final List<String> keywords = new ArrayList<>();

    private PdfDocument pdfDoc;

    private static List<String> stringList = new ArrayList<>();
    private StringBuilder stringBuilder = new StringBuilder();

    private HashMap<String, List<TextRenderInfo>> templateKeywords = new HashMap<>();
    private String templateKeywordKey = UUID.randomUUID().toString();

//    private List<TemplateKeyword> templateKeywords = null;

    private List<WordTextRenderInfo> words = null;
    private List<TextRenderInfo> chars = null;

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

    /*
     * getting templateKeywords on the go maybe is not an ideal solution
     * if something goes wrong with time complexity you should first get all TextRenderInfos
     * then start searching in the List<TextRenderInfos> instead of
     * calling stringContainsItemFromList() function on every eventOccurred
     */
    @Override
    public void eventOccurred(IEventData data, EventType type) {
        // you can first check the type of the event
        if (!type.equals(EventType.RENDER_TEXT))
            return;

        // now it is safe to cast
        TextRenderInfo textRenderInfo = (TextRenderInfo) data;

        String text = textRenderInfo.getText();
        CanvasGraphicsState canvasGraphicsState = textRenderInfo.getGraphicsState();
        FontProgram fontProgram = textRenderInfo.getFont().getFontProgram();
        String fntName = fontProgram.getFontNames().getFontName();
        Color fontColor = canvasGraphicsState.getStrokeColor();
        float fntColor = fontColor.getColorValue()[0];

        Matrix textToUserSpaceTransformMatrix = canvasGraphicsState.getCtm();
        float fntSize = new Vector(0, canvasGraphicsState.getFontSize(), 0).cross(textToUserSpaceTransformMatrix)
                .length();

//        String templateKeywordKey = fntName + "_" + fntSize + "_" + fntColor;

//        System.out.println("textRenderInfo font infos:");
//        System.out.println();
//        System.out.println("text:\"" + text + "\"" + (text.equals(" ") ? "-> new line" : ""));
//        System.out.println(templateKeywordKey);
        for (float clr :
                fontColor.getColorValue()) {
//            System.out.println(clr);
        }
////        System.out.println("fntName:" + fntName + ", fntSize:" + fntSize + ", fntColor:" + fntColor.getColorValue()[0]);
//        System.out.println();

        textRenderInfo.preserveGraphicsState();

//        float x1 = textRenderInfo.getBaseline().getStartPoint().get(Vector.I1);
//        float x2 = textRenderInfo.getBaseline().getEndPoint().get(Vector.I1);
//        float y1 = textRenderInfo.getBaseline().getStartPoint().get(Vector.I2);
//        float y2 = textRenderInfo.getBaseline().getEndPoint().get(Vector.I2);
//
//        Rectangle rectangle = new Rectangle(x1, 792 - y1 - fntSize, x2 - x1, fntSize);
//        PdfDrawService.drawRectangleOnPdf(pdfDoc, rectangle, ColorConstants.RED);

        // CREATING stringBuilder by splitting new line char
//        if (!textRenderInfo.getText().equals(" ")) {
//            stringBuilder.append(textRenderInfo.getText());
//        } else {
//            stringList.add(stringBuilder.toString());
//            stringBuilder = new StringBuilder();
//        }

        // CREATING templateKeywords by splitting new line
        // add every line with unique uuid key to list
        if (textRenderInfo.getText().equals(" ")) {
            templateKeywordKey = UUID.randomUUID().toString();
        } else {
            if (templateKeywords.get(templateKeywordKey) == null) {
                templateKeywords.put(templateKeywordKey, new ArrayList<>());
            }
            templateKeywords.get(templateKeywordKey).add(textRenderInfo);
        }


//        String text = textRenderInfo.getText();
//
////		System.out.println(" ".equals(" "));
//        if (words == null) {
//            words = new ArrayList<>();
//        }
//
//        if (templateKeywords == null) {
//            templateKeywords = new ArrayList<>();
//        }
//
//        // if text is a string(means a word/words) and equals to any keywords
//        // 1. create templateKeyword
//        // 2. add that templateKeyword item to templateKeyWords list
//        if (keywords.contains(text.toLowerCase())) {
//            textRenderInfo.preserveGraphicsState();
//
//            TemplateKeyword templateKeyword = new TemplateKeyword();
//            float left = textRenderInfo.getBaseline().getStartPoint().get(Vector.I1);
//            float right = textRenderInfo.getBaseline().getEndPoint().get(Vector.I1);
//            float bottom = textRenderInfo.getBaseline().getStartPoint().get(Vector.I2);
//            float charWidth = (right - left) / textRenderInfo.getText().length();
//            float width = right - left;
//
//            templateKeyword.setFont(textRenderInfo.getFont());
//            templateKeyword.setText(textRenderInfo.getText());
//            templateKeyword.setLeft(left);
//            templateKeyword.setBottom(bottom);
//            templateKeyword.setWidth(width);
//            templateKeyword.setGraphicsState(textRenderInfo.getGraphicsState());
//
//            templateKeywords.add(templateKeyword);
//        }
//
//        // if text is a char, keep adding characters to chars list
////        if (text.length() == 1) {
//        if (!keywords.contains(text)) {
//            if (chars == null) {
//                chars = new ArrayList<>();
//            }
//
//            textRenderInfo.preserveGraphicsState();
//
//            System.out.println("actualChar:" + text + "->" + text.equals(" "));
//
////            CharacterRenderInfo characterRenderInfo = new CharacterRenderInfo(textRenderInfo);
//            chars.add(textRenderInfo);
//        } else {
//            System.out.println("actualLine:" + text + "->" + text.equals(" "));
//        }
//
//        if (chars != null) {
////            System.out.println(convertCharacterRenderInfoListToString(chars).toLowerCase());
//            String actualString = convertCharacterRenderInfoListToString(chars).toLowerCase();
//            System.out.println("actualString:" + actualString);
//
//
//            // if chars list begin equal to any keywords,
//            // 1. convert chars list to TextRenderInfo
//            // 2. add that TextRenderInfo item to templateKeywords list
//            // 3. clean chars list
//            stringContainsItemFromList(actualString.replace(" ", " "), keywords);
//        }
    }

//    public static void stringContainsItemFromList(String inputStr, String[] items) {
//        System.out.println(StringUtils.indexOfAny(inputStr, items));
////        Arrays.stream(items).parallel().filter(inputStr::contains).findAny();
////        Arrays.stream(items).parallel().anyMatch(inputStr::contains);
//    }


    public HashMap<String, List<TextRenderInfo>> getTemplateKeywords() {
        return templateKeywords;
    }


    public List<String> getStringList() {
        return stringList;
    }

    private int[] indexesOfWord(List<TextRenderInfo> chars, String keyword) {

        String searchString = keyword;
//        String searchString = "123 your street";
        int searchStringLength = searchString.length();
        int searchStartIndex = 0;
        int searchEndIndex = 0;

        int searchIndex = 0;
        int searchStringIndex = 0;

        int letterCount = 0;

        int index = 0;
//        for (String item :
//                stringArr) {
//            if(item.equals(searchString)){
//                    searchIndex = index;
//                    break;
//            }
//
//            index++;
//        }

        int[] matchedIndexes = new int[keyword.length()];

        System.out.println("keyword:" + keyword);

//        System.out.println("stringItem:");
        for (TextRenderInfo item :
                chars) {

            String stringItem = item.getText().toLowerCase();
            System.out.println("stringItem[" + index + "]:" + stringItem);

            if (stringItem.length() == 1) {

//                System.out.println("if(item.length() == 1)");
//                System.out.println("stringItem:" + stringItem);
//                System.out.println("searchString.toCharArray()[searchIndex]:" + searchString.toCharArray()[searchIndex]);
//                if("1".equals(searchString.toCharArray()[searchIndex])){
//                    System.out.println("yes");
//                }
                if (stringItem.toCharArray()[0] == searchString.toCharArray()[searchIndex]) {

                    matchedIndexes[searchIndex] = index;

                    System.out.println("indexxxx:" + index);
                    System.out.println("searchIndexxx:" + searchIndex);

                    if (searchIndex >= 1 && matchedIndexes[searchIndex - 1] + 1 == matchedIndexes[searchIndex]) {
                        searchIndex++;
                        letterCount++;
                    } else if (searchIndex == 0) {
                        searchIndex++;
                        letterCount++;
                    } else if (searchIndex >= 1 && matchedIndexes[searchIndex - 1] + 1 != matchedIndexes[searchIndex]) {


                        if (chars.get(index - 1).getText().toCharArray()[0] == searchString.toCharArray()[searchIndex - 1]) {
                            searchStartIndex = index - 1;
                        }
                        searchIndex = 0;
                        letterCount = 0;
                        searchStartIndex = 0;
                    }
//                    System.out.println("if(item.equals(searchString.toCharArray()[searchIndex]))");

                }
            } else if (stringItem.length() > 1) {

                int realItemLength = item.getText().length();
                stringItem = stringItem.trim();

                if (stringItem.toCharArray()[0] == searchString.toCharArray()[searchIndex]) {
//                    System.out.println("if(item.toCharArray()[0] == searchString.toCharArray()[searchIndex])");
                    searchIndex += stringItem.length();

                }
            }

            if (searchIndex == searchString.length()) {
//                System.out.println("break");
                searchEndIndex = index + 1;
                if (searchStartIndex == 0) {
                    searchStartIndex = index;
                }
                System.out.println("break");
                break;
            }

            if (searchIndex - 1 == 0 || searchIndex - stringItem.length() == 0) {

                searchStartIndex = index;
//                System.out.println("searchStartIndex:" + searchStartIndex  + " filled");
            }

            index++;

            System.out.println("searchIndex:" + searchIndex);
        }

        System.out.println();
        System.out.println("searchStartIndex:" + searchStartIndex);
        System.out.println("searchEndIndex:" + searchEndIndex);


        for (int i = searchStartIndex; i < searchEndIndex; i++) {
            chars.remove(searchStartIndex);
        }

//        System.out.println();
//        System.out.println("Final version of stringArr");
//        for (TextRenderInfo item :
//                chars) {
//            System.out.print(item.getText());
//        }

//        System.out.println(" ");
//        System.out.println("Final:");
//        System.out.println("searchIndex:" + searchIndex);
//        System.out.println("searchStartIndex:" + searchStartIndex);
//        System.out.println("searchEndIndex:" + searchEndIndex);

        return new int[]{searchStartIndex, searchEndIndex};
    }

    public void stringContainsItemFromList(String inputStr, List<String> keywords) {
        for (String item : keywords) {
            if (inputStr.contains(item)) {

                int[] indexesOfMatchedWord = indexesOfWord(chars, item);

                System.out.println("searchStartIndex:" + indexesOfMatchedWord[0]);
                System.out.println("searchEndIndex:" + indexesOfMatchedWord[1]);
                System.out.println();


//                System.out.println("matched item:" + item);
//                int matchedIndex = inputStr.indexOf(item);
//                System.out.println("matched item.length:" + item.length());
//                System.out.println("matched index:" + inputStr.indexOf(item));
//                System.out.println("chars.size:" + chars.size());

//                TemplateKeyword templateKeyword = new TemplateKeyword();
//                templateKeyword.setFont(chars.get(matchedIndex).getFont());
//
//                // get first and last chars of a keyword
//                TextRenderInfo firstChar = chars.get(matchedIndex);
//                System.out.println("firstChar:" + firstChar.getText());
//                TextRenderInfo lastChar = null;
//
//                float left = firstChar.getBaseline().getStartPoint().get(Vector.I1);
//                float bottom = firstChar.getBaseline().getStartPoint().get(Vector.I2);
//
//                templateKeyword.setLeft(left);
//                templateKeyword.setBottom(bottom);
//
//                templateKeyword.setGraphicsState(firstChar.getGraphicsState());
//
//                StringBuilder stringBuilder = new StringBuilder();
//                int lastIndexOfLoop = matchedIndex + item.length();
//                System.out.println("lastIndexOfLoop:" + lastIndexOfLoop);
//
//                System.out.println("chars.toCharArray.size:" + chars);
//
//                for (int k = matchedIndex; k < lastIndexOfLoop; k++) {
//                    System.out.println("k:" + k);
//                    System.out.println("char length in chars:" + chars.get(matchedIndex).getText().length());
//                    int charLengthInChars = chars.get(matchedIndex).getText().length();
//                    System.out.println("char in loop:" + chars.get(matchedIndex).getText());
//
//
//                    // somehow sometimes char.length would be > 1, and we need to factor that plus from lastIndexOfLoop
//                    if (charLengthInChars > 1) {
//                        System.out.println("charLengthInChars > 1");
//                        lastIndexOfLoop -= (charLengthInChars - 1);
//                    }
//
//                    // getting lastChar inside of a loop, because we dont know what will be the last index of a keyword
//                    // because lastIndexOfLoop can change in loop
//                    if (k == lastIndexOfLoop - 1) {
//                        System.out.println("k == lastIndexOfLoop - 1");
//                        lastChar = chars.get(matchedIndex);
//                        System.out.println("lastChar:" + lastChar.getText());
//                    }
//
//                    stringBuilder.append(chars.get(matchedIndex).getText());
//                    chars.remove(matchedIndex);
//                }
//
//                float right = lastChar.getBaseline().getEndPoint().get(Vector.I1);
//                float charWidth = (right - left) / item.length();
//                float width = right - left;
//
//                templateKeyword.setWidth(width);
//                templateKeyword.setText(stringBuilder.toString());
//                templateKeywords.add(templateKeyword);

            }
        }
    }

    public void stringContainsItemFromList(String inputStr, String[] items) {
//        for (int i = 0; i < items.length; i++) {
//            if (inputStr.contains(items[i])) {
//                System.out.println("matched item:" + items[i]);
//                int matchedIndex = inputStr.indexOf(items[i]);
//                System.out.println("matched item.length:" + items[i].length());
//                System.out.println("matched index:" + inputStr.indexOf(items[i]));
//
//                TemplateKeyword templateKeyword = new TemplateKeyword();
//                StringBuilder stringBuilder = new StringBuilder();
//
//                for (int k = matchedIndex; k < matchedIndex + items[i].length(); k++) {
////                    System.out.println("chars.size:" + chars.size());
////                    System.out.println("k:" + k);
//                    stringBuilder.append(chars.get(matchedIndex).getText());
//                    chars.remove(matchedIndex);
////                    System.out.println(convertCharacterRenderInfoListToString());
//                }
//
//                templateKeyword.setText(stringBuilder.toString());
//                templateKeywords.add(templateKeyword);
//
//            }
//        }
    }

//    public List<TemplateKeyword> getTemplateKeywords() {
//        return templateKeywords;
//    }

    private void cleanCharacterRenderInfoListFromSpecialChars(List<TextRenderInfo> characterRenderInfos) {
        for (Iterator<TextRenderInfo> iterator = characterRenderInfos.iterator(); iterator.hasNext(); ) {
            TextRenderInfo textRenderInfo = iterator.next();
            if (textRenderInfo.getText().equals(" ")) {
                iterator.remove();
            }
        }
    }

    private TemplateKeyword convertCharacterRenderInfoListToTextRenderInfo() {
        cleanCharacterRenderInfoListFromSpecialChars(chars);

        TemplateKeyword templateKeyword = new TemplateKeyword();
        templateKeyword.setText(convertCharacterRenderInfoListToString(chars));

        return templateKeyword;
    }

    private String convertCharacterRenderInfoListToString(List<TextRenderInfo> characterRenderInfos) {
        StringBuilder stringBuilder = new StringBuilder();
        for (TextRenderInfo textRenderInfo :
                characterRenderInfos) {
            stringBuilder.append(textRenderInfo.getText());
        }
        return stringBuilder.toString();
    }

//    public void setTemplateKeywords(List<TemplateKeyword> templateKeywords) {
//        this.templateKeywords = templateKeywords;
//    }

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

    public List<WordTextRenderInfo> getWords() {
        return words;
    }

    public void setWords(List<WordTextRenderInfo> words) {
        this.words = words;
    }

}
