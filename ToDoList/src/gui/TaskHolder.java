package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TaskHolder extends JPanel{
	ArrayList<Task> tasks;
	
	JLabel userLabel;
	JTextField buttonLabeler;
	JButton adderButton;
	JPanel makerPanel;
	
	TaskHolder(){		
		tasks = new ArrayList<Task>();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		makerPanel = new JPanel();
		
		userLabel = new JLabel("Type in a task:");
				
		makerPanel.add(userLabel);
		
		buttonLabeler = new JTextField();
		buttonLabeler.addActionListener(new AddTask());
		buttonLabeler.setPreferredSize(new Dimension(300,30));
		makerPanel.add(buttonLabeler);
		
		adderButton = new JButton("Add");
		adderButton.addActionListener(new AddTask());
		makerPanel.add(adderButton);
		
		add(makerPanel);
	}
	
	public void update(Task task){
		add(task);
		tasks.add(task);
		revalidate();
	}
	public void delete(Task task){
		this.remove(task);
		tasks.remove(task);
		revalidate();
	}
	
	private TaskHolder getHolder(){
		return this;
	}
	
	private class AddTask implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			update(new Task(buttonLabeler.getText(),getHolder()));
			buttonLabeler.setText("");
		}
	}
}
