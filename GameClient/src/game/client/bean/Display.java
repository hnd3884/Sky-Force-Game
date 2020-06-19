package game.client.bean;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {
	private String title;
	private int width;
	private int height;

	public static JFrame frame;
	private Canvas canvas;

	public Display(String title,int width,int height){
	       this.title = title;
	        this.width = width;
	        this.height= height;
	        createDisplay();
	       }

	public void createDisplay() {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setBackground(new Color(90, 105, 161));
		canvas.setFocusable(false);
		frame.add(canvas);
		frame.pack();

	}

	public Canvas getCanvas() {
		return canvas;
	}
}
