import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;

public class GridTest {

    private static FileWriter fw;
    private static BufferedWriter bw;
    private static PrintWriter out;
    private static String testFileResults = "TestResults.txt";

    @BeforeAll
    public static void Setup(){

        try{
            File file = new File(testFileResults);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();

            fw = new FileWriter(testFileResults, true);
            bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void printResults(Grid gc) {
        this.out.println("\tResults from " + gc.getFilePath());
        this.out.println("\t---\t---\t---\t---\t---\t---\t---\t---\t---\t---\t---");
        this.out.println("Time Elapsed = " + gc.getDuration().toMillis() + " ms");
        this.out.println("Number of Ones = " + gc.getNrOfOnes());
        this.out.println("Number of Clusters = " + gc.getNrOfClusters());
        this.out.println();
        this.out.println();
        this.out.println("\t-------------------------------------------");
        this.out.println();
        this.out.println();
    }

    @Test
    public void testWithParserBaseExample(){
        Grid gc = new Grid("./testFiles/baseExample.json");
        gc.checkAdjacents();
        printResults(gc);
    }

    @Test
    public void testWithParser100x100(){
        Grid gc = new Grid("./testFiles/100x100.json");
        gc.checkAdjacents();
        printResults(gc);
    }

    @Test
    public void testWithParser1000x1000(){
        Grid gc = new Grid("./testFiles/1000x1000.json");
        gc.checkAdjacents();
        printResults(gc);
    }

    @Test
    public void testWithParser10000x10000(){
        Grid gc = new Grid("./testFiles/10000x10000.json");
        gc.checkAdjacents();
        printResults(gc);
    }

    @Test
    public void testWithParser20000x20000(){
        Grid gc = new Grid("./testFiles/20000x20000.json");
        gc.checkAdjacents();
        printResults(gc);
    }


    @AfterAll
    public static void closeSetup(){
        out.close();
    }
}