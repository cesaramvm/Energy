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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Metaheuristic.MetaSolver;
import Models.MetaResults;
import Util.Optimizers.LSBIEvaluationOptimizer;
import Util.Optimizers.LSFIEvaluationOptimizer;
import Util.Optimizers.RandomEvaluationOptimizer;
import Util.Writers.CSVTableWriter;
import energytfg.Principal;

public class MetaGui extends DefaultTab implements ActionListener{

	private JButton bt1, bt2;
    
	protected MetaGui() {
		super("Metaheuristica", "Modo Metaheurísticas");
		
		JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));
        bt1 = new JButton("Entrenamiento simple");
        //TODO
        bt1.setToolTipText("Explora bla bla bla bla bla bla bla");
        bt2 = new JButton("Entrenamiento avanzado");
        //TODO
        bt2.setToolTipText("Explora bla bla bla bla bla bla bla");
        bt2.setMargin(new Insets(1, 1, 1, 1));
        bt1.setMargin(new Insets(3, 5, 3, 5));
        panel.add(bt1);
        panel.add(bt2);
        this.setTabContent(panel);
        bt1.addActionListener(this);
        bt2.addActionListener(this);
	}

	@Override
    public void actionPerformed(ActionEvent e) {//sobreescribimos el metodo del listener
    	
    	try {
//          easy();
//          advanced();
      } catch (Exception ex) {
          Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
      }

    	if(e.getSource()==bt1){//podemos comparar por el contenido del boton
    		String branchesStr = JOptionPane.showInputDialog(null, "Número de ramas (Def: 1)","Ramas",JOptionPane.QUESTION_MESSAGE);
    		String leavesStr = JOptionPane.showInputDialog(null, "Número de hojas (Def: 5)","Hojas",JOptionPane.QUESTION_MESSAGE);
    		String partsStr = JOptionPane.showInputDialog(null, "Número de partes (Def: 99)","Partes",JOptionPane.QUESTION_MESSAGE);
    		Class<?>[] optimizers = { RandomEvaluationOptimizer.class, LSFIEvaluationOptimizer.class, LSBIEvaluationOptimizer.class };
    		Class<?> optimizer = (Class<?>) JOptionPane.showInputDialog(null, 
        		        "Clase del optimizador",
        		        "Optimizadores",
        		        JOptionPane.QUESTION_MESSAGE,
        		        null, 
        		        optimizers, 
        		        optimizers[0]);
    		Integer branchesNum = giveInteger(branchesStr);
    		int branches = (branchesNum== 0) ? 1 : branchesNum;
    		Integer leavesNum = giveInteger(leavesStr);
    		int leaves = (leavesNum== 0) ? 5 : leavesNum;
    		Integer partsNum = giveInteger(partsStr);
    		int parts =  (partsNum== 0) ? 99 : partsNum;
    		this.metaEasy(branches, leaves, parts, optimizer);
        	
            JOptionPane.showMessageDialog(null, "Busqueda simple finalizada");
        } else if(e.getSource()==bt2){
        	List<Class<? extends Object>> optimizers = new ArrayList<>();
        	List<Integer> numParts = new ArrayList<>();
        	List<Integer> numBranches = new ArrayList<>();
        	List<Integer> numLeaves = new ArrayList<>();
            
        	JCheckBox RandomCheck = new JCheckBox("RandomEvaluationOptimizer");
        	JCheckBox LSFICheck = new JCheckBox("LSFIEvaluationOptimize");
        	JCheckBox LSBICheck = new JCheckBox("LSBIEvaluationOptimize");
            String msg = "Selecciona las clases";
            Object[] msgContent = {msg,RandomCheck,LSFICheck,LSBICheck};
            boolean someSelected = false;
            while (!someSelected){
            	int n = JOptionPane.showConfirmDialog(null, msgContent,"Optimizadores", JOptionPane.DEFAULT_OPTION);
                boolean random = RandomCheck.isSelected();
                boolean lsfi = LSFICheck.isSelected();
                boolean lsbi = LSBICheck.isSelected();
                if(JOptionPane.OK_OPTION == n){
                    someSelected = random||lsfi||lsbi;
                    if(someSelected){
                    	if(random){optimizers.add(RandomEvaluationOptimizer.class);}
                    	if(lsfi){optimizers.add(LSFIEvaluationOptimizer.class);}
                    	if(lsbi){optimizers.add(LSBIEvaluationOptimizer.class);}
                    }
                } else {
                	break;
                }
            }
            
    		while (numBranches.size() ==0){
    			String branchesStr = JOptionPane.showInputDialog(null, "Número de ramas separadas por ,","Ramas",JOptionPane.QUESTION_MESSAGE);
    			branchesStr = branchesStr.replaceAll("\\s","");
    			int[] branchesArray = Arrays.asList(branchesStr.split(",")).stream().mapToInt(x -> this.giveInteger(x)).filter(x -> this.validBranch(x)).toArray();
        		numBranches = IntStream.of(branchesArray).boxed().collect(Collectors.toList());
        		Set<Integer> set = new HashSet<>(numBranches);
        		numBranches.clear();
        		numBranches.addAll(set);
        		Collections.sort(numBranches);
    		}
    		
    		while (numLeaves.size() ==0){
    			String leavesStr = JOptionPane.showInputDialog(null, "Número de hojas separadas por ,","Hojas",JOptionPane.QUESTION_MESSAGE);
    			leavesStr = leavesStr.replaceAll("\\s","");
    			int[] leavesArray = Arrays.asList(leavesStr.split(",")).stream().mapToInt(x -> this.giveInteger(x)).filter(x -> this.validLeaf(x)).toArray();
        		numLeaves = IntStream.of(leavesArray).boxed().collect(Collectors.toList());
        		Set<Integer> set = new HashSet<>(numLeaves);
        		numLeaves.clear();
        		numLeaves.addAll(set);
        		Collections.sort(numLeaves);
    		}
    		
    		while (numParts.size() ==0){
    			String partsStr = JOptionPane.showInputDialog(null, "Número de hojas separadas por ,","Hojas",JOptionPane.QUESTION_MESSAGE);
    			
    			int[] partsArray = Arrays.asList(partsStr.split(",")).stream().mapToInt(x -> this.giveInteger(x)).filter(x -> this.validPart(x)).toArray();
        		numLeaves = IntStream.of(partsArray).boxed().collect(Collectors.toList());
        		Set<Integer> set = new HashSet<>(numParts);
        		numParts.clear();
        		numParts.addAll(set);
        		Collections.sort(numParts);
    		}            
    		
            this.metaAdvanced(optimizers, numParts, numBranches, numLeaves);
            
            JOptionPane.showMessageDialog(null, "Busqueda avanzada finalizada");
        }
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void metaEasy(int searchBranches, int leaves, int parts, Class<?> optimizer) {

        MetaSolver metaSol = new MetaSolver(problem, searchBranches, leaves, parts);
        metaSol.setEvaluationClass(optimizer);
        metaSol.search();
        MetaResults results = metaSol.getResults();
        CSVTableWriter tw = MetaSolver.initTableWriter("testingLSFI.csv");
        metaSol.writeRow(tw);
        tw.close();
        
//        System.out.println(results.getBestSolution());
//        System.out.println("Secuencial: " + results.getTotalSecuentialTime());
//        System.out.println("Concurrent: " + results.getTotalConcurrentTime());
//        System.out.println("Avg Error :" + results.getAvgError());
//        System.out.println("Avg Time  :" + results.getAvgTime());
//            System.out.println(solution);
//            System.out.println("Tiempo de busqueda " + );

    }

	private void metaAdvanced(List<Class<? extends Object>> optimizers,
			List<Integer> parts, List<Integer> numBranches, List<Integer> numLeaves) {

        boolean keepGoing = true;
        CSVTableWriter tw = MetaSolver.initTableWriter("niceOrder.csv");
        for (int leaves : numLeaves) {
        	for (int part : parts) {
        		for (int branches : numBranches) {
                    for (Class<? extends Object> optimizer : optimizers) {
                    	if(optimizer == LSBIEvaluationOptimizer.class && branches == 8 && leaves ==100 && part == 999){
                    		keepGoing = false;
                    	}
                    	if(!keepGoing){
	                    	
	                        MetaSolver metaSol = new MetaSolver(problem, branches, leaves, part);
	                        metaSol.setEvaluationClass(optimizer);
	                        metaSol.search();
	                        MetaResults results = metaSol.getResults();
	//                        System.out.println(results.getBestSolution());
	//                        System.out.println("Secuencial: " + results.getTotalSecuentialTime());
	//                        System.out.println("Concurrent: " + results.getTotalConcurrentTime());
	//                        System.out.println("Avg Error :" + results.getAvgError());
	//                        System.out.println("Avg Time  :" + results.getAvgTime());
	//                                System.out.println(LSFIEvaluationOptimizer.class.getCanonicalName());
	                        metaSol.writeRow(tw);
	
	//                                Runtime runtime = Runtime.getRuntime();
	//                                Process proc = runtime.exec("shutdown -s -t 0");
	//                                System.exit(0);
	                            }
	                        System.out.println(optimizer.toString() + " - parts: " + part + " branches: " + branches + " leaves: " + leaves);
//                    	}
                    }
                }
            }
        }
        tw.close();

    }

    private boolean validBranch(int number) {
    	if(number <=0 || number >8){
    		return false;
		}
    	return ((number & (number -1)) == 0);
	}

    private boolean validLeaf(int number) {
    	if(number <=0 || number >1000){
    		return false;
		} else {
			return true;
		}
	}

    private boolean validPart(int number) {
    	if(number <=1 || number >9999){
    		return false;
		} else {
			return true;
		}
	}
    
    private Integer giveInteger(String number) {
    	Integer numberInt;
    	try { 
    		numberInt = Integer.parseInt(number); 
        } catch(Exception e) { 
        		return 0;
        }
        return numberInt;
	}

}
