package resources;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public final class ImageManager {

	private static final String RESOURCE_PATH = "/resources/";

	private static BufferedImage deleteIconImage;
	private static BufferedImage deleteIconHoverImage;
	private static BufferedImage appIconImage;
	private static BufferedImage attendingIconImage;
	private static BufferedImage declinedIconImage;
	private static BufferedImage invitedIconImage;
	private static BufferedImage letterOpenedImage;
	private static BufferedImage letterClosedImage;
	
	private static ImageIcon attendingIcon;
	private static ImageIcon declinedIcon;
	private static ImageIcon invitedIcon;
	private static ImageIcon appIcon;
	private static ImageIcon deleteIcon;
	private static ImageIcon deleteIconHover;
	private static ImageIcon letterOpenedIcon;
	private static ImageIcon letterClosedIcon;
	
	private static volatile ImageManager INSTANCE = null;

	private ImageManager() {

		INSTANCE = this;

		try {
			appIconImage = ImageIO.read(getClass().getResource(
					RESOURCE_PATH + "deer.png"));
			deleteIconImage = ImageIO.read(getClass().getResource(
					RESOURCE_PATH + "delete_icon.png"));
			deleteIconHoverImage = ImageIO.read(getClass().getResource(
					RESOURCE_PATH + "delete_icon_hover.png"));
			attendingIconImage = ImageIO.read(getClass().getResource(
					RESOURCE_PATH + "person_icon_green.png"));
			declinedIconImage = ImageIO.read(getClass().getResource(
					RESOURCE_PATH + "person_icon_red.png"));
			invitedIconImage = ImageIO.read(getClass().getResource(
					RESOURCE_PATH + "person_icon_yellow.png"));
			letterClosedImage = ImageIO.read(getClass().getResource(
					RESOURCE_PATH + "letter_closed_icon.png"));
			letterOpenedImage = ImageIO.read(getClass().getResource(
					RESOURCE_PATH + "letter_open_icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		appIcon = new ImageIcon(resizeImage(appIconImage, 120, 90));
		deleteIcon = new ImageIcon(resizeImage(deleteIconImage, 15, 15));
		deleteIconHover = new ImageIcon(resizeImage(deleteIconHoverImage, 15, 15));
		attendingIcon = new ImageIcon(resizeImage(attendingIconImage, 10, 10));
		declinedIcon = new ImageIcon(resizeImage(declinedIconImage, 10, 10));
		invitedIcon = new ImageIcon(resizeImage(invitedIconImage, 10, 10));
		letterClosedIcon = new ImageIcon(resizeImage(letterClosedImage, 20, 20));
		letterOpenedIcon = new ImageIcon(resizeImage(letterOpenedImage, 20, 20));
	}

	public static ImageManager getInstance() {
		if (INSTANCE == null) {
			synchronized (ImageManager.class) {
				INSTANCE = new ImageManager();
			}
		}
		return INSTANCE;
	}

	/**
	 * Resizes an image to given width and height.
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage resizeImage(BufferedImage image, int width,
			int height) {
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY));
		g2d.drawImage(image, 0, 0, width, height, null);
		g2d.dispose();
		return bi;
	}
	
	public static ImageIcon getAppIcon() {
		return appIcon;
	}

	public static ImageIcon getDeleteIcon() {
		return deleteIcon;
	}

	public static ImageIcon getDeleteIconHover() {
		return deleteIconHover;
	}
	
	public static ImageIcon getAttendingIcon() {
		return attendingIcon;
	}

	public static ImageIcon getDeclinedIcon() {
		return declinedIcon;
	}

	public static ImageIcon getInvitedIcon() {
		return invitedIcon;
	}
	
	public static ImageIcon getLetterOpenedIcon() {
		return letterOpenedIcon;
	}

	public static ImageIcon getLetterClosedIcon() {
		return letterClosedIcon;
	}

}