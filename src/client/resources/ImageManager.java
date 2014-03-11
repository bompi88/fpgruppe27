package resources;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class ImageManager {

	private static final String RESOURCE_PATH = "/resources/";

	public static BufferedImage deleteIcon;

	private static volatile ImageManager INSTANCE = null;

	private ImageManager() {

		INSTANCE = this;

		try {
			deleteIcon = ImageIO.read(getClass().getResource(
					RESOURCE_PATH + "deer.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public static BufferedImage getImage(String icon) {
		if (icon.equals(IconType.DELETE_ICON.toString())) {
			return deleteIcon;
		}
		return null;
	}

	public enum IconType {
		DELETE_ICON("delete_icon");

		private IconType(final String text) {
			this.text = text;
		}

		private final String text;

		@Override
		public String toString() {
			return text;
		}
	}
}
