package net.hiwii.db.bind;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.DatabaseException;

import net.hiwii.db.ent.FunctionAssign;
import net.hiwii.db.ent.StoredValue;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.view.Entity;

public class FunctionAssignBinding extends TupleBinding<FunctionAssign> {
	@Override
	public FunctionAssign entryToObject(TupleInput arg0) {
		try {
			FunctionAssign ass = new FunctionAssign();
			char type = arg0.readChar();
			String val = arg0.readString();
			StoredValue sv = new StoredValue();
			sv.setType(type);
			sv.setValue(val);
			Entity value = EntityUtil.recordToEntity(sv);
			ass.setValue(value);
			int num = arg0.readInt();
			List<Entity> list = new ArrayList<Entity>();
			for(int n=0;n<num;n++){
				type = arg0.readChar();
				val = arg0.readString();
				sv.setType(type);
				sv.setValue(val);
				Entity ent = EntityUtil.recordToEntity(sv);
				list.add(ent);
			}
			ass.setArguments(list);
			return ass;
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void objectToEntry(FunctionAssign arg0, TupleOutput arg1) {
		try {
			StoredValue val = EntityUtil.entityToRecord(arg0.getValue());
			arg1.writeChar(val.getType());
			arg1.writeString(val.getValue());
			arg1.writeInt(arg0.getArguments().size());
			for(int i=0;i<arg0.getArguments().size();i++) {
				StoredValue sv = EntityUtil.entityToRecord(arg0.getArguments().get(i));
				arg1.writeChar(sv.getType());
				arg1.writeString(sv.getValue());
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
