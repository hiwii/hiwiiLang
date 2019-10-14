package net.hiwii.user.bind;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.user.Group;

public class GroupBinding extends TupleBinding<Group> {

	@Override
	public Group entryToObject(TupleInput arg0) {
		Group grp = new Group();
		String name = arg0.readString();
		String note = arg0.readString();
		grp.setName(name);
		grp.setNote(note);
		return grp;
	}

	@Override
	public void objectToEntry(Group arg0, TupleOutput arg1) {
		arg1.writeString(arg0.getName());
		arg1.writeString(arg0.getNote());
	}

}
