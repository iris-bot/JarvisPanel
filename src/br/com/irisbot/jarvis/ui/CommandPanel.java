package br.com.irisbot.jarvis.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bytedeco.javacpp.opencv_core.IplImage;

import br.com.irisbot.jarvis.Main;
import br.com.irisbot.jarvis.ipcam.IPCamCapture;
import br.com.irisbot.jarvis.raspi.ServiceInstance;

public class CommandPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private ImagePane imgPane = new ImagePane(null); 
	
	private JButton[] buttons = new JButton[]{
		new JButton("não configurado"), new JButton("não configurado"),
		new JButton("não configurado"), new JButton("não configurado"),
		new JButton("não configurado"), new JButton("não configurado"),
		new JButton("não configurado"), new JButton("não configurado"),
		new JButton("não configurado") 
	};
	
	public CommandPanel(){

		this.setLayout(new BorderLayout());
		this.add(imgPane, BorderLayout.CENTER);
		
		JPanel east = new JPanel();
		
		east.setLayout(new GridLayout(3,3,10,10));
		east.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
		for(JButton bt : buttons) {
			if(bt!=null){
				bt.setBackground(Color.LIGHT_GRAY);
				bt.setBorder(BorderFactory.createRaisedBevelBorder());
				east.add(bt);
			}else{
				east.add(new JLabel(""));
			}
		}

		this.add(east, BorderLayout.EAST);
		
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
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				IPCamCapture cam = new IPCamCapture("rtsp://admin:123@192.168.0.100:554/onvif1");
				//IPCamCapture cam = new IPCamCapture("rtsp://viewer:temp4now@192.168.0.191:554/videoMain");
				cam.start();
				while(true/*cam.isActive()*/){
					try{Thread.sleep(1000/cam.getFrameRate());}catch (Exception e) {}
					imgPane.setImage(cam.getImage());
				}
			}
		}).start();
		
	}
	
	private static class ImagePane extends JPanel {
		private static final long serialVersionUID = 1L;
		private BufferedImage img;
		private BufferedImage NULL; 

		private ImagePane(BufferedImage img) {
			try{
				NULL = (BufferedImage) ImageIO.read(CommandPanel.class.getResource("img/ipcam.png"));
			}catch (Exception e) {}
			this.img = img;
		}

		@Override
		public void paintComponent(Graphics g) {
			if(img==null) img = NULL;
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), 0, 0, img.getWidth(), img.getHeight(),
					null);
			super.paintComponents(g);
		}

		public void setImage(BufferedImage img) {
			this.img = img;
			repaint();
		}

	}

}
