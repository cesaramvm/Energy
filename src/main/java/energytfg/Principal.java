package energytfg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import GUI.MainWindow;

/**
 * @author César Valdés
 */
// raul cabido
// juanjo
// montemayor
// jose velez
// buenaposada
public class Principal {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
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
