import java.io.File;

public class AELBP {
	public static final String FROMPATH = "C:\\Users\\John_DOE\\eclipse-workspace\\Classification\\RepertoireImage\\from";
	public static final String TOPATH = "C:\\Users\\John_DOE\\eclipse-workspace\\Classification\\RepertoireImage\\to";
	public static final String AELBPPATH = "C:\\Users\\John_DOE\\eclipse-workspace\\Classification\\RepertoireImage\\aelbp";

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();
		System.out.println(startTime);

		// Conversion des images en LBP
		File folder2 = new File(TOPATH);
		File[] listOfFiles2 = folder2.listFiles();
		for (int i = 0; i < listOfFiles2.length; i++) {
			if (!listOfFiles2[i].getName().equals("Thumbs.db")) {
				AELBPThreadGray aelbpThread = new AELBPThreadGray(TOPATH, AELBPPATH, listOfFiles2[i].getName());
				aelbpThread.start();
			}
		}

	}
}
