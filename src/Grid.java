import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Grid {

    private final String filePath;
    private byte[][] cellsGrid;
    private byte[][] checkedCells;

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
     *
     * @param filePath
     */
    Grid(String filePath){

        this.filePath = filePath;

        String solutionPath = filePath+".solution";

        try{
            File file = new File(solutionPath);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();

            this.fw = new FileWriter(solutionPath, true);
            this.bw = new BufferedWriter(fw);
            this.out = new PrintWriter(bw);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void checkAdjacents(){

        Gson gson = new Gson();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            this.cellsGrid = gson.fromJson(reader, byte[][].class);
            buildCheckedGrid();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Instant before = Instant.now();
        checkAdjacentsGrid();
        Instant after = Instant.now();
        this.duration = Duration.between(before, after);

        this.out.close();

        System.out.println("time = " + duration.toMillis() + "ms");
        System.out.println("ones count = " + getNrOfOnes() + " | clusters = " + getNrOfClusters());
    }

    /**
     *
     */
    private void buildCheckedGrid() {

        this.checkedCells = new byte[this.cellsGrid.length][];

        for(int i = 0; i < this.cellsGrid.length; i++){
            byte[] row = new byte[this.cellsGrid[i].length];
            for(int j = 0; j < this.cellsGrid[i].length; j++){
                if(this.cellsGrid[i][j]==1){
                    row[j] = 0;
                }else{
                    row[j] = 1;
                }
            }
            this.checkedCells[i] = row;
        }
    }

    /**
     *
     */
    private void checkAdjacentsGrid() {

        for(int i = 0; i < this.cellsGrid.length; i++){
            System.out.println(((float) i/this.cellsGrid.length*100) + "% complete." + "Processing row:" + i);
            for(int j = 0; j < this.cellsGrid[i].length; j++){
                if((this.cellsGrid[i][j]== (byte) 1) && (this.checkedCells[i][j] == (byte) 0)){
                    cellClusterTemp = new ArrayList<>();
                    checkCellAdjacents(i, j);

                    /* Only counts as a cluster if there is at least two 1's adjacent */
                    if(cellClusterTemp.size() > 1){
                        printAdjacents(cellClusterTemp);
                        this.nrOfClusters++;
                    }
                }
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     */
    private void checkCellAdjacents(int x, int y) {

        checkCell(x, y);
        this.cellClusterTemp.add(new Cell(x, y, 1));
        this.nrOfOnes++;

        for(int[] offset : OFFSET){
            if(isInsideGrid(x + offset[1], y + offset[0])) {
                if (this.checkedCells[x+offset[1]][y+offset[0]] == (byte) 0){
                    checkCellAdjacents(x + offset[1], y + offset[0]);
                }
            }
        }
    }

    /**
     *
     * @param adjacentList
     */
    private void printAdjacents(ArrayList<Cell> adjacentList) {

        boolean previous = false;
        this.out.print("[ ");
        for (Cell cell : adjacentList) {
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
     * @return
     */
    public int getNrOfClusters(){
        return this.nrOfClusters;
    }

    /**
     *
     * @return
     */
    public int getNrOfOnes(){
        return this.nrOfOnes;
    }

    /**
     *
     * @return
     */
    public Duration getDuration(){
        return this.duration;
    }

    public String getFilePath(){
        return this.filePath;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isInsideGrid(int x, int y) {
        return (x < this.cellsGrid.length &&  x >= 0 && y < this.cellsGrid[x].length && y >= 0);
    }

    /**
     *
     * @param x
     * @param y
     */
    private void checkCell(int x, int y){
        this.checkedCells[x][y] = 1;
    }
}
