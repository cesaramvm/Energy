package GUI;

import javax.swing.JComponent;

/**
 * @author César Valdés
 */
public abstract class DefaultTab {

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
