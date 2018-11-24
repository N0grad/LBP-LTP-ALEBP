import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LTPLowerThreadGray implements Runnable {

	private Thread t;
	private String fromPath;
	private String toPath;
	private String nomFichier;
	private int seuil;

	LTPLowerThreadGray(String fromPath, String toPath, String nomFichier, int seuil) {
		this.fromPath = fromPath;
		this.toPath = toPath;
		this.nomFichier = nomFichier;
		this.seuil = seuil;
	}

	public void run() {
		// on récupère l'image
		BufferedImage fromImage = null;
		File fromFile = null;

		try {
			fromFile = new File(fromPath + "\\" + nomFichier);
			fromImage = ImageIO.read(fromFile);
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

		// image dimension
		int width = fromImage.getWidth();
		int height = fromImage.getHeight();

		// on créer la nouvelle image (-2 car LBP fait des carrés)
		BufferedImage toImage = new BufferedImage(width - 2, height - 2, BufferedImage.TYPE_INT_RGB);

		// on parcoure l'image (GRAYSCALE)
		// On commence à 1 et fini à la longueur - 1 car on veut un carré entier
		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				
				//échelle de gris du pixel central
				int grayCentral = new Color(fromImage.getRGB(x, y)).getRed();
				
				//1 si le voisin à une valeur plus grand que le pixel central, sinon 0.
				int[] oneOrZero = new int[8];
				
				if (new Color(fromImage.getRGB(x-1, y-1)).getRed() < grayCentral - seuil) {
					oneOrZero[0] = 1;
				}
				if (new Color(fromImage.getRGB(x, y-1)).getRed() < grayCentral - seuil) {
					oneOrZero[1] = 1;
				}
				if (new Color(fromImage.getRGB(x+1, y-1)).getRed() < grayCentral - seuil) {
					oneOrZero[2] = 1;
				}
				if (new Color(fromImage.getRGB(x+1, y)).getRed() < grayCentral - seuil) {
					oneOrZero[3] = 1;
				}
				if (new Color(fromImage.getRGB(x+1, y+1)).getRed() < grayCentral - seuil) {
					oneOrZero[4] = 1;
				}
				if (new Color(fromImage.getRGB(x, y+1)).getRed() < grayCentral - seuil) {
					oneOrZero[5] = 1;
				}
				if (new Color(fromImage.getRGB(x-1, y+1)).getRed() < grayCentral - seuil) {
					oneOrZero[6] = 1;
				}
				if (new Color(fromImage.getRGB(x-1, y)).getRed() < grayCentral - seuil) {
					oneOrZero[7] = 1;
				}
				
				int[] weights = new int[] {1, 2, 4, 8, 16, 32, 64, 128};
				int lbp = 0;
				
				for (int i = 0; i < oneOrZero.length; i++) {
					if (oneOrZero[i] == 1) {
						lbp += weights[i];
					}
				}
				
				Color gray = new Color(lbp, lbp, lbp);

				toImage.setRGB(x-1, y-1, gray.getRGB());
			}
		}

		// write image
		try {
			File file = new File(toPath + "\\LOWER_" + nomFichier);
			ImageIO.write(toImage, "jpg", file);
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
		
		System.out.println(System.currentTimeMillis());
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}


}
