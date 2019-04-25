package com.etp.resumeg.resumeg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.webfonts.Webfonts;
import com.google.api.services.webfonts.model.Webfont;
import com.google.api.services.webfonts.model.WebfontList;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDictionary;

public class GoogleWebFontService {
	// API Key: AIzaSyDfSsn4RIfbYsY82qGaqnLEQTlUJ_R1mDM
	private final String WEBFONT_API_KEY = "AIzaSyDfSsn4RIfbYsY82qGaqnLEQTlUJ_R1mDM";

	private final String APPLICATION_NAME = "resumeg";

	private final NetHttpTransport httpTransport = new NetHttpTransport();
	private final JacksonFactory jacksonFactory = new JacksonFactory();

//	private Webfonts webFonts = null;
	private WebfontList fonts = null;

	private final Map<String, String> GoogleFontVariants = new HashMap<String, String>();

	public GoogleWebFontService() throws IOException {
		
		GoogleFontVariants.put("thin", "100");
		GoogleFontVariants.put("extra-light", "200");
		GoogleFontVariants.put("light", "300");
		GoogleFontVariants.put("regular", "regular");
		GoogleFontVariants.put("medium", "500");
		GoogleFontVariants.put("semi-bold", "600");
		GoogleFontVariants.put("bold", "700");
		GoogleFontVariants.put("black", "900");
		
		setFonts(new Webfonts.Builder(httpTransport, jacksonFactory, new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) throws IOException {
				// TODO Auto-generated method stub
			}
		}).setApplicationName(APPLICATION_NAME).build().webfonts().list().setKey(WEBFONT_API_KEY).execute());
	}

	public PdfFont downloadFontByFamily(String family, String variant) throws IOException {
		for (Webfont item : this.getFontList().getItems()) {
			// Font Family found in all google fonts list
			if (item.getFamily().equals(family)) {
//				System.out.println("Font found:" + item.getFamily());
				for (Entry<String, String> file : item.getFiles().entrySet()) {
					// Font variant found in files
					if (file.getKey().equals(GoogleFontVariants.get(variant.toLowerCase()))) {
//						System.out.println("Font found:" + item.getFamily() + "-" + variant);

						// Download and create FontProgram object from file link
						// file.getValue() -> font file's download url
						// file.getKey() -> font file's variant name
						return PdfFontFactory.createFont(file.getValue());
					}
				}
			}
		}

		return null;
	}

	public List<PdfFont> downloadFonts(PdfDictionary fontsDict) {
		List<PdfFont> fonts = new ArrayList<>();
		return fonts;
	}

	public WebfontList getFontList() {
		return this.fonts;
	}

	public WebfontList getFonts() {
		return fonts;
	}

	public void setFonts(WebfontList fonts) {
		this.fonts = fonts;
	}
}
