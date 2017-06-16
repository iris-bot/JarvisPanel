package br.com.irisbot.jarvis;

import java.awt.Dimension;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;

import br.com.irisbot.jarvis.net.PlatformDetection;
import br.com.irisbot.jarvis.raspi.ServiceInstance;
import br.com.irisbot.jarvis.ui.MainPanel;
import br.com.irisbot.ws.HttpService;
import br.com.irisbot.ws.HttpService.Port;

public class Main {

	public static final Logger log = Logger.getGlobal();
	
	public static void main(String[] args) {
		try{
			FileHandler fh = new FileHandler("jarvis_"+(new Date()).getTime()+".log",true);
			fh.setFormatter(new SimpleFormatter());
			log.addHandler(fh);
		}catch (Exception e) {}
		
		PlatformDetection.detectPlatform();
		
		HttpService srv = HttpService.intance(Port.xHTTP);
		log.fine("HTTP SERVER READY ON PORT "+Port.xHTTP);
		
		ServiceInstance.config(srv);

		JFrame frm = new JFrame();
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setContentPane(new MainPanel());
		frm.setMinimumSize(new Dimension(400, 400));
		//frm.setAlwaysOnTop(true);
		//frm.setUndecorated(true);
		//frm.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frm.setVisible(true);
		//frm.setResizable(false);
	}
}
