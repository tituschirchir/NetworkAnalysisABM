import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.utility.ListIterate;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractNode;
import org.graphstream.graph.implementations.SingleNode;

import java.util.List;

/**
 * Created by: tituskc
 * Created On  Sun, Oct 01, 2017 at 12:38 PM.
 */
public class Bank

{
    public double shortTerminterbankAssets, otherAssets;
    public double longTermInterbankAssets;
    public double shortTerminterbankLiabilities;
    public double longTermInterbankLiabilities;
    public List<Double> relations = FastList.newList();
    public String name;
    private int networkIndex;
    private Network network;


    public Bank(String name)
    {
        this.name = name;
    }

    public Bank(String name, int networkIndex)
    {
        this.name = name;
        this.networkIndex = networkIndex;
    }
    public List<Double> getSizeScore()
    {
        List<Double> scores = FastList.newList();
        List<Integer> connections = ListIterate.collect(getConnections(), aDouble -> aDouble != 0.0 ? 1 : 0);
        int denominator = (int) ListIterate.sumOfInt(connections, integer -> integer);
        List<Bank> allBanks = network.getAllBanks();
        double totalAssets = 0.0;
        for (int i =0; i<allBanks.size(); i++)
        {
            totalAssets += allBanks.get(i).getTotalAssets()*connections.get(i);
        }
        for (int i =0; i<allBanks.size(); i++)
        {
            Bank bankJ = allBanks.get(i);
            if(bankJ.name.equals(this.name))
                scores.add(i,0.0);
            else
                scores.add(i,Math.log(bankJ.getTotalAssets())-(totalAssets/denominator));
        }
        return scores;
    }

    public double getTotalAssets()
    {
        return this.shortTerminterbankAssets + this.longTermInterbankAssets + this.otherAssets;
    }

    public void setNetwork(Network network)
    {
        this.network = network;
    }
    public List<Double> getConnections(){
        return this.network.getConnections().get(networkIndex);
    }

    public void updateRelations(){
        final List<Double> DList = getConnections();
        if (this.relations.isEmpty())
        {
            for (int i =0; i<network.getNumberOfBanks();i++) {
                relations.add(DList.get(i) == 0.0 ? 0.0 : Math.log(DList.get(i)));
            }
        }
        else
        {
            for (int i =0; i<network.getNumberOfBanks();i++) {
                relations.set(i, relations.get(i) * Math.exp(-.1));
            }
        }
    }
    public List<Double> getRelations(){
        return this.relations;
    }
}
