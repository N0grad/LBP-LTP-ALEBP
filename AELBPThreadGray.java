import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AELBPThreadGray implements Runnable {

	private Thread t;
	private String fromPath;
	private String toPath;
	private String nomFichier;

	AELBPThreadGray(String fromPath, String toPath, String nomFichier) {
		this.fromPath = fromPath;
		this.toPath = toPath;
		this.nomFichier = nomFichier;
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

		// on créer la nouvelle image (-4 car LBP fait des carrés)
		BufferedImage toImage = new BufferedImage(width - 4, height - 4, BufferedImage.TYPE_INT_RGB);

		// on parcoure l'image (GRAYSCALE)
		// On commence à 2 et fini à la longueur -2 car on regarde tous les pixels d'un carré de 6 * 6
		for (int y = 2; y < height - 2; y++) {
			for (int x = 2; x < width - 2; x++) {
				
				//échelle de gris du pixel central (2,2)
				int grayCentral = new Color(fromImage.getRGB(x, y)).getRed();
				
				//1 si le voisin à une valeur plus grand que le pixel central, sinon 0.
				int[] oneOrZero = new int[8];
				
				// voisin (1,1)
				if (this.getMoyenneAutour(x-1, y-1, fromImage) >= grayCentral) {
					oneOrZero[0] = 1;
				}
				if (this.getMoyenneAutour(x, y-1, fromImage) >= grayCentral) {
					oneOrZero[1] = 1;
				}
				if (this.getMoyenneAutour(x+1, y-1, fromImage) >= grayCentral) {
					oneOrZero[2] = 1;
				}
				if (this.getMoyenneAutour(x+1, y, fromImage) >= grayCentral) {
					oneOrZero[3] = 1;
				}
				if (this.getMoyenneAutour(x+1, y+1, fromImage) >= grayCentral) {
					oneOrZero[4] = 1;
				}
				if (this.getMoyenneAutour(x, y+1, fromImage) >= grayCentral) {
					oneOrZero[5] = 1;
				}
				if (this.getMoyenneAutour(x-1, y+1, fromImage) >= grayCentral) {
					oneOrZero[6] = 1;
				}
				if (this.getMoyenneAutour(x-1, y, fromImage) >= grayCentral) {
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

				toImage.setRGB(x-2, y-2, gray.getRGB());
			}
		}

		// write image
		try {
			File file = new File(toPath + "\\" + nomFichier);
			ImageIO.write(toImage, "jpg", file);
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
		
		System.out.println(System.currentTimeMillis());
	}

	private int getMoyenneAutour(int x, int y, BufferedImage fromImage) {
		
		int moyenne = (new Color(fromImage.getRGB(x-1, y-1)).getRed() +
			new Color(fromImage.getRGB(x, y-1)).getRed() +
			new Color(fromImage.getRGB(x+1, y-1)).getRed() +
			new Color(fromImage.getRGB(x+1, y)).getRed() +
			new Color(fromImage.getRGB(x+1, y+1)).getRed() +
			new Color(fromImage.getRGB(x, y+1)).getRed() + 
			new Color(fromImage.getRGB(x-1, y+1)).getRed()+
			new Color(fromImage.getRGB(x-1, y)).getRed()) / 8;
		
		return moyenne;
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}


}
