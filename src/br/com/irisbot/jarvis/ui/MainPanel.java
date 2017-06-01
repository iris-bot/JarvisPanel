package br.com.irisbot.jarvis.ui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private ImageIcon logo = new ImageIcon(CommandPanel.class.getResource("img/iris-bot-250.png"));

	public MainPanel(){
		this.setLayout(new BorderLayout());
		this.add(new JLabel(logo), BorderLayout.NORTH);
		this.add(new CommandPanel(), BorderLayout.CENTER);
		
	}
}
