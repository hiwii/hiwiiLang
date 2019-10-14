package net.hiwii.db.bind;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.def.Definition;
import net.hiwii.def.InstanceDefinition;

/**
 * 继承中的definition是不重名的。在数据库中以name为索引。
 * 内部definition的索引名为定义名@容器定义
 * signature表示definition的继承关系。
 * @author hiwii
 *
 */
public class DefinitionBinding extends TupleBinding<Definition> {

	@Override
	public Definition entryToObject(TupleInput arg0) {
		InstanceDefinition  def = new InstanceDefinition();
		String name = arg0.readString();
		String parent = arg0.readString();
		String sign = arg0.readString();
		String master = arg0.readString();
		boolean closing = arg0.readBoolean();
		
		def.setName(name);
		if(parent.length() != 0){
			def.setParent(parent);
		}
		def.setSignature(sign);
		def.setMaster(master);
		def.setClosing(closing);
		return def;
	}

	@Override
	public void objectToEntry(Definition arg0, TupleOutput arg1) {
		arg1.writeString(arg0.getName());
		if(arg0.getParent() != null){
			arg1.writeString(arg0.getParent());
		}else{
			arg1.writeString("");
		}
		arg1.writeString(arg0.getSignature());
		String master = arg0.getMaster();
		if(master == null){
			master = "";
		}
		arg1.writeString(master);
		arg1.writeBoolean(arg0.isClosing());
	}

}
