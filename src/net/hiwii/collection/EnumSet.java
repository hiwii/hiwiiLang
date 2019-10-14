package net.hiwii.collection;

import java.io.IOException;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.def.list.SetClass;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.view.Entity;

public class EnumSet extends EntityList {
	private SetClass setType;
	public EnumSet(){
		super();
	}

	public SetClass getSetType() {
		return setType;
	}
	public void setSetType(SetClass setType) {
		this.setType = setType;
	}

	public boolean contains(Entity ent) throws ApplicationException{
		return false;
	}
	
	public void plus(EnumSet ees) throws ApplicationException{
		for(Entity it:ees.getItems()){
			if(contains(it)){
				continue;
			}else{
				getItems().add(it);
			}
		}
	}
	
	public void minus(EnumSet ees) throws ApplicationException{
		for(Entity it:ees.getItems()){
			if(contains(it)){
				getItems().remove(it);
			}else{
				continue;
			}
		}
	}

	public boolean containSet(EnumSet set) throws ApplicationException{
		for(Entity it:set.getItems()){
			try {
				if(contains(it)){
					continue;
				}else{
					return false;
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean belongSet(EnumSet set) throws ApplicationException{
		for(Entity it:getItems()){
			try {
				if(set.contains(it)){
					continue;
				}else{
					return false;
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean realContainSet(EnumSet set) throws ApplicationException{
		if(getItems().size() > set.getItems().size()){
			for(Entity it:set.getItems()){
				try {
					if(contains(it)){
						continue;
					}else{
						return false;
					}
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean realBelongSet(EnumSet set) throws ApplicationException{
		if(getItems().size() < set.getItems().size()){
			for(Entity it:getItems()){
				try {
					if(set.contains(it)){
						continue;
					}else{
						return false;
					}
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean notEqualSet(EnumSet set) throws ApplicationException{
		if(equalSet(set)){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean equalSet(EnumSet set) throws ApplicationException{
		if(getItems().size() == set.getItems().size()){
			for(Entity it:set.getItems()){
				try {
					if(contains(it)){
						continue;
					}else{
						return false;
					}
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public Expression doFunctionAction(String name, List<Entity> args) {
		if(name.equals("add")){
			if(args.size() == 1 || args.size() == 2){
				if(setType != null){
					try {
						boolean jdg = setType.judgeEntity(args.get(0));
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
		return null;
	}
	
	@Override
	public String toString() {
		String ret = "enum(";
		for(Entity ent:getItems()){
			ret = ret + ent.toString();
			if(ent != getItems().get(getItems().size() - 1)){
				  ret = ret + ", ";
			}
		}
		
		ret = ret + ")";
		return ret;
	}
}
