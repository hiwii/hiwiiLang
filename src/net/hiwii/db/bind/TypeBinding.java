package net.hiwii.db.bind;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.def.HiwiiType;

public class TypeBinding extends TupleBinding<HiwiiType> {

	@Override
	public HiwiiType entryToObject(TupleInput arg0) {
		String type = arg0.readString();
		if(type.equals("a")) {
			HiwiiType ret = new HiwiiType();
			ret.setFrom(arg0.readString());
			return ret;
		}
		return null;
	}

	@Override
	public void objectToEntry(HiwiiType arg0, TupleOutput arg1) {
		if(arg0 instanceof HiwiiType) {
			String type = "a";
			arg1.writeString(type);
			arg1.writeString(arg0.getFrom());
		}
		
	}

}
