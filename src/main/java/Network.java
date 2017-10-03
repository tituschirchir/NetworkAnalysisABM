import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.utility.ListIterate;
import org.graphstream.algorithm.coloring.WelshPowell;
import org.graphstream.algorithm.generator.*;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by: tituskc
 * Created On  Sun, Oct 01, 2017 at 12:38 PM.
 */
public class Network
{
    private int numberOfBanks;
    private List<List<Integer>> adjacencyMatrix;
    private List<List<Double>> exposureMatrix;
    private List<List<Double>> connections;
    private List<Bank> allBanks = FastList.newList();
    private List<List<Double>> invertedExposureMatrix;

    Network(List<List<Double>> exposureMatrix)
    {
        this.numberOfBanks = exposureMatrix.size();
        this.exposureMatrix = exposureMatrix;
        this.adjacencyMatrix = getEmptyInt(numberOfBanks);
        this.invertedExposureMatrix = flipMatrix(exposureMatrix);
        initializeAdjacencyMatrix();
        initializeBanks();
        simulate();
    }

    private void simulate()
    {

    }

    private List<List<Double>> flipMatrix(List<List<Double>> exposureMatrix)
    {
        List<List<Double>> inverted = getEmpty(numberOfBanks);
        for (int i=0;i<exposureMatrix.size(); i++)
            for (int j = 0; j < exposureMatrix.get(0).size(); j++)
                inverted.get(j).set(i, exposureMatrix.get(i).get(j));
        return inverted;
    }


    private void initializeAdjacencyMatrix()
    {
        Graph graph = new SingleGraph("Barabàsi-Albert");
        Generator gen = new BarabasiAlbertGenerator(numberOfBanks/3);
        gen.addSink(graph);
        gen.begin();
        for(int i=2; i<numberOfBanks; i++) {
            gen.nextEvents();
        }
        gen.end();
        graph.display();
        graph.getEachEdge().forEach(edge -> adjacencyMatrix.get(Integer.parseInt(edge.getNode0().getId())).set(Integer.parseInt(edge.getNode1().getId()), 1));
    }
    private void initializeBanks()
    {
        for (int i=0; i<this.numberOfBanks; i++) {
            Bank bank1 = new Bank("B"+i, i);
            bank1.setNetwork(this);
            bank1.shortTerminterbankAssets = ListIterate.sumOfDouble(exposureMatrix.get(i), aDouble -> aDouble);
            bank1.shortTerminterbankLiabilities = ListIterate.sumOfDouble(invertedExposureMatrix.get(i), aDouble -> aDouble);
            allBanks.add(bank1);
        }
    }

    public int getNumberOfBanks()
    {
        return this.numberOfBanks;
    }

    public Bank getBankI(int i)
    {
        return allBanks.get(i);
    }
    List<Bank> getAllBanks()
    {
        return this.allBanks;
    }

    List<List<Integer>> getAdjacencyMatrix()
    {
        return adjacencyMatrix;
    }
    private static List<List<Double>> getEmpty(int size)
    {
        List<List<Double>> empty = FastList.newList();
        for (int i=0; i<size;i++)
        {
            List<Double> listEmpty = FastList.newList();
            for (int j=0; j<size;j++){
                listEmpty.add(0.0);
            }
            empty.add(listEmpty);
        }
        return empty;
    }
    private static List<List<Integer>> getEmptyInt(int size)
    {
        List<List<Integer>> empty = FastList.newList();
        for (int i=0; i<size;i++)
        {
            List<Integer> listEmpty = FastList.newList();
            for (int j=0; j<size;j++){
                listEmpty.add(0);
            }
            empty.add(listEmpty);
        }
        return empty;
    }

    public List<List<Double>> getConnections()
    {
        connections = getEmpty(this.numberOfBanks);
        for (int i=0; i<numberOfBanks-1;i++)
        {
            for (int j=i+1; j<numberOfBanks;j++){
                connections.get(i).set(j, exposureMatrix.get(i).get(j)+ exposureMatrix.get(j).get(i));
                connections.get(j).set(i, exposureMatrix.get(i).get(j)+ exposureMatrix.get(j).get(i));
            }
        }
        return connections;
    }
}
