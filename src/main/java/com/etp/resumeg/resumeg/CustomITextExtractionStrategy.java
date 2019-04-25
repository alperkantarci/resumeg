package com.etp.resumeg.resumeg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;

public class CustomITextExtractionStrategy implements ITextExtractionStrategy {

	private List<WordTextRenderInfo> words = null;
	private List<TextRenderInfo> chars = null;

	@Override
	public void eventOccurred(IEventData data, EventType type) {
		// you can first check the type of the event
		if (!type.equals(EventType.RENDER_TEXT))
			return;

		// now it is safe to cast
		TextRenderInfo renderInfo = (TextRenderInfo) data;
//		System.out.println("renderInfo.getText():" + renderInfo.getText());
		String text = renderInfo.getText();

//		System.out.println(" ".equals(" "));
		if (words == null) {
			words = new ArrayList<>();
		}

		if (text.length() == 1) {
			if (chars == null) {
				chars = new ArrayList<>();
			}
			renderInfo.preserveGraphicsState();
			System.out.println("actualChar:" + text + "->" + text.equals(" "));
			chars.add(renderInfo);
		}else{
			System.out.println("actualLine:" + text + "->" + text.equals(" "));
		}

		if (text.equals(" ")) {
			// new line char
//			if (chars != null && chars.stream().filter(item -> !item.getText().equals(" ")).toArray().length > 0) {
//			if (chars != null && chars.size() > 1 && !chars.get(0).getText().equals(" ")) {
			if (chars != null) {
				WordTextRenderInfo word = new WordTextRenderInfo();
				word.setContent(chars);
				words.add(word);
//				for (TextRenderInfo item : chars) {
//					System.out.print(item.getText());
//				}
				chars = null;
			}
		}

		// " " new line char

//		if (text.equals(" ")) {
//			// space
//			System.out.println("\"" + text + "\"" + " -> " + "space");
//		} else if (text.equals(" ")) {
//			// new line
//			System.out.println("\"" + text + "\"" + " -> " + "new line");
//		} else {
//			System.out.println(text);
//		}
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
