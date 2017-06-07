package global;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.Normalizer;

/**
 * @author César Valdés
 */
public class Problem {

	private static Problem instance = null;
	private static final String FULLPATH = "ProjectData/N-fulldataset.csv";
	private static final String TRAINPATH = "ProjectData/N-train.csv";
	private static final String TESTPATH = "ProjectData/N-test.csv";
	private static final String ORIGENPATH = "ProjectData/O-data.txt";
	private Map<Integer, YearInfo> years = new HashMap<>();
	private int numParams;
	private static Normalizer normalizer;
	// 1 MEANS: KEEPING PROPORCIONALITY IN RANGE (0,1]
	// 0 MEANS: NOT KEEPING PROPORCIONALITY IN RANGE [0,1] Abraham
	private static final int NORMALIZATION_TYPE = 0;
	private static final double NORMALIZATION_RANGE_PERCENTAGE = 0.15;
	
	protected Problem() {
		String cadena;
		FileReader f;
		HashMap<Integer, YearInfo> auxYears = new HashMap<>();
		ArrayList<Double> maxs = new ArrayList<>();
		ArrayList<Double> mins = new ArrayList<>();
		try {
			f = new FileReader(ORIGENPATH);

			BufferedReader b = new BufferedReader(f);

			while ((cadena = b.readLine()) != null) {

				cadena = cadena.replace(",", ".");
				String[] auxString = cadena.split(" ");
				numParams = auxString.length - 2;
				YearInfo year = new YearInfo(Integer.parseInt(auxString[0]), Double.parseDouble(auxString[1]));
				if (maxs.isEmpty()) {
					for (int j = 0; j < numParams + 1; j++) {
						maxs.add(Double.NEGATIVE_INFINITY);
						mins.add(Double.POSITIVE_INFINITY);
					}
				}

				for (int i = 0; i < numParams + 1; i++) {
					double number = Double.parseDouble(auxString[i + 1]);
					if (i != 0) {
						year.insertData(number);
					}
					if (number > maxs.get(i)) {
						maxs.set(i, number);
					}
					if (number < mins.get(i)) {
						mins.set(i, number);
					}
				}
				auxYears.put(year.getYear(), year);
			}
			b.close();
		} catch (IOException ex) {
			Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
		}

		Normalizer norm = new Normalizer(numParams + 1, NORMALIZATION_TYPE, NORMALIZATION_RANGE_PERCENTAGE);
		years = norm.normalizeData(auxYears, maxs, mins);
		normalizer = norm;
	}
	
	public static Problem getInstance() {
	      if(instance == null) {
	    	  instance = new Problem().saveNormalizedData();
	      }
	      return instance;
	   }

	public Map<Integer, YearInfo> getYears() {
		return years;
	}

	public int getNumParams() {
		return numParams;
	}

	public Normalizer getNormalizer() {
		return normalizer;
	}

	public static String getFullpath() {
		return FULLPATH;
	}

	public static String getTrainpath() {
		return TRAINPATH;
	}

	public static String getTestpath() {
		return TESTPATH;
	}

	private Problem saveNormalizedData() {

		try {
			String fileFormat = "UTF-8";
			PrintWriter fullwriter = new PrintWriter(FULLPATH, fileFormat);
			PrintWriter testwriter = new PrintWriter(TESTPATH, fileFormat);
			PrintWriter trainwriter = new PrintWriter(TRAINPATH, fileFormat);
			for (int i = 1981; i <= 2011; i++) {
				PrintWriter actualWriter = (i <= 2005) ? trainwriter : testwriter;
				YearInfo year = years.get(i);
				List<Double> yearData = year.getFullData();
				Double yearObj = year.getObj();
				for (Double d : yearData) {
					actualWriter.print(d + ";");
					fullwriter.print(d + ";");
				}
				actualWriter.println(yearObj);
				fullwriter.println(yearObj);
			}
			fullwriter.close();
			testwriter.close();
			trainwriter.close();

		} catch (FileNotFoundException | UnsupportedEncodingException ex) {
			Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
		}
		return this;

	}
}
