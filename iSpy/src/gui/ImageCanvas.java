package gui;

import ispy.YUYVImageList;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;

@SuppressWarnings("serial")
public class ImageCanvas extends JPanel{

    public int imgX;
    public int imgY;
    private String imageString;
    private YUYVImageList imgList;
    private CustomAdapter adapter;

    public ImageCanvas() {
	imageString = open(new File("images.txt"));
	imgList = YUYVImageList.fromString(imageString);
	adapter = new CustomAdapter();
	imgX = imgList.getImage(0).toBufferedImage().getWidth();
	imgY = imgList.getImage(0).toBufferedImage().getHeight()+45;
	addMouseListener(adapter);
	addMouseMotionListener(adapter);
    }

    private String open(File file){
	String retStr = new String();
	Scanner in = null;
	try {
	    in = new Scanner(file);
	    while(in.hasNext()) {
		String line = in.nextLine();
		retStr = retStr + line;
	    }
	    in.close();
	} catch (IOException ex) {
	    ex.printStackTrace();
	    System.out.println("You're not using the YUYV Image Format.");
	}
	return retStr;
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g.create();
	int curX = 0;
	int curY = 0;
	g2d.drawImage(imgList.getImage(1).toBufferedImage(), curX, curY, null);
	
	float dash1[] = {10.0f};
        BasicStroke dashed =
                new BasicStroke(3.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, dash1, 0.0f);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(dashed);
        if(adapter.clicked){
            Rectangle rs = new Rectangle();     
            rs.setFrameFromDiagonal(adapter.mouseAnchor,adapter.dragPoint);
            g2d.draw(rs);
        }
        repaint();
	
    }

}
