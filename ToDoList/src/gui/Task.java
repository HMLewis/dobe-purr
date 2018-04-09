package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Task extends JPanel {
	public JLabel label;
	public String startLabel;
	public int count;
	private JButton checkAndClear;
	private TaskHolder parent;
	
	public Task(String startLabel, TaskHolder parent){
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		this.parent = parent;
		this.startLabel = startLabel;
		this.label = new JLabel(parent.tasks.size()+1+")  "+this.startLabel);
		checkAndClear = new JButton();
		if(startLabel.contains("--Is Done--")){
			checkAndClear.setText("Delete");
		}else{
			checkAndClear.setText("Done?");
		}
		checkAndClear.addActionListener(new Checker());
		this.setBounds(this.getX(), this.getY(), this.getWidth(), this.label.getHeight()+this.checkAndClear.getHeight());
		setBorder(BorderFactory.createLineBorder(Color.black));
		add(this.label);
		add(checkAndClear);
	}
	
	private void deleteSelf(){
		parent.delete(this);
	}

	private class Checker implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!checkAndClear.getText().equals("Delete")){
				label.setText(label.getText()+"\u2713");
				startLabel += "--Is Done--";
				checkAndClear.setText("Delete");
			}
			else{
				deleteSelf();
			}
		}
	}
}