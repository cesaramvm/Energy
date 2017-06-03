package GUI;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.TransferFunctionType;
import NeuralNetwork.NeurophSolver;

/**
 * @author César Valdés
 */
public class NeuralGui extends DefaultTab implements ActionListener {

	private JButton simpleButton, advancedButton;
	private NeurophSolver neurophSolver = new NeurophSolver();

	protected NeuralGui() {
		super("Redes", "Modo Redes Neuronales");

		JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(1, 1));
		simpleButton = new JButton("Entrenamiento simple");
		// TODO
		simpleButton.setToolTipText("Entrena con una sola combinación");
		simpleButton.setMargin(new Insets(3, 5, 3, 5));

		advancedButton = new JButton("Impresion");
		// TODO
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
		if (e.getSource() == simpleButton) {
			String iterationsStr = JOptionPane.showInputDialog(null, "Número de iteraciones (min 1000)", "Iteraciones",
					JOptionPane.QUESTION_MESSAGE);
			if (iterationsStr != null) {
				Class<?>[] propagations = { ResilientPropagation.class, BackPropagation.class };
				Class<?> propagationClass = (Class<?>) JOptionPane.showInputDialog(null, "Clase del optimizador",
						"Optimizadores", JOptionPane.QUESTION_MESSAGE, null, propagations, propagations[0]);
				if (propagationClass != null) {
					String linesStr = JOptionPane.showInputDialog(null, "¿Cuántas veces quieres realizar la prueba? (lineas que aparecerán en el gráfico) Default: 1 (1-100)", "Lineas del gráfico",
							JOptionPane.QUESTION_MESSAGE);
					if (linesStr != null) {
						String learningStr = JOptionPane.showInputDialog(null, "¿Qué learningRate quieres estudiar? (Min 0.0 Max 1.0 Def 0.5)",
								"Learning Rate", JOptionPane.QUESTION_MESSAGE);
						if (learningStr != null) {
							TransferFunctionType[] transfers = { TransferFunctionType.SIN, TransferFunctionType.TANH,
									TransferFunctionType.GAUSSIAN };
							TransferFunctionType transferClass = (TransferFunctionType) JOptionPane.showInputDialog(
									null, "¿Qué función de transferencia?", "Función de transferencia",
									JOptionPane.QUESTION_MESSAGE, null, transfers, transfers[0]);
							if (transferClass != null) {
								String layersStr = "";
								int[] emptyArray = new int[0];
								int[] inputLayersArray = emptyArray;
								int[] layersArray = { 14 };
								while (layersStr != null && inputLayersArray.length == 0) {
									layersStr = JOptionPane.showInputDialog(null,
											"Número de capas ocultas separadas por , (máximo 4 capas)", "Capas ocultas",
											JOptionPane.QUESTION_MESSAGE);
									if (layersStr != null) {
										layersStr = layersStr.replaceAll("\\s", "");
										inputLayersArray = Arrays.asList(layersStr.split(",")).stream()
												.mapToInt(x -> this.giveInteger(x)).filter(x -> this.validLayer(x))
												.toArray();
										if (inputLayersArray.length > 4 || inputLayersArray.length < 1) {
											JOptionPane.showMessageDialog(null,
													"Por favor introduzca entre 1 y 4 capas ocultas.", "Error",
													JOptionPane.WARNING_MESSAGE);
											inputLayersArray = emptyArray;
										} else {
											layersArray = (int[]) ArrayUtils.addAll(layersArray,
													inputLayersArray);
										}
									}
								}
								if (layersStr != null) {
									int[] layers = ArrayUtils.add(layersArray, 1);

									boolean showGraph;
									int reply = JOptionPane.showConfirmDialog(null,
											"¿Quieres que se muestren los gráficos de entrenamiento?", "Gráficos",
											JOptionPane.YES_NO_OPTION);
									if (reply == JOptionPane.YES_OPTION) {
										showGraph = true;
									} else {
										showGraph = false;
									}

									String file = JOptionPane.showInputDialog(null, "Nombre del arhivo de guardado",
											"Archivo", JOptionPane.QUESTION_MESSAGE);
									if (file != null) {
										String fileName = file + ".csv";

										Integer iterationsNum = giveInteger(iterationsStr);
										int iterations = this.validIteration(iterationsNum) ? iterationsNum : 1000;
										Integer linesNum = giveInteger(linesStr);
										int lines = this.validLinesNum(linesNum) ? linesNum : 1;
										Double learningNum = giveDouble(learningStr);
										double learningRate = this.validLR(learningNum) ? learningNum : 0.5;
										
										new Thread(() -> neurophSolver.simpleSearch(iterations, propagationClass, lines, learningRate, transferClass, layers, showGraph, fileName)).start();
									}
								}
							}
						}
					}
				}
			}
		} else if (e.getSource() == advancedButton) {
			
			
			int iterations = 1000;
			Class<? extends Object> propagationTypeClass = ResilientPropagation.class;
			ArrayList<Double> lrates = new ArrayList<>(Arrays.asList(0.2, 0.3, 0.4));;
			TransferFunctionType transfer = TransferFunctionType.SIN;
			int maxHiddenLayers = 3;
			Integer[] neuronsInLayers = {4,5,6};
			//Integer[] possibleNeurons = { 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
			//Integer[] possible3Neurons = { 6, 7, 8, 9, 10, 11, 12, 13, 14 };
			boolean showGraph = true;
			String fileName = "Name.csv";
			new Thread(() -> neurophSolver.advancedLRSearch(iterations, propagationTypeClass, lrates, transfer, maxHiddenLayers, neuronsInLayers, 
					showGraph, fileName)).start();
			
		}

	}

	/*private void neuralSearch() {
		neurophSolver = new NeurophSolver();
		neurophSolver.fullSearch();
		neurophSolver.findBestNetwork();
		String fileRoute = "Net.nnet";
		neurophSolver.networkTest(fileRoute, "FinalNnetOut.csv");

	}*/

	private boolean validLR(Double learningNum) {
		if (learningNum <= 0 || learningNum > 10) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validLinesNum(Integer linesNum) {
		if (linesNum <= 0 || linesNum > 100) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validIteration(Integer iterationsNum) {
		if (iterationsNum < 100 || iterationsNum > 1000000) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validLayer(int number) {
		if (number <= 0 || number > 20) {
			return false;
		} else {
			return true;
		}
	}

	private Integer giveInteger(String number) {
		Integer numberInt;
		try {
			numberInt = Integer.parseInt(number);
		} catch (Exception e) {
			return 0;
		}
		return numberInt;
	}

	private Double giveDouble(String number) {
		Double numberInt;
		try {
			numberInt = Double.parseDouble(number);
		} catch (Exception e) {
			return 0.0;
		}
		return numberInt;
	}

}
