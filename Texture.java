//Textures will be applied to the various walls in the 
//environment, and will come from images saved in the 
//project folder
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Texture 
{
	//Example textures
	public static Texture wood = new Texture("res/brick_wood.png", 64);
	public static Texture brick = new Texture("res/brick2.png", 64);
	public static Texture bluestone = new Texture("res/brick_blue.png", 64);
	public static Texture stone = new Texture("res/brick1.png", 64);
	//pixels is used to hold the data for all the pixels 
	//in the image of the texture
	public int[] pixels;
	//Loc is used to indicate to the computer where the 
	//image file for the texture can be found
	private String loc;
	//SIZE is how big the texture is on one side (a 64x64 
	//image would have size 64), and all textures will be 
	//perfectly square
	public int SIZE;
	public Texture(String location, int size)
	{
		loc = location;
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		load();
	}
	private void load()
	{
		try 
		{
			//reading the data from the file that loc points to 
			//and writing this data to a buffered image
			BufferedImage image = ImageIO.read(new java.io.FileInputStream(loc));
			int w = image.getWidth();
			int h = image.getHeight();
			//The data for every pixel is then taken from the buffered
			//image and stored in pixels
			image.getRGB(0, 0, w, h, pixels, 0, w);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
