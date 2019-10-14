package net.hiwii.user.key;

import java.io.UnsupportedEncodingException;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

import net.hiwii.system.util.StringUtil;

public class EntityUserKeyCreater implements SecondaryKeyCreator {

	@Override
	public boolean createSecondaryKey(SecondaryDatabase arg0, DatabaseEntry key, DatabaseEntry data,
			DatabaseEntry result) {
		try {
			String sign = new String(data.getData(), "UTF-8");
			if(StringUtil.matched(sign, "User")){
//				result.setData(key0.getBytes("UTF-8"));
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
