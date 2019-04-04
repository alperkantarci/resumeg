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
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;

public class GoogleWebFontService {
	// API Key: AIzaSyDfSsn4RIfbYsY82qGaqnLEQTlUJ_R1mDM
	private final String WEBFONT_API_KEY = "AIzaSyDfSsn4RIfbYsY82qGaqnLEQTlUJ_R1mDM";

	private final String APPLICATION_NAME = "resumeg";

	private final NetHttpTransport httpTransport = new NetHttpTransport();
	private final JacksonFactory jacksonFactory = new JacksonFactory();

	private Webfonts webFonts = null;
	private WebfontList fonts = null;

	private final Map<String, String> GoogleFontVariants = Map.of("bold", "700", "regular", "regular");

	public GoogleWebFontService() {
		setWebFonts(new Webfonts.Builder(httpTransport, jacksonFactory, new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) throws IOException {
				// TODO Auto-generated method stub
			}
		}).setApplicationName(APPLICATION_NAME).build());
	}

	public PdfFont downloadFontByFamily(String family, String variant) throws IOException {
		System.out.println();
		for (Webfont item : this.getFontList().getItems()) {
			// Font Family found in all google fonts list
			if (item.getFamily().equals(family)) {
//				System.out.println("Font found:" + item.getFamily());
				for (Entry<String, String> file : item.getFiles().entrySet()) {
					// Font variant found in files
//					System.out.println(file.getKey());
					if (file.getKey().equals(GoogleFontVariants.get(variant.toLowerCase()))) {
						System.out.println("Font found:" + item.getFamily() + "-" + variant);
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

	public WebfontList getFontList() throws IOException {
		return webFonts.webfonts().list().setKey(WEBFONT_API_KEY).execute();
	}

	public Webfonts getWebFonts() {
		return webFonts;
	}

	public void setWebFonts(Webfonts webFonts) {
		this.webFonts = webFonts;
	}

	public WebfontList getFonts() {
		return fonts;
	}

	public void setFonts(WebfontList fonts) {
		this.fonts = fonts;
	}
}
