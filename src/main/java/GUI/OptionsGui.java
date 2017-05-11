package GUI;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OptionsGui extends DefaultTab implements ActionListener{

	private JButton bt1;
	
	protected OptionsGui() {
		super("Opciones", "Pulsa para ver las opciones");
		
		JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));

        bt1 = new JButton("Salir");
        //TODO
        bt1.setToolTipText("Salir del programa");
        bt1.setMargin(new Insets(3, 5, 3, 5));
        panel.add(bt1);
        this.setTabContent(panel);
        bt1.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==bt1){
			System.exit(0);
        }
		
	}

}
