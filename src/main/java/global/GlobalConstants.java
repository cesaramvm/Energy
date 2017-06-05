package global;

import global.models.Problem;

public abstract class GlobalConstants {

	protected static final String FULLPATH = "ProjectData/N-fulldataset.csv";
	protected static final String TRAINPATH = "ProjectData/N-train.csv";
	protected static final String TESTPATH = "ProjectData/N-test.csv";
	protected static final Problem problem = new Problem("ProjectData/O-data.txt").saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);
	
	protected GlobalConstants() {
	}

}
