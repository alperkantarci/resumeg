package com.etp.resumeg.resumeg;

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.kernel.geom.Rectangle;

public class ExampleRectArea {
	private static Map<String, Rectangle> rectangles = new HashMap<>();
	
	public ExampleRectArea() {
		rectangles.put("EDUCATION_Resume2", new Rectangle(89.25f, 232.5f, 54f, 16f));
		rectangles.put("Name_Resume", new Rectangle(145.2675f, 708.0f, 116f, 48f));
		rectangles.put("Your_Resume", new Rectangle(50.695f, 708.0f, 92f, 48f));
		rectangles.put("Your Name_Resume", new Rectangle(50.695f, 708.0f, 185f, 48f));
	}
}
