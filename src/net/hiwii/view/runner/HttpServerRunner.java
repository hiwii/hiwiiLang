package net.hiwii.view.runner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.log.Log;

import net.hiwii.web.DummyLogger;
import net.hiwii.web.DumpServlet;
import net.hiwii.web.HiwiiServlet;
import net.hiwii.web.weixin.WechatServlet;

public class HttpServerRunner extends Thread {
	@Override
	public void run() {
		try {
			listen();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void listen() {
		Log.setLog(new DummyLogger());
		Server server = new Server(80); //8080
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

		context.setContextPath("/");
        context.setResourceBase("web");
        server.setHandler(context);
 
        // Add dump Servlet
        context.addServlet(DumpServlet.class, "/hiwii/*");
        // Add server Servlet
        context.addServlet(HiwiiServlet.class, "/base"); //"/base"
        // Add weixin Servlet
        context.addServlet(WechatServlet.class, "/weixin");
        // Add default servlet
        context.addServlet(DefaultServlet.class, "/");
		
		try {
			server.start();
			server.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
