package game.client.bean;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class loadImage {

	public static BufferedImage image;
	public static BufferedImage entities, ene;
	public static BufferedImage player, enemy;

	public static void init() {
		image = imageLoader("/whte.png");
		entities = imageLoader("/airplane.png");
		ene = imageLoader("/enemy.png");
		crop();

	}

	public static BufferedImage imageLoader(String path) {
		try {
			return ImageIO.read(loadImage.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public static void crop() {
		enemy = ene.getSubimage(0, 0, 90, 70);
		player = entities.getSubimage(0, 0, 90, 70);
	}
}
