package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.filter.IEventFilter;

public class CustomTextRegionEventFilter implements IEventFilter {

	private final Rectangle filterRect;

	/**
	 * Constructs a filter instance.
	 * 
	 * @param filterRect the realRectangle to filter text against
	 */
	public CustomTextRegionEventFilter(Rectangle filterRect) {
		this.filterRect = filterRect;
	}

	public boolean accept(IEventData data, EventType type) {
		if (type.equals(EventType.RENDER_TEXT)) {

			TextRenderInfo renderInfo = (TextRenderInfo) data;
			LineSegment segment = renderInfo.getBaseline();

			Vector startPoint = segment.getStartPoint();
			Vector endPoint = segment.getEndPoint();

			float x1 = startPoint.get(Vector.I1);
			float y1 = startPoint.get(Vector.I2);
			float x2 = endPoint.get(Vector.I1);
			float y2 = endPoint.get(Vector.I2);

			return filterRect == null || filterRect.intersectsLine(x1, y1, x2, y2);
		} else {
			return false;
		}
	}

}
