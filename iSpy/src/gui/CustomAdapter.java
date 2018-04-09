package gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomAdapter extends MouseAdapter{

    public Point mouseAnchor;
    public Point dragPoint;
    public int x,y;
    public boolean clicked = false;
    
    @Override
    public void mousePressed(MouseEvent e) {
	mouseAnchor = e.getPoint();
	dragPoint = e.getPoint();
	clicked = true;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	clicked = true;
	dragPoint = e.getPoint();
	int width = dragPoint.x - mouseAnchor.x;
	int height = dragPoint.y - mouseAnchor.y;

	x = mouseAnchor.x;
	y = mouseAnchor.y;

	if (width < 0) {
	    x = dragPoint.x;
	    width *= -1;
	}
	if (height < 0) {
	    y = dragPoint.y;
	    height *= -1;
	}
    }
}
