package br.com.irisbot.jarvis;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class MessageUtils {
	
	public static void popUp(final String msg, final long time){
		JOptionPane opt = new JOptionPane(msg, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{});
		final JDialog dlg = opt.createDialog("Atenção");
		dlg.setAlwaysOnTop(true);
		dlg.setTitle(null);
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		new Thread(new Runnable(){
			public void run(){
				try{
					Thread.sleep(time);
		            dlg.dispose();
	            }catch(Throwable th){}
			}
		}).start();
		dlg.setVisible(true);
	}
}
