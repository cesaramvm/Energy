package GUI;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import NeuralNetwork.NeurophSolver;

/**
 * @author César Valdés
 */
public class NeuralGui extends DefaultTab implements ActionListener{
	
	private JButton simpleButton, advancedButton;
	private NeurophSolver neurophSolver;
	
	protected NeuralGui() {
		super("Redes", "Modo Redes Neuronales");
		
		JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));
        simpleButton = new JButton("Entrenamiento simple");
        //TODO
        simpleButton.setToolTipText("Entrena con una sola combinación");
        simpleButton.setMargin(new Insets(3, 5, 3, 5));
        
        advancedButton = new JButton("Impresion");
        //TODO
        advancedButton.setToolTipText("Imprime bla bla bla bla bla bla bla");
        advancedButton.setMargin(new Insets(1, 1, 1, 1));
        
        panel.add(simpleButton);
        panel.add(advancedButton);
        this.setTabContent(panel);
        simpleButton.addActionListener(this);
        advancedButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==simpleButton){
	      neurophSolver = new NeurophSolver();
	      neurophSolver.simpleSearch();
			
			
        } else if(e.getSource()==advancedButton){
			
			System.exit(0);
        }
		
	}
	

    private void neuralSearch(){
      neurophSolver = new NeurophSolver();
      neurophSolver.fullSearch();
      neurophSolver.findBestNetwork();
      String fileRoute = "Net.nnet";
      neurophSolver.networkTest(fileRoute, "FinalNnetOut.csv");
    	
    }

}
