package gui;

import javax.swing.*;

@SuppressWarnings("serial")
public class ImageFrame extends JFrame{
    ImageCanvas canvas;
    public ImageFrame() {
	setTitle("Image");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	canvas = new ImageCanvas();
	add(canvas);
	setSize(canvas.imgX, canvas.imgY);
    }
    
    public static void main(String[] args) {
	new ImageFrame().setVisible(true);
    }
}