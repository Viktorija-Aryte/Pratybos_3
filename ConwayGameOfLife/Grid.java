package ConwayGameOfLife;
public class Grid {

    //the 2D array that will represent the grid
    private char[][] grid;
    private int grid_size;

    public Grid(int gridSize)
    {
        this.grid_size = gridSize;
        this.grid = new char[grid_size][grid_size];
    }

    /* PRIVATE MERTHODS */

    /*
     * 'X' - alive cell
     * '-' - dead cell
     */
    private void fill_cells()
    {
        int row = 0;
        int col = 0;

        while(col < grid_size && row < grid_size)
        {
            //do something
            grid[row][col] = '-';
            //end do something
            col++;

            if(col == (grid_size))
            {
                col = 0;
                row++;
            }
        }
    }

    /* PUBLIC METHODS */
    public void print_grid()
    {
        int row = 0;
        int col = 0;

        while(col < grid_size && row < grid_size)
        {
            //do something
            System.out.print(grid[row][col] + " ");
            //end do something
            col++;

            if(col == (grid_size))
            {
                System.out.println();
                col = 0;
                row++;
            }
        }
    }

    public void set_value(int row_ind, int col_ind, char value)
    {
        grid[row_ind][col_ind] = value;
    }

    public char get_value(int row_ind, int col_ind)
    {
        return grid[row_ind][col_ind];
    }
}
