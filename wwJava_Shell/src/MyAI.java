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
	private String lastAction;
	private String agentStatus;

	//edgeWalking AI
	private int botCount;
	private int rightCount;
	private int topCount;
	private int leftCount;
	public MyAI ( )
	{
		// ======================================================================
		// YOUR CODE BEGINS
		// ======================================================================
		botCount = 0; rightCount = 0; topCount = 0; leftCount = 0;agentStatus = "EXPLORING";
		
		arrowFired = false; goldGot = false; wumpusDead = false;
		outOfBoundTile = new Tile(); outOfBoundTile.outOfBounds = true;
                board = new Tile[10][10];
		agentX = 0;agentY = 0;agentMoved = false;
		agentDirection = 2;
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
		//updateState(stench, breeze, glitter, bump, scream);
		String myAction = getDirection(stench, breeze, glitter, bump, scream);
		if (myAction.equals("FORWARD")) commitAction(myAction);
		System.out.println("testing bump walls on return: " + Boolean.toString(botCount == 0) + " " + Boolean.toString(agentStatus.equals("EXITING")));
		System.out.println(myAction.equals("FORWARD"));
		System.out.println(agentStatus.equals("EXPLORING"));
		System.out.println("bot right top left ");
		System.out.println(Integer.toString(botCount) + " "+ Integer.toString(rightCount) + " " + Integer.toString(topCount) + " " + Integer.toString(leftCount));
		System.out.println("agentStatus: " + agentStatus);
		System.out.println("Agent Direction: " + Integer.toString(agentDirection));
		System.out.println("Going to commit action: " + myAction);
		switch (myAction)
		{
			case "FORWARD":  return Action.FORWARD;
			case "TURN_LEFT": return Action.TURN_LEFT;
                        case "TURN_RIGHT": return Action.TURN_RIGHT;
                        case "SHOOT": return Action.SHOOT;
                        case "GRAB": return Action.GRAB;
                        case "CLIMB": return Action.CLIMB;
		}
		return Action.CLIMB;
		// ======================================================================
		// YOUR CODE ENDS
		// ======================================================================
	}
	
	// ======================================================================
	// YOUR CODE BEGINS
	// ======================================================================
	private void commitAction(String action)
	{
		if (action.equals("FORWARD"))
		{
			if (agentStatus.equals("EXPLORING"))
			{
				if (agentDirection == 2)	botCount += 1;
				else if (agentDirection == 1)	rightCount += 1;
				else if (agentDirection == 0)	topCount += 1;
				else leftCount += 1; 
			}
			else
			{
				if (agentDirection == 0) botCount -= 1;
				else if (agentDirection == 3) rightCount -= 1;
				else if (agentDirection == 2) topCount -= 1;
				else leftCount -= 1;
			}
		}
	}
	private String getDirection
		(
		boolean stench,
                boolean breeze,
                boolean glitter,
                boolean bump,
                boolean scream
		)
	{
		if (agentDirection == 4) agentDirection = 0;
		if (bump) {if (agentDirection == 2) botCount -= 1;
                                else if (agentDirection == 1) rightCount -= 1;
                                else if (agentDirection == 0) topCount -= 1;
                                else leftCount -= 1;}
		if (breeze) agentStatus = "BREEZE"; if (scream) wumpusDead = true;
		if (stench && arrowFired && !wumpusDead) agentStatus = "EXITING";
		if (glitter && !goldGot) {lastAction = "GRAB"; goldGot = true; agentStatus = "EXITING";return "GRAB";}
		if (stench && !arrowFired &&  !breeze && !agentStatus.equals("EXITING")) {
			lastAction = "SHOOT";
			arrowFired = true;
			return "SHOOT";
		}
		if (botCount == 0 && agentStatus.equals("EXITING"))
			return "CLIMB";
		if (agentStatus.equals("EXPLORING")) 
		{
			if (botCount > 0 && rightCount == 0 && bump)
			{ agentDirection -=1; lastAction = "TURN_LEFT"; return "TURN_LEFT";}
			if (botCount > 0 && rightCount > 0 && topCount == 0 && bump)
			{ agentDirection -=1; lastAction = "TURN_LEFT"; return "TURN_LEFT";}
			if (botCount > 0 && rightCount > 0 && topCount > 0 && leftCount == 0 && bump)
                                {agentDirection = 3; lastAction = "TURN_LEFT"; return "TURN_LEFT";}
			if (botCount > 0 && rightCount > 0 && topCount > 0 && leftCount > 0 && leftCount == rightCount)
				return "CLIMB";
		}
		else if (agentStatus.equals("EXITING"))
		{
			if (botCount == 0) return "CLIMB";
			if (botCount > 0 && rightCount == 0  && agentDirection != 0)
                        { agentDirection +=1; lastAction = "TURN_RIGHT"; return "TURN_RIGHT";}
                        if (botCount > 0 && rightCount > 0 && topCount == 0  && agentDirection != 3)
                        { agentDirection +=1; lastAction = "TURN_RIGHT"; return "TURN_RIGHT";}
                        if (botCount > 0 && rightCount > 0 && topCount > 0 && leftCount == 0 && agentDirection == 1)
			{ agentDirection +=1; lastAction = "TURN_RIGHT"; return "TURN_RIGHT";}
		}
		else if (agentStatus.equals("BREEZE"))
		{
			if (botCount > 0 && rightCount == 0 && agentDirection != 0)
                        { agentDirection -=1; lastAction = "TURN_LEFT"; return "TURN_LEFT";}
                        if (botCount > 0 && rightCount > 0 && topCount == 0  && agentDirection != 3)
                        { agentDirection +=1; lastAction = "TURN_RIGHT"; return "TURN_RIGHT";}
                        if (botCount > 0 && rightCount > 0 && topCount > 0 && leftCount == 0 && agentDirection != 2)
                        { agentDirection +=1; lastAction = "TURN_RIGHT"; return "TURN_RIGHT";}
			if (botCount > 0 && rightCount > 0 && topCount > 0 && leftCount > 0 && agentDirection != 1)
			{ agentDirection -= 1; lastAction = "TURN_LEFT"; return "TURN_LEFT";}
			if (botCount == 0)
				return "CLIMB";
			agentStatus = "EXITING";
		}
		lastAction = "FORWARD";
		return "FORWARD";
	}
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
