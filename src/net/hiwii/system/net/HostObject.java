package net.hiwii.system.net;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.StringExpression;
import net.hiwii.http.TheHttpClient;
import net.hiwii.view.Entity;

public class HostObject extends Expression {
	private String ipaddress;
	private int port;
//	private String sessionId;

	public HostObject(String ipaddress) {
		this.ipaddress = ipaddress;
		this.port = 9099;
	}
	public HostObject(String ipaddress, int port) {
		this.ipaddress = ipaddress;
		this.port = port;
//		sessionId = "";
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String doRemoteCalculation(Expression expr) {
		try {
			TheHttpClient sc = new TheHttpClient();
			String src = expr.toString();
			String ret = sc.doRequest(this, src, "calculation");

//		String sessionId = StringUtil.getSessionId(ret);
//		setSessionId(sessionId);
//			StringExpression se = new StringExpression(ret);
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

	public String doRemoteAction(Expression expr) {
		try {
			TheHttpClient sc = new TheHttpClient();
			String src = expr.toString();
			String ret = sc.doRequest(this, src, "action");

//			StringExpression se = new StringExpression(ret);
			return ret;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String doRemoteDecision(Expression expr) {
		try {
			TheHttpClient sc = new TheHttpClient();
			String src = expr.toString();
			String ret = sc.doRequest(this, src, "decision");

//			StringExpression se = new StringExpression(ret);
			return ret;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Expression doNegative(Expression expr) {
		try {
			TheHttpClient sc = new TheHttpClient();
			String src = expr.toString();
			String ret = sc.doRequest(this, src, "ne");

			StringExpression se = new StringExpression(ret);
			return se;
		} catch (Exception e) {
			return null;
		}
	}

	public void sendMessage(String msg){
		try {
			TheHttpClient sc = new TheHttpClient();
			sc.doRequest(this, msg, "ac");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		String ret = "host(\"" + ipaddress + "\", " + port + ")";
		return ret;
	}
}
