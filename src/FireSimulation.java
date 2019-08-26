//------------------------------------------------------------------------------------
//Aniss Fadel
//Fire Spread Simulation project
//CISC 4900
//http://nifty.stanford.edu/2007/shiflet-fire/

/* 1. (This assignment does not require visualization.) Develop a simulation for
        fire as described above. Display the resulting grids by showing the value
        in each cell. Run the program several times for each of the following situations.
        Notice how the probability affects the spread of fire.
        a. probCatch is 0.2
        b. probCatch is 0.8
        c. probCatch is 0.5
*/
//------------------------------------------------------------------------------------
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class FireSimulation {
    int gridSize; //column and row size
    Cell forest[][];
    int forestX, forestY;
    int burningTrees=0;

    
     public List<Cell[][]> fire(int n, double probCatch){
        gridSize = n;
        forestX = n+2; forestY = n+2;
        forest = new Cell[forestX][forestY];
        Cell[][] init = initForest();
        List<Cell[][]> grids = new ArrayList<>();
        grids.add(init);

        //spreads fire until there isn't any fire
        do{
            grids.add(copy(applySpread(forest,probCatch)));
        }while(burningTrees>0);

//         printListGrid(grids);


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
//        printGrid(forest);
        return grid;

    }
    // takes in a grid as parameter and prints it out
    void printGrid(Cell[][] cells){
        for(int y = 0; y<cells.length; y++){
            for(int x = 0; x<cells[0].length; x++){
                if(cells[x][y]==null) {
                    System.out.print("n ");
                    continue;
                }
                else
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
                    cellsOnFire.add(territory[x][y]);
                }
            }
        }
        while(!cellsOnFire.isEmpty()){
            Cell cell = cellsOnFire.peek();
            Random ran = new Random();

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
    // prints 12 grid frames into console (6 from the beginning,
    // 3 from the middle, and 3 from the end)
    public void showGraphsTxt(List<Cell[][]> grids){
        // if the grid is of size 12 or less
        // all its frame will be printed

        if(grids.size()<=12){
            for(int i=0; i<grids.size(); i++){
                System.out.println();
                if(i == 0){
                    System.out.println("Initial grid: ");
                }else{
                    System.out.println("Grid at time step "+i+":");
                }
                printGrid(grids.get(i));
            }
        }
        else{
            for(int i=0; i<=5; i++){
                System.out.println();
                if(i == 0){
                    System.out.println("Initial grid: ");
                }else{
                    System.out.println("Grid at time step "+i+":");
                }
                printGrid(grids.get(i));
            }
            int mid = (grids.size()/2) + 1;
            for(int i = mid; i < mid+3; i++){
                System.out.println();
                System.out.println("Grid at time step "+i+":");
                printGrid(grids.get(i));
            }
            for(int i = grids.size()-3; i < grids.size(); i++){
                System.out.println();
                System.out.println("Grid at time step "+i+":");
                printGrid(grids.get(i));
            }

        }
    }
    // creates and stores at most 12 graphical grid frames into PNG file
    // (6 from the beginning, 3 from the middle, and 3 from the end)
    public void showGraphsPNG(List<Cell[][]> grids, String name)  {
        Color colors[]= {new Color(255,255,0),
                new Color(0,255,0), new Color(255,0,0) };
        int cellLength = 5;

        //each row has three grid
        //each column has four grid
        //12 grids in PNG file
        int gridHeight = grids.get(0).length, gridWidth = grids.get(0)[0].length;
        int width = cellLength * gridWidth*3;
        int height = cellLength * gridHeight*4;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        //sets background white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0, width, height);

        List<Integer> frameNum = new ArrayList<>();
        if(grids.size() <= 12) {
            for (int i = 0; i < grids.size(); i++) {
                frameNum.add(i);
            }
        }
        else{
            for(int i = 0; i<=5; i++){
                frameNum.add(i);
            }
            int mid = (grids.size()/2) + 1;
            for(int i = mid; i < mid+3; i++){
                frameNum.add(i);
            }
            for(int i = grids.size()-3; i < grids.size(); i++){
                frameNum.add(i);
            }
        }
        System.out.println("FRAME LENGTH-------------------------"+frameNum.size());
        int nextX = 0;
        int nextY = 0;
        for(int j:frameNum) {
            System.out.println("j-----------"+j);
            for(int y = 0; y<gridHeight; y++){
                for(int x = 0; x<gridWidth; x++){
                    g2d.setColor(colors[grids.get(j)[x][y].getState()]);
                    g2d.fillRect((x+nextX) * cellLength, (y+nextY) * cellLength, cellLength, cellLength);

                }
            }
            nextX+=gridWidth;
            if(nextX == gridWidth * 3){
                nextX = 0;
                nextY += gridHeight;
            }
        }

        g2d.dispose();
        File file = new File(name+".png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            System.out.println("FAILED TO CREATE PNG FILE");
        }


    }
    public static void main(String[] args) {
        FireSimulation sim = new FireSimulation();
        List<Cell[][]> grids;
        System.out.println("a. probCatch is 0.2");
        grids = sim.fire(25, 0.2);
        sim.showGraphsTxt(grids);
        sim.showGraphsPNG(grids, "grid1");
        System.out.println("-----------------------------------------------------------");
        System.out.println("b. probCatch is 0.8");
        grids = sim.fire(25, 0.8);
        sim.showGraphsTxt(grids);
        sim.showGraphsPNG(grids, "grid2");
        System.out.println("-----------------------------------------------------------");
        System.out.println("c. probCatch is 0.5");
        grids = sim.fire(25, 0.5);
        sim.showGraphsTxt(grids);
        sim.showGraphsPNG(grids, "grid3");
        System.out.println("-----------------------------------------------------------");


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

    public void setState(byte state) {
        this.state = state;
    }
}