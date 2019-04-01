package com.etp.resumeg.resumeg;

import java.io.IOException;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.webfonts.Webfonts;
import com.google.api.services.webfonts.model.WebfontList;

public class GoogleWebFontService {
	// API Key: AIzaSyDfSsn4RIfbYsY82qGaqnLEQTlUJ_R1mDM
	private final String WEBFONT_API_KEY = "AIzaSyDfSsn4RIfbYsY82qGaqnLEQTlUJ_R1mDM";

	private final String APPLICATION_NAME = "resumeg";

	private final NetHttpTransport httpTransport = new NetHttpTransport();
	private final JacksonFactory jacksonFactory = new JacksonFactory();

	private Webfonts webFonts = null;
	private WebfontList fonts = null;

	public GoogleWebFontService() {
		setWebFonts(new Webfonts.Builder(httpTransport, jacksonFactory, new HttpRequestInitializer() {

			@Override
			public void initialize(HttpRequest request) throws IOException {
				// TODO Auto-generated method stub
			}
		}).setApplicationName(APPLICATION_NAME).build());
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
