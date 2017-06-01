package br.com.irisbot.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public final class HttpService {

	public static enum Port{
		HTTP(80),
		HTTPS(443),
		xHTTP(8080);
		private int numb;
		Port(int n){this.numb = n;}
	}

	public static enum ResponseCode{
		OK(200),
		ERROR(500),
		NOT_FOUND(404),
		FORBIDEN(403);
		private int numb;
		ResponseCode(int n){this.numb = n;}
	}
	
	public static enum Method{
		GET,
		POST,
		PUT,
		DELETE
	}

	private static String CRLF = "\r\n";
	
	private HttpServer srv = null;

	public static HttpService intance(Port p){
		return HttpService.intance(p.numb);
	}

	public static HttpService intance(int port){
		HttpService _srv = new HttpService();
		if(_srv.srv==null)
			try{
				_srv.srv = HttpServer.create(new InetSocketAddress(port), 0);
				_srv.srv.start();
			}catch (Exception e) {
				System.out.println(e.toString());
			}
		return _srv;
	}
	
	public void createContext(String context, RestHandler handler){
		this.srv.createContext(context, handler);
	}
	
	public static abstract class RestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(t.getRequestBody()));
        	StringBuffer body = new StringBuffer();
        	Map<String, String> parms = new HashMap<String, String>();
        	if(t.getRequestURI().getQuery()!=null){
            	String[] params = t.getRequestURI().getQuery().split("\\&");
            	for (String param : params) {
    				String[] pair = param.split("=");
    				parms.put(URLDecoder.decode(pair[0],"UTF-8"), (pair.length>1)?URLDecoder.decode(pair[1],"UTF-8"):"");
    			}
        	}
        	String line;
        	while((line = reader.readLine())!=null){
        		body.append(line+CRLF);
        	}
        	JSONObject payload = new JSONObject();
        	try{
        		payload = new JSONObject(new String(body));
        	}catch (Exception e) {}

        	Response resp = new Response();
        	Request req = new Request(Method.valueOf(t.getRequestMethod()), t.getRequestHeaders(), t.getRequestURI().getPath(), parms, payload);
        	
        	try{
        		handle(req, resp);
        	}catch (Exception e) {
				resp.setCode(ResponseCode.ERROR);
				resp.setBody(new JSONObject(e));
			}
        	
        	String response = resp.body.toString();
        	t.getResponseHeaders().add("Content-Type", "application/json");
        	t.sendResponseHeaders(resp.code, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
        public abstract void handle(Request req, Response resp) throws Exception;
    }
	
	public static final class Response{
		private int code;
		private JSONObject body;
		private Response(){
			code = 200;
			body = new JSONObject();
		}
		public void setCode(ResponseCode c){
			code = c.numb;
		};
		public void setBody(JSONObject j){
			body = j;
		}
	}

	public static final class Request{
		private Method method;
		private Headers headers;
		private String path;
		private Map<String, String> map;
		private JSONObject payload;
		private Request(Method m, Headers h, String p, Map<String, String> mp, JSONObject pl){
			method = m; headers = h; path = p; map = mp; payload = pl;
		}
		public boolean isMethod(Method m){
			return method.equals(m);
		}
		public boolean isPath(String p){
			return path.equals(p) || path.equals(p+"/");
		}
		public Map<String, String> getParmeters(){
			return map;
		}
		public Headers getHeaders(){
			return headers;
		}
		public JSONObject getPayload(){
			return payload;
		}
	}

}
