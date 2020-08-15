package view;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ViewWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	DataVisualizer dv;

	/**
	 * Create the frame.
	 */
	public ViewWindow(int matrixSize) {
		setTitle("View Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 100, 670, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		dv = new DataVisualizer(matrixSize);
		dv.setBounds(10, 11, 638, 643);
		dv.setBackground(Color.DARK_GRAY);
		contentPane.add(dv);
	}
	
	public void setVisualData(int[][] visualData) {
		dv.setVisualData(visualData);
	}
	
	public Point getMouseOnArray() {
		return dv.getMousIndices();
	}
	
}
