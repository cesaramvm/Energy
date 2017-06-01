package GUI;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.TransferFunctionType;
import NeuralNetwork.NeurophSolver;
import Util.Optimizers.LSBIEvaluationOptimizer;
import Util.Optimizers.LSFIEvaluationOptimizer;
import Util.Optimizers.RandomEvaluationOptimizer;

/**
 * @author C�sar Vald�s
 */
public class NeuralGui extends DefaultTab implements ActionListener {

	private JButton simpleButton, advancedButton;
	private NeurophSolver neurophSolver;

	protected NeuralGui() {
		super("Redes", "Modo Redes Neuronales");

		JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(1, 1));
		simpleButton = new JButton("Entrenamiento simple");
		// TODO
		simpleButton.setToolTipText("Entrena con una sola combinaci�n");
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
			// TODO DIFERENCIA TIMES E ITERACION

			String iterationsStr = JOptionPane.showInputDialog(null, "N�mero de iteraciones m�nimo 100", "Iteraciones",
					JOptionPane.QUESTION_MESSAGE);
			if (iterationsStr != null) {
				Class<?>[] propagations = { ResilientPropagation.class, BackPropagation.class,
						LSBIEvaluationOptimizer.class };
				Class<?> propagationClass = (Class<?>) JOptionPane.showInputDialog(null, "Clase del optimizador",
						"Optimizadores", JOptionPane.QUESTION_MESSAGE, null, propagations, propagations[0]);

				if (propagationClass != null) {
					String timesStr = JOptionPane.showInputDialog(null, "N�mero de times? m�nimo 100", "times",
							JOptionPane.QUESTION_MESSAGE);
					if (timesStr != null) {
						String learningStr = JOptionPane.showInputDialog(null, "�Qu� learningRate quieres estudiar?",
								"Learning Rate", JOptionPane.QUESTION_MESSAGE);
						if (learningStr != null) {
							TransferFunctionType[] transfers = { TransferFunctionType.SIN, TransferFunctionType.TANH,
									TransferFunctionType.GAUSSIAN };
							TransferFunctionType transferClass = (TransferFunctionType) JOptionPane.showInputDialog(
									null, "�Qu� funci�n de transferencia?", "Funci�n de transferencia",
									JOptionPane.QUESTION_MESSAGE, null, transfers, transfers[0]);
							if (transferClass != null) {
								String layersStr = "";
								int[] emptyArray = new int[0];
								int[] inputLayersArray = emptyArray;
								int[] layersArray = { 14 };
								while (layersStr != null && inputLayersArray.length == 0) {
									layersStr = JOptionPane.showInputDialog(null,
											"N�mero de capas ocultas separadas por , (m�ximo 4 capas)", "Capas ocultas",
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
									layersArray = ArrayUtils.add(layersArray, 1);

									boolean showGraph = false;
									int reply = JOptionPane.showConfirmDialog(null,
											"�Quieres que se muestren los gr�ficos de entrenamiento?", "Gr�ficos",
											JOptionPane.YES_NO_OPTION);
									if (reply == JOptionPane.YES_OPTION) {
										showGraph = true;
									}

									String fileName = JOptionPane.showInputDialog(null, "Nombre del arhivo de guardado",
											"Archivo", JOptionPane.QUESTION_MESSAGE);
									if (fileName != null) {
										fileName += ".csv";

										neurophSolver = new NeurophSolver();
										//iterationsStr, timesStr learningStr
										Integer iterationsNum = giveInteger(iterationsStr);
										//TODO DEFAULT Y PONERLO EN EL GUI
										int iterations = this.validIteration(iterationsNum) ? iterationsNum : 1;
										Integer timesNum = giveInteger(timesStr);
										//TODO DEFAULT Y PONERLO EN EL GUI
										int times = this.validTime(timesNum) ? timesNum : 1;
										Double learningNum = giveDouble(learningStr);
										double learningRate = this.validLR(learningNum) ? learningNum : 0.5;
										
										neurophSolver.simpleSearch(iterations, propagationClass, times, learningRate, transferClass, layersArray, showGraph, fileName);

									}
								}

							}

						}
					}

				}
			}

		} else if (e.getSource() == advancedButton) {

			System.exit(0);
		}

	}

	private boolean validLR(Double learningNum) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean validTime(Integer timesNum) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean validIteration(Integer iterationsNum) {
		// TODO Auto-generated method stub
		return false;
	}

	private void neuralSearch() {
		neurophSolver = new NeurophSolver();
		neurophSolver.fullSearch();
		neurophSolver.findBestNetwork();
		String fileRoute = "Net.nnet";
		neurophSolver.networkTest(fileRoute, "FinalNnetOut.csv");

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
