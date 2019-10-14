package net.hiwii.def;

import java.io.IOException;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.collection.TypedEntityList;
import net.hiwii.db.HiwiiDB;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.LocalHost;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.view.Entity;

/**
 * 基于一个definition，筛选后确定一个新的definition，没有命名。
 * @author WangZhenHai
 *
 */
public class DecoratedDefinition extends Definition {
	private List<Expression> limits;

	public List<Expression> getLimits() {
		return limits;
	}

	public void setLimits(List<Expression> limits) {
		this.limits = limits;
	}
	

	public Entity doGetMultiInstance(Expression expr){
		try {			
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			TypedEntityList ret = db.getMultiInstance(getName(), getLimits());
			return ret;
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
//		return null;
	}
	
	@Override
	public String toString() {
		String ret = this.getName();
		ret = ret + ".limit[";
		int i = 1;
		for(Expression expr:limits) {
			if(i == limits.size()) {
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
