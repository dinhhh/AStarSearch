package PathFinding;

import java.util.*;

import javafx.util.Pair;



public class Algorithm {


    public static final int ROW = 9;
    public static final int COL = 10;


    public static void main(String[] args) {

        int[][] grid =
                {
                        { 1, 0, 1, 1, 1, 1, 0, 1, 1, 1 },
                        { 1, 1, 1, 0, 1, 1, 1, 0, 1, 1 },
                        { 1, 1, 1, 0, 1, 1, 0, 1, 0, 1 },
                        { 0, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
                        { 1, 1, 1, 0, 1, 1, 1, 0, 1, 0 },
                        { 1, 0, 1, 1, 1, 1, 0, 1, 0, 0 },
                        { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
                        { 1, 0, 1, 1, 1, 1, 0, 1, 1, 1 },
                        { 1, 1, 1, 0, 0, 0, 1, 0, 0, 1 }
                };
        Pair<Integer, Integer> src = new Pair<Integer, Integer>(4, 0);
        Pair<Integer, Integer> dest = new Pair<Integer, Integer>(0, 5);
        aStarSearch(grid, src, dest);
    }


    // check cell(row, col) is a valid cell or not
    public static boolean isValid(int row, int col) {
        // return true if this cell is in range
        return (row >= 0) && (row < ROW) && (col >= 0) && (col < COL);
    }



    public static boolean isUnBlocked(int grid[][], int row, int col) {
        // return true if this cell is not blocked
        if(grid[row][col] == 1)
            return true;
        else
            return false;

    }

    public static boolean isDestination(int row, int col, Pair<Integer, Integer> dest) {
        // return true if the destination cell can be reached
        if(row == dest.getKey() && col == dest.getValue())
            return true;
        else
            return false;
    }

    public static double calculateHCost(int row, int col, Pair<Integer, Integer> dest) {
        // return h cost
        return (double) Math.sqrt((row - dest.getKey())*(row - dest.getKey())
                + (col - dest.getValue())*(col - dest.getValue()));
    }

    public static Pair<Double, Pair<Integer, Integer>> makePPair(double x, int a, int b){
        return new Pair<Double, Pair<Integer, Integer>>(x, makePair(a, b));
    }

    public static Pair<Integer, Integer> makePair(int x, int y){
        return new Pair<Integer, Integer>(x, y);
    }

    // print the path
    public static void tracePath(Node nodeDetail[][], Pair<Integer, Integer> dest) {

        System.out.println("The path is: ");
        int row = dest.getKey();
        int col = dest.getValue();

        Stack<Pair<Integer, Integer>> path = new Stack<Pair<Integer, Integer>>();

        // while the parent node of current node is not destination => loop
        while(!(nodeDetail[row][col].parent_i == row && nodeDetail[row][col].parent_j == col)) {
            //Pair<Integer, Integer> p = new Pair<Integer, Integer>(row, col);
            path.push(makePair(row, col));
            int temp_row = nodeDetail[row][col].parent_i;
            int temp_col = nodeDetail[row][col].parent_j;
            row = temp_row;
            col = temp_col;
        }

        // when the parent node of current node is destination
        path.push(makePair(row, col));

        // print path
        while(!path.isEmpty()) {
            Pair<Integer, Integer> p = path.peek();
            path.pop();
            System.out.println("-> (" + p.getKey() + ", " + p.getValue() +")");
        }
    }

    public static void aStarSearch(int grid[][], Pair<Integer, Integer> src, Pair<Integer, Integer> dest) {

        // if the source is out of range
        if(isValid(src.getKey(), src.getValue()) == false) {
            System.out.println("source is invalid");
            return;
        }

        // if the destination is out of range
        if(isValid(dest.getKey(), dest.getValue()) == false) {
            System.out.println("destination is invalid");
            return;
        }

        if (isUnBlocked(grid, src.getKey(), src.getValue()) == false ||
                isUnBlocked(grid, dest.getKey(), dest.getValue()) == false)
        {
            System.out.println ("Source or the destination is blocked");
            return;
        }
        if (isDestination(src.getKey(), src.getValue(), dest) == true)
        {
            System.out.println ("We are already at the destination");
            return;
        }

        // init closed list
        // return true if node at [row, col] is in closed
        // else false
        boolean[][] closedList = new boolean[ROW][COL];
        // set all in closedList to false
        for(int i = 0; i < closedList.length; i++) {
            for(int j = 0; j < closedList[i].length; j++) {
                closedList[i][j] = false;
            }
        }
        // init Node for nodeDetails
        Node[][] nodeDetails =  new Node[ROW][COL];
        for(int i = 0; i< ROW; i++){
            for(int j = 0; j< COL; j++){
                nodeDetails[i][j] = new Node();
            }
        }

        for(int i = 0; i < ROW; i++) {
            for(int j = 0; j < COL; j++) {
                nodeDetails[i][j].f = Double.MAX_VALUE;
                nodeDetails[i][j].g = Double.MAX_VALUE;
                nodeDetails[i][j].h = Double.MAX_VALUE;
                nodeDetails[i][j].parent_i = -1;
                nodeDetails[i][j].parent_j = -1;
            }
        }

        // init the parameters for starting node
        int h = src.getKey();
        int k = src.getValue();
        nodeDetails[h][k].f = 0.0;
        nodeDetails[h][k].g = 0.0;
        nodeDetails[h][k].h = 0.0;
        nodeDetails[h][k].parent_i = h;
        nodeDetails[h][k].parent_j = k;

        // init the open list having information: <f, <i, j>> where f = g + h,
        // i, j are the row and column index of that cell
        // 0 <= i <= ROW-1, 0 <= j <= COL-1
        // put the source node on openList
        Set<Pair<Double, Pair<Integer, Integer>>> openList = new HashSet<Pair<Double, Pair<Integer, Integer>>>();

        // add source node
        // f = 0.0
        openList.add(makePPair(0.0, h, k));

        boolean foundDest = false;

        while(!openList.isEmpty()) {

            ArrayList<Pair<Double, Pair<Integer, Integer>>> sortedList = new ArrayList<Pair<Double, Pair<Integer, Integer>> >();

            Iterator<Pair<Double, Pair<Integer, Integer>>> iter = openList.iterator();
            // find min F cost
            double min = iter.next().getKey();

            for (Pair<Double, Pair<Integer, Integer>> lo : openList){
                if( lo.getKey() < min){
                    min = lo.getKey();
                }
            }
            // sorted List contain the node has min F cost in open List
            for(Pair<Double, Pair<Integer, Integer>> lo : openList){
                if(lo.getKey() == min){
                    sortedList.add(lo);
                }
            }
            // remove node has min F cost in open list
            for (Iterator<Pair<Double, Pair<Integer, Integer>>> iterator = openList.iterator(); iterator.hasNext();) {
                Pair<Double, Pair<Integer, Integer>> temp = iterator.next();
                if (temp.getKey() == min) {
                    // Remove the current element from the iterator and the list.
                    iterator.remove();
                }
            }

            // now returning parent information of this min F cost node in open list????
            int i = sortedList.get(0).getValue().getKey();
            int j = sortedList.get(0).getValue().getValue() ;

            closedList[i][j] = true;



	    	/*
	        Generating all the 8 successor of this node

	            N.W   N   N.E
	              \   |   /
	               \  |  /
	            W----node----E
	                 / | \
	               /   |  \
	            S.W    S   S.E

	        node-->Popped node (i, j)
	        N -->  North       (i-1, j)
	        S -->  South       (i+1, j)
	        E -->  East        (i, j+1)
	        W -->  West           (i, j-1)
	        N.E--> North-East  (i-1, j+1)
	        N.W--> North-West  (i-1, j-1)
	        S.E--> South-East  (i+1, j+1)
	        S.W--> South-West  (i+1, j-1)
	        */

            // store g, h, f of successor
            double gNew, hNew, fNew;

            //----------- 1st Successor (North) ------------
            // Only process this node if this is a valid one
            if (isValid(i-1, j) == true) {
                // if N node == destination
                if(isDestination(i-1, j, dest) == true) {
                    // set the parent of destination node
                    nodeDetails[i-1][j].parent_i = i;
                    nodeDetails[i-1][j].parent_j = j;
                    System.out.println("The destination node is found: ");
                    tracePath(nodeDetails, dest);
                    foundDest = true;
                    return;
                }

                // if the N-successor is on the closedList or is blocked => ignore
                // if the N-successor != destination
                else if(closedList[i-1][j] == false && isUnBlocked(grid, i-1, j) == true) {
                    gNew = nodeDetails[i][j].g + 1.0;
                    hNew = calculateHCost(i-1, j, dest);
                    fNew = gNew + hNew;
                    // if N-node is NOT on the openList => add it the openList, update this f cost
                    // else if N-node is on the openList => if fNew cost < current node f => update f cost
                    if(nodeDetails[i-1][j].f == Double.MAX_VALUE || nodeDetails[i-1][j].f > fNew) {
                        openList.add(makePPair(fNew, i-1, j));
                        // update f cost
                        nodeDetails[i-1][j].f = fNew;
                        nodeDetails[i-1][j].h = hNew;
                        nodeDetails[i-1][j].g = gNew;
                        nodeDetails[i-1][j].parent_i = i;
                        nodeDetails[i-1][j].parent_j = j;
                    }
                }
            }
            //----------- 2nd Successor (South) ---`---------
            // Only process this node if this is a valid one
            if(isValid(i+1, j) == true) {
                if(isDestination(i+1, j, dest)) {
                    nodeDetails[i+1][j].parent_i = i;
                    nodeDetails[i+1][j].parent_j = j;
                    System.out.println("The destination node is found: ");
                    tracePath(nodeDetails, dest);
                    foundDest = true;
                    return;
                }
                else if(closedList[i+1][j] == false && isUnBlocked(grid, i+1, j) == true) {
                    gNew = nodeDetails[i][j].g + 1.0;
                    hNew = calculateHCost(i+1, j, dest);
                    fNew = gNew + hNew;
                    if (nodeDetails[i+1][j].f == Double.MAX_VALUE || nodeDetails[i+1][j].f > fNew)
                    {
                        openList.add(makePPair(fNew, i+1, j));
                        // Update the details of this cell
                        nodeDetails[i+1][j].f = fNew;
                        nodeDetails[i+1][j].g = gNew;
                        nodeDetails[i+1][j].h = hNew;
                        nodeDetails[i+1][j].parent_i = i;
                        nodeDetails[i+1][j].parent_j = j;
                    }
                }
            }
            //----------- 3rd Successor (East) ------------
            if (isValid (i, j+1) == true) {

                if (isDestination(i, j+1, dest) == true)
                {
                    nodeDetails[i][j+1].parent_i = i;
                    nodeDetails[i][j+1].parent_j = j;
                    System.out.println("The destination node is found: ");
                    tracePath(nodeDetails, dest);
                    foundDest = true;
                    return;
                }


                else if (closedList[i][j+1] == false &&  isUnBlocked (grid, i, j+1) == true) {
                    gNew = nodeDetails[i][j].g + 1.0;
                    hNew = calculateHCost (i, j+1, dest);
                    fNew = gNew + hNew;

                    // If it isn’t on the open list, add it to
                    // the open list. Make the current square
                    // the parent of this square. Record the
                    // f, g, and h costs of the square cell
                    //                OR
                    // If it is on the open list already, check
                    // to see if this path to that square is better,
                    // using 'f' cost as the measure.
                    if (nodeDetails[i][j+1].f == Double.MAX_VALUE ||
                            nodeDetails[i][j+1].f > fNew)
                    {
                        openList.add(makePPair(fNew, i, j+1));

                        // Update the details of this cell
                        nodeDetails[i][j+1].f = fNew;
                        nodeDetails[i][j+1].g = gNew;
                        nodeDetails[i][j+1].h = hNew;
                        nodeDetails[i][j+1].parent_i = i;
                        nodeDetails[i][j+1].parent_j = j;
                    }
                }
            }

            //----------- 4th Successor (West) ------------
            if (isValid(i, j-1) == true) {
                if (isDestination(i, j-1, dest) == true) {
                    nodeDetails[i][j-1].parent_i = i;
                    nodeDetails[i][j-1].parent_j = j;
                    System.out.println("The destination node is found: ");
                    tracePath(nodeDetails, dest);
                    foundDest = true;
                    return;
                }
                else if (closedList[i][j-1] == false &&
                        isUnBlocked(grid, i, j-1) == true)
                {
                    gNew = nodeDetails[i][j].g + 1.0;
                    hNew = calculateHCost(i, j-1, dest);
                    fNew = gNew + hNew;
                    if (nodeDetails[i][j-1].f == Double.MAX_VALUE || nodeDetails[i][j-1].f > fNew) {
                        openList.add(makePPair(fNew, i, j-1));
                        nodeDetails[i][j-1].f = fNew;
                        nodeDetails[i][j-1].g = gNew;
                        nodeDetails[i][j-1].h = hNew;
                        nodeDetails[i][j-1].parent_i = i;
                        nodeDetails[i][j-1].parent_j = j;
                    }
                }
            }

            //----------- 5th Successor (North-East) ------------

            // Only process this cell if this is a valid one
            if (isValid(i-1, j+1) == true) {
                if (isDestination(i-1, j+1, dest) == true) {
                    nodeDetails[i-1][j+1].parent_i = i;
                    nodeDetails[i-1][j+1].parent_j = j;
                    System.out.println ("The destination cell is found\n");
                    tracePath (nodeDetails, dest);
                    foundDest = true;
                    return;
                }
                else if (closedList[i-1][j+1] == false && isUnBlocked(grid, i-1, j+1) == true)  {
                    gNew = nodeDetails[i][j].g + 1.414;
                    hNew = calculateHCost(i-1, j+1, dest);
                    fNew = gNew + hNew;

                    if (nodeDetails[i-1][j+1].f == Double.MAX_VALUE ||
                            nodeDetails[i-1][j+1].f > fNew)
                    {
                        openList.add(makePPair(fNew, i-1, j+1));
                        nodeDetails[i-1][j+1].f = fNew;
                        nodeDetails[i-1][j+1].g = gNew;
                        nodeDetails[i-1][j+1].h = hNew;
                        nodeDetails[i-1][j+1].parent_i = i;
                        nodeDetails[i-1][j+1].parent_j = j;
                    }
                }
            }
            //----------- 6th Successor (North-West) ------------

            if (isValid (i-1, j-1) == true)  {
                if (isDestination (i-1, j-1, dest) == true)  {
                    nodeDetails[i-1][j-1].parent_i = i;
                    nodeDetails[i-1][j-1].parent_j = j;
                    System.out.println ("The destination cell is found\n");
                    tracePath (nodeDetails, dest);
                    foundDest = true;
                    return;
                }

                else if (closedList[i-1][j-1] == false &&
                        isUnBlocked(grid, i-1, j-1) == true)
                {
                    gNew = nodeDetails[i][j].g + 1.414;
                    hNew = calculateHCost(i-1, j-1, dest);
                    fNew = gNew + hNew;

                    if (nodeDetails[i-1][j-1].f == Double.MAX_VALUE ||
                            nodeDetails[i-1][j-1].f > fNew)
                    {
                        openList.add(makePPair(fNew, i-1, j-1));
                        nodeDetails[i-1][j-1].f = fNew;
                        nodeDetails[i-1][j-1].g = gNew;
                        nodeDetails[i-1][j-1].h = hNew;
                        nodeDetails[i-1][j-1].parent_i = i;
                        nodeDetails[i-1][j-1].parent_j = j;
                    }
                }
            }

            //----------- 7th Successor (South-East) ------------

            if (isValid(i+1, j+1) == true) {
                if (isDestination(i+1, j+1, dest) == true)  {

                    nodeDetails[i+1][j+1].parent_i = i;
                    nodeDetails[i+1][j+1].parent_j = j;
                    System.out.println ("The destination cell is found\n");
                    tracePath (nodeDetails, dest);
                    foundDest = true;
                    return;
                }
                else if (closedList[i+1][j+1] == false &&  isUnBlocked(grid, i+1, j+1) == true) {
                    gNew = nodeDetails[i][j].g + 1.414;
                    hNew = calculateHCost(i+1, j+1, dest);
                    fNew = gNew + hNew;

                    if (nodeDetails[i+1][j+1].f == Double.MAX_VALUE ||
                            nodeDetails[i+1][j+1].f > fNew)
                    {
                        openList.add(makePPair(fNew, i+1, j+1));

                        nodeDetails[i+1][j+1].f = fNew;
                        nodeDetails[i+1][j+1].g = gNew;
                        nodeDetails[i+1][j+1].h = hNew;
                        nodeDetails[i+1][j+1].parent_i = i;
                        nodeDetails[i+1][j+1].parent_j = j;
                    }
                }
            }

            //----------- 8th Successor (South-West) ------------

            if (isValid (i+1, j-1) == true)  {
                if (isDestination(i+1, j-1, dest) == true)  {
                    nodeDetails[i+1][j-1].parent_i = i;
                    nodeDetails[i+1][j-1].parent_j = j;
                    System.out.println("The destination cell is found\n");
                    tracePath(nodeDetails, dest);
                    foundDest = true;
                    return;
                }
                else if (closedList[i+1][j-1] == false && isUnBlocked(grid, i+1, j-1) == true) {
                    gNew = nodeDetails[i][j].g + 1.414;
                    hNew = calculateHCost(i+1, j-1, dest);
                    fNew = gNew + hNew;

                    if (nodeDetails[i+1][j-1].f == Double.MAX_VALUE ||
                            nodeDetails[i+1][j-1].f > fNew)
                    {
                        openList.add(makePPair(fNew, i+1, j-1));
                        nodeDetails[i+1][j-1].f = fNew;
                        nodeDetails[i+1][j-1].g = gNew;
                        nodeDetails[i+1][j-1].h = hNew;
                        nodeDetails[i+1][j-1].parent_i = i;
                        nodeDetails[i+1][j-1].parent_j = j;
                    }
                }
            }

        }
        // end while loop

        if(foundDest == false) {
            System.out.println("Can not reach the destination");
            return;
        }

    }

    // end function



}

