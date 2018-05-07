import java.util.ArrayList;

public class Cell {
    private int value;
    private boolean checked;
    private Position cellPosition;

    public class Position{
        private int i;
        private int j;

        Position(int posI, int posJ){
            i = posI;
            j = posJ;
        }

        public int getI(){
            return this.i;
        }

        public int getJ(){
            return this.j;
        }

        public ArrayList<Integer> toList(){
            ArrayList<Integer> positionAsList = new ArrayList<>();
            positionAsList.add(i);
            positionAsList.add(j);

            return positionAsList;
        }
    }

    Cell(int i, int j, int v){

        this.cellPosition = new Position(i, j);
        this.value = v;

        //We only check for cells wit 1, so we skip the 0 valued ones
        if(v==0){
            this.checked = true;
        }else{
            this.checked = false;
        }
    }

    Cell(int i, int j){
        this.cellPosition = new Position(i, j);
        this.value = 0;

        this.checked = true;
    }

    public int getValue(){
        return this.value;
    }

    public Position getPosition(){
        return cellPosition;
    }

    public boolean isChecked(){
        return checked;
    }

    public void checkCell(){
        this.checked = true;
    }

    @Override
    public boolean equals(Object object){
        boolean same = false;

        if(object != null && object instanceof Cell){
            same = (this.cellPosition.i == ((Cell) object).cellPosition.i) && (this.cellPosition.j == ((Cell) object).cellPosition.j);
        }

        return same;
    }

    @Override
    public int hashCode(){
        return this.value   ;
    }
}
