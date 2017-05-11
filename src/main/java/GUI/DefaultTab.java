/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javax.swing.JComponent;

import Models.Problem;

/**
 *
 * @author Usuario
 */
public abstract class DefaultTab {
	protected static final String FULLPATH = "ProjectData/N-fulldataset.csv";
	protected static final String TRAINPATH = "ProjectData/N-train.csv";
	protected static final String TESTPATH = "ProjectData/N-test.csv";
	protected static final Problem problem = new Problem("ProjectData/O-data.txt").saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);
	
	protected String mouseOver;
	protected String title;
	protected JComponent tab;
	
	
	protected DefaultTab(String title, String mouseOver) {
		super();
		this.mouseOver = mouseOver;
		this.title = title;
	}


	public String getMouseOver() {
		return mouseOver;
	}


	public String getTitle() {
		return title;
	}


	public JComponent getTabContent() {
		return tab;
	}


	public void setTabContent(JComponent tab) {
		this.tab = tab;
	}
	
	
	
	
}
