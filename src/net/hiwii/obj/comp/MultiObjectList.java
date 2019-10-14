package net.hiwii.obj.comp;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.collection.EntityList;
import net.hiwii.view.Entity;
import net.hiwii.view.ProjectedObject;

public class MultiObjectList extends Entity {
	private List<Expression> fields;
	private List<ProjectedObject> values;
	public List<Expression> getFields() {
		return fields;
	}
	public void setFields(List<Expression> fields) {
		this.fields = fields;
	}
	public List<ProjectedObject> getValues() {
		return values;
	}
	public void setValues(List<ProjectedObject> values) {
		this.values = values;
	}
	@Override
	public String toString() {
		String str = "";
		int i = 0;
		for(Expression expr:fields) {
			if(i == 0) {
				str = str + expr.toString();
			}else {
				str = str + "," + expr.toString();
			}
			i++;
		}
		str = str + "\r\n";
		for(ProjectedObject item:values) {
			i = 0;
			for(Entity ent:item.getValues()) {
				if(i == 0) {
					str = str + ent.toString();
				}else {
					str = str + "," + ent.toString();
				}
				i++;
			}
			str = str + "\r\n";			
		}
		return str;
	}
}
