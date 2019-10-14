package net.hiwii.user.bind;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.user.User;

public class UserBinding extends TupleBinding<User> {

	@Override
	public User entryToObject(TupleInput arg0) {
		User ret = new User();
		String userid = arg0.readString();
		ret.setUserid(userid);
		return ret;
	}

	@Override
	public void objectToEntry(User arg0, TupleOutput arg1) {
		arg1.writeString(arg0.getUserid());
	}

}
