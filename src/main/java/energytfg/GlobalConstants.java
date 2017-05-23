package energytfg;

import Models.Problem;

public interface GlobalConstants {

	String FULLPATH = "ProjectData/N-fulldataset.csv";
	String TRAINPATH = "ProjectData/N-train.csv";
	String TESTPATH = "ProjectData/N-test.csv";
	Problem problem = new Problem("ProjectData/O-data.txt").saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);
	
}
