import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Imaged 
{
	protected static Image spritesheet;
	
	protected void setUpImages()
	{
		if(spritesheet == null)
		{
			try 
			{
				spritesheet = ImageIO.read(new File("Arkanoid_Sheet.png"));
			}
			catch(IOException e)
			{}
		}
	}
	
	abstract Image openImageFromSpriteSheet(int x, int y, int w, int h);
	abstract void setUp();
}
