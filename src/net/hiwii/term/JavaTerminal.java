package net.hiwii.term;

import net.hiwii.system.LocalHost;
import net.hiwii.system.exception.ApplicationException;

public class JavaTerminal extends Terminal {
	private String request;
	private String response;
	private boolean received;
	
	public JavaTerminal() {
		try {
			setSession(LocalHost.getInstance().generateSession(this));
			if(LocalHost.getInstance().getConsole() == null){
				LocalHost.getInstance().setConsole(this);
			}
		} catch (ApplicationException e) {
			response = "Exception[unknown]";
		}
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public void doRequest() {
		doRequest(request);
	}
	
	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	@Override
	public void doRequest(String comm) {
		if(comm != null && comm.length() != 0){
			getSession().doRequest(comm);
			doRequest();
		}else{
			
		}
	}

	@Override
	public String doResponse(String comm) {
		response = comm;
		received = true;
		return null;
	}

	@Override
	public String toString() {
		return "java";
	}

	@Override
	public void doQuestion(String quest) {
		// TODO Auto-generated method stub
		
	}
}
