package net.hiwii.def;

import java.util.List;

import net.hiwii.cognition.Expression;

public class DefinitionView extends Definition {
	private List<Expression> fields;

	public List<Expression> getFields() {
		return fields;
	}

	public void setFields(List<Expression> fields) {
		this.fields = fields;
	}
	
//	public Entity doGetSingleInstance(){
//		try {			
//			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
//			HiwiiInstance ret = db.getSingleInstance(getName(), null);
//			return ret;
//		} catch (DatabaseException e) {
//			return new HiwiiException();
//		} catch (IOException e) {
//			return new HiwiiException();
//		} catch (ApplicationException e) {
//			return new HiwiiException();
//		} catch (Exception e) {
//			return new HiwiiException();
//		}
////		return null;
//	}

	@Override
	public String toString() {
		String ret = this.getName();
		ret = ret + ".view[";
		int i = 1;
		for(Expression expr:fields) {
			if(i == fields.size()) {
				ret = ret + expr.toString();
			}else {
				ret = ret + expr.toString() + ",";
			}
			i++;
		}
		
		ret = ret + "]";
		return ret;
	}
	
}
