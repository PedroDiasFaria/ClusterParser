import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AuxParser {

    private int[][] grid = null;
    FileDoubleMatrix arr = null;
    private ArrayList<ArrayList<Integer>> gridA = null;

    AuxParser(String filePath)  {

        FileInputStream inputStream = null;
        Scanner sc = null;

        try {
            inputStream = new FileInputStream(filePath);
            sc = new Scanner(inputStream, "UTF-8");

            int row = 0;
            int gridSize = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String rowString = line.replaceAll("\\[", "").replaceAll("\\]", "");

                if(rowString.isEmpty())
                    continue;

                String[] values = rowString.split(",");

                if(gridA == null){
                    gridSize = values.length;
                    gridA = new ArrayList<>();
                    //grid = new int[gridSize][gridSize];
                }

                ArrayList<Integer> rowL = new ArrayList<>();
                for (int col = 0; col < gridSize; col++) {
                    rowL.add(Integer.parseInt(values[col]));
                    //grid[row][col] = Integer.parseInt(values[col]);
                }
                gridA.add(rowL);
                row++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    AuxParser(String filePath, String rafUse){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            int row = 0;
            int gridSize = 0;

            while ((line = br.readLine()) != null) {
                String rowString = line.replaceAll("\\[", "").replaceAll("\\]", "");

                if(rowString.isEmpty())
                    continue;

                String[] values = rowString.split(",");

                if(arr == null){
                    gridSize = values.length;

                    arr = new FileDoubleMatrix(new File("arr.dat"), gridSize, gridSize);
                }

                for (int col = 0; col < gridSize; col++) {
                    arr.set(row, col, Integer.parseInt(values[col]));
                }

                row++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public FileDoubleMatrix getFDM(){
        return this.arr;
    }

    public int[][] getGrid(){
        return this.grid;
    }

    public void printGrid() {
        for(int i = 0 ; i < gridA.get(0).size(); i++){
            for(int j = 0; j < gridA.get(0).size(); j++){
                System.out.print(gridA.get(i).get(j));
            }
            System.out.println(",");
        }
    }
}
