import dist.AbstractDistribution;
import shared.DataSet;
import shared.Instance;
import util.linalg.DenseVector;
import util.linalg.Vector;

/**
 * Created by Jessica on 3/10/2016.
 */
public class PrettyGraphDistribution extends AbstractDistribution {
    private int vertexCount;
    private final int max = 200;

    public PrettyGraphDistribution(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public Instance sample(Instance ignored) {
        Vector v = new DenseVector(vertexCount * 2);
        for (int i = 0; i < vertexCount * 2; i++) {
            v.set(i, random.nextInt(max));
        }
        return new Instance(v);
    }

    @Override
    public double p(Instance i) {
        return 1;
    }

    @Override
    public Instance mode(Instance ignored) {
        return sample(ignored);
    }

    @Override
    public void estimate(DataSet set) {
        return;
    }
}
