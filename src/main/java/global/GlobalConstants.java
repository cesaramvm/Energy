package global;

import global.models.Problem;

public abstract class GlobalConstants {

	protected static String fullPath = "ProjectData/N-fulldataset.csv";
	protected static String trainPath = "ProjectData/N-train.csv";
	protected static String testPath = "ProjectData/N-test.csv";
	protected static Problem problem = new Problem("ProjectData/O-data.txt").saveNormalizedData(fullPath, trainPath, testPath);
	
	protected GlobalConstants() {
	}

}
