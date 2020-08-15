package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DataVisualizer extends JPanel {
	private static final long serialVersionUID = 1L;
	
    int matrixSize;
	//0 : current tile
	//1 : solid
	//2 : can be interacted with
	//3 : can't be interacted with
	private int[][] visualData;
	
    //FPS is a bit primitive, 
	//you can set the MaxFPS as high as u want
    static double drawFPS = 0, 
    	   MaxFPS = 24, 
    	   SleepTime = 1000.0/MaxFPS, 
    	   LastRefresh = 0, 
    	   StartTime = System.currentTimeMillis(), 
    	   LastFPSCheck = 0, 
    	   Checks = 0;
    
    Color colorFPS = Color.ORANGE;
   
    int yArrayMouse = 0;
    int xArrayMouse = 0;
    
    int tileSize = 3;
    
    public DataVisualizer(int matrixSize) {
    	this.matrixSize = matrixSize;
    	visualData = new int[matrixSize][matrixSize];
    	
    }
    
    public Point getMousIndices() {
    	Point p = new Point();
    	if(isInBounds()) {
    		p.y = yArrayMouse;
    		p.x = xArrayMouse;
    	}
    	else {
    		p.y = 0;
    		p.x = 0;
    	}
    	return p;
    }
    
    private boolean isInBounds() {
    	boolean myBoolean = false;
		if ((yArrayMouse >= 0) && (yArrayMouse < matrixSize)) {
			if ((xArrayMouse >= 0) && (xArrayMouse < matrixSize)) {
				myBoolean = true;
			}
		}   	
    	return myBoolean;
    }
    
    public void setVisualData(int[][] visualData) {
    	this.visualData = visualData;
    }
	
	//Refill the Background and overdraw
	void fillBackground(Graphics g) {
		g.setColor(super.getBackground());
		g.fillRect(0, 0, super.getWidth(), super.getHeight());
	}

	
	void drawData(Graphics g) {
		for(int x = 0; x < matrixSize; x++)
			for(int y = 0; y < matrixSize; y++) {
				int xPos = x*tileSize+20;
				int yPos = y*tileSize+20;
				
				g.setColor(Color.DARK_GRAY);
				g.drawRect(xPos, yPos, tileSize, tileSize);
				
				Color c = Color.BLUE;
				//0 : current tile
				//1 : solid
				//2 : can be interacted with
				//3 : can't be interacted with
				if(visualData[x][y] == 0) {
					c = Color.GREEN;
				}
				if(visualData[x][y] == 1) {
					c = new Color(200,200,20);
				}
				if(visualData[x][y] == 2) {
					c = new Color(100,100,100);
				}
				if(visualData[x][y] == 3) {
					c = Color.BLACK;
				}		
				g.setColor(c);
				g.fillRect(xPos, yPos, tileSize, tileSize);
			}

	}

	
	//Draws the FPS
	void drawInformation(Graphics g) {
		g.setColor(colorFPS);
		g.drawString("FPS: " + (int)drawFPS, 2, 11);
	}
	
	public void setShownData() {
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
	
		fillBackground(g);

		drawData(g);
		
		drawInformation(g);
		
		showMouseInfo(g);
		
		//Repaint with FPS
		SleepAndRefresh();
	}
	
	private void showMouseInfo(Graphics g){
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, getRootPane());
		// Then use 'p', which was modified by method call above
		
		xArrayMouse = (p.x-20-10)/tileSize;
		yArrayMouse = (p.y-20-11)/tileSize;		
		
		boolean inField = xArrayMouse < matrixSize && 
						  yArrayMouse < matrixSize && 
						  xArrayMouse >= 0 && 
						  yArrayMouse >= 0;
		int info = 0;	
		if(xArrayMouse >= 0 && 
		   yArrayMouse >= 0 &&
		   xArrayMouse < matrixSize &&
		   yArrayMouse < matrixSize) {
			info = visualData[xArrayMouse][yArrayMouse];
		}
		
		g.setColor(Color.WHITE);
		g.drawString("Mouse Position: " +
					 xArrayMouse + ", "+ yArrayMouse, 120, 635);
		g.drawString("Mouse in Field: " + inField + "   " +
				     "Info at Mouse: " + info, 270, 635);
	
	}
	
	//FPS
	void SleepAndRefresh()
    {
    	long timeSLU = (long) 
    			(System.currentTimeMillis() - LastRefresh); 
    
    	Checks ++;			
    	if(Checks >= 15)
    	{
    		drawFPS = Checks/((System.currentTimeMillis() 
    				- LastFPSCheck)/1000.0);
    		LastFPSCheck = System.currentTimeMillis();
    		Checks = 0;
    	}
    	
    	if(timeSLU < 1000.0/MaxFPS)
    	{
    		try {
    			Thread.sleep((long) (1000.0/MaxFPS 
    					- timeSLU));
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}	
    	}
    			
    	LastRefresh = System.currentTimeMillis();	
    	repaint();
    }	

}

