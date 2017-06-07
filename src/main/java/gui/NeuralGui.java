package gui;

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
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang3.ArrayUtils;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.TransferFunctionType;

import neuralnetwork.NeurophSolver;

/**
 * @author César Valdés
 */
public class NeuralGui extends DefaultTab implements ActionListener {

	private JButton simpleButton;
	private JButton lRatesdButton;
	private JButton findBestNetworkButton;
	private JButton networkTestButton;

	protected NeuralGui() {
		super("Redes", "Modo Redes Neuronales");

		JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(2, 2));
		simpleButton = new JButton("Entrenamiento simple");
		simpleButton.setToolTipText("Entrena con una sola combinación");
		simpleButton.setMargin(new Insets(3, 5, 3, 5));

		lRatesdButton = new JButton("<html>Entrenamiento de<br />Learning Rates</html>");
		lRatesdButton.setToolTipText("Entrena con varios LearningRates y guarda los resultados");
		lRatesdButton.setMargin(new Insets(1, 1, 1, 1));

		findBestNetworkButton = new JButton("<html>Buscar mejor <br />red neuronal</html>");
		findBestNetworkButton.setToolTipText(
				"Recorre todos  los archivos .nnet (de la carpeta NeurophSolutions/Networks/) e imprime el que genere mejor resultado");
		findBestNetworkButton.setMargin(new Insets(1, 1, 1, 1));

		networkTestButton = new JButton("<html>Test de <br />red neuronal</html>");
		networkTestButton.setToolTipText(
				"Dado el nombre de un archivo .nnet (en la carpeta NeurophSolutions/Networks/) genera la tabla final de resultados");
		networkTestButton.setMargin(new Insets(1, 1, 1, 1));

		panel.add(simpleButton);
		panel.add(lRatesdButton);
		panel.add(findBestNetworkButton);
		panel.add(networkTestButton);
		this.setTabContent(panel);
		simpleButton.addActionListener(this);
		lRatesdButton.addActionListener(this);
		findBestNetworkButton.addActionListener(this);
		networkTestButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == simpleButton) {
			String iterationsStr = JOptionPane.showInputDialog(null, "Número de iteraciones (min 1000)", "Iteraciones",
					JOptionPane.QUESTION_MESSAGE);
			if (iterationsStr == null) {
				return;
			}

			Class<?>[] propagations = { ResilientPropagation.class, BackPropagation.class };
			Class<?> propagationClass = (Class<?>) JOptionPane.showInputDialog(null, "Clase del optimizador",
					"Optimizadores", JOptionPane.QUESTION_MESSAGE, null, propagations, propagations[0]);
			if (propagationClass == null) {
				return;
			}

			String linesStr = JOptionPane.showInputDialog(null,
					"¿Cuántas veces quieres realizar la prueba? (lineas que aparecerán en el gráfico) Default: 1 (1-100)",
					"Lineas del gráfico", JOptionPane.QUESTION_MESSAGE);
			if (linesStr == null) {
				return;
			}

			String learningStr = JOptionPane.showInputDialog(null,
					"¿Qué learningRate quieres estudiar? (Min 0.0 Max 1.0 Def 0.5)", "Learning Rate",
					JOptionPane.QUESTION_MESSAGE);
			if (learningStr == null) {
				return;
			}

			TransferFunctionType[] transfers = { TransferFunctionType.SIN, TransferFunctionType.TANH,
					TransferFunctionType.GAUSSIAN };
			TransferFunctionType transferClass = (TransferFunctionType) JOptionPane.showInputDialog(null,
					"¿Qué función de transferencia?", "Función de transferencia", JOptionPane.QUESTION_MESSAGE, null,
					transfers, transfers[0]);
			if (transferClass == null) {
				return;
			}

			String layersStr = "";
			int[] emptyArray = new int[0];
			int[] inputLayersArray = emptyArray;
			int[] layersArray = { 14 };
			while (layersStr != null && inputLayersArray.length == 0) {
				layersStr = JOptionPane.showInputDialog(null,
						"Número de capas ocultas separadas por , (máximo 4 capas)", "Capas ocultas",
						JOptionPane.QUESTION_MESSAGE);
				// TODO VER SI SE PUEDE PONER ==NULL con continue;
				if (layersStr != null) {
					layersStr = layersStr.replaceAll("\\s", "");
					inputLayersArray = Arrays.asList(layersStr.split(",")).stream().mapToInt(this::giveInteger)
							.filter(this::validLayer).toArray();
					if (inputLayersArray.length > 4 || inputLayersArray.length < 1) {
						JOptionPane.showMessageDialog(null, "Por favor introduzca entre 1 y 4 capas ocultas.", "Error",
								JOptionPane.WARNING_MESSAGE);
						inputLayersArray = emptyArray;
					} else {
						layersArray = (int[]) ArrayUtils.addAll(layersArray, inputLayersArray);
					}
				}
			}
			if (layersStr == null) {
				return;
			}

			int[] layers = ArrayUtils.add(layersArray, 1);

			boolean saveGraph = this.requestSaveGraph();

			String file = JOptionPane.showInputDialog(null, "Nombre del arhivo de guardado", "Archivo",
					JOptionPane.QUESTION_MESSAGE);
			if (file == null) {
				return;
			}

			String fileName = file + ".csv";

			Integer iterationsNum = giveInteger(iterationsStr);
			int iterations = this.validIteration(iterationsNum) ? iterationsNum : 1000;
			Integer linesNum = giveInteger(linesStr);
			int lines = this.validLinesNum(linesNum) ? linesNum : 1;
			Double learningNum = giveDouble(learningStr);
			double learningRate = this.validLR(learningNum) ? learningNum : 0.5;

			NeurophSolver neurophSolver = new NeurophSolver();
			new Thread(() -> neurophSolver.simpleSearch(iterations, propagationClass, lines, learningRate,
					transferClass, layers, saveGraph, fileName)).start();
		} else if (e.getSource() == lRatesdButton) {
			String iterationsStr = JOptionPane.showInputDialog(null, "Número de iteraciones (min 1000)", "Iteraciones",
					JOptionPane.QUESTION_MESSAGE);
			if (iterationsStr == null) {
				return;
			}

			Class<?>[] propagations = { ResilientPropagation.class, BackPropagation.class };
			Class<?> propagationClass = (Class<?>) JOptionPane.showInputDialog(null, "Clase del optimizador",
					"Optimizadores", JOptionPane.QUESTION_MESSAGE, null, propagations, propagations[0]);
			if (propagationClass == null) {
				return;
			}

			List<Double> lRatesList = new ArrayList<>();
			String lRatesStr = "";
			while (lRatesStr != null && lRatesList.isEmpty()) {
				lRatesStr = JOptionPane.showInputDialog(null,
						"Introduce los LearningRates que quieres estudiar separados por ,", "Hojas",
						JOptionPane.QUESTION_MESSAGE);
				if (lRatesStr != null) {
					lRatesStr = lRatesStr.replaceAll("\\s", "");
					double[] lRatesArray = Arrays.asList(lRatesStr.split(",")).stream().mapToDouble(this::giveDouble)
							.filter(this::validLR).toArray();
					DoubleStream stream = DoubleStream.of(lRatesArray);
					lRatesList = stream.boxed().collect(Collectors.toList());
					stream.close();
					Set<Double> set = new HashSet<>(lRatesList);
					lRatesList.clear();
					lRatesList.addAll(set);
					Collections.sort(lRatesList);
				}
			}
			if (lRatesStr == null) {
				return;
			}

			List<Double> lRates = lRatesList;
			TransferFunctionType[] transfers = { TransferFunctionType.SIN, TransferFunctionType.TANH,
					TransferFunctionType.GAUSSIAN };
			TransferFunctionType transferClass = (TransferFunctionType) JOptionPane.showInputDialog(null,
					"¿Qué función de transferencia?", "Función de transferencia", JOptionPane.QUESTION_MESSAGE, null,
					transfers, transfers[0]);
			if (transferClass == null) {
				return;
			}

			String maxHiddenLayersStr = JOptionPane.showInputDialog(null,
					"Número máximo de capas ocultas (Min 1 Max 4 Def 3). Se estudiará desde 1 capa hasta el número de capas que se inserte",
					"Capas ocultas", JOptionPane.QUESTION_MESSAGE);
			if (maxHiddenLayersStr == null) {
				return;
			}

			List<Integer> neuronsNumList = new ArrayList<>();
			String neuronsNumStr = "";
			while (neuronsNumStr != null && neuronsNumList.isEmpty()) {
				neuronsNumStr = JOptionPane.showInputDialog(null,
						"Introduce todos los posbiles números de neurona en cada capa separados por , (Se estudiarán todas las posibles combinaciones para cada número que insertes en cada capa)",
						"Hojas", JOptionPane.QUESTION_MESSAGE);
				if (neuronsNumStr != null) {
					neuronsNumStr = neuronsNumStr.replaceAll("\\s", "");
					int[] neuronsNumArray = Arrays.asList(neuronsNumStr.split(",")).stream().mapToInt(this::giveInteger)
							.filter(this::validNeuronsNum).toArray();
					IntStream stream = IntStream.of(neuronsNumArray);
					neuronsNumList = stream.boxed().collect(Collectors.toList());
					stream.close();
					Set<Integer> set = new HashSet<>(neuronsNumList);
					neuronsNumList.clear();
					neuronsNumList.addAll(set);
					Collections.sort(neuronsNumList);
				}
			}
			if (neuronsNumStr == null) {
				return;
			}

			Integer[] neuronsNum = neuronsNumList.toArray(new Integer[neuronsNumList.size()]);
			boolean saveGraph = this.requestSaveGraph();

			String file = JOptionPane.showInputDialog(null, "Nombre del arhivo de guardado", "Archivo",
					JOptionPane.QUESTION_MESSAGE);
			if (file == null) {
				return;
			}

			String fileName = file + ".csv";
			Integer iterationsNum = giveInteger(iterationsStr);
			int iterations = this.validIteration(iterationsNum) ? iterationsNum : 1000;
			Integer maxHiddenLayersNum = giveInteger(maxHiddenLayersStr);
			int maxHiddenLayers = this.validMaxHiddenLayers(maxHiddenLayersNum) ? maxHiddenLayersNum : 3;
			NeurophSolver neurophSolver = new NeurophSolver();
			new Thread(() -> neurophSolver.advancedLRSearch(iterations, propagationClass, lRates, transferClass,
					maxHiddenLayers, neuronsNum, saveGraph, fileName)).start();
		} else if (e.getSource() == findBestNetworkButton) {
			new Thread(() -> NeurophSolver.findBestNetwork()).start();
		} else if (e.getSource() == networkTestButton) {
			String inputFile = JOptionPane.showInputDialog(null,
					"Nombre del arhivo en el que se encuentra la red (debe estar en NeurophSolutions/Networks/ )",
					"Elección red neuronal", JOptionPane.QUESTION_MESSAGE);
			if (inputFile == null) {
				return;
			}
			String outputFile = JOptionPane.showInputDialog(null,
					"Nombre del arhivo en el que quieres guardar los resultados (estará eh NeurophSolutions/ )",
					"Archivo de salida", JOptionPane.QUESTION_MESSAGE);
			if (outputFile == null) {
				return;
			}
			String inputFilePath = inputFile.endsWith(".nnet") ? inputFile : inputFile + ".nnet";
			String outputFilePath = outputFile.endsWith(".csv") ? outputFile : outputFile + ".csv";
			new Thread(() -> NeurophSolver.networkTest(inputFilePath, outputFilePath)).start();
		}

	}

	private boolean requestSaveGraph() {
		int reply = JOptionPane.showConfirmDialog(null,
				"¿Quieres que se muestren y guarden los gráficos de entrenamiento?", "Gráficos",
				JOptionPane.YES_NO_OPTION);
		return reply == JOptionPane.YES_OPTION;
	}

	private boolean validMaxHiddenLayers(Integer maxHiddenLayersStr) {
		return !(maxHiddenLayersStr <= 0 || maxHiddenLayersStr > 4);
	}

	private boolean validNeuronsNum(int neuronsNum) {
		return !(neuronsNum <= 0 || neuronsNum > 15);
	}

	private boolean validLR(Double learningNum) {
		return !(learningNum <= 0 || learningNum > 10);
	}

	private boolean validLinesNum(Integer linesNum) {
		return !(linesNum <= 0 || linesNum > 100);
	}

	private boolean validIteration(Integer iterationsNum) {
		return !(iterationsNum < 100 || iterationsNum > 1000000);
	}

	private boolean validLayer(int number) {
		return !(number <= 0 || number > 20);
	}

	private Integer giveInteger(String number) {
		Integer numberInt;
		try {
			numberInt = Integer.parseInt(number);
			return numberInt;
		} catch (Exception ex) {
			return 0;
		}
	}

	private Double giveDouble(String number) {
		Double numberInt;
		try {
			numberInt = Double.parseDouble(number);
			return numberInt;
		} catch (Exception ex) {
			return 0.0;
		}
	}

}
