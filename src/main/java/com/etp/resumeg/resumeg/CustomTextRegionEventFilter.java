package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.filter.IEventFilter;

public class CustomTextRegionEventFilter implements IEventFilter {

	private final Rectangle filterRect;
	private PdfCanvas pdfCanvas;

	/**
	 * Constructs a filter instance.
	 * 
	 * @param filterRect the rectangle to filter text against
	 */
	public CustomTextRegionEventFilter(Rectangle filterRect, PdfCanvas canvas) {
		this.filterRect = filterRect;
		this.pdfCanvas = canvas;
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

//			renderInfo.getUnscaledWidth()

			

			if (Character.isLetterOrDigit(renderInfo.getText().charAt(0))) {
				Rectangle rect = new Rectangle(startPoint.get(0), startPoint.get(1), x2, renderInfo.getFontSize());
				this.pdfCanvas.rectangle(rect);
				
				System.out.println("id:" + data.hashCode()
						+ String.format("(%s => %s)", renderInfo.getText(),
								Character.isLetterOrDigit(renderInfo.getText().charAt(0)))
						+ "=> " + x1 + "f, " + y1 + "f, " + x2 + "f, " + renderInfo.getFontSize() + "f");
			}

			if (x1 < 140 && y2 > 400) {
//				this.pdfCanvas.rectangle(x1, y1, x2, 1);
//				System.out.println("x1:" + x1 + " ,y1:" + y1 + " x2:" + x2 + " ,y2:" + y2);
//				System.out.println(x1 + "f, " + y1 + "f, " + x2 + "f, " + "1f");
			}

			return filterRect == null || filterRect.intersectsLine(x1, y1, x2, y2);
		} else {
			return false;
		}
	}

}
