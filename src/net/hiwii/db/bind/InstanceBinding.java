package net.hiwii.db.bind;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.def.Definition;
import net.hiwii.view.HiwiiInstance;

/**
 * needless, to be deleted
 * @author hiwii
 *
 */
public class InstanceBinding extends TupleBinding<HiwiiInstance> {

	@Override
	public HiwiiInstance entryToObject(TupleInput arg0) {
		HiwiiInstance  ret = new HiwiiInstance();
		String name = arg0.readString();
		return ret;
	}

	@Override
	public void objectToEntry(HiwiiInstance arg0, TupleOutput arg1) {
		if(arg0.getClassName() != null){
			arg1.writeString("deleted");//arg0.getType().takeSignature()
		}
		
	}

}
