package resources;

import java.awt.Color;

public final class AppConstants {
	
	public static final String COOKIE_FILE_PATH = "cookie.txt";
	public static final int MAIN_FRAME_WIDTH = 1000;
	public static final int MAIN_FRAME_HEIGHT = 600;
	
	public static final int SIDEBAR_WIDTH = 150;
	public static final int HEADER_PANEL_HEIGHT = 100;

	public static final float HEADER_TITLE_PANEL_SCALE_WIDTH = 0.33f;
	
	public static final int LOG_IN_DIALOG_WIDTH = 400;
	public static final int LOG_IN_DIALOG_HEIGHT = 300;
	public static final String LOG_IN_BUTTON_TEXT = "Logg inn";
	public static final String LOG_IN_ERROR_TEXT = "Brukernavn/passord stemmer ikke overens.";
	public static final Color LOG_IN_ERROR_TEXT_COLOR = Color.red;
	
	public static final String SIDE_BAR_INBOX_TEXT = "Innboks";
	public static final String SIDE_BAR_CREATE_EVENT_TEXT = "Opprett avtale";
	public static final String SIDE_BAR_LOGOUT_TEXT = "Logg ut";
	public static final String SIDE_BAR_CALENDAR_TEXT = "Kalender";
	
	// Maybe not that necessary?
	public static final String CALENDAR_HEADER_TEXT = "Ukeskalender";
	public static final String INBOX_HEADER_TEXT = "Din innboks";
	public static final String APPOINTMENT_HEADER_TEXT = "Opprett en avtale";
	public static final String SHOW_CALENDAR_LABEL_TEXT = "Vis kalender til:";
	public static final String SHOW_OTHER_CALENDARS_BUTTON_TEXT = "Vis";
	
	public static final Color LOGIN_BG_COLOR = Color.lightGray;
	public static final Color SIDE_BAR_BG_COLOR = Color.lightGray;
	public static final Color HEADER_BG_COLOR = Color.gray;

	private AppConstants() {
		throw new AssertionError();
	}
}
