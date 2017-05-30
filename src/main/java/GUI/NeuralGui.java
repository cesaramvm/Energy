package GUI;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.TransferFunctionType;
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
			
			int iterations = 100; //min 100
			// BackPropagation or ResilientPropagation
			
			String[] values = {"ResilientPropagation", "BackPropagation"};

			Object selected = JOptionPane.showInputDialog(null, "Elige la clase de propagación", "Propagación", JOptionPane.DEFAULT_OPTION, null, values, "ResilientPropagation");
			if ( selected != null ){//null if the user cancels. 
			    String selectedString = selected.toString();
			    //do something
			}else{
			    System.out.println("User cancelled");
			}
			Class<?> propagationClass = ResilientPropagation.class;
			boolean showGraph = true; 
			String fileName = "Name.csv";
			
			
			int times = 20;
			double learningRate = 0.3;
			//(TransferFunctionType.SIN, TransferFunctionType.TANH, TransferFunctionType.GAUSSIAN)
			TransferFunctionType transfer = TransferFunctionType.TANH;
			int[] hiddenLayers = { 14, 7, 7, 1 };
			
			
			neurophSolver = new NeurophSolver();
			neurophSolver.simpleSearch(iterations, propagationClass, showGraph, fileName, times, learningRate, transfer, hiddenLayers);
			
			
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
