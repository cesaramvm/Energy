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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import Metaheuristic.MetaSolver;
import Metaheuristic.Models.MetaResults;
import Util.CSVTableWriter;
import Util.Optimizers.LSBIEvaluationOptimizer;
import Util.Optimizers.LSFIEvaluationOptimizer;
import Util.Optimizers.RandomEvaluationOptimizer;

/**
 * @author César Valdés
 */
public class MetaGui extends DefaultTab implements ActionListener {

	private JButton simpleButton, advancedButton;
	private MetaSolver metaSol;
	
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

		try {
		} catch (Exception ex) {
			Logger.getLogger(MetaGui.class.getName()).log(Level.SEVERE, null, ex);
		}

		if (e.getSource() == simpleButton) {
			String branchesStr = JOptionPane.showInputDialog(null, "Número de ramas 1-8 (Def: 1)", "Ramas",
					JOptionPane.QUESTION_MESSAGE);
			if (branchesStr != null) {
				String leavesStr = JOptionPane.showInputDialog(null, "Número de hojas 1-1000 (Def: 5)", "Hojas",
						JOptionPane.QUESTION_MESSAGE);
				if (leavesStr != null) {
					String partsStr = JOptionPane.showInputDialog(null, "Número de partes 3-9999 (Def: 99)", "Partes",
							JOptionPane.QUESTION_MESSAGE);
					if (partsStr != null) {
						Class<?>[] optimizers = { RandomEvaluationOptimizer.class, LSFIEvaluationOptimizer.class,
								LSBIEvaluationOptimizer.class };
						Class<?> optimizer = (Class<?>) JOptionPane.showInputDialog(null, "Clase del optimizador",
								"Optimizadores", JOptionPane.QUESTION_MESSAGE, null, optimizers, optimizers[0]);
						Integer branchesNum = giveInteger(branchesStr);
						int branches = this.validBranch(branchesNum) ? branchesNum : 1;
						Integer leavesNum = giveInteger(leavesStr);
						int leaves = this.validLeaf(leavesNum) ? leavesNum : 5;
						Integer partsNum = giveInteger(partsStr);
						int parts = this.validPart(partsNum) ? partsNum : 99;
						if (optimizer != null) {
							String fileName = JOptionPane.showInputDialog(null, "Nombre del arhivo de guardado", "Archivo",
									JOptionPane.QUESTION_MESSAGE);
							if(fileName!=null){
								JOptionPane.showMessageDialog(null,
										"Branches " + branches + " , leaves " + leaves + ", parts " + parts);
								fileName += ".csv";
								this.metaEasy(branches, leaves, parts, optimizer, fileName);
							}
						}
					}
				}
			}

		} else if (e.getSource() == advancedButton) {
			List<Class<? extends Object>> optimizers = new ArrayList<>();
			List<Integer> numParts = new ArrayList<>();
			List<Integer> numBranches = new ArrayList<>();
			List<Integer> numLeaves = new ArrayList<>();

			JCheckBox RandomCheck = new JCheckBox("RandomEvaluationOptimizer");
			JCheckBox LSFICheck = new JCheckBox("LSFIEvaluationOptimize");
			JCheckBox LSBICheck = new JCheckBox("LSBIEvaluationOptimize");
			String msg = "Selecciona las clases";
			Object[] msgContent = { msg, RandomCheck, LSFICheck, LSBICheck };
			boolean someSelected = false;
			int n = JOptionPane.OK_OPTION;
			while (n == JOptionPane.OK_OPTION && !someSelected) {
				n = JOptionPane.showConfirmDialog(null, msgContent, "Optimizadores", JOptionPane.DEFAULT_OPTION);
				boolean random = RandomCheck.isSelected();
				boolean lsfi = LSFICheck.isSelected();
				boolean lsbi = LSBICheck.isSelected();
				someSelected = random || lsfi || lsbi;
				if (someSelected) {
					if (random) {optimizers.add(RandomEvaluationOptimizer.class);}
					if (lsfi) {optimizers.add(LSFIEvaluationOptimizer.class);}
					if (lsbi) {optimizers.add(LSBIEvaluationOptimizer.class);}
				}
			}

			if (n == JOptionPane.OK_OPTION) {
				String branchesStr = "";
				while (branchesStr!=null && numBranches.size() == 0) {
					branchesStr = JOptionPane.showInputDialog(null, "Número de ramas separadas por ,", "Ramas",
							JOptionPane.QUESTION_MESSAGE);
					if(branchesStr!=null){
						branchesStr = branchesStr.replaceAll("\\s", "");
						int[] branchesArray = Arrays.asList(branchesStr.split(",")).stream().mapToInt(x -> this.giveInteger(x))
								.filter(x -> this.validBranch(x)).toArray();
						numBranches = IntStream.of(branchesArray).boxed().collect(Collectors.toList());
						Set<Integer> set = new HashSet<>(numBranches);
						numBranches.clear();
						numBranches.addAll(set);
						Collections.sort(numBranches);
					}
				}
				
				if(branchesStr!=null){
					String leavesStr = "";
					while (leavesStr != null && numLeaves.size() == 0) {
						leavesStr = JOptionPane.showInputDialog(null, "Número de hojas separadas por ,", "Hojas",
								JOptionPane.QUESTION_MESSAGE);
						if(leavesStr!=null){
							leavesStr = leavesStr.replaceAll("\\s", "");
							int[] leavesArray = Arrays.asList(leavesStr.split(",")).stream().mapToInt(x -> this.giveInteger(x))
									.filter(x -> this.validLeaf(x)).toArray();
							numLeaves = IntStream.of(leavesArray).boxed().collect(Collectors.toList());
							Set<Integer> set = new HashSet<Integer>(numLeaves);
							numLeaves.clear();
							numLeaves.addAll(set);
							Collections.sort(numLeaves);
							}
					}
					if(leavesStr!=null){
						String partsStr = "";
						while (partsStr !=null && numParts.size() == 0) {
							partsStr = JOptionPane.showInputDialog(null, "Número de partes separadas por ,", "Hojas",
									JOptionPane.QUESTION_MESSAGE);
							if (partsStr != null){
								int[] partsArray = Arrays.asList(partsStr.split(",")).stream().mapToInt(x -> this.giveInteger(x))
										.filter(x -> this.validPart(x)).toArray();
								numParts = IntStream.of(partsArray).boxed().collect(Collectors.toList());
								Set<Integer> set = new HashSet<>(numParts);
								numParts.clear();
								numParts.addAll(set);
								Collections.sort(numParts);
							}
						}
						if(partsStr != null){
							String fileName = JOptionPane.showInputDialog(null, "Nombre del arhivo de guardado", "Archivo",
									JOptionPane.QUESTION_MESSAGE);
							if(fileName!=null){
								fileName += ".csv";
								this.metaAdvanced(optimizers, numParts, numBranches, numLeaves, fileName);
							}
						}
					}
				}				
			}
		}
	}

	private void metaEasy(int searchBranches, int leaves, int parts, Class<?> optimizer, String fileName) {

		metaSol = new MetaSolver(searchBranches, leaves, parts, optimizer, fileName);
		//metaSol.search();
		MetaResults results = metaSol.getAndSaveResults();
		metaSol.closeTableWriter();
		
		String resultado = "Tiempo secuencial: " + results.getTotalSecuentialTime() + "\nTiempo concurrente: " + results.getTotalConcurrentTime()
		+ "\nError medio :" + results.getAvgError() + "\nTiempo medio:" + results.getAvgTime() + "\n\nArchivo " + fileName +" actualizado";
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
						MetaSolver metaSol = new MetaSolver(branches, leaves, part, optimizer, tw);
						metaSol.getAndSaveResults();
					}
				}
			}
		}
		tw.close();
		
		String resultado = "Para visualizar los resultados abrir el archivo " + fileName;
		JOptionPane msg = new JOptionPane(resultado, JOptionPane.INFORMATION_MESSAGE);
	    final JDialog dlg = msg.createDialog("Metaheurística Avanzada Finalizada");
	    dlg.setVisible(true);

	}

	private boolean validBranch(int number) {
		if (number <= 0 || number > 8) {
			return false;
		}
		return ((number & (number - 1)) == 0);
	}

	private boolean validLeaf(int number) {
		if (number <= 0 || number > 1000) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validPart(int number) {
		if (number <= 2 || number > 9999) {
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

}
