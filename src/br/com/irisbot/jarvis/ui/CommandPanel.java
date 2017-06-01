package br.com.irisbot.jarvis.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.irisbot.jarvis.Main;
import br.com.irisbot.jarvis.raspi.ServiceInstance;

public class CommandPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private JButton[] buttons = new JButton[]{
		new JButton("não configurado"), new JButton("não configurado"),
		new JButton("não configurado"), new JButton("não configurado"),
		new JButton("não configurado"), new JButton("não configurado"),
		new JButton("não configurado"), null,
		new JButton("não configurado") 
	};
	
	public CommandPanel(){

		this.setLayout(new GridLayout(3,3,10,10));
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
		for(JButton bt : buttons) {
			if(bt!=null){
				bt.setBackground(Color.LIGHT_GRAY);
				bt.setBorder(BorderFactory.createRaisedBevelBorder());
				this.add(bt);
			}else{
				this.add(new JLabel(""));
			}
		}

		buttons[0].setText("abrir portão");
		buttons[0].setBackground(Color.CYAN);
		buttons[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					ServiceInstance.abrirPortao();
				}catch (Exception ex) {
					Main.log.warning(ex.toString());
				}
			}
		});
		
		buttons[1].setText("acender luz garagem");
		buttons[1].setBackground(Color.YELLOW);
		buttons[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					if(ServiceInstance.luzGaragemAcesa()) {
						ServiceInstance.apagarLuzGaragem();
						buttons[1].setText("acender luz garagem");
					}else {
						ServiceInstance.acenderLuzGaragem();
						buttons[1].setText("apagar luz garagem");
					}
				}catch (Exception ex) {
					Main.log.warning(ex.toString());
				}
			}
		});
		
		buttons[8].setText("teste");
		buttons[8].setBackground(Color.MAGENTA);
		buttons[8].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					ServiceInstance.teste();
				}catch (Exception ex) {
					Main.log.warning(ex.toString());
				}
			}
		});
	}

}
