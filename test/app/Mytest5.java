package app;

import net.hiwii.http.TheHttpClient;
import net.hiwii.system.net.HostObject;

import org.junit.Test;

public class Mytest5 {
	@Test
	public void testHttpClient(){
		HostObject ho = new HostObject("localhost", 80);
		TheHttpClient hc = new TheHttpClient();
		try {
			String ret = hc.doRequest(ho, "3+4", "");
			System.out.println("----------");
			System.out.println(ret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
