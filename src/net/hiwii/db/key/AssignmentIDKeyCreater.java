package net.hiwii.db.key;

import java.io.UnsupportedEncodingException;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public class AssignmentIDKeyCreater implements SecondaryKeyCreator {

	@Override
	public boolean createSecondaryKey(SecondaryDatabase arg0, DatabaseEntry key, DatabaseEntry data,
			DatabaseEntry result) {
		try {
			String key0 = new String(key.getData(), "UTF-8");
			int pos = key0.indexOf('@');
			if(pos < 0){
				return false;
			}
			result.setData(key0.substring(pos + 1).getBytes("UTF-8"));
			return true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
