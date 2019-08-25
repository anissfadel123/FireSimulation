import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FireSimulation {
    int gridSize; //column and row size
    Cell forest[][];
    int forestX, forestY;
    int burningTrees;
    FireSimulation(int n, double probCatch){
        gridSize = n;
        fire(n, probCatch);
    }
    
     public List<Cell[][]> fire(int n, double probCatch){
        forestX = n+2; forestY = n+2;
        forest = new Cell[forestX][forestY];
        Cell[][] init = initForest();
        List<Cell[][]> grids = new ArrayList<>();
        grids.add(init);

        do{
            applySpread(forest,probCatch);

        }while(burningTrees>0);

        return grids;
    }
    // initForest() initializes the forest with EMPTY cells on the
    // boundary and TREE cells in the interior except for one BURNING
    // cell in the middle
    // returns the initial Forest grid
    public Cell[][] initForest(){
        Cell[][] grid = new Cell[forestX][forestY];
        for(int x = 0; x<forestX; x++){
            grid[x][0] = new Cell(Cell.EMPTY, x, 0);
            grid[x][forestY-1] = new Cell(Cell.EMPTY, x, forestY-1);
        }
        for(int y = 1; y < forestY-1; y++){
            grid[0][y] = new Cell(Cell.EMPTY, 0, y);
            grid[forestX-1][y] = new Cell(Cell.EMPTY, forestX-1, y);
        }

        for(int y = 1; y < forestY-1; y++){
            for(int x = 1; x < forestX-1; x++){
                grid[x][y] = new Cell(Cell.TREE);
            }
        }
        //sets the fire in the middle of the grid;
        int xMid = grid[0].length/2, yMid = grid.length/2;
        grid[xMid][yMid].setState(Cell.BURNING);
        burningTrees = 1;

        for(int y = 0; y<grid.length; y++){
            for(int x = 0; x<grid[0].length;x++)
                forest[x][y] = new Cell(grid[x][y].getState());
        }
        printGrid(forest);
        return grid;

    }
    // takes in a grid as parameter and prints it out
    void printGrid(Cell[][] cells){
        for(int y = 0; y<cells.length; y++){
            for(int x = 0; x<cells[0].length; x++){
                if(cells[x][y]==null) {
                    System.out.print("n ");
                    continue;
                } else
                System.out.print(cells[x][y].getState()+" ");
            }
            System.out.println("");
        }
    }
    // prints out a list of grids
    void printListGrid(List<Cell[][]> grids){
        for(Cell[][] gr:grids){
            printGrid(gr);
            System.out.println("");
        }
    }
    // applies spread of fire to forest('territory') if and only if
    // fire exist in that forest
    void applySpread(Cell[][] territory, double prob){
        Queue<Cell> cellsOnFire = new LinkedList<>();
        //scans for fire and places it in queue
        for(int x = 0; x < territory[0].length; x++){
            for(int y = 0; y < territory.length; y++){
                if(territory[x][y].getState() == Cell.BURNING){
                    cellsOnFire.add(territory[x][y]);
                }
            }
        }

    }

    public static void main(String[] args) {
        FireSimulation sim = new FireSimulation(25,0.5);
    }

}
class Cell{
    public static final byte EMPTY = 0;
    public static final byte TREE = 1;
    public static final byte BURNING = 2;
    
    public int x, y;  //its location if it is in a grid
    private byte state;

    public Cell(byte state){
        this(state, -1, -1);
    }
    public Cell(byte state, int x, int y){
        this.state = state;
    }
    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }
}