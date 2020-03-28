package net.hiwii.db.key;

import java.io.UnsupportedEncodingException;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public class MultiFunctionKeyCreater implements SecondaryKeyCreator{

	@Override
	public boolean createSecondaryKey(SecondaryDatabase arg0, 
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {
		try {
			String str = new String(key.getData(), "UTF-8");
			int pos = str.indexOf("^");
			if(pos > 0){
				result.setData(str.substring(0, pos).getBytes("UTF-8"));
				return true;
			}else{
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

}
