package net.hiwii.collection;

import java.io.IOException;

import com.sleepycat.je.DatabaseException;

import net.hiwii.arg.Argument;
import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.def.list.Tuple;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.view.Entity;

public class TupleValue extends Collection {
	private Tuple type;
	
	public Tuple getType() {
		return type;
	}
	public void setType(Tuple type) {
		this.type = type;
	}
	
	public Expression doAssign(String name, Entity value){
		int i = 0;
		for(Argument arg:type.getNames()){
			if(name.equals(arg.getName())){
				try {
					if(!arg.doAccept(value)){
						return new HiwiiException("not accept.");
					}
				} catch (DatabaseException e) {
					return new HiwiiException();
				} catch (IOException e) {
					return new HiwiiException();
				} catch (ApplicationException e) {
					return new HiwiiException();
				} catch (Exception e) {
					return new HiwiiException();
				}
				break;
			}
			i++;
		}
		getItems().set(i, value);
		return new NormalEnd();
	}
	@Override
	public String toString() {
		String ret = "Tuple[";
		int i=0;
		while(i<getItems().size()){
			if(i == getItems().size()-1){
				ret = ret + getItems().get(i);
			}else{
				ret = ret + getItems().get(i) + ",";
			}
			i++;
		}
		ret = ret + "]";
		return ret;
	}
	
	
}
