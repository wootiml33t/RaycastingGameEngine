//Tutorial for this project found at: https://www.instructables.com/id/Making-a-Basic-3D-Engine-in-Java/
/*
 * TODO:
 * -Move map into its own object
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import javax.swing.JFrame;
public class Game extends JFrame implements Runnable
//This class will handle displaying images to the user
//, calling on other classes that will recalculate
//what needs to be displayed, and updating the position of the camera
{
	private static final long serialVersionUID = 1L;
	public int mapWidth = 15;
	public int mapHeight = 15;
	private Thread thread;
	private boolean running;	
	public ArrayList<Texture> textures;
	public Camera camera;
	public Screen screen;
	// BufferedImage is what is displayed to the user
	private BufferedImage image;
	//pixels is an array of all the pixels in the image
	public int[] pixels;
	//A 0 represents empty space while any other number 
	//represents a solid wall and the texture that goes with it
	public static int[][] map =
		{
				{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
				{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
				{1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
				{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
				{1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
				{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
				{1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
				{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
				{1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
				{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
				{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
				{1,0,0,2,0,0,1,4,0,3,3,3,3,0,4},
				{1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
				{1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
			};
	//The other variables won't really appear again, they are just 
	//used to get the graphics and program working properly.
	public Game()
	{
		//"this" can be the target because it implements Runnable
		thread = new Thread(this);
		image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		//connect pixels and image so that any time the data values in pixels 
		//are changed the corresponding changes appear on the image when it is 
		//displayed to the user
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		textures = new ArrayList<Texture>();
		textures.add(Texture.wood);
		textures.add(Texture.brick);
		textures.add(Texture.bluestone);
		textures.add(Texture.stone);
		
		
		//.66 id FOV but can be adjusted to prefrences
		camera = new Camera(4.5, 4.5, 1, 0, 0, -.66);
		screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480);
		
		//Since the Camera class implements keyListener we have to add it
		addKeyListener(camera);		
		
		//JFrame methods to configure the JFrame
		setSize(640, 480);
		setResizable(false);
		setTitle("Raycasting Engine");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);
		start();
	}
	//in a multi-threaded environment, a race condition occurs when two or more 
	//threads attempt to update mutable shared data at the same time. Java offers
	//a mechanism to avoid race conditions by synchronizing thread access to 
	//shared data: the keyword synchronized
	private synchronized void start() 
	{
		running = true;
		thread.start();		
	}
	public synchronized void stop()
	{
		running = false;
		try
		{
			thread.join();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	public void render()
	{
		//buffer strategy is used when rendering so that screen updates are smoother
		//using a buffer strategy just helps the game look better when running
		BufferStrategy bs = getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		//to actually draw the image to the screen a graphics object is obtained from 
		//the buffer strategy and is used to draw our image
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image,  0, 0, image.getWidth(), image.getHeight(), null);
		bs.show();
	}
	//despite having a nested loop this method doesnt run in n^2 because the
	//second loop runs a fixed number of iterations?
	public void run() 						
	{
		long lastTime = System.nanoTime();
		final double nanoSeconds = 1000000000.0 / 60.0;//60 times per sec
		double delta = 0;
		//Requests that this Component gets the input focus
		//Note that the use of this method is discouraged because its behavior is platform dependent.
		requestFocus();
		while(running)
		{
			long now = System.nanoTime();
			delta = delta + ((now - lastTime) / nanoSeconds);
			lastTime = now;
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//handles all of the logic restricted time
				screen.update(camera, pixels);
				camera.update(map);				
				delta--;
			}
			//displays to the screen unrestricted time
			render();
		}				
	}
	public static void main(String[] args)
	{
		Game game = new Game();
	}
}