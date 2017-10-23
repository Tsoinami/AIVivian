// ======================================================================
// FILE:        MyAI.java
//
// AUTHOR:      Abdullah Younis
//
// DESCRIPTION: This file contains your agent class, which you will
//              implement. You are responsible for implementing the
//              'getAction' function and any helper methods you feel you
//              need.
//
// NOTES:       - If you are having trouble understanding how the shell
//                works, look at the other parts of the code, as well as
//                the documentation.
//
//              - You are only allowed to make changes to this portion of
//                the code. Any changes to other portions of the code will
//                be lost when the tournament runs your code.
// ======================================================================

public class MyAI extends Agent
{
	private class Tile
                {
                        boolean pit     = false;
                        boolean wumpus  = false;
                        boolean gold    = false;
                        boolean breeze  = false;
                        boolean stench  = false;
			boolean outOfBounds = false;//assign negative infiniti in heuristic
			double risk = 0.0;
                }
	private Tile[][] board;
	private Tile outOfBoundTile;
	private int agentX;
	private int agentY;
	private int agentDirection; //0: left 1: up 2: right 3: down
	private boolean agentMoved; //Tracks if last action was move
	private boolean arrowFired;
	private boolean goldGot;
	private boolean wumpusDead;
	public MyAI ( )
	{
		// ======================================================================
		// YOUR CODE BEGINS
		// ======================================================================
		arrowFired = false; goldGot = false; wumpusDead = false;
		outOfBoundTile = new Tile(); outOfBoundTile.outOfBounds = true;
                board = new Tile[10][10];
		agentX = 0;agentY = 0;agentMoved = false;
		agentDirection = 3;
		for ( int r = 0; r < 10; ++r )
                                for ( int c = 0; c < 10; ++c )
                                        board[r][c] = new Tile();

		// ======================================================================
		// YOUR CODE ENDS
		// ======================================================================
	}
	
	public Action getAction
	(
		boolean stench,
		boolean breeze,
		boolean glitter,
		boolean bump,
		boolean scream
	)
	{
		// ======================================================================
		// YOUR CODE BEGINS
		// ======================================================================
		// Only function called by main
		updateState(stench, breeze, glitter, bump, scream);
		printBoardInfo();
		return Action.CLIMB;
		// ======================================================================
		// YOUR CODE ENDS
		// ======================================================================
	}
	
	// ======================================================================
	// YOUR CODE BEGINS
	// ======================================================================
	private void updateState
	(
		boolean stench,
                boolean breeze,
                boolean glitter,
                boolean bump,
                boolean scream
	)
	{
		if ( scream ){wumpusDead = true;}
		if (breeze) {board[agentX][agentY].breeze = true;}
		if (stench) {board[agentX][agentY].stench = true;}
		if (glitter) {board[agentX][agentY].gold = true;}
		if ( !bump && agentMoved )
		{
			//update agentx and agenty
			if (agentDirection == 0) {agentX -= 1;}
			if (agentDirection == 1) {agentY += 1;}
			if (agentDirection == 2) {agentX += 1;}
			if (agentDirection == 3) {agentY -= 1;}
		}
		else
		{	if (agentDirection == 1)
			{
				for (int x = 0; x < 10; ++x)
				{
					for (int y = agentY + 1; y < 10; ++y)
					{
						board[x][y].outOfBounds = true;
					}
				}
			}
			if (agentDirection == 2)
                        {
                                for (int x = agentX + 1; x < 10; ++x)
                                {
                                        for (int y = 0; y < 10; ++y)
                                        {
                                                board[x][y].outOfBounds = true;
                                        }
                                }
                        }

		}
	}
	//HELPERS
	
	private void printWorldInfo ( )
        {
                printBoardInfo();
        }

        private void printBoardInfo ( )
        {
                for ( int r = 9; r >= 0; --r )
                {
                        for ( int c = 0; c < 10; ++c )
                                printTileInfo ( c, r );
                        System.out.println(r);
                        System.out.println("");
                }
        }

        private void printTileInfo ( int c, int r )
        {//Is our board inverted axes?
                StringBuilder tileString = new StringBuilder();

                if (board[c][r].pit)    tileString.append("P");
                if (board[c][r].wumpus) tileString.append("W");
                if (board[c][r].gold)   tileString.append("G");
                if (board[c][r].breeze) tileString.append("B");
                if (board[c][r].stench) tileString.append("S");
		if (board[c][r].outOfBounds) tileString.append("X");

                if ( agentX == c && agentY == r )
                        tileString.append("@");

                tileString.append(".");

                System.out.printf("%8s", tileString.toString());
        }
	private Tile getLeft(int c, int r) {if (c >= 1) return board[r][c-1]; return outOfBoundTile;}
	private Tile getUp(int c, int r) {if (r <= 9) return board[r+1][c];return outOfBoundTile;}
	private Tile getRight(int c, int r) {if (c <= 9) return board[r][c+1];return outOfBoundTile;}
	private Tile getDown(int c, int r) {if (r >= 1) return board[r-1][c];return outOfBoundTile;}
	// ======================================================================
	// YOUR CODE ENDS
	// ======================================================================
}
