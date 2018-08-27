//The Screen class is where the majority of the calculations
//are done to get the program working
import java.util.ArrayList;
import java.awt.Color;
public class Screen
{
	//map is the same map created in the game class
	//The screen object uses this to figure out where walls
	//are and how far away from the player they are
	public int[][] map;
	//Width and height define the size of the screen, and should
	//always be the same as the width and height of the frame 
	//created in the Game class.
	public int mapWidth, mapHeight, width, height;
	//Textures is a list of all the textures so that 
	//the screen object can access the pixels of the textures
	public ArrayList textures;
	public Screen(int[][] m, int mapW, int mapH, ArrayList<Texture> tex, int w, int h) 
	{
		map = m;
		mapWidth = mapW;
		mapHeight = mapH;
		textures = tex;
		width = w;
		height = h;
	}
	//The update method recalculates how the screen should look 
	//to the user based on their position in the map. The method 
	//is called constantly, and returns the updated array of 
	//pixels to the Game class.
	public int[] update(Camera camera, int[] pixels)
	{
		//Having the top and bottom of the screen be two different 
		//colors also makes it seem like there is a floor and a ceiling
		//Color top of the screen
		for(int n = 0; n < pixels.length/2; n++)
		{
			if(pixels[n] != Color.DARK_GRAY.getRGB()) 
			{
				pixels[n] = Color.DARK_GRAY.getRGB(); 
			}
		}
		//color bottom of the screen
		for(int i = pixels.length / 2; i < pixels.length; i++)
		{
			if(pixels[i] != Color.gray.getRGB()) 
			{
				pixels[i] = Color.gray.getRGB();
			}
		}
		//The program loops through every vertical bar on the screen and
		//casts a ray to figure out what wall should be on the screen at that 
		//vertical bar
		for(int x=0; x<width; x=x+1) 
		{
			//CameraX is the x-coordinate of the current vertical stripe 
			//on the camera plane
		    double cameraX = 2 * x / (double)(width) -1;
		    //rayDir variables make a vector for the ray
		    double rayDirX = camera.xDir + camera.xPlane * cameraX;
		    double rayDirY = camera.yDir + camera.yPlane * cameraX;
		    //Map position
		    int mapX = (int)camera.xPos;
		    int mapY = (int)camera.yPos;
		    //All of the variables ending in DistX or DistY are calculated 
		    //so that the program only checks for collisions at the places 
		    //where collisions could possibly occur
		    //
		    //Length of ray from current position to next x or y-side
		    double sideDistX;
		    double sideDistY;
		    //Length of ray from one side to next in map
		    double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
		    double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
		    //perpWallDist is the distance from the player to the first wall the ray 
		    //collides with
		    double perpWallDist;
		    //Direction to go in x and y
		    int stepX, stepY;
		    boolean hit = false;//was a wall hit
		    int side = 0;//was the wall vertical or horizontal
		    //Figure out the step direction and initial distance to a side
		    if (rayDirX < 0)
		    {
		        stepX = -1;
		        sideDistX = (camera.xPos - mapX) * deltaDistX;
		    }
		    else
		    {
		        stepX = 1;
		        sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
		    }
		    if (rayDirY < 0)
		    {
		        stepY = -1;
		        sideDistY = (camera.yPos - mapY) * deltaDistY;
		    }
		    else
		    {
		        stepY = 1;
		        sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
		    }
		    //time to figure out where the ray collides with a wall. To do 
		    //this the program goes through a loop where it checks if the 
		    //ray has come into contact with a wall, and if not moves to the 
		    //next possible collision point before checking again.
		    //
		    //Loop to find where the ray hits a wall
		    while(!hit) 
		    {
		        //Jump to next square
		        if (sideDistX < sideDistY)
		        {
		            sideDistX += deltaDistX;
		            mapX += stepX;
		            side = 0;
		        }
		        else
		        {
		            sideDistY += deltaDistY;
		            mapY += stepY;
		            side = 1;
		        }
		        //Check if ray has hit a wall
		        if(map[mapX][mapY] > 0) hit = true;
		        
		        //Now that we know where the ray hits a wall we can 
		        //start figuring out how the wall should look in the vertical
		        //stripe we are currently on. To do this we first calculate
		        //the distance to the wall, and then use that distance to 
		        //figure out how tall the wall should appear in the vertical
		        //strip. We then translate that height to a start and finish
		        //in terms of the pixels on the screen
		        //Calculate distance to the point of impact
		        if(side==0)
		        {
		            perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX);
		        }
	            else
	            {
		            perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY);    
	            }
	            //Now calculate the height of the wall based on the distance from the camera
		        int lineHeight;
		        if(perpWallDist > 0) 
	        	{
		        	lineHeight = Math.abs((int)(height / perpWallDist));
	        	}
		        else 
	        	{
		        	lineHeight = height;
	        	}
		        //calculate lowest and highest pixel to fill in current stripe
		        int drawStart = -lineHeight/2+ height/2;
		        if(drawStart < 0)
	        	{
		        	drawStart = 0;
	        	}
		        int drawEnd = lineHeight/2 + height/2;
		        if(drawEnd >= height) 
	            {
		        	drawEnd = height - 1;
	            }
		        //begin figuring out what pixels from the texture of the wall 
		        //will actually appear to the user. For this we first must figure 
		        //out what texture is associated with the wall we just hit and then 
		        //figure out the x-coordinate on the texture of the pixels that 
		        //will appear to the user.
		        //
		        //add a texture
		        int texNum = map[mapX][mapY] - 1;
		        double wallX;//Exact position of where wall was hit
		        if(side==1) 
		        {	
		        	//If its a y-axis wall
		            wallX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX);
		        }
		        else
		        {
		        	//X-axis wall
		            wallX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY);
		        }
		        wallX-=Math.floor(wallX);
		        //x coordinate on the texture
		        int texX = (int)(wallX * (textures.get(texNum).SIZE));
		        if(side == 0 && rayDirX > 0)
		        {
		        	texX = textures.get(texNum).SIZE - texX - 1;
		        }
		        if(side == 1 && rayDirY < 0) 
		        {
		        	texX = textures.get(texNum).SIZE - texX - 1;
		        }
		        //The x-coordinate is calculated by taking the exact position of where the wall was hit on the 
		        //2D map and subtracting the integer value, leaving only the decimal. This decimal (wallX) is 
		        //then multiplied by the size of the texture of the wall to get the exact x-coordinate on 
		        //the wall of the pixels we wish to draw. Once we know that the only thing left to do is calculate
		        //the y-coordinates of the pixels on the texture and draw them on the screen. To do this we loop 
		        //through all of the pixels on the screen in the vertical strip we are doing calculations for 
		        //and calculate the the exact y-coordinate of the pixel on the texture. Using this the program 
		        //then writes the data from the pixel on the texture into the array of pixels on the screen. 
		        //The program also makes horizontal walls darker than vertical walls here to give a basic lighting effect.
		        //
		       //calculate y coordinate on texture
		        for(int y=drawStart; y<drawEnd; y++)
		        {
		            int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2;
		            int color;
		            if(side==0)
	            	{
		            	color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
	            	}
		            else 
	            	{
		            	//Make y sides darker
		            	color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)]>>1) & 8355711;
	            	}
		            pixels[x + y*(width)] = color;
		            return pixels;
		        }
		    }		    
		}
	}
}
