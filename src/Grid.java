import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Grid{


    private String filePath;
    private ArrayList<List<Cell>> adjacentCellList = null;
    private int nrOfRows = 0;
    private int nrOfColumns = 0;

    private int sizeOfGrid = 0;


    private Cell[][] grid;

    private ArrayList<ArrayList<ArrayList<Integer>>> adjacentList;

    private volatile List<Cell> tempCellStack = null;

    private Duration duration = null;

    private ArrayList<Cell> onesList;


    /**
     * Aux array to check adjacents in grid
     */
    private static final int[][] OFFSET = {
                                                     {-1, 0},
                                            { 0, -1},        { 0, +1},
                                                     {+1, 0}};


    /**
     * Constructor w/ file parameter
     * @param filePath
     */
    Grid(String filePath){

        this.filePath = filePath;
    }

    /**
     * Constructor w/ 2D int array parameter
     * @param gridInput
     */
    Grid(int[][] gridInput){

        nrOfRows = gridInput.length;
        nrOfColumns = gridInput[0].length;
        this.grid = new Cell[nrOfRows][nrOfColumns];

        for(int i = 0; i <  nrOfRows; i++){
            Cell[] row = new Cell[nrOfColumns];
            for(int j = 0; j < nrOfColumns; j++){
                if(gridInput[i][j] == 0){
                    row[j] = null;
                }else{
                    row[j] = new Cell(i, j, gridInput[i][j]);
                }
            }
            this.grid[i] = row;
        }
    }

    /**
     * Main function to list all the clusters
     */
    public void checkAdjacents(){

        this.adjacentCellList = new ArrayList<>();

        Instant before = Instant.now();
        if(filePath != null){
            checkAdjacentsFile();
        }else if(grid != null){
            checkAdjacentsGrid();
        }else{
            System.out.println("filePath OR grid not initialized!");
            return;
        }
        Instant after = Instant.now();
        this.duration = Duration.between(before, after);
        System.out.println("time = " + duration.toMillis() + "ms");
    }

    /**
     * Creates a list of clusters in the file
     * @return Array of lists of adjacent clusters
     */
    private ArrayList<List<Cell>> checkAdjacentsFile(){
        if(!adjacentCellList.isEmpty()){
            return adjacentCellList;
        }

        try{
            InputStream in = new FileInputStream(filePath);

            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            Gson gson = new GsonBuilder().create();
            reader.beginArray();

            int row = 0;
            int count = 0; onesList = new ArrayList<>();
            while (reader.hasNext()){


                ArrayList ar = gson.fromJson(reader, ArrayList.class);
                if(nrOfColumns==0){
                    nrOfColumns = ar.size();
                    nrOfRows = ar.size();   //TODO check exactly how many rows
                }

                //System.out.println("Processing row: " + row);
                System.out.println(((float) row/nrOfRows *100)+ "% Complete...");

                for (int col =0; col < nrOfColumns; col++) {
                        if (ar.get(col).equals(1.0) && !isCellChecked(new Cell(row, col))) {

                            count++;

                            tempCellStack = new ArrayList<>();
                            checkCellAdjacents(new Cell(row, col, 1), true);

                            tempCellStack = tempCellStack.stream().distinct().collect(Collectors.toList());
                            if (tempCellStack.size() > 1) {
                                this.adjacentCellList.add(tempCellStack);
                            }
                        }
                    //}
                }

                row++;
            }
            reader.close();
            in.close();
            System.out.println("NUMBER OF 1'S: " + count);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return adjacentCellList;
    }

    /**
     * Creates a list of clusters in the 2D array
     * @return Array of lists of adjacent clusters
     */
    private ArrayList<List<Cell>> checkAdjacentsGrid(){

        for(int i = 0; i < nrOfRows; i++){
            for(int j = 0; j < nrOfColumns; j++){
                if(grid[i][j] != null && !grid[i][j].isChecked()) {
                    ArrayList<Cell> tempList = checkCellAdjacentsGrid(grid[i][j]);
                    if(tempList.size() > 1)
                        this.adjacentCellList.add(tempList);
                }
            }
        }
        return adjacentCellList;
    }

    /**
     * Recursive function that adds all adjacents of the cell param to a temporary stack
     * Reads the grid from a file
     * @param cell Cell to check its adjacents
     * @param root
     */
    private void checkCellAdjacents(Cell cell, boolean root){

        if(root && tempCellStack != null){
            tempCellStack.clear();
        }

        if(cell == null || cell.getValue() != 1.0){
            return;
        }

        this.tempCellStack.add(cell);

        List<Thread> listT = new ArrayList<>();
        for(int[] offset : OFFSET){
            if(isInsideBounds(cell.getPosition().getI() + offset[1], cell.getPosition().getJ() + offset[0])) {
                if (!isCellInCluster(new Cell(cell.getPosition().getI() + offset[1], cell.getPosition().getJ() + offset[0])) &&
                    !isCellChecked(new Cell(cell.getPosition().getI() + offset[1], cell.getPosition().getJ() + offset[0]))
                    ){

                    Thread t = new Thread(() -> {

                            checkCellAdjacents(getCell(cell.getPosition().getI() + offset[1], cell.getPosition().getJ() + offset[0]), false);

                    });
                    listT.add(t);

                }
            }
        }

        for (Thread t : listT) {
            try {
                t.start();
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Recursive function that adds all adjacents of the cell param to a temporary stack
     * Reads the input from a 2D array
     * @param cell Cell to check its adjacents
     */
    private  ArrayList<Cell> checkCellAdjacentsGrid(Cell cell) {

        ArrayList<Cell> cellAdjacentList = new ArrayList<>();

        if(cell == null || cell.isChecked()){
            return cellAdjacentList;
        }else {
            cell.checkCell();
        }

        for(int[] offset: OFFSET){
            if(isInsideBounds(cell.getPosition().getI() + offset[1], cell.getPosition().getJ() + offset[0])){
                cellAdjacentList.addAll(checkCellAdjacentsGrid(grid[cell.getPosition().getI() + offset[1]][cell.getPosition().getJ() + offset[0]]));
            }
        }

        cellAdjacentList.add(cell);
        return cellAdjacentList;

    }

    /**
     * Aux function to get a specific Cell from the input file
     * @param i Cell's row position
     * @param j Cell's column position
     * @return
     */
    private Cell getCell(int i, int j) {

        try{
            InputStream in = new FileInputStream(filePath);

            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            Gson gson = new GsonBuilder().create();
            reader.beginArray();

            int row = 0;
            while (reader.hasNext()){
                ArrayList ar = gson.fromJson(reader, ArrayList.class);

                if(row==i){
                    Double value = (double) ar.get(j);
                    Cell newCell = new Cell(i, j, value.intValue());
                    reader.close();
                    in.close();
                    return newCell;
                }

                row++;
            }
            reader.close();
            in.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Aux function to check if the cell was already processed in the current cluster
     * @param cell
     * @return
     */
    private boolean isCellInCluster(Cell cell) {
        return tempCellStack.contains(cell);
    }

    /**
     * Aux function to check if the cell was already processed previously
     * @param cell
     * @return
     */
    private boolean isCellChecked(Cell cell) {
        for(int i = 0; i < adjacentCellList.size(); i++){
            if(adjacentCellList.get(i).contains(cell)){
                return true;
            }
        }
        return false;
    }

    /**
     * Prints the grid in the console
     */
    public void printGrid(){

        if(grid == null){
            System.out.println("Grid from File: Can not print!");
            return;
        }

        System.out.println("\t\t----------\t\tPRINTING GRID\t\t----------");

        System.out.print("[");

        for(int i = 0; i < nrOfRows; i++) {
            if(i!=0)
                System.out.print(" ");
            System.out.print("[");


            for (int j = 0; j < nrOfColumns; j++ ){

                if(grid[i][j] != null) {
                    System.out.print(grid[i][j].getValue());
                }else{
                    System.out.print("0");
                }


                if(j < nrOfColumns-1){
                    System.out.print(", ");
                }else{
                    System.out.print("]");
                }

            }

        if(i < nrOfRows-1){
                System.out.println(",");
        }

        }
        System.out.println("]");
    }

    /**
     * Prints the list of Clusters to a .solution file
     */
    public void printAdjacentList(){

        if(this.adjacentCellList == null) {
            checkAdjacents();
        }

        try {
            PrintWriter writer;
            if(filePath != null) {
                writer = new PrintWriter(filePath + ".solution");
            }else{
                writer = new PrintWriter("./testFiles/CustomGrid.solution");
            }

            System.out.println("\t\t----------\tPRINTING ADJACENT LIST\t----------");
            for (List<Cell> arrayOfCells : this.adjacentCellList) {
                writer.print("[");
                System.out.print("[");
                for(Cell cell : arrayOfCells){
                    System.out.print("["+ cell.getPosition().getI() + ", "+ cell.getPosition().getJ() +"]");
                    writer.print("["+ cell.getPosition().getI() + ", "+ cell.getPosition().getJ() +"]");
                }
                writer.println("]");
                System.out.println("]");
            }

            writer.print("Time elapsed = " + duration.toMillis() + "ms");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Ignore.
     * Testing function
     */
    public void printOnes(){

        try {
            PrintWriter writer;
            if(filePath != null) {
                writer = new PrintWriter(filePath + ".ones");
            }else{
                writer = new PrintWriter("./testFiles/CustomGrid.ones");
            }

            try {
                Instant before = Instant.now();
                InputStream in = new FileInputStream(filePath);

                JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
                Gson gson = new GsonBuilder().create();
                reader.beginArray();

                int row = 0;
                int count = 0;
                this.sizeOfGrid = 0;
                writer.println("[");
                while (reader.hasNext()) {
                    ArrayList ar = gson.fromJson(reader, ArrayList.class);

                    if(sizeOfGrid == 0){
                        sizeOfGrid = ar.size();
                    }

                    writer.print("[");

                    //System.out.println(((float) row/sizeOfGrid*100) + "% complete." + "Processing row:" + row);

                    for (int col =0; col < ar.size(); col++) {
                        if (ar.get(col).equals(1.0)) {
                            writer.print("["+row+","+col+"," + "U]");
                                if(col < ar.size()-1){
                                    writer.print(",");
                                }
                            count++;
                        }
                    }
                    System.out.println("Row " + row + " count: " + count);
                    row++;
                    if(row < sizeOfGrid) {
                        writer.println("],");
                    }else{
                        writer.println("]");
                    }

                }
                writer.println("]");
                Instant after = Instant.now();
                this.duration = Duration.between(before, after);
                writer.println("Number of Ones: " + count);
                writer.print("Time elapsed = " + duration.toMillis() + "ms");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ignore.
     * Testing function
     */
    public void checkAdjacentOnes(){
        try{
            PrintWriter writer;
            if(filePath != null){
                writer = new PrintWriter(filePath + ".solution");
            }else{
                writer = new PrintWriter("./testFiles/CustomGrid.solution");
            }

            try {
                InputStream in = new FileInputStream(filePath+".ones");

                JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
                Gson gson = new GsonBuilder().create();
                reader.beginArray();

                int count = 0;
                int row = 0;
                while (reader.hasNext()) {
                    ArrayList ar = gson.fromJson(reader, ArrayList.class);
                    writer.print("[");
                    boolean previousCell = false;
                    for (int col =0; col < ar.size(); col++) {
                        if(ar.get(col) != null){
                            if(previousCell){
                                writer.print(",");
                            }
                            writer.print("[C]");
                            previousCell = true;
                            count++;
                        }
                    }
                    System.out.println("Row " + row + " count: " + count);
                    writer.println("]");
                    row++;

                }

                writer.println("Count " + count);



            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private boolean isInsideBounds(int posI, int posJ){
        return (posI < nrOfRows &&  posI >= 0 && posJ < nrOfColumns && posJ >= 0);
    }

}
