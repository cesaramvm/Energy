/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package GUI;

/*
 * TabbedPaneDemo.java requires one additional file:
 *   images/middle.gif.
 */

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

public class MainWindow extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainWindow() {
        super(new GridLayout(1, 1));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        //ImageIcon icon = createImageIcon("images/middle.gif");
        ImageIcon icon = null;
        
        MetaGui metaG = new MetaGui();
        tabbedPane.addTab(metaG.getTitle(), icon, metaG.getTabContent(),
        		metaG.getMouseOver());
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        NeuralGui neuralG = new NeuralGui();
        tabbedPane.addTab(neuralG.getTitle(), icon, neuralG.getTabContent(),
        		neuralG.getMouseOver());
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        /*
        OptionsGui optionsG = new OptionsGui();
        tabbedPane.addTab(optionsG.getTitle(), icon, optionsG.getTabContent(),
        		optionsG.getMouseOver());
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        */
        
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
    /*
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));
        //JLabel filler = new JLabel(text);
        //filler.setHorizontalAlignment(JLabel.CENTER);
        //panel.setLayout(new GridLayout(1, 1));
        //panel.add(filler);
        //return panel;
        
        JButton bt2 = new JButton("Entrenamiento avanzado");
        JButton bt3 = new JButton("Imprimir en excel"); //creamos el boton con una imagen
        bt2.setMargin(new Insets(1, 1, 1, 1));
      //color de texto para el boton
        bt2.setForeground(Color.blue);
        JButton bt4 = new JButton("asd"); //creamos el boton con una imagen
        //Instanciando botones con texto
        JButton bt1 = new JButton("Entrenamiento simple");
        //margenes para texto en boton
        bt1.setMargin(new Insets(3, 5, 3, 5));
        //color de fondo del boton
        bt1.setBackground(Color.orange);
        panel.add(bt1);
        panel.add(bt2);
        panel.add(bt3);
        panel.add(bt4);
        return panel;
        
    }
    */
    /** Returns an ImageIcon, or null if the path was invalid.
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MainWindow.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    } */
    
}