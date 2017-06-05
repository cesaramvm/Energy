package util.optimizers;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author César Valdés
 */
public abstract class LSEvaluationOptimizer extends EvaluationOptimizer {

	protected List<Integer> paramsIndex = new LinkedList<>();
	private List<Integer> paramsIndexBackup = new LinkedList<>();

	public LSEvaluationOptimizer(int newParts, Random r) {
		super(newParts, r);
		for (Integer i = 0; i <= problem.getNumParams(); i++) {
			paramsIndexBackup.add(i);
		}
		this.restoreParamsIndex();
	}

	public final void restoreParamsIndex() {
		paramsIndex = new LinkedList<>(paramsIndexBackup);
	}

}
