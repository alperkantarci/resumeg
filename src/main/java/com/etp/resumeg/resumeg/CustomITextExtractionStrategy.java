package com.etp.resumeg.resumeg;

import java.util.*;

import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.CharacterRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.layout.element.Paragraph;

public class CustomITextExtractionStrategy implements ITextExtractionStrategy {

    public CustomITextExtractionStrategy() {
        keywords.add("123 your street");
        keywords.add("your city, st 12345");
        keywords.add("(123) 456-7890");
        keywords.add("no_reply@example.com");
        keywords.add("your name");
        keywords.add("skills");
        keywords.add("education");
    }

    // Keywords
    private static final List<String> keywords = new ArrayList<>();

    private List<TemplateKeyword> templateKeywords = null;

    private List<WordTextRenderInfo> words = null;
    private List<CharacterRenderInfo> chars = null;

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

//		System.out.println(" ".equals(" "));
        if (words == null) {
            words = new ArrayList<>();
        }

        if (templateKeywords == null) {
            templateKeywords = new ArrayList<>();
        }

        // if text is a string(means a word/words) and equals to any keywords
        // 1. create templateKeyword
        // 2. add that templateKeyword item to templateKeyWords list
        if (keywords.contains(text.toLowerCase())) {
            textRenderInfo.preserveGraphicsState();

            TemplateKeyword templateKeyword = new TemplateKeyword();
            float left = textRenderInfo.getBaseline().getStartPoint().get(Vector.I1);
            float right = textRenderInfo.getBaseline().getEndPoint().get(Vector.I1);
            float bottom = textRenderInfo.getBaseline().getStartPoint().get(Vector.I2);
            float charWidth = (right - left) / textRenderInfo.getText().length();
            float width = right - left;

            templateKeyword.setFont(textRenderInfo.getFont());
            templateKeyword.setText(textRenderInfo.getText());
            templateKeyword.setLeft(left);
            templateKeyword.setBottom(bottom);
            templateKeyword.setWidth(width);

            templateKeywords.add(templateKeyword);
        }

        // if text is a char, keep adding characters to chars list
        if (text.length() == 1) {
            if (chars == null) {
                chars = new ArrayList<>();
            }

            textRenderInfo.preserveGraphicsState();

            System.out.println("actualChar:" + text + "->" + text.equals(" "));

            CharacterRenderInfo characterRenderInfo = new CharacterRenderInfo(textRenderInfo);
            chars.add(characterRenderInfo);
        } else {
            System.out.println("actualLine:" + text + "->" + text.equals(" "));
        }

        if (chars != null) {
//            System.out.println(convertCharacterRenderInfoListToString(chars).toLowerCase());
            String actualString = convertCharacterRenderInfoListToString(chars).toLowerCase();
            String[] keywordsArr = keywords.toArray(new String[0]);
            // if chars list begin equal to any keywords,
            // 1. convert chars list to TextRenderInfo
            // 2. add that TextRenderInfo item to templateKeywords list
            // 3. clean chars list
            stringContainsItemFromList(actualString, keywords);
        }
    }

//    public static void stringContainsItemFromList(String inputStr, String[] items) {
//        System.out.println(StringUtils.indexOfAny(inputStr, items));
////        Arrays.stream(items).parallel().filter(inputStr::contains).findAny();
////        Arrays.stream(items).parallel().anyMatch(inputStr::contains);
//    }

    public void stringContainsItemFromList(String inputStr, List<String> keywords) {
        for (String item : keywords) {
            if (inputStr.contains(item)) {
                System.out.println("matched item:" + item);
                int matchedIndex = inputStr.indexOf(item);
                System.out.println("matched item.length:" + item.length());
                System.out.println("matched index:" + inputStr.indexOf(item));

                TemplateKeyword templateKeyword = new TemplateKeyword();
                StringBuilder stringBuilder = new StringBuilder();

                for (int k = matchedIndex; k < matchedIndex + item.length(); k++) {
                    stringBuilder.append(chars.get(matchedIndex).getText());
                    chars.remove(matchedIndex);
                }

                templateKeyword.setText(stringBuilder.toString());
                templateKeywords.add(templateKeyword);

            }
        }
    }

    public void stringContainsItemFromList(String inputStr, String[] items) {
        for (int i = 0; i < items.length; i++) {
            if (inputStr.contains(items[i])) {
                System.out.println("matched item:" + items[i]);
                int matchedIndex = inputStr.indexOf(items[i]);
                System.out.println("matched item.length:" + items[i].length());
                System.out.println("matched index:" + inputStr.indexOf(items[i]));

                TemplateKeyword templateKeyword = new TemplateKeyword();
                StringBuilder stringBuilder = new StringBuilder();

                for (int k = matchedIndex; k < matchedIndex + items[i].length(); k++) {
//                    System.out.println("chars.size:" + chars.size());
//                    System.out.println("k:" + k);
                    stringBuilder.append(chars.get(matchedIndex).getText());
                    chars.remove(matchedIndex);
//                    System.out.println(convertCharacterRenderInfoListToString());
                }

                templateKeyword.setText(stringBuilder.toString());
                templateKeywords.add(templateKeyword);

            }
        }
    }

    public List<TemplateKeyword> getTemplateKeywords() {
        return templateKeywords;
    }

    private void cleanCharacterRenderInfoListFromSpecialChars(List<CharacterRenderInfo> characterRenderInfos) {
        for (Iterator<CharacterRenderInfo> iterator = characterRenderInfos.iterator(); iterator.hasNext(); ) {
            CharacterRenderInfo characterRenderInfo = iterator.next();
            if (characterRenderInfo.getText().equals(" ")) {
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

    private String convertCharacterRenderInfoListToString(List<CharacterRenderInfo> characterRenderInfos) {
        StringBuilder stringBuilder = new StringBuilder();
        for (CharacterRenderInfo characterRenderInfo :
                characterRenderInfos) {
            stringBuilder.append(characterRenderInfo.getText());
        }
        return stringBuilder.toString();
    }

    public void setTemplateKeywords(List<TemplateKeyword> templateKeywords) {
        this.templateKeywords = templateKeywords;
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

    public List<WordTextRenderInfo> getWords() {
        return words;
    }

    public void setWords(List<WordTextRenderInfo> words) {
        this.words = words;
    }

}
