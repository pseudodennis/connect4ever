package Legacy;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VictoryStarter
{

    public static void main(String[] args)
    {

        int nRows = 3;  // the number of rows in the board
        int nCols = 3;  // the number of cols in the board
        int iRow = 0;   // row index number for incrementing
        int iCol = 0;   // col index number for incrementing


        // create a reference chart to visualize the index locations

        // create a INDArray and initialize it with zeros
        INDArray chart = Nd4j.zeros(nRows, nCols);

        double cellRef = 0.0; // starting value for first cell

        for (iRow=0; iRow<nRows; iRow++)
        {
            cellRef = iRow;

            for (iCol = 0; iCol < nCols; iCol++)
            {
                chart.put(iRow, iCol, cellRef);
                cellRef += 0.1;
            }
            System.out.println();
        }

        System.out.print(chart);
        System.out.println();
        System.out.println();


        // create an array with sequential numbers to show how it fills up

        INDArray gbSample = Nd4j.zeros(nRows, nCols);
        cellRef=1; // starting value for first cell

        for (iRow=0; iRow<nRows; iRow++)
        {
            for (iCol = 0; iCol < nCols; iCol++)
            {
                gbSample.put(iRow, iCol, cellRef);
                System.out.print("[" + gbSample.getInt(iRow, iCol) + "]");
                cellRef++;
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();



        // check for victory

        INDArray gb = Nd4j.zeros(nRows, nCols);

        gb.put(2,0,1);
        gb.put(2,1,1);
        gb.put(2,2,1);

        for (iRow=0; iRow<nRows; iRow++)
        {
            for (iCol = 0; iCol < nCols; iCol++)
            {
                System.out.print("[" + gb.getInt(iRow, iCol) + "]");
            }
            System.out.println();
        }

        iRow=0;
        iCol=0;
        double cellVal=0;
        int cl = 3; // chain length for win
        boolean win = false;

        // get the value of 1st cell
        cellVal = gb.getDouble(iRow,iCol);

        // get coordinates of

        Set<Double> hs = new HashSet<>(Arrays.asList(cellVal));

        for (int i=iCol+1; i<cl || i>nCols; i++)
        {
            win = true;
            if (hs.add(gb.getDouble(iRow, i)));
            {
                win=false;
            }

        }



    }

}
