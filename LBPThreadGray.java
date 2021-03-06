import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LBPThreadGray implements Runnable {

	private Thread t;
	private String fromPath;
	private String toPath;
	private String nomFichier;

	LBPThreadGray(String fromPath, String toPath, String nomFichier) {
		this.fromPath = fromPath;
		this.toPath = toPath;
		this.nomFichier = nomFichier;
	}

	public void run() {
		
		long depart = System.currentTimeMillis();
		
		// on r�cup�re l'image
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

		// on cr�er la nouvelle image (-2 car LBP fait des carr�s)
		BufferedImage toImage = new BufferedImage(width - 2, height - 2, BufferedImage.TYPE_INT_RGB);

		// on parcoure l'image (GRAYSCALE)
		// On commence � 1 et fini � la longueur - 1 car on veut un carr� entier
		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				
				//�chelle de gris du pixel central
				int grayCentral = new Color(fromImage.getRGB(x, y)).getRed();
				System.out.println("GrayCentral : "+grayCentral);
				//1 si le voisin � une valeur plus grand que le pixel central, sinon 0.
				int[] oneOrZero = new int[8];
				
				if (new Color(fromImage.getRGB(x-1, y-1)).getRed() >= grayCentral) {
					System.out.println("1 : "+new Color(fromImage.getRGB(x-1, y-1)).getRed());
					oneOrZero[0] = 1;
				}
				if (new Color(fromImage.getRGB(x, y-1)).getRed() >= grayCentral) {
					System.out.println("2 : "+new Color(fromImage.getRGB(x, y-1)).getRed());
					oneOrZero[1] = 1;
				}
				if (new Color(fromImage.getRGB(x+1, y-1)).getRed() >= grayCentral) {
					System.out.println("3 : "+new Color(fromImage.getRGB(x+1, y-1)).getRed());
					oneOrZero[2] = 1;
				}
				if (new Color(fromImage.getRGB(x+1, y)).getRed() >= grayCentral) {
					System.out.println("4 : "+new Color(fromImage.getRGB(x+1, y)).getRed());
					oneOrZero[3] = 1;
				}
				if (new Color(fromImage.getRGB(x+1, y+1)).getRed() >= grayCentral) {
					System.out.println("5 : "+new Color(fromImage.getRGB(x+1, y+1)).getRed());
					oneOrZero[4] = 1;
				}
				if (new Color(fromImage.getRGB(x, y+1)).getRed() >= grayCentral) {
					System.out.println("6 : "+new Color(fromImage.getRGB(x, y+1)).getRed());
					oneOrZero[5] = 1;
				}
				if (new Color(fromImage.getRGB(x-1, y+1)).getRed() >= grayCentral) {
					System.out.println("7 : "+new Color(fromImage.getRGB(x-1, y+1)).getRed());
					oneOrZero[6] = 1;
				}
				if (new Color(fromImage.getRGB(x-1, y)).getRed() >= grayCentral) {
					System.out.println("8 : "+new Color(fromImage.getRGB(x-1, y)).getRed());
					oneOrZero[7] = 1;
				}
				
				int[] weights = new int[] {1, 2, 4, 8, 16, 32, 64, 128};
				int lbp = 0;
				
				System.out.println();
				for (int value : oneOrZero) {
					System.out.print(value +" - ");
				}
				
				for (int i = 0; i < oneOrZero.length; i++) {
					lbp += oneOrZero[i] * weights[i];
				}
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Color gray = new Color(lbp, lbp, lbp);

				toImage.setRGB(x-1, y-1, gray.getRGB());
			}
		}

		// write image
		try {
			File file = new File(toPath + "\\" + nomFichier);
			ImageIO.write(toImage, "jpg", file);
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
		
		System.out.println("Image ("+width+"*"+height+") Temps : "+ (System.currentTimeMillis()-depart));
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}


}
