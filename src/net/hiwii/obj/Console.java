package net.hiwii.obj;

import net.hiwii.system.LocalHost;
import net.hiwii.system.exception.ApplicationException;

public class Console {
	public static void main(String[] args) {
		try {
			LocalHost.getInstance().startup();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
