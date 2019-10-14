package net.hiwii.web.weixin;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * ΢�ŷ�����շ���Ϣ�ӿ� 
 *  
 * @author wangzhenhai 
 *  
 */  
public class WechatServlet extends HttpServlet {  

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * The doGet method of the servlet. <br> 
	 *  
	 * This method is called when a form has its tag value method equals to get. 
	 *  
	 * @param request 
	 *            the request send by the client to the server 
	 * @param response 
	 *            the response send by the server to the client 
	 * @throws ServletException 
	 *             if an error occurred 
	 * @throws IOException 
	 *             if an error occurred 
	 */  
	public void doGet(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException {  
		request.setCharacterEncoding("UTF-8");  
		response.setCharacterEncoding("UTF-8");  

		/** ��ȡ���յ���xml��Ϣ */  
		StringBuffer sb = new StringBuffer();  
		InputStream is = request.getInputStream();  
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");  
		BufferedReader br = new BufferedReader(isr);  
		String s = "";  
		while ((s = br.readLine()) != null) {  
			sb.append(s);  
		}  
		String xml = sb.toString(); //�μ�Ϊ���յ�΢�Ŷ˷��͹�����xml����  
		
		System.out.println("recived:" + xml);//For test
		
		String result = "";  
		/** �ж��Ƿ���΢�Ž��뼤����֤��ֻ���״ν�����֤ʱ�Ż��յ�echostr��������ʱ��Ҫ����ֱ�ӷ��� */  
		String echostr = request.getParameter("echostr");
		if (echostr != null && echostr.length() > 1) { 
			System.out.println("echostr:" + echostr);//For test
			result = echostr;  
		} else {  
			//������΢�Ŵ�������  
			result = new WechatProcess().processWechatMag(xml);
		}  

		try {  
			OutputStream os = response.getOutputStream();
			System.out.println("result:" + result);//For test
			os.write(result.getBytes("UTF-8"));  
			os.flush();  
			os.close();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  

	/** 
	 * The doPost method of the servlet. <br> 
	 *  
	 * This method is called when a form has its tag value method equals to 
	 * post. 
	 *  
	 * @param request 
	 *            the request send by the client to the server 
	 * @param response 
	 *            the response send by the server to the client 
	 * @throws ServletException 
	 *             if an error occurred 
	 * @throws IOException 
	 *             if an error occurred 
	 */  
	public void doPost(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException {  
		doGet(request, response);  
	}  

}  