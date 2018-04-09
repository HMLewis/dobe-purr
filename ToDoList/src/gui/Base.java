package gui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Base extends JFrame {
	private MenuBar bar;
	private TaskHolder holder;
	
	public Base() {
		setSize(800, 500);
		setTitle("To-do List");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		holder = new TaskHolder();
		
		add(holder);
		
		bar = new MenuBar(new JTextField(), new JFileChooser(), holder);
		this.setJMenuBar(bar);
		
	}
	
	public static void main(String[] args) {
		new Base().setVisible(true);
	}
	}