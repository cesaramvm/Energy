package Util;

import Models.Problem;

public abstract class GlobalConstants {

	protected String FULLPATH = "ProjectData/N-fulldataset.csv";
	protected String TRAINPATH = "ProjectData/N-train.csv";
	protected String TESTPATH = "ProjectData/N-test.csv";
	protected Problem problem = new Problem("ProjectData/O-data.txt").saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);
	
}
