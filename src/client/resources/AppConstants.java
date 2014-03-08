package resources;
public final class AppConstants {
	
	public static final String COOKIE_FILE_PATH = "cookie.txt";
	public static final int MAIN_FRAME_WIDTH = 1000;
	public static final int MAIN_FRAME_HEIGHT = 600;

	public static final String LOG_IN_BUTTON_TEXT = "Logg inn";

	public static final String LOG_IN_ERROR_TEXT = "Brukernavn/passord stemmer ikke overens.";
	
	public static final String SIDE_BAR_INBOX_TEXT = "Innboks";
	public static final String SIDE_BAR_CREATE_EVENT_TEXT = "Opprett avtale";
	public static final String SIDE_BAR_LOGOUT_TEXT = "Logg ut";
	public static final String SIDE_BAR_CALENDAR_TEXT = "Kalender";
	
	// Maybe not that necessary?
	public static final String CALENDAR_HEADER_TEXT = "Ukeskalender";
	public static final String INBOX_HEADER_TEXT = "Din innboks";
	public static final String APPOINTMENT_HEADER_TEXT = "Opprett en avtale";

	private AppConstants() {
		throw new AssertionError();
	}
}
