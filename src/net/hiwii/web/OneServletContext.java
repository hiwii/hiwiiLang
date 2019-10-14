package net.hiwii.web;

import net.hiwii.web.weixin.WechatServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class OneServletContext {

	public static void main(String[] args) throws Exception {
		Server server = new Server(80); //8080


		//        ResourceHandler resource_handler = new ResourceHandler();
		//        resource_handler.setDirectoriesListed(true);
		//        resource_handler.setWelcomeFiles(new String[] { "index.html" });
		//        resource_handler.setResourceBase(".");
		//        HandlerList handlers = new HandlerList();
		//        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
		//        server.setHandler(handlers);

		//        context.addFilter(new FilterHolder(new EncodingFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		/********old begin********
		context.setContextPath("/");
		server.setHandler(context);

		ServletHolder holder = context.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/*");
		holder.setInitParameter("resourceBase", "web");
		holder.setInitParameter("pathInfoOnly", "true");
		context.addServlet(new ServletHolder(new DumpServlet()), "/hiwii/*");
		************old end***********/
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
		server.start();
		server.join();
	}
}
