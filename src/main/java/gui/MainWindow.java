package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * @author César Valdés
 */
public class MainWindow extends JPanel {
	private static final long serialVersionUID = 1L;
	private static MetaGui metaGui = new MetaGui();
	private static NeuralGui neuralGui = new NeuralGui();

	public MainWindow() {
		super(new GridLayout(1, 1));

		JTabbedPane tabbedPane = new JTabbedPane();
		ImageIcon icon = null;

		tabbedPane.addTab(neuralGui.getTitle(), icon, neuralGui.getTabContent(), neuralGui.getMouseOver());
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
		tabbedPane.addTab(metaGui.getTitle(), icon, metaGui.getTabContent(), metaGui.getMouseOver());
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_1);

		// Add the tabbed pane to this panel.
		this.add(tabbedPane);

		// The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	/*
	 * raul cabido juanjo montemayor jose velez buenaposada
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> { 
			UIManager.put("swing.boldMetal", Boolean.FALSE);
			createAndShowGUI(); 
			});
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Energy Problem TFG");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(400, 300));
		frame.add(new MainWindow(), BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

}