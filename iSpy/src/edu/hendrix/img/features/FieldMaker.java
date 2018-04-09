package edu.hendrix.img.features;

import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

public class FieldMaker {
    public FieldMaker() {
	
    }
    public static void main (String[] args) throws FileNotFoundException{
	String points, featureWidth, featureHeight, name;
	
	points = JOptionPane.showInputDialog("Number of Points: ");
	featureWidth = JOptionPane.showInputDialog("Width: ");
	featureHeight = JOptionPane.showInputDialog("Height: ");
	name = JOptionPane.showInputDialog("Name: ");
	
	PointFieldGenerator.generate(Integer.parseInt(points), 
		Integer.parseInt(featureWidth), 
		Integer.parseInt(featureHeight), 
		name);
    }
}
