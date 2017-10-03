import org.eclipse.collections.impl.list.mutable.FastList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by: tituskc
 * Created On  Tue, Oct 03, 2017 at 12:20 AM.
 */
public class Client
{
    public static void main(String[]args) throws IOException
    {
        int m = Integer.parseInt(args[0]);
        int numberOfBanks = m;
        List<List<Double>> a = FastList.newList();
        for (int i = 0; i < numberOfBanks; i++) {
            List<Double> b = FastList.newList();
            for (int j = 0; j < numberOfBanks; j++)
                b.add(i == j ? 0.0 : (double) Math.round(Math.random() * 331));
            a.add(b);
        }

        Network network = new Network(a);
        FileWriter fileWriter = new FileWriter("/home/tituskc/Documents/projects/NetworkAnalysis/adjMat2.csv");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (int i = 0; i < numberOfBanks; i++)
            printWriter.print("B"+i+(i==numberOfBanks-1?"\n":","));

        for (int i = 0; i < numberOfBanks; i++)
            for (int j = 0; j < numberOfBanks; j++)
                printWriter.print(network.getAdjacencyMatrix().get(i).get(j) + (j==numberOfBanks-1?"\n":","));
        printWriter.close();
    }
}
