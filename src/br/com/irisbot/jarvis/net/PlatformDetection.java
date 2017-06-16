package br.com.irisbot.jarvis.net;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

import java.lang.reflect.Method;

public class PlatformDetection {
	
	public static void detectPlatform(){
		
		//System.getProperties().list(System.out);
		
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		
		System.out.println("Detected platform: "+osName+" - "+osArch);
		
		if(osName.toLowerCase().contains("windows")){
			if(osArch.toLowerCase().contains("amd64")) loadJars("windows-x86_64");
			else if(osArch.toLowerCase().contains("x86")) loadJars("windows-x86");
			else notSupported();
		}else if(osName.toLowerCase().contains("linux")){
			if(osArch.toLowerCase().contains("amd64")) loadJars("linux-x86_64");
			else if(osArch.toLowerCase().contains("i386")) loadJars("linux-x86");
			else if(osArch.toLowerCase().contains("ppc")) loadJars("linux-ppc64le");
			else if(osArch.toLowerCase().contains("arm")) loadJars("linux-armhf");
			else notSupported();
		}else if(osName.toLowerCase().contains("android")){
			if(osArch.toLowerCase().contains("arm")) loadJars("android-arm");
			else if(osArch.toLowerCase().contains("x86")) loadJars("android-x86");
			else notSupported();
		}else if(osName.toLowerCase().contains("mac")){
			if(osArch.toLowerCase().contains("amd64")) loadJars("macosx-x86_64");
			else if(osArch.toLowerCase().contains("i386")) loadJars("macosx-x86_64");
			else notSupported();
		}else notSupported();
		
	}
	
	private static void notSupported(){
		System.out.println("Not supported platform");
		try{JOptionPane.showMessageDialog(null, "Not supported platform");}catch (Exception e) {}
		System.exit(0);
	}
	
	private static void loadJars(String plat){
		loadJar("libs/platform/opencv-"+plat+".jar");
		loadJar("libs/platform/ffmpeg-"+plat+".jar");
	}
	
	private static void loadJar(String jar){
		try {
		    File file = new File(jar);
		    URL url = file.toURI().toURL();
		    URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		    Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		    method.setAccessible(true);
		    method.invoke(classLoader, url);
		} catch (Exception ex) {
		    notSupported();
		}
	}

}
