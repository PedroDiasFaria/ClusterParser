import org.junit.jupiter.api.Test;

public class GridTest {

    public int[][] setupGridInput(){
        int[][] gridInput = new int[][] {  {0,0,0,1,0,0,1,1},
                                        {0,0,1,1,1,0,1,1},
                                        {0,0,0,0,0,0,1,0},
                                        {0,0,0,1,0,0,1,1},
                                        {0,0,0,1,0,0,1,1}};
        return gridInput;
    }

    //@Test
    public void testGridPrint(){
        Grid newGrid = new Grid(setupGridInput());

        newGrid.printGrid();
    }

    //@Test
    public void testAdjacents1(){
        Grid newGrid = new Grid(setupGridInput());

        newGrid.checkAdjacents();
        newGrid.printAdjacentList();

    }

    @Test
    public void testWithParser100x100(){
        Grid newGrid = new Grid("./testFiles/100x100.json");

        newGrid.checkAdjacents();
        newGrid.printAdjacentList();
    }

    @Test
    public void testWithParser1000x1000(){
        Grid newGrid = new Grid("./testFiles/1000x1000.json");

        newGrid.checkAdjacents();
        newGrid.printAdjacentList();
    }

    //@Test
    public void testWithParser10000x10000(){
        Grid newGrid = new Grid("./testFiles/10000x10000.json");

        newGrid.checkAdjacents();
        newGrid.printAdjacentList();
    }

    //@Test
    public void testWithParser20000x20000(){
        Grid newGrid = new Grid("./testFiles/20000x20000.json");

        newGrid.checkAdjacents();
        newGrid.printAdjacentList();
    }


}