package net.hiwii.user.key;

import java.io.UnsupportedEncodingException;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

import net.hiwii.user.User;
import net.hiwii.user.bind.UserBinding;

public class IdActionUserKeyCreater implements SecondaryKeyCreator{
	private TupleBinding<User> binding = new UserBinding();
	@Override
	public boolean createSecondaryKey(SecondaryDatabase arg0, DatabaseEntry key, DatabaseEntry data,
			DatabaseEntry result) {
		try {
			User user = binding.entryToObject(data);
			String action = new String(key.getData(), "UTF-8");
			String key0 = user.getUserid() + "@" + action;
			result.setData(key0.getBytes("UTF-8"));
			return true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
