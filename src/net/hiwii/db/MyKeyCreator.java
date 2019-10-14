package net.hiwii.db;

import java.io.UnsupportedEncodingException;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public class MyKeyCreator implements SecondaryKeyCreator  {

	@Override
	public boolean createSecondaryKey(SecondaryDatabase arg0,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {
		try {
			String data0 = new String(data.getData(), "UTF-8");
			result.setData(data0.getBytes("UTF-8"));
			return true;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		
	}

}
