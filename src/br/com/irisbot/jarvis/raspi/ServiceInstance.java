package br.com.irisbot.jarvis.raspi;

import org.json.JSONObject;

import br.com.irisbot.jarvis.Main;
import br.com.irisbot.jarvis.MessageUtils;
import br.com.irisbot.ws.HttpService;
import br.com.irisbot.ws.HttpService.Method;
import br.com.irisbot.ws.HttpService.Request;
import br.com.irisbot.ws.HttpService.Response;
import br.com.irisbot.ws.HttpService.RestHandler;

public class ServiceInstance {
	

	public static void config(HttpService srv){
		
		srv.createContext("/jarvis/portao/liga" , new RestHandler() {
			@Override
			public void handle(Request req, Response resp) throws Exception {
				JSONObject json = new JSONObject();
				
				if(req.isMethod(Method.GET)){
					abrirPortao();
				}
				
				resp.setBody(json);
			}
		});
	
		srv.createContext("/jarvis/garagem/liga" , new RestHandler() {
			@Override
			public void handle(Request req, Response resp) throws Exception {
				JSONObject json = new JSONObject();
				
				if(req.isMethod(Method.GET)){
					abrirGaragem();
				}
				
				resp.setBody(json);
			}
		});
	
		srv.createContext("/jarvis/garagem/desliga" , new RestHandler() {
			@Override
			public void handle(Request req, Response resp) throws Exception {
				JSONObject json = new JSONObject();
				
				if(req.isMethod(Method.GET)){
					fecharGaragem();
				}
				
				resp.setBody(json);
			}
		});
	
		srv.createContext("/jarvis/garagem/para" , new RestHandler() {
			@Override
			public void handle(Request req, Response resp) throws Exception {
				JSONObject json = new JSONObject();
				
				if(req.isMethod(Method.GET)){
					pararPortaoGaragem();
				}
				
				resp.setBody(json);
			}
		});
	
		srv.createContext("/jarvis/iluminacao/garagem/liga" , new RestHandler() {
			@Override
			public void handle(Request req, Response resp) throws Exception {
				JSONObject json = new JSONObject();
				
				if(req.isMethod(Method.GET)){
					acenderLuzGaragem();
				}
				
				resp.setBody(json);
			}
		});

		srv.createContext("/jarvis/iluminacao/garagem/desliga" , new RestHandler() {
			@Override
			public void handle(Request req, Response resp) throws Exception {
				JSONObject json = new JSONObject();
				
				if(req.isMethod(Method.GET)){
					apagarLuzGaragem();
				}
				
				resp.setBody(json);
			}
		});
	
		srv.createContext("/jarvis/iluminacao/sala/liga" , new RestHandler() {
			@Override
			public void handle(Request req, Response resp) throws Exception {
				JSONObject json = new JSONObject();
				
				if(req.isMethod(Method.GET)){
					acenderLuzSala();
				}
				
				resp.setBody(json);
			}
		});

		srv.createContext("/jarvis/iluminacao/sala/desliga" , new RestHandler() {
			@Override
			public void handle(Request req, Response resp) throws Exception {
				JSONObject json = new JSONObject();
				
				if(req.isMethod(Method.GET)){
					apagarLuzSala();
				}
				
				resp.setBody(json);
			}
		});
		
		
	}

	private static void logAndAlert(String txt){
		Main.log.fine(txt);
		MessageUtils.popUp(txt, 3000);
	}
	
	public static void abrirPortao() throws Exception {
		logAndAlert("abrindo o portão");
		GpioUtils.pulse(4, 3000, true);
		GpioUtils.pulse(5, 3000, true);
	}
	
	public static void abrirGaragem() throws Exception {
		logAndAlert("abrindo garagem");
		GpioUtils.pulse(1, 1500, true);
	}
	
	public static void fecharGaragem() throws Exception {
		logAndAlert("fechando garagem");
		GpioUtils.pulse(1, 1500, true);
	}
	
	public static void pararPortaoGaragem() throws Exception {
		logAndAlert("parando garagem");
		GpioUtils.pulse(1, 1500, true);
	}
	
	public static boolean luzGaragemAcesa(){
		return GpioUtils.isHigh(6);
	}
	
	public static void acenderLuzGaragem() throws Exception {
		logAndAlert("acendendo luz da garagem");
		GpioUtils.high(6, true);
	}

	public static void apagarLuzGaragem() throws Exception {
		logAndAlert("apagando luz da garagem");
		GpioUtils.low(6, true);
	}
	
	public static boolean luzSalaAcesa(){
		return GpioUtils.isHigh(0);
	}
	
	public static void acenderLuzSala() throws Exception {
		logAndAlert("acendendo luz da sala");
		GpioUtils.high(0, true);
	}

	public static void apagarLuzSala() throws Exception {
		logAndAlert("apagando luz da sala");
		GpioUtils.low(0, true);
	}
		
	public static void teste() throws Exception {
		logAndAlert("teste de interface");
	}
		
		

}
