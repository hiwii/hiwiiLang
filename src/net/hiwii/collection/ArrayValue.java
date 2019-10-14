package net.hiwii.collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.def.TypeView;
import net.hiwii.def.list.Array;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.view.Entity;

public class ArrayValue extends Collection {
	private Array type;
	
	public ArrayValue(){
		super();
		setClassName("Array");
	}
	public Array getType() {
		return type;
	}
	public void setType(Array type) {
		this.type = type;
	}	

	@Override
	public Expression doFunctionAction(String name, List<Entity> args) {
		if(name.equals("set")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			if(type.dimension() == 1){
				if(!(args.get(0) instanceof IntegerNumber)){
					return new HiwiiException();
				}
				IntegerNumber in = (IntegerNumber) args.get(0);
				int pos = Integer.parseInt(in.getValue());
				if(pos < 0){
					return new HiwiiException();
				}
				int max = type.getDimension().get(0);
				if(pos >= max){
					return new HiwiiException();
				}
				setValue(pos, args.get(1));
				return new NormalEnd();
			}else{
				if(!(args.get(0) instanceof EntityList)){
					return new HiwiiException();
				}
				EntityList entlist = (EntityList) args.get(0);
				if(entlist.getItems().size() != type.getDimension().size()){
					return new HiwiiException("dimension err.");
				}
				try {
					if(!entlist.typeDecision(new TypeView("Integer"))){
						return new HiwiiException();
					}
					int i = 0;
					for(Entity ent:entlist.getItems()){
						if(!(ent instanceof IntegerNumber)){
							return new HiwiiException();
						}
						IntegerNumber in = (IntegerNumber) ent;
						int pos = Integer.parseInt(in.getValue());
						if(pos < 0){
							return new HiwiiException();
						}
						int max = type.getDimension().get(i);
						if(pos >= max){
							return new HiwiiException();
						}
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
				List<Integer> parms = new ArrayList<Integer>();
				for(Entity ent:entlist.getItems()){
					IntegerNumber in = (IntegerNumber) ent;
					int pos = Integer.parseInt(in.getValue());
					parms.add(pos);
				}
				setValue(parms, args.get(1));
			}
			return new NormalEnd();
		}
		return null;
	}
	
	public void setValue(int pos, Entity value){
		getItems().set(pos, value);
	}
	
	public void setValue(List<Integer> plist, Entity value){
		int i = 0, pos = 0;
		int product = 1;
		for(int dim=0;dim<type.dimension();dim++){
			product = product * type.getDimension().get(dim);				
		}
		for(int p:plist){
			int len = type.getDimension().get(i);
			product = product / len;
			pos = pos + p * product;
			i++;
		}
		getItems().set(pos, value);
	}
	
	@Override
	public String toString() {
		String ret = "{";
		int product = 1;
		for(int dim=0;dim<type.dimension();dim++){
			product = product * type.getDimension().get(dim);				
		}
		for(int pos=0;pos<getItems().size();pos++){
			int i = 0;
			String item = "[";
			int sum = pos;
			int prod = product;
			for(int dim=0;dim<type.dimension();dim++){
				int len = type.getDimension().get(dim);
				prod = prod / len;
				int num = sum / prod;
//				int remain = (pos - sum) % len;
				if(i == type.dimension() - 1){
					item = item + sum; 
				}else{
					item = item + num + ",";
				}
				sum = sum - num * prod;
				i++;				
			}
			item = item + "]";
			ret = ret + item + ":" + getItems().get(pos).toString();
			ret = ret + "\r\n";
		}
		ret = ret + "}";
		return ret;
	}
}
