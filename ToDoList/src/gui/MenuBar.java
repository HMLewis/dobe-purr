package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar{
	private JTextField text;
	private JFileChooser chooser;
	private TaskHolder taskHolder;

	public MenuBar(JTextField text, JFileChooser chooser, TaskHolder taskHolder){
		this.text = text;
		this.chooser = chooser;
		this.taskHolder = taskHolder;
		
		addFileMenu(this);
	}
	
	public class Opener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int choice = chooser.showOpenDialog(null);
			setFilter("Text Files", "txt");
			if (choice == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				clearText();
				clearTasks();
				open(file);
			}
		}}
	
	class Saver implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int choice = chooser.showSaveDialog(null);
			setFilter("Text Files", "txt");
			if (choice == JFileChooser.APPROVE_OPTION) {
				save();
			}
		}
	}
	
	private void clearText(){
		text.setText("");
	}
	
	private void clearTasks(){
		ArrayList<Task> deletes = new ArrayList<Task>();
		for(Task task: taskHolder.tasks){
			deletes.add(task);
		}
		for(Task task: deletes){
			taskHolder.delete(task);
		}
	}
	
	private void open(File file){
		Scanner in = null;
		try {
			in = new Scanner(file);
			while(in.hasNext()) {
				String line = in.nextLine();
				taskHolder.update(new Task(line, taskHolder));
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	private void setFilter(String fileType, String fileExtension){
		FileNameExtensionFilter filter = new FileNameExtensionFilter(fileType, fileExtension);
		chooser.setFileFilter(filter);
		
	}
	private void save(){
		try {
			PrintStream ps = new PrintStream(chooser.getSelectedFile());
			for (Task task: taskHolder.tasks){
				ps.println(task.startLabel);
			}
				ps.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	private void addFileMenu(MenuBar bar) {
		JMenu fileMenu = new JMenu("File");
		bar.add(fileMenu);
		
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new Opener());
		fileMenu.add(open);
		
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new Saver());
		fileMenu.add(save);
		
		chooser = new JFileChooser(".");
	}
}
