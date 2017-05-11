package GUI;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import NeuralNetwork.NeurophSolution;

public class NeuralGui extends DefaultTab implements ActionListener{
	
	private JButton bt1, bt2;
	private NeurophSolution ns = new NeurophSolution(FULLPATH, TRAINPATH, TESTPATH);
	
	protected NeuralGui() {
		super("Redes", "Modo Redes Neuronales");
		
		JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));
        bt1 = new JButton("Busqueda");
        //TODO
        bt1.setToolTipText("Explora bla bla bla bla bla bla bla");
        bt1.setMargin(new Insets(3, 5, 3, 5));
        
        bt2 = new JButton("Impresion");
        //TODO
        bt2.setToolTipText("Imprime bla bla bla bla bla bla bla");
        bt2.setMargin(new Insets(1, 1, 1, 1));
        
        panel.add(bt1);
        panel.add(bt2);
        this.setTabContent(panel);
        bt1.addActionListener(this);
        bt2.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==bt1){
			
			this.neuralSearch();
        } else if(e.getSource()==bt2){
			
			System.exit(0);
        }
		
	}
	

    private void neuralSearch(){
    	
      ns.fullSearch(problem);
      ns.findBestNetwork1(problem);
      String fileRoute = "Net.nnet";
      ns.networkTest(fileRoute, problem, "FinalNnetOut.csv");
    	
    }

}
