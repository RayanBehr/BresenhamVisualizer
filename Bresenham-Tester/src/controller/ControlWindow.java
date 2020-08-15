package controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Emulator;
import view.ViewWindow;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

public class ControlWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	private Emulator emulator;
	private ViewWindow vw;
	
	private boolean showLive = true;
	
	final int matrixSize = 200;

	/**
	 * Create the frame.
	 */
	public ControlWindow() {
		setTitle("Control Window");
		//Control Window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1000, 100, 300, 240);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		///////////////////////////////////////////////////// Position Sliders Start
		JLabel lblPositionY = new JLabel("position y");
		lblPositionY.setBounds(10, 76, 80, 14);
		contentPane.add(lblPositionY);
		
		JLabel lblPositionX = new JLabel("position x");
		lblPositionX.setBounds(10, 127, 80, 14);
		contentPane.add(lblPositionX);
		
		JSlider sliderY = new JSlider();
		JSlider sliderX = new JSlider();
		sliderY.setValue(0);
		sliderX.setValue(0);
		
		sliderY.setMaximum(matrixSize-1);
		sliderX.setMaximum(matrixSize-1);
		sliderY.setBounds(10, 101, 268, 15);
		sliderX.setBounds(10, 150, 268, 15);
		
		sliderY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				vw.setVisualData(emulator.getVisualData(sliderX.getValue(), 
														sliderY.getValue()));
			}
		});

		sliderX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				vw.setVisualData(emulator.getVisualData(sliderX.getValue(), 
														sliderY.getValue()));
			}
		});
		
		contentPane.add(sliderY);	
		contentPane.add(sliderX);	
		///////////////////////////////////////////////////// Position Sliders End
		
		JTextField textFieldPathJPG = new JTextField();
		textFieldPathJPG.setText("C:\\StaticData\\Geometry\\4_200x200.jpg");
		textFieldPathJPG.setBackground(Color.GRAY);
		textFieldPathJPG.setBounds(10, 11, 268, 20);
		contentPane.add(textFieldPathJPG);
		textFieldPathJPG.setColumns(10);
		
		JButton btnImportAndApply = new JButton("Import & Apply");
		btnImportAndApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					emulator.setSolidMask(textFieldPathJPG.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnImportAndApply.setBackground(Color.GRAY);
		btnImportAndApply.setBounds(10, 42, 268, 23);
		contentPane.add(btnImportAndApply);
		
		JToggleButton tglbtnShowLive = new JToggleButton("Show Live");
		tglbtnShowLive.setBackground(Color.GRAY);
		tglbtnShowLive.setBounds(10, 171, 268, 23);
		tglbtnShowLive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showLive = tglbtnShowLive.isSelected();
			}
		});
		contentPane.add(tglbtnShowLive);
		
		////////////////
		
		
		//Emulator
		emulator = new Emulator(matrixSize); 
		emulator.start();
	
		//View Window
		vw = new ViewWindow(matrixSize);
		vw.setVisible(true);
		/////////////
		
		/*
		observer = new Thread(new Runnable() {
		    @Override
		    public void run() {
				while(true) {
					if(tglbtnShowLive.isSelected()) {
						sliderY.setValue(Emulator.currentTy);
						sliderX.setValue(Emulator.currentTx);
					}
				}
		    }
		});  
		observer.start();
		*/
		
		new Thread(() -> {
			while(this.isEnabled()) {
				
				if(showLive) {
					sliderY.setValue(Emulator.currentTy);
					sliderX.setValue(Emulator.currentTx);
				}
				else {
					Point m = new Point();
					m = vw.getMouseOnArray();
					//System.out.println(m.x + ", " + m.y);
					sliderY.setValue(m.y);
					sliderX.setValue(m.x);
				}
			}
		}).start();
		
	}
}
