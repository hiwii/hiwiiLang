package net.hiwii.db.bind;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.db.ent.FunctionHead;

public class FunctionHeadBinding extends TupleBinding<FunctionHead> {
	@Override
	public FunctionHead entryToObject(TupleInput arg0) {
		FunctionHead head = new FunctionHead();
		head.setType(arg0.readString());
		int num = arg0.readInt();
		List<String> list = new ArrayList<String>();
		for(int n=0;n<num;n++){
			list.add(arg0.readString());
		}
		head.setArgumentType(list);
		return head;
	}

	@Override
	public void objectToEntry(FunctionHead arg0, TupleOutput arg1) {
		arg1.writeString(arg0.getType());
		arg1.writeInt(arg0.getArgumentType().size());
		for(String str:arg0.getArgumentType()){			
			arg1.writeString(str);			
		}
		//statement
		
	}
}
