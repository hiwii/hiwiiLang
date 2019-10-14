package net.hiwii.collection;

import java.io.IOException;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.def.list.ListClass;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.util.SortUtil;
import net.hiwii.view.Entity;


public class EntityList extends Collection {
	private ListClass listType;

	public EntityList(){
		super();
		setClassName("List");
	}
	
	public EntityList(List<Entity> items){
		super();
	}
	
	public ListClass getListType() {
		return listType;
	}

	public void setListType(ListClass listType) {
		this.listType = listType;
	}

	public void add(Entity ent){
		getItems().add(ent);
	}

	public void add(int pos, Entity ent){
		getItems().add(pos, ent);
	}
	
	public int count(){
		return getItems().size();
	}
	
	
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("size")){
			return new IntegerNumber(String.valueOf(count()));
		}else if(name.equals("count")){
			return new IntegerNumber(String.valueOf(count()));
		}
		return super.doIdentifierCalculation(name);
	}

	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		if(name.equals("item")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity ent = args.get(0);
			if(!(ent instanceof IntegerNumber)){
				return new HiwiiException();
			}
			IntegerNumber in = (IntegerNumber) ent;
			int pos = Integer.parseInt(in.getValue());
			if(pos < 0 || pos >= getItems().size()){
				return new HiwiiException();
			}
			return getItems().get(pos);
		}else if(name.equals("plus")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity ent = args.get(0);
			if(!(ent instanceof EntityList)){
				return new HiwiiException();
			}
			EntityList el = (EntityList) ent;
			EntityList res = new EntityList();
			res.getItems().addAll(getItems());
			res.getItems().addAll(el.getItems());
			return res;
		}
		return null;
	}

	@Override
	public Expression doFunctionAction(String name, List<Entity> args) {
		if(name.equals("add")){
			if(args.size() == 1 || args.size() == 2){
				if(listType != null){
					try {
						boolean jdg = listType.judgeEntity(args.get(0));
						if(!jdg){
							return new HiwiiException("type err!");
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
				}
			}else{
				return new HiwiiException();
			}
			if(args.size() == 1){
				getItems().add(args.get(0));
			}else{
				Entity a = args.get(0);
				if(!(a instanceof IntegerNumber)){
					return new HiwiiException();
				}
				IntegerNumber in = (IntegerNumber) a;
				int pos = Integer.parseInt(in.getValue());
				if(pos < 0 || pos >= getItems().size()){
					return new HiwiiException();
				}
				getItems().add(pos, args.get(1));
			}
			return new NormalEnd();
		}else if(name.equals("remove")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			IntegerNumber in = (IntegerNumber) args.get(0);
			int pos = Integer.parseInt(in.getValue());
			if(pos < 0 || pos >= getItems().size()){
				return new HiwiiException();
			}
			getItems().remove(pos);
			new NormalEnd();
		}else if(name.equals("replace")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			IntegerNumber in = (IntegerNumber) args.get(0);
			int pos = Integer.parseInt(in.getValue());
			if(pos < 0 || pos >= getItems().size()){
				return new HiwiiException();
			}
			getItems().remove(pos);
			getItems().add(pos, args.get(1));
			new NormalEnd();
		}else if(name.equals("addAll")){
			if(args.size() == 1){
				Entity a = args.get(0);
				if(!(a instanceof EntityList)){
					return new HiwiiException();
				}
				EntityList el = (EntityList) a;
				getItems().addAll(el.getItems());
			}else if(args.size() == 2){
				Entity a = args.get(0);
				if(!(a instanceof IntegerNumber)){
					return new HiwiiException();
				}
				Entity b = args.get(1);
				if(!(b instanceof EntityList)){
					return new HiwiiException();
				}
				IntegerNumber in = (IntegerNumber) a;
				int pos = Integer.parseInt(in.getValue());
				if(pos < 0 || pos >= getItems().size()){
					return new HiwiiException();
				}
				EntityList el = (EntityList) b;
				getItems().addAll(pos, el.getItems());
			}else{
				return new HiwiiException();
			}
			new NormalEnd();
		}
//		else if(name.equals("sort")){
//			if(args.size() != 1){
//				return new HiwiiException();
//			}
//			boolean asc = true;
//			Entity a = args.get(0);
//			if(a instanceof NullExpression){
//				
//			}else if(a instanceof AscentExpression){
//				
//			}else if(a instanceof DescentExpression){
//				asc = false;
//			}else{
//				return new HiwiiException();
//			}
//			sort(asc);
//			new NormalEnd();
//		}
		return null;
	}

	/**
	 * arg 是认识或认识的逆，如：id.f(x).g(y)
	 * 逆序：id.f(x).g(y):desc
	 */

	public void sort(boolean asc){
		SortUtil su = new SortUtil(asc);//asc = true
		su.quickSort(getItems(), 0, getItems().size() - 1);
	}
	
	@Override
	public String toString() {
		String ret = "[";
		for(Entity ent:getItems()){
			ret = ret + ent.toString();
			if(ent != getItems().get(getItems().size() - 1)){
				  ret = ret + ", ";
			}
		}
		
		ret = ret + "]";
		return ret;
	}
}
