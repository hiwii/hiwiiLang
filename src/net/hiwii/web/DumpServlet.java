package net.hiwii.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DumpServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        String comm = request.getParameter("username");
        List<String> result = new ArrayList<String>();
        HttpSession session = request.getSession();
        
//        System.out.println("path:" + request.getRequestURI());
        //Ĭ��interval=-1
//        System.out.println("interval:" + session.getMaxInactiveInterval());
        
        if(session.getAttribute("terminal") == null){
        	session.setAttribute("terminal", new NetTerminal());
        }
        if(comm != null && comm.length() > 0){
        	NetTerminal nt = (NetTerminal) session.getAttribute("terminal");
        	nt.doCommand(comm);
        	result = nt.getResults();
        } 
        
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.getWriter().println("<html>");
        response.getWriter().println("<head></head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<a href=\"/index.jsp\">hiwiiϵͳ��hiwii���Խ���</a>");
        response.getWriter().println("<b>hiwii���ԻỰ</b>");
        response.getWriter().println("<hr/>");
        
        response.getWriter().println("<form action=\"/hiwii/\" method=\"post\">");
        response.getWriter().println("��������:<textarea rows=10 cols= 40 name=\"username\"></textarea><br/>");
        response.getWriter().println("<input type=\"reset\" value=\"����\"/>");
        response.getWriter().println("<input type=\"submit\" value=\"�ύ\"/>");
        response.getWriter().println("</form>");
        response.getWriter().println("���磺����ask[2+3]�����ύ��ϵͳ�᷵�ؽ��5��<br/>");
        response.getWriter().println("���磺����whether[3.5 > 2]�����ύ��ϵͳ�᷵�ؽ��true��");
        
        response.getWriter().println("<h1>result:</h1>");
        for(String str:result){
        	response.getWriter().println("<h3>" + str + "</h3>");
        }
        
        response.getWriter().println("<br/><hr/>");
        response.getWriter().println("<p>��ӭ��ע�ҵ�΢����hiwiinet���ҵ�΢��qq�ţ�332700461���ҵĲ��ͣ�hiwiinet.blog.163.com</p>");
        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
