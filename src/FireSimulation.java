import java.sql.SQLOutput;
import java.util.*;

public class FireSimulation {
    int gridSize; //column and row size
    Cell forest[][];
    int forestX, forestY;
    int burningTrees=0;
//    FireSimulation(){ }
//    FireSimulation(int n, double probCatch){
//        gridSize = n;
//        fire(n, probCatch);
//    }
    
     public List<Cell[][]> fire(int n, double probCatch){
        gridSize = n;
        forestX = n+2; forestY = n+2;
        forest = new Cell[forestX][forestY];
        Cell[][] init = initForest();
        List<Cell[][]> grids = new ArrayList<>();
        grids.add(init);

         System.out.println("------------------------------------------------------------------");
        do{
//            Cell[][] a = applySpread(forest,probCatch).clone();

            grids.add(copy(applySpread(forest,probCatch)));
//            printGrid(a);
//            grids.add(a);


        }while(burningTrees>0);
         System.out.println("Grid:  ");


         System.out.println("------------------------------------------------------------------");
         printListGrid(grids);


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
                grid[x][y] = new Cell(Cell.TREE, x, y);
            }
        }
        //sets the fire in the middle of the grid;
        int xMid = grid[0].length/2, yMid = grid.length/2;
        grid[xMid][yMid].setState(Cell.BURNING);
        grid[xMid][yMid].x = xMid;
        grid[xMid][yMid].y = yMid;
        burningTrees++;

        for(int y = 0; y<grid.length; y++){
            for(int x = 0; x<grid[0].length;x++)
                forest[x][y] = new Cell(grid[x][y].getState(),x,y);
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
    public Cell[][] applySpread(Cell[][] territory, double prob){
        Queue<Cell> cellsOnFire = new LinkedList<>();
        //scans for fire and places it in queue
        for(int x = 0; x < territory[0].length; x++){
            for(int y = 0; y < territory.length; y++){
                if(territory[x][y].getState() == Cell.BURNING){
                    System.out.println("burning:("+territory[x][y].x+","+territory[x][y].y+")");
                    cellsOnFire.add(territory[x][y]);
                }
            }
        }
        while(!cellsOnFire.isEmpty()){
            Cell cell = cellsOnFire.peek();
            Random ran = new Random();
            System.out.println(cell.x+" "+cell.y);

            // checks if north cell is burnable (it is burnable if it is a TREE)
            // and creates a fire if the ran.nextDouble is equal to or less than the probability
            if(territory[cell.x][cell.y-1].getState() == Cell.TREE && ran.nextDouble() <= prob){
                territory[cell.x][cell.y-1].setState(Cell.BURNING);
                burningTrees++;
            }

            //south
            if(territory[cell.x][cell.y+1].getState() == Cell.TREE && ran.nextDouble() <= prob) {
                territory[cell.x][cell.y + 1].setState(Cell.BURNING);
                burningTrees++;
            }
            //west
            if(territory[cell.x-1][cell.y].getState() == Cell.TREE && ran.nextDouble() <= prob){
                territory[cell.x-1][cell.y].setState(Cell.BURNING);
                burningTrees++;
            }

            //east
            if(territory[cell.x+1][cell.y].getState() == Cell.TREE && ran.nextDouble() <= prob){
                territory[cell.x+1][cell.y].setState(Cell.BURNING);
                burningTrees++;
            }
            cell.setState(Cell.EMPTY);
            burningTrees--;
            cellsOnFire.remove();
        }
//        System.out.println("territory: ");
//        printGrid(territory);
        return territory;

    }
    //makes and returns a copy of a cell grid
    public Cell[][] copy(Cell[][] cells){
         Cell[][] copyGrid = new Cell[cells[0].length][cells.length];
        for(int y = 0; y<cells.length; y++){
            for(int x = 0; x<cells[0].length; x++){
                copyGrid[x][y] = new Cell(cells[x][y].getState(), x, y);
            }
        }
        return copyGrid;
    }

    public static void main(String[] args) {
        FireSimulation sim = new FireSimulation();
//        sim.printListGrid(sim.fire(25, 0.50));
        sim.fire(25, 0.50);
    }

}
class Cell{
    public static final byte EMPTY = 0;
    public static final byte TREE = 1;
    public static final byte BURNING = 2;
    
    public int x, y;  //keeps track of the cell's location if it is in a grid
    private byte state;

    public Cell(byte state){
        this(state, -1, -1);
    }
    public Cell(byte state, int x, int y){
        this.state = state;
        this.x = x;
        this.y = y;
    }
    public byte getState() {
        return state;
    }
    //returns a copy of this cell


    public void setState(byte state) {
        this.state = state;
    }
}