package GUI;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

/**
 * @author César Valdés
 */
public class MainWindow extends JPanel {
	private static final long serialVersionUID = 1L;

	public MainWindow() {
        super(new GridLayout(1, 1));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = null;
        
        MetaGui metaG = new MetaGui();
        tabbedPane.addTab(metaG.getTitle(), icon, metaG.getTabContent(),
        		metaG.getMouseOver());
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        NeuralGui neuralG = new NeuralGui();
        tabbedPane.addTab(neuralG.getTitle(), icon, neuralG.getTabContent(),
        		neuralG.getMouseOver());
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        tabbedPane.addTab("Salir", icon, new JPanel(),
        		"Pulse para salir");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        
        //Add the tabbed pane to this panel.
        this.add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(tabbedPane.getSelectedIndex()==2){
					System.exit(0);
				}
				
			}
        });
    }
    
}