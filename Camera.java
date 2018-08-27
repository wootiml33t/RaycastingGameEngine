//The Camera class keeps track of where the player is 
//located in the 2D map, and also takes care of updating
//the player's position
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Camera implements KeyListener
{
	//xPos and yPos are the location of the player on 
	//the 2D map that was created in the Game class.
	
	//xDir and yDir are the x and y components of a 
	//vector that points in the direction the player is facing
	
	//xPlane and yPlane are also the x and y components of a vector
	//The vector defined by xPlane and yPlane is always perpendicular
	//to the direction vector, and it points to the farthest edge of 
	//the camera's field of view on one side. The farthest edge on the 
	//other side is just the negative plane vector. The combination of 
	//the direction vector and the plane vector defines what is in the
	//camera's field of view	
	public double xPos, yPos, xDir, yDir, xPlane, yPlane;
	//The booleans are used to keep track of what keys are being pressed
	//by the user so that the user can move the camera. 
	//yPlane is also the Field of View
	public boolean left, right, forward, backward;
	//MOVE_SPEED and ROTATION_SPEED dictate how quickly the camera moves
	//and turns while the user is pressing the corresponding key.
	public final double MOVE_SPEED = .08;
	public final double ROTATION_SPEED = .045;
	public Camera(double x, double y, double xd, double yd, double xp, double yp)
	{
		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;
	}
	public void keyPressed(KeyEvent key)
	{
		if((key.getKeyCode() == KeyEvent.VK_LEFT))
		{
			left = true;
		}
		if((key.getKeyCode() == KeyEvent.VK_RIGHT))
		{
			right = true;
		}
		if((key.getKeyCode() == KeyEvent.VK_UP))
		{
			forward = true;
		}
		if((key.getKeyCode() == KeyEvent.VK_DOWN))
		{
			backward = true;
		}
	}
	public void keyReleased(KeyEvent key)
	{
		if((key.getKeyCode() == KeyEvent.VK_LEFT))
		{
			left = false;
		}
		if((key.getKeyCode() == KeyEvent.VK_RIGHT))
		{
			right = false;
		}
		if((key.getKeyCode() == KeyEvent.VK_UP))
		{
			forward = false;
		}
		if((key.getKeyCode() == KeyEvent.VK_DOWN))
		{
			backward = false;
		}
	}
	//update method that is called in the run method of the Game class
	public void update(int[][] map)
	{
		//The parts of the method that control forward and backwards movement
		//work by adding xDir and yDir to xPos and yPos, respectively. Before 
		//this movement happens the program checks if the movement will put the 
		//camera inside a wall, and doesn't go through with the movement if it will.
		if(forward)
		{
			if(map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0)
			{
				xPos+=xDir*MOVE_SPEED;
			}
		}
		if(backward) 
		{
			if(map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0)
			{
				xPos-=xDir*MOVE_SPEED;
			}
			if(map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == 0)
			{
				yPos-=yDir*MOVE_SPEED;
			}
		}
		if(right)
		{
			double oldxDir=xDir;
			xDir=xDir*Math.cos(-ROTATION_SPEED) - yDir*Math.sin(-ROTATION_SPEED);
			yDir=oldxDir*Math.sin(-ROTATION_SPEED) + yDir*Math.cos(-ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane=xPlane*Math.cos(-ROTATION_SPEED) - yPlane*Math.sin(-ROTATION_SPEED);
			yPlane=oldxPlane*Math.sin(-ROTATION_SPEED) + yPlane*Math.cos(-ROTATION_SPEED);
		}
		if(left) 
		{
			double oldxDir=xDir;
			xDir=xDir*Math.cos(ROTATION_SPEED) - yDir*Math.sin(ROTATION_SPEED);
			yDir=oldxDir*Math.sin(ROTATION_SPEED) + yDir*Math.cos(ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane=xPlane*Math.cos(ROTATION_SPEED) - yPlane*Math.sin(ROTATION_SPEED);
			yPlane=oldxPlane*Math.sin(ROTATION_SPEED) + yPlane*Math.cos(ROTATION_SPEED);
		}
	}
	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub		
	}

}
