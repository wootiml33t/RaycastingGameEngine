//This class is for holding all map data
//Moving the implementation of the game map from the game object to here
	//will allow for the ability to load diffrent maps into the game with more ease
public class Map 
{
	public int width;
	public int height;
	public int[][] map =
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
	public Map(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	public Map(int width, int height, int[][] mapData)
	{
		this(width, height);	
		this.map = mapData;
	}		
}
