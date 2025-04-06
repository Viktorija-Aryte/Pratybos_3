package ND3;

public class ConwayGameOfLIfe
{
    private Grid starting_grid;
    private int generation_amount;
    private static final int GRID_SIZE = 10;

    public ConwayGameOfLIfe(int gen_amount)
    {
        this.generation_amount = gen_amount;
        this.starting_grid = new Grid(GRID_SIZE);
        initailizeGrid();
    }

    public void doCalculation()
    {
        for(int i = 0; i < GRID_SIZE; i++)
        {
            for(int j = 0; j < GRID_SIZE; j++)
            {
                int neighbours = getNeighbours(i, j);
                //if the cell is alive
                if(starting_grid.get_value(i, j) == 'X')
                {
                    //if not 2 or 3 neigtbours cell dies
                    char new_value = (neighbours == 2 || neighbours == 3) ? 'X' : '-';
                    //WRITE NEW VALUE TO NEXTGRID
                    starting_grid.set_value(i, j, new_value);

                }
                if(starting_grid.get_value(i, j) == '-')
                {
                    char new_value = (neighbours == 3) ? 'X' : '-';
                    //WRITE NEW VALUE TO NEXTGRID
                    starting_grid.set_value(i, j, new_value);
                }
            }
        }
    }
    public void perform()
    {
        System.out.println("The starting grid: ");
        starting_grid.print_grid();

        for(int i = 0; i < generation_amount; i++)
        {
            System.out.println("The generation " + (i+1) + ":");
            doCalculation();
            starting_grid.print_grid();
        }
    }

    public int getNeighbours(int col, int row)
    {
        int neighbours = 0;

        for(int i = -1; i <= 1; i++)
        {
            for(int j = -1; j <= 1; j++)
            {
                //ignore the cell itself
                if(i == 0 && j == 0)
                {
                    continue;
                }
                else if((row + i >= 0 && row + i <= (GRID_SIZE - 1)) && (col + j >= 0 && col + j <= (GRID_SIZE - 1)))
                {
                    if(starting_grid.get_value(row + i, col + j) == 'X')
                    {
                        neighbours++;
                    }

                }
            }
        }

        return neighbours;
    }
    private void initailizeGrid()
    {
        for(int i = 0; i < GRID_SIZE; i++)
        {
            for(int j = 0; j < GRID_SIZE; j++)
            {
                char newValue = (Math.random() > 0.5) ? 'X' : '-';
                starting_grid.set_value(i, j, newValue);
            }
        }
    }

    
}
