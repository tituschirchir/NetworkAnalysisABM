import com.gs.tablasco.TableVerifier;
import com.gs.tablasco.verify.ListVerifiableTable;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Rule;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by: tituskc
 * Created On  Sun, Oct 01, 2017 at 12:47 PM.
 */
public class NetworkTest
{

    @Rule
    public final TableVerifier tableVerifier = new TableVerifier()
            .withExpectedDir("src/test/resources")
            .withOutputDir("target").withTolerance(1e-5)
            //.withRebase()
            ;

    @Test
    public void testBankInitialize() throws IOException
    {
        int numberOfBanks = 20;
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
        List<List<Object>> adjMat = FastList.newList();
        List<Object> tmp = FastList.newList();
        for (int i = 0; i < numberOfBanks; i++) {
            tmp.add("B"+i);
            printWriter.print("B"+i+(i==numberOfBanks-1?"\n":","));
        }
        adjMat.add(tmp);
        final List<List<Integer>> adjacencyMatrix = network.getAdjacencyMatrix();
        for (int i = 0; i < numberOfBanks; i++) {
            tmp = FastList.newList();
            for (int j = 0; j < numberOfBanks; j++) {
                tmp.add(adjacencyMatrix.get(i).get(j));
                printWriter.print(adjacencyMatrix.get(i).get(j) + (j==numberOfBanks-1?"\n":","));
            }
            adjMat.add(tmp);
        }
        printWriter.close();
        final ListVerifiableTable listVerifiableTable = new ListVerifiableTable(adjMat);
        this.tableVerifier.verify("Test 1", listVerifiableTable);
        //Verify.assertSize(4, sizeScore);
        //Verify.assertEquals(50.0, (double)sizeScore.get(0), 0.01);
    }
}
