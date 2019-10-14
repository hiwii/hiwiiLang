package net.hiwii.user.key;

import java.io.UnsupportedEncodingException;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public class UserGroupKeyCreater2 implements SecondaryKeyCreator {
//	private TupleBinding<Group> binding = new GroupBinding();
	@Override
	public boolean createSecondaryKey(SecondaryDatabase arg0, DatabaseEntry key, DatabaseEntry data,
			DatabaseEntry result) {
		try {
//			Group grp = binding.entryToObject(data);
			String map = new String(key.getData(), "UTF-8");
			int pos = map.indexOf('@');
			if(pos > 0){
				String key0 = map.substring(pos + 1);
				result.setData(key0.getBytes("UTF-8"));
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
