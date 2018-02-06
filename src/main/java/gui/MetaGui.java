package gui;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import metaheuristic.MetaSolver;
import metaheuristic.models.MetaResults;
import util.CSVTableWriter;
import util.optimizers.LSBIEvaluationOptimizer;
import util.optimizers.LSFIEvaluationOptimizer;
import util.optimizers.RandomEvaluationOptimizer;

/**
 * @author César Valdés
 */
public class MetaGui extends DefaultTab implements ActionListener {

	private JButton simpleButton;
	private JButton advancedButton;

	protected MetaGui() {
		super("Metaheuristica", "Modo Metaheurísticas");

		JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(1, 1));
		simpleButton = new JButton("Entrenamiento simple");
		simpleButton.setToolTipText("Explora una única configuración");
		advancedButton = new JButton("Entrenamiento avanzado");
		advancedButton.setToolTipText("Explora varias configuraciones simultáneamente");
		advancedButton.setMargin(new Insets(1, 1, 1, 1));
		simpleButton.setMargin(new Insets(3, 5, 3, 5));
		panel.add(simpleButton);
		panel.add(advancedButton);
		this.setTabContent(panel);
		simpleButton.addActionListener(this);
		advancedButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == simpleButton) {
			String branchesStr = JOptionPane.showInputDialog(null, "Número de ramas 1-8 (Def: 1)", "Ramas",
					JOptionPane.QUESTION_MESSAGE);
			if (branchesStr == null) {
				return;
			}

			String leavesStr = JOptionPane.showInputDialog(null, "Número de hojas 1-1000 (Def: 5)", "Hojas",
					JOptionPane.QUESTION_MESSAGE);
			if (leavesStr == null) {
				return;
			}
			String partsStr = JOptionPane.showInputDialog(null, "Número de partes 3-9999 (Def: 99)", "Partes",
					JOptionPane.QUESTION_MESSAGE);
			if (partsStr == null) {
				return;
			}

			Class<?>[] optimizers = { RandomEvaluationOptimizer.class, LSFIEvaluationOptimizer.class,
					LSBIEvaluationOptimizer.class };
			Class<?> optimizer = (Class<?>) JOptionPane.showInputDialog(null, "Clase del optimizador", "Optimizadores",
					JOptionPane.QUESTION_MESSAGE, null, optimizers, optimizers[0]);
			if (optimizer == null) {
				return;
			}

			Integer branchesNum = giveInteger(branchesStr);
			int branches = this.validBranch(branchesNum) ? branchesNum : 1;
			Integer leavesNum = giveInteger(leavesStr);
			int leaves = this.validLeaf(leavesNum) ? leavesNum : 5;
			Integer partsNum = giveInteger(partsStr);
			int parts = this.validPart(partsNum) ? partsNum : 99;
			String file = JOptionPane.showInputDialog(null, "Nombre del arhivo de guardado", "Archivo",
					JOptionPane.QUESTION_MESSAGE);
			if (file == null) {
				return;
			}

			JOptionPane.showMessageDialog(null, "Branches " + branches + " , leaves " + leaves + ", parts " + parts);
			String fileName = file + ".csv";
			new Thread(() -> this.metaEasy(branches, leaves, parts, optimizer, fileName)).start();

		} else if (e.getSource() == advancedButton) {
			List<Class<? extends Object>> optimizers = new ArrayList<>();
			JCheckBox randomCheck = new JCheckBox("RandomEvaluationOptimizer");
			JCheckBox lsfiCheck = new JCheckBox("LSFIEvaluationOptimize");
			JCheckBox lsbiCheck = new JCheckBox("LSBIEvaluationOptimize");
			randomCheck.setSelected(true);
			lsfiCheck.setSelected(true);
			lsbiCheck.setSelected(true);
			String msg = "Selecciona las clases";
			Object[] msgContent = { msg, randomCheck, lsfiCheck, lsbiCheck };
			boolean someSelected = false;
			int n = JOptionPane.OK_OPTION;
			while (n == JOptionPane.OK_OPTION && !someSelected) {
				n = JOptionPane.showConfirmDialog(null, msgContent, "Optimizadores", JOptionPane.DEFAULT_OPTION);
				boolean random = randomCheck.isSelected();
				boolean lsfi = lsfiCheck.isSelected();
				boolean lsbi = lsbiCheck.isSelected();
				someSelected = random || lsfi || lsbi;
				if (random) {
					optimizers.add(RandomEvaluationOptimizer.class);
				}
				if (lsfi) {
					optimizers.add(LSFIEvaluationOptimizer.class);
				}
				if (lsbi) {
					optimizers.add(LSBIEvaluationOptimizer.class);
				}

			}

			if (!(n == JOptionPane.OK_OPTION)) {
				return;
			}

			List<Integer> numBranches = new ArrayList<>();
			String branchesStr = "";
			while (branchesStr != null && numBranches.isEmpty()) {
				branchesStr = JOptionPane.showInputDialog(null, "Número de ramas separadas por ,", "1,2,4,8");
				if (branchesStr != null) {
					branchesStr = branchesStr.replaceAll("\\s", "");
					int[] branchesArray = Arrays.asList(branchesStr.split(",")).stream().mapToInt(this::giveInteger)
							.filter(this::validBranch).toArray();
					IntStream stream = IntStream.of(branchesArray);
					numBranches = stream.boxed().collect(Collectors.toList());
					stream.close();
					Set<Integer> set = new HashSet<>(numBranches);
					numBranches.clear();
					numBranches.addAll(set);
					Collections.sort(numBranches);
				}
			}

			if (branchesStr == null) {
				return;
			}

			List<Integer> branches = numBranches;
			List<Integer> numLeaves = new ArrayList<>();
			String leavesStr = "";
			while (leavesStr != null && numLeaves.isEmpty()) {
				leavesStr = JOptionPane.showInputDialog(null, "Número de hojas separadas por ,", "5,50,100");
				if (leavesStr != null) {
					leavesStr = leavesStr.replaceAll("\\s", "");
					int[] leavesArray = Arrays.asList(leavesStr.split(",")).stream().mapToInt(this::giveInteger)
							.filter(this::validLeaf).toArray();
					IntStream stream = IntStream.of(leavesArray);
					numLeaves = stream.boxed().collect(Collectors.toList());
					stream.close();
					Set<Integer> set = new HashSet<>(numLeaves);
					numLeaves.clear();
					numLeaves.addAll(set);
					Collections.sort(numLeaves);
				}
			}
			if (leavesStr == null) {
				return;
			}

			List<Integer> leaves = numLeaves;
			List<Integer> numParts = new ArrayList<>();
			String partsStr = "";
			while (partsStr != null && numParts.isEmpty()) {
				partsStr = JOptionPane.showInputDialog(null, "Número de partes separadas por ,", "99,499,999,2999");
				if (partsStr != null) {
					int[] partsArray = Arrays.asList(partsStr.split(",")).stream().mapToInt(this::giveInteger)
							.filter(this::validPart).toArray();
					IntStream stream = IntStream.of(partsArray);
					numParts = stream.boxed().collect(Collectors.toList());
					stream.close();
					Set<Integer> set = new HashSet<>(numParts);
					numParts.clear();
					numParts.addAll(set);
					Collections.sort(numParts);
				}
			}
			if (partsStr == null) {
				return;
			}

			String file = JOptionPane.showInputDialog(null, "Nombre del arhivo de guardado", "All");
			if (file == null) {
				return;
			}
			String fileName = file + ".csv";
			List<Integer> parts = numParts;

			new Thread(() -> this.metaAdvanced(optimizers, parts, branches, leaves, fileName)).start();
		}
	}

	private void metaEasy(int searchBranches, int leaves, int parts, Class<?> optimizer, String fileName) {

		MetaSolver metaSol = new MetaSolver(searchBranches, leaves, parts, optimizer, fileName);
		MetaResults results = metaSol.getAndSaveResults();
		metaSol.closeTableWriter();

		String resultado = "Tiempo secuencial: " + results.getTotalSecuentialTime() + "\nTiempo concurrente: "
				+ results.getTotalConcurrentTime() + "\nError medio :" + results.getAvgError() + "\nTiempo medio:"
				+ results.getAvgTime() + "\n\nArchivo " + fileName + " actualizado";
		JOptionPane msg = new JOptionPane(resultado, JOptionPane.INFORMATION_MESSAGE);
		final JDialog dlg = msg.createDialog("Metaheurística Simple Finalizada");
		dlg.setVisible(true);

	}

	private void metaAdvanced(List<Class<? extends Object>> optimizers, List<Integer> parts, List<Integer> numBranches,
			List<Integer> numLeaves, String fileName) {
		CSVTableWriter tw = MetaSolver.initTableWriter(fileName);
		for (int leaves : numLeaves) {
			for (int part : parts) {
				for (int branches : numBranches) {
					for (Class<? extends Object> optimizer : optimizers) {
						System.out.println("branches" + branches);
						System.out.println("leaves" + leaves);
						System.out.println("part" + part);
						System.out.println("optimizer" + optimizer);
						MetaSolver metaSol = new MetaSolver(branches, leaves, part, optimizer, tw);
						metaSol.getAndSaveResults();
					}
				}
			}
		}
		tw.close();
	    try {
	    	Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("shutdown -s -t 0");
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String resultado = "Para visualizar los resultados abrir el archivo " + fileName;
		JOptionPane msg = new JOptionPane(resultado, JOptionPane.INFORMATION_MESSAGE);
		final JDialog dlg = msg.createDialog("Metaheurística Avanzada Finalizada");
		dlg.setVisible(true);

	}

	private boolean validBranch(int number) {
		if (number <= 0 || number > 8) {
			return false;
		}
		return (number & (number - 1)) == 0;
	}

	private boolean validLeaf(int number) {
		return !(number <= 0 || number > 1000);
	}

	private boolean validPart(int number) {
		return !(number <= 2 || number > 9999);
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

}
