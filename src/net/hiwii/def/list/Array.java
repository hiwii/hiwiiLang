package net.hiwii.def.list;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.NullValue;
import net.hiwii.collection.ArrayValue;
import net.hiwii.def.Definition;
import net.hiwii.message.HiwiiException;
import net.hiwii.view.Entity;
/**
 * Array是一个定长的List。
 * @author hiwii
 *
 */
public class Array extends Definition {
	private String type;
	private List<Expression> limits;
	private List<Integer> dimension;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Expression> getLimits() {
		return limits;
	}
	public void setLimits(List<Expression> limits) {
		this.limits = limits;
	}
	public List<Integer> getDimension() {
		return dimension;
	}
	public void setDimension(List<Integer> dimension) {
		this.dimension = dimension;
	}
	
	public int dimension(){
		return dimension.size();
	}
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("new")){
			ArrayValue list = new ArrayValue();
			list.setType(this);
			int len = 1;
			for(int dim:dimension){
				len = len * dim;
			}
			List<Entity> items = new ArrayList<Entity>();
			int pos = 0;
			while(pos < len){
				items.add(new NullValue());
				pos++;
			}
			list.setItems(items);
			return list;
		}
		return null;
	}
	
	@Override
	public Entity doMappingCalculation(String name, List<Expression> args) {
		if(name.equals("At")){
			if(args.size() != 1){
				return new HiwiiException();
			}
//			Expression arg1 = args.get(1);
//			try {
//				TypeView tv = getTypeView(arg1);
//				array.setType(tv.getType());
//				array.setLimits(tv.getLimits());
//			} catch (DatabaseException e) {
//				return new HiwiiException("type err!");
//			} catch (IOException e) {
//				return new HiwiiException("type err!");
//			} catch (ApplicationException e) {
//				return new HiwiiException("type err!");
//			} catch (Exception e) {
//				return new HiwiiException("type err!");
//			}				 
		}
		return null;
	}
	@Override
	public String toString() {
		String ret = "Array[";
		int i=0;
		for(int len:dimension){
			if(i == dimension() - 1){
				ret = ret + len; 
			}else{
				ret = ret + len + ",";
			}
			i++;
		}
		ret = ret + "]";
		return ret;
	}
}
