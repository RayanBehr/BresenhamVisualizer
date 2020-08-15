package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Emulator extends Thread {
	private boolean shouldTerminate = false;
	
	int matrixSize;
	
	private boolean[][] solidMask;
	private boolean[][][][] collisionMap;
	
	public static int currentTx = 0;
	public static int currentTy = 0;
	
	private int computationsPerTimeStep = 0;
	
	//0 : current tile
	//1 : solid
	//2 : can be interacted with
	//3 : can't be interacted with
	
	public Emulator(int matrixSize) {
		this.matrixSize = matrixSize;
		
		solidMask = new boolean[matrixSize][matrixSize];
		collisionMap = new boolean[matrixSize][matrixSize][matrixSize][matrixSize];
		

	}
	
	
	public void run() {	
		while(!shouldTerminate) {
			for(int column = 0; column < matrixSize; column++) {
				computeCollisionMap(column);
			}
		}
		
	}
	
    private void computeCollisionMap(int column) {
        int Tx = column;
        for(int Ty = 0; Ty < matrixSize; Ty++) {
            for(int Nx = 0; Nx < matrixSize; Nx++) {
                for(int Ny = 0; Ny < matrixSize; Ny++) {
                    if(collision(Tx,Ty,Nx,Ny)) {
                        collisionMap[Tx][Ty][Nx][Ny] = true;
                    }
                    computationsPerTimeStep++;
                }
            }
            currentTy = Ty;
        }
        currentTx = Tx;
        System.out.println("Column : " + Tx);
        System.out.println(computationsPerTimeStep);
        computationsPerTimeStep = 0;
    }
	
	public int[][] getVisualData(int Tx, int Ty) {
		int[][] visualData = new int[matrixSize][matrixSize];
		//0 : current tile
		//1 : solid
		//2 : can be interacted with
		//3 : can't be interacted with
    	for(int Nx = 0; Nx < matrixSize; Nx++)
			for(int Ny = 0; Ny < matrixSize; Ny++) {
				if(collisionMap[Tx][Ty][Nx][Ny] || solidMask[Tx][Ty]) {
					visualData[Nx][Ny] = 3;
				}
				else {
						visualData[Nx][Ny] = 2;
				}
				if(solidMask[Nx][Ny]) {
					visualData[Nx][Ny] = 1;
				}
					
			}
    	visualData[Tx][Ty] = 0;
					
		
		
		return visualData;	
	}
	
	
	private boolean collision(int Tx, int Ty,int Nx,int Ny) {	
		//Bresenham algorithm from the Internet 
		
	    // Bresenham-based super-cover line algorithm
	    // see http://www.ese-metz.fr/~dedu/projects/bresenham/
		// drawing pixels was replaced by checking the solidMask
		{
			    boolean vEnabledEnhanced = true;

			    int y1 = Ty;
			    int x1 = Tx;
			    int y2 = Ny;
			    int x2 = Nx;

			    int i;               // loop counter
			    int ystep, xstep;    // the step on y and x axis
			    int error;           // the error accumulated during the increment
			    int errorprev;       // *vision the previous value of the error variable
			    int y = y1, x = x1;  // the line points
			    int ddy, ddx;        // compulsory variables: the double values of dy and dx
			    int dx = x2 - x1;
			    int dy = y2 - y1;
			    
			    // NB the last point can't be here, because of its previous point (which has to be verified)
			    if (dy < 0)
			    {
			      ystep = -1;
			      dy = -dy;
			    }
			    else
			    {
			      ystep = 1;
			    }
			    if (dx < 0)
			    {
			      xstep = -1;
			      dx = -dx;
			    }
			    else
			    {
			      xstep = 1;
			    }
			    ddy = 2 * dy;  // work with double values for full precision
			    ddx = 2 * dx;
			    if (ddx >= ddy)
			    {  // first octant (0 <= slope <= 1)
			      // compulsory initialization (even for errorprev, needed when dx==dy)
			      errorprev = error = dx;  // start in the middle of the square
			      for (i=0 ; i < dx ; i++)
			      {  // do not use the first point (already done)
			        x += xstep;
			        error += ddy;
			        if (error > ddx)
			        {  // increment y if AFTER the middle  (> )
			          y += ystep;
			          error -= ddx;
			          if (vEnabledEnhanced)
			          {
			            // three cases (octant == right->right-top for directions below):
			            if (error + errorprev < ddx)  // bottom square also
  			              if(solidMask[x][y-ystep]) return true;
			            else if (error + errorprev > ddx)  // left square also
			              if(solidMask[x-xstep][y]) return true;
			            else
			            {  // corner: bottom and left squares also
			              if(solidMask[x][y-ystep]) return true;
			              if(solidMask[x-xstep][y]) return true;
			            }
			          }
			        }
			        if(solidMask[x][y]) return true;
			        errorprev = error;
			      }
			    }
			    else
			    {  // the same as above
			      errorprev = error = dy;
			      for (i=0 ; i < dy ; i++)
			      {
			        y += ystep;
			        error += ddx;
			        if (error > ddy)
			        {
			          x += xstep;
			          error -= ddy;
			          if (vEnabledEnhanced)
			          {
			            if (error + errorprev < ddy)
			            if(solidMask[x-xstep][y]) return true;
			            else if (error + errorprev > ddy)
			            if(solidMask[x][y-ystep]) return true;
			            else
			            {
			              if(solidMask[x-xstep][y]) return true;
			              if(solidMask[x][y-ystep]) return true;
			            }
			          }
			        }
			        if(solidMask[x][y]) return true;
			        errorprev = error;
			      }
			    }
			  }
		return false;
	}
	
	public void setSolidMask(String path) throws IOException {
        final File file = new File(path);
        final BufferedImage image = ImageIO.read(file);   
        
        int width = image.getWidth();
        int height = image.getHeight();
        
        boolean[][] visualData = new boolean[(int)(width)][(int)(height)];
        
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                final int clr = image.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;

                if(red + green + blue < 240) {
                	visualData[x][y] = true;
                } 
                else {
                	visualData[x][y] = false;
                }
            }
        }
        
        solidMask = visualData;
	}
		
}
