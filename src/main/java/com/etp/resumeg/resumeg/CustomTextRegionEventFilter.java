package com.etp.resumeg.resumeg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.filter.IEventFilter;

public class CustomTextRegionEventFilter implements IEventFilter {

	private final Rectangle filterRect;
	private PdfCanvas pdfCanvas;
	private StringBuilder content;

	/**
	 * Constructs a filter instance.
	 * 
	 * @param filterRect the rectangle to filter text against
	 */
	public CustomTextRegionEventFilter(Rectangle filterRect, PdfCanvas canvas, StringBuilder content) {
		this.filterRect = filterRect;
		this.pdfCanvas = canvas;
		this.content = content;
	}

	public boolean accept(IEventData data, EventType type) {
		if (type.equals(EventType.RENDER_TEXT)) {
			TextRenderInfo renderInfo = (TextRenderInfo) data;

			LineSegment segment = renderInfo.getBaseline();
			
			Vector startPoint = segment.getStartPoint();
			Vector endPoint = segment.getEndPoint();
			Vector topRight = renderInfo.getAscentLine().getEndPoint();

			float x1 = startPoint.get(Vector.I1);
			float y1 = startPoint.get(Vector.I2);
			float x2 = endPoint.get(Vector.I1);
			float y2 = endPoint.get(Vector.I2);
			
			this.content.append(renderInfo.getText());
			
			// Patter for special chars only
			Pattern pattern = Pattern.compile("[a-zA-Z0-9 ]*");
			Matcher matcher = pattern.matcher(renderInfo.getText());

			System.out.println("id:" + data.hashCode()
					+ String.format("(%s => %s)", renderInfo.getText(),
							!matcher.matches())
					+ "=> " + x1 + "f, " + y1 + "f, " + x2 + "f, " + renderInfo.getFontSize() + "f");
			
			Rectangle rect = new Rectangle(startPoint.get(0), 792 - startPoint.get(1) - renderInfo.getFontSize(), x2, renderInfo.getFontSize());
			this.pdfCanvas.rectangle(rect);

			// print only letters or digits
//			if (Character.isLetterOrDigit(renderInfo.getText().charAt(0))) {
//				Rectangle rect = new Rectangle(startPoint.get(0), 792 - startPoint.get(1) - renderInfo.getFontSize(), x2, renderInfo.getFontSize());
//				this.pdfCanvas.rectangle(rect);
//
//				System.out.println("id:" + data.hashCode()
//						+ String.format("(%s => %s)", renderInfo.getText(),
//								Character.isLetterOrDigit(renderInfo.getText().charAt(0)))
//						+ "=> " + x1 + "f, " + y1 + "f, " + x2 + "f, " + renderInfo.getFontSize() + "f");
//			}

			return filterRect == null || filterRect.intersectsLine(x1, y1, x2, y2);
		} else {
			return false;
		}
	}

}
