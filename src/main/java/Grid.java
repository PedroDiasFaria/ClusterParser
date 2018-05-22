import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Grid {

    private final Path filePath;
    private final Path solutionPath =  Paths.get(System.getProperty("user.dir"), "solutionFiles");
    private byte[][] cellsGrid;

    /**
     * Temporary list of adjacent cells
     */
    private ArrayList<Cell> cellClusterTemp = null;

    private int nrOfClusters = 0;
    private int nrOfOnes = 0;

    private FileWriter fw;
    private BufferedWriter bw;
    private PrintWriter out;

    private Duration duration = null;

    /**
     * Aux array to check adjacents in grid
     */
    private static final int[][] OFFSET = {
                                                        {-1, 0},
                                            { 0, -1},               { 0, +1},
                                                        {+1, 0}};


    /**
     * Class constructor thar sets up the solution file with all
     * the lists of adjacent points
     * @param filePath the file path to be parsed as a grid
     */
    Grid(Path filePath){

        this.filePath = filePath;


        Path solutionPath = Paths.get(this.solutionPath.toString(), filePath.getFileName().toString()+".solution");
        try{
            File file = solutionPath.toFile();//new File(solutionPath.toString());
            if(file.exists()){
                file.delete();
            }else{
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            this.fw = new FileWriter(solutionPath.toString(), true);
            this.bw = new BufferedWriter(fw);
            this.out = new PrintWriter(bw);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Main function of the class
     * Reads all the values from the file to a 2D array of Bytes
     * representing each cell of the grid
     */
    public void checkAdjacents(){

        /**
         * Gson - A Java serialization/deserialization library to convert Java Objects into JSON and back
         * https://github.com/google/gson
         */
        Gson gson = new Gson();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            this.cellsGrid = gson.fromJson(reader, byte[][].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Instant before = Instant.now();
        checkAdjacentsGrid();
        Instant after = Instant.now();

        this.duration = Duration.between(before, after);
        this.out.close();

        System.out.println("-----------------------------------------------");
        System.out.println("File: " + getFileName());
        System.out.println("time = " + duration.toMillis() + "ms");
        System.out.println("ones count = " + getNrOfOnes() + " | clusters = " + getNrOfClusters());
        System.out.println("-----------------------------------------------\n");
    }

    /**
     * Computing function that will create the list of cells in a cluster
     * each time we pass through an unvisited cell with a value of 1
     *
     */
    private void checkAdjacentsGrid() {

        for(int i = 0; i < this.cellsGrid.length; i++){
            for(int j = 0; j < this.cellsGrid[i].length; j++){
                if(this.cellsGrid[i][j]== (byte) 1){
                    cellClusterTemp = new ArrayList<>();

                    checkCellAdjacents(i, j);

                    /* Only counts as a cluster if there is at least two 1's adjacent */
                    if(cellClusterTemp.size() > 1){
                        printAdjacents();
                        this.nrOfClusters++;
                    }
                }
            }
        }
    }

    /**
     * Computing function that receives a cell and finds, checks
     * and adds to a temporary list all its adjacent neighbours (non diagonally)
     * @param x x position of the cell
     * @param y y position of the cell
     */
    private void checkCellAdjacents(int x, int y) {

        checkCell(x, y);
        this.cellClusterTemp.add(new Cell(x, y, 1));
        this.nrOfOnes++;

        for(int[] offset : OFFSET){
            if(isInsideGrid(x + offset[1], y + offset[0])) {
                if (this.cellsGrid[x+offset[1]][y+offset[0]] == (byte) 1){
                    checkCellAdjacents(x + offset[1], y + offset[0]);
                }
            }
        }
    }

    /**
     * Prints every array of cells representing a cluster
     * to the .solution file
     * This function is called every time it is found
     * all the cells of a cluster
     */
    private void printAdjacents() {

        boolean previous = false;
        this.out.print("[ ");
        for (Cell cell : this.cellClusterTemp) {
            if(previous){
                this.out.print(", ");
            }
            previous = true;
            this.out.print("["+ cell.getPosition().getX() + ","+ cell.getPosition().getY() +"]");
        }
        this.out.println(" ]");
    }

    /**
     *
     * @return the number of clusters in the grid
     */
    public int getNrOfClusters(){
        return this.nrOfClusters;
    }

    /**
     *
     * @return the number of cells with value 1 in the grid
     */
    public int getNrOfOnes(){
        return this.nrOfOnes;
    }

    /**
     *
     * @return the duration of the adjacent lists computing
     */
    public Duration getDuration(){
        return this.duration;
    }

    /**
     *
     * @return the name of the file representing the grid
     */
    public String getFileName(){
        return this.filePath.getFileName().toString();
    }

    /**
     * Computing function that evaluates if a cell is inside the grid's boundaries
     * @param x x position of the cell
     * @param y y position of the cell
     * @return true if the cell is inside the grid, false otherwise
     */
    private boolean isInsideGrid(int x, int y) {
        return (x < this.cellsGrid.length &&  x >= 0 && y < this.cellsGrid[x].length && y >= 0);
    }

    /**
     * Marks the cell received as checked, so that future iterations
     * do not process it again.
     * A cell is checked if its already a 0
     * @param x x position of the cell
     * @param y y position of the cell
     */
    private void checkCell(int x, int y){
        this.cellsGrid[x][y] = 0;
    }
}
