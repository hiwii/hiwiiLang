package net.hiwii.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HiwiiServlet  extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");  
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("terminal") == null){
        	session.setAttribute("terminal", new NetTerminal());
        }
		
		List<String> result = new ArrayList<String>();
		String ret = "";
		
		/** 读取接收到的消息 */  
		StringBuffer sb = new StringBuffer();  
		InputStream is = request.getInputStream();  
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");  
		BufferedReader br = new BufferedReader(isr);  
		String s = "";  
		while ((s = br.readLine()) != null) {  
			sb.append(s);  
		}

		String comm = sb.toString();
		
		String mode = request.getParameter("mode");
		if(mode.equals("calculation")){
			if(comm != null && comm.length() > 0){
				NetTerminal nt = (NetTerminal) session.getAttribute("terminal");
				ret = nt.doCalculation(comm);
			}
		}else if(mode.equals("action")){
			
			if(comm != null && comm.length() > 0){
				NetTerminal nt = (NetTerminal) session.getAttribute("terminal");
				ret = nt.doAction(comm);
//				result = nt.getResults();
			}
			
//			if(result.size() >= 1){
//				ret = result.get(0);
//			}
//			for(int i=1;i<result.size();i++){
//				ret = ret + "\r\n" + result.get(i);
//			}
		}else if(mode.equals("decision")){
			if(comm != null && comm.length() > 0){
				NetTerminal nt = (NetTerminal) session.getAttribute("terminal");
				ret = nt.doCalculation(comm);
			}
		}
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);

		//		ServletOutputStream os = response.getOutputStream();
		response.getWriter().print(ret);
	}
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}
