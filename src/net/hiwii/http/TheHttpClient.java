package net.hiwii.http;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import net.hiwii.system.net.HostObject;
import net.hiwii.web.DummyLogger;

public class TheHttpClient {
	public String  doRequest(HostObject host, String msg, String method) throws Exception{
		Log.setLog(new DummyLogger());
		String url = "http://" + host.getIpaddress() + "/base";
//		if(host.getPort() != 80){
//			url = "http://" + host.getIpaddress() + ":" + host.getPort() + "/base";
//		}
		
		// Instantiate HttpClient
		HttpClient httpClient = new HttpClient();
		 
		// Configure HttpClient, for example:
		httpClient.setFollowRedirects(false);
		 
		// Start HttpClient
		httpClient.start();
		Request request = httpClient.newRequest(url);
		request.method(HttpMethod.POST);
		request.param("mode", method)
			.param("messageType", "text");
		ContentProvider cp = new StringContentProvider(msg, "UTF-8");
		request.content(cp);
		String returnString;
		ContentResponse resp = request//.timeout(4, TimeUnit.SECONDS)
		        .send();
//		ContentResponse resp = request//.timeout(4, TimeUnit.SECONDS)
//		        .send(new Response.Listener.Adapter() {
//		            @Override
//		            public void onContent(Response response, ByteBuffer buffer) {
//		                // Your logic here
//		            }
//		        }
//		        		);
		httpClient.stop();
		String ret = resp.getContentAsString();
		returnString = ret;

		return returnString;
	}
	
	public String  doRequestGet(String url) throws Exception{
		Log.setLog(new DummyLogger());
		
		// Instantiate and configure the SslContextFactory
		SslContextFactory sslContextFactory = new SslContextFactory();
		
		// Instantiate HttpClient
		HttpClient httpClient = new HttpClient(sslContextFactory);
		 
		// Configure HttpClient, for example:
		httpClient.setFollowRedirects(false);
		 
		// Start HttpClient
		httpClient.start();
		ContentResponse resp = httpClient.GET(url);

		httpClient.stop();
		String ret = resp.getContentAsString();

		return ret;
	}
}
