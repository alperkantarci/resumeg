package com.etp.resumeg.resumeg;

import java.util.List;

import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;

public class WordTextRenderInfo {

	private List<TextRenderInfo> content;

	public List<TextRenderInfo> getContent() {
		return content;
	}

	public void setContent(List<TextRenderInfo> content) {
		this.content = content;
	}

	public String getText() {
		StringBuilder strBuilder = new StringBuilder();
		for (TextRenderInfo textItem : getContent()) {
			if (!textItem.getText().equals(" "))
				strBuilder.append(textItem.getText());
		}
		return strBuilder.toString();
	}
	
	public TextRenderInfo getFirstChar() {
		return getContent().get(0);
	}
	
	public TextRenderInfo getLastChar() {
		return getContent().get(getContent().size() - 1);
	}

}
