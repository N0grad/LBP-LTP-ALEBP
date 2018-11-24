import java.io.File;

public class LTP {

	public static final String FROMPATH = "C:\\Users\\John_DOE\\eclipse-workspace\\Classification\\RepertoireImage\\from";
	public static final String TOPATH = "C:\\Users\\John_DOE\\eclipse-workspace\\Classification\\RepertoireImage\\to";
	public static final String LTPPATH = "C:\\Users\\John_DOE\\eclipse-workspace\\Classification\\RepertoireImage\\ltp";

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();
		System.out.println(startTime);
		
		// Conversion des images en noir et blanc (bleu domiant)
		/*File folder = new File(FROMPATH);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (!listOfFiles[i].getName().equals("Thumbs.db")) {
				ConversionEnGris.main(FROMPATH, TOPATH, listOfFiles[i].getName());
			}
		}*/

		// Conversion des images en LBP
		File folder2 = new File(TOPATH);
		File [] listOfFiles2 = folder2.listFiles();
		for (int i = 0; i < listOfFiles2.length; i++) {
			if (!listOfFiles2[i].getName().equals("Thumbs.db")) {
				LTPUpperThreadGray LTPUpperThread = new LTPUpperThreadGray(TOPATH, LTPPATH, listOfFiles2[i].getName(), 5);
				LTPUpperThread.start();
				LTPLowerThreadGray LTPLowerThread = new LTPLowerThreadGray(TOPATH, LTPPATH, listOfFiles2[i].getName(), 5);
				LTPLowerThread.start();
			}
		}

	}
}