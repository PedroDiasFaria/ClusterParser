import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest {

    private static FileWriter fw;
    private static BufferedWriter bw;
    private static PrintWriter out;
    private static String testFileResults = "TestResults.txt";
    private static Path testFilesPath = Paths.get(System.getProperty("user.dir"), "testFiles");

    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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

            out.println("Tests Date: " + sdf.format(new Date()));
            out.println("-----------------------------------------------\n\n");

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void printResults(Grid gc) {
        this.out.println("\tResults from " + gc.getFileName());
        this.out.println("---\t---\t---\t---\t---\t---\t---\t---\t---\t---\t---\t---");
        this.out.println("Time Elapsed = " + gc.getDuration().toMillis() + " ms");
        this.out.println("Number of Ones = " + gc.getNrOfOnes());
        this.out.println("Number of Clusters = " + gc.getNrOfClusters());
        this.out.println("-----------------------------------------------");
        this.out.println();
        this.out.println();
    }

    @Test
    public void testWithParserBaseExample(){
        String fileName = "baseExample.json";
        System.out.println("Testing: " + fileName);
        Grid gc = new Grid(Paths.get(testFilesPath.toString(), fileName));
        gc.checkAdjacents();
        assertEquals(3, gc.getNrOfClusters());
        assertEquals(15, gc.getNrOfOnes());
        printResults(gc);
    }

    @Test
    public void testWithParser100x100(){
        String fileName = "100x100.json";
        System.out.println("Testing: " + fileName);
        Grid gc = new Grid(Paths.get(testFilesPath.toString(), fileName));
        gc.checkAdjacents();
        assertEquals(586, gc.getNrOfClusters());
        assertEquals(3004, gc.getNrOfOnes());
        printResults(gc);
    }

    @Test
    public void testWithParser1000x1000(){
        String fileName = "1000x1000.json";
        System.out.println("Testing: " + fileName);
        Grid gc = new Grid(Paths.get(testFilesPath.toString(), fileName));
        gc.checkAdjacents();
        assertEquals(56053, gc.getNrOfClusters());
        assertEquals(299527, gc.getNrOfOnes());
        printResults(gc);
    }

    @Test
    public void testWithParser10000x10000(){
        String fileName = "10000x10000.json";
        System.out.println("Testing: " + fileName);
        Grid gc = new Grid(Paths.get(testFilesPath.toString(), fileName));
        gc.checkAdjacents();
        assertEquals(5612372, gc.getNrOfClusters());
        assertEquals(29998767, gc.getNrOfOnes());
        printResults(gc);
    }

    @Test
    public void testWithParser20000x20000(){
        String fileName = "20000x20000.json";
        System.out.println("Testing: " + fileName);
        Grid gc = new Grid(Paths.get(testFilesPath.toString(), fileName));
        gc.checkAdjacents();
        assertEquals(22450980, gc.getNrOfClusters());
        assertEquals(119999200, gc.getNrOfOnes());
        printResults(gc);
    }


    @AfterAll
    public static void closeSetup(){
        out.close();
    }
}