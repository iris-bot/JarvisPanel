package br.com.irisbot.jarvis.ipcam;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import br.com.irisbot.jarvis.Main;


public class IPCamCapture {
	
	private Frame frame;
	private BufferedImage image;
	private boolean colect = false; 
	private String url;
	private int frameRate = 12;
	
	private static final String[] text = {"","    *","   * ","  *  "," *   ","*    ","","",""};
	private int ct = 0;
	
	public IPCamCapture(String url){
		this.url = url;
	}
	
	public IPCamCapture(String url, int frameRate){
		this.url = url;
		this.frameRate = frameRate;
	}
	
	public void start(){
		if(colect) return;
		colect = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				FFmpegFrameGrabber grabber = null;
				try{
					grabber = new FFmpegFrameGrabber(url);
					grabber.setFrameRate(frameRate);
					grabber.setFormat("rtsp");
					//grabber.setImageMode(ImageMode.RAW);
					grabber.setTimeout(1000/frameRate);
					grabber.start();
			        while(colect){
			        	try{
			        		accessFrame(grabber.grabImage());
			        	}catch (Exception e) {
			        		//System.out.println(e.toString());
			        		Main.log.warning(e.toString());
						}
					}
					grabber.close();
				}catch (Exception e) {
					//e.printStackTrace();
					Main.log.throwing("IPCamCapture", "start", e);
					try{grabber.close();}catch (Exception ex) {}
				}
				colect = false;
			}
		}).start();
	}
	
	public void stop(){
		colect = false;
	}

	private synchronized BufferedImage accessFrame(Frame frm){
		if(frm!=null){
			frame = frm;
			Java2DFrameConverter jconv = new Java2DFrameConverter();
			image = jconv.convert(frm);
			//System.out.println("new frame was set...");
			return image;
		}else{
			//System.out.println("last frame was given...");
			return image;
		}
	}
	
	public synchronized BufferedImage getImage(){
		return convertAndAddBullet(accessFrame(null));
	}
	
	public boolean isActive(){
		return colect;
	}
	
	public int getFrameRate(){
		return frameRate;
	}
	
	private synchronized BufferedImage convertAndAddBullet(BufferedImage img) {
		try {
			ct++;
	    	if(ct>=text.length) ct=0;
	    	
			try{
				if(img==null) throw new Exception();
			}catch (Exception e) {
				img = ImageIO.read(IPCamCapture.class.getResource("ui/ipcam.png"));
			}

	    	Graphics2D g2d;
	    	BufferedImage bullet;
	    	if(!colect) bullet = ImageIO.read(IPCamCapture.class.getResource("ui/red.png"));
	    	else if(image==null) bullet = ImageIO.read(IPCamCapture.class.getResource("ui/orange.png"));
	    	else bullet = ImageIO.read(IPCamCapture.class.getResource("ui/green.png"));
	    	
	    	if(colect){
		    	g2d = (Graphics2D) bullet.getGraphics();
		    	g2d.setColor(Color.WHITE);
		        g2d.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		        FontMetrics fontMetrics = g2d.getFontMetrics();
		        Rectangle2D rect = fontMetrics.getStringBounds(text[ct], g2d);
		        int centerX = (bullet.getWidth() - (int) rect.getWidth()) / 2;
		        int centerY = bullet.getHeight() / 2;
		        g2d.drawString(text[ct], centerX, centerY);
		        g2d.dispose();
	    	}
	        
	    	g2d = (Graphics2D) img.getGraphics();
	        int topLeftX = img.getWidth() - 50;
	        int topLeftY = img.getHeight() - 50;
	        g2d.drawImage(bullet, topLeftX, topLeftY, null);
	        g2d.dispose();

	    } catch (Exception ex) {}
		return img;
	}
}
