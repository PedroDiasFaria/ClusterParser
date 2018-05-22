public class Cell {
    private int value;
    private boolean checked;
    private Position cellPosition;

    public class Position{
        private int x;
        private int y;

        Position(int posX, int posY){
            x = posX;
            y = posY;
        }

        public int getX(){
            return this.x;
        }

        public int getY(){
            return this.y;
        }
    }

    Cell(int x, int y, int v){

        this.cellPosition = new Position(x, y);
        this.value = v;

        //We only check for cells wit 1, so we skip the 0 valued ones
        if(v==0){
            this.checked = true;
        }else{
            this.checked = false;
        }
    }

    Cell(int x, int y){
        this.cellPosition = new Position(x, y);
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

}
