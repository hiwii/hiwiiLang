package net.hiwii.expr.date;

import java.util.Calendar;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.NumberUnit;
import net.hiwii.message.HiwiiException;
import net.hiwii.obj.time.TimeObject;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.syntax.number.RealNumber;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.view.Entity;

/**
 * time("hhmmss")
 * @author Administrator
 *
 */
public class TimeValue extends Entity {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		Calendar time = EntityUtil.DateToCalendar(value);
		if (name.equals("add")) {
			if (args.size() != 1) {
				return new HiwiiException();
			}
			Entity ent = args.get(0);
			if (ent instanceof NumberUnit) {
				NumberUnit goo = (NumberUnit) ent;
				RealNumber ne = goo.getNumber();
				Expression unit = goo.getUnit();
				if (!(ne instanceof IntegerNumber)) {
					return new HiwiiException();
				}
				if (!(unit instanceof IdentifierExpression)) {
					return new HiwiiException();
				}
				IntegerNumber in = (IntegerNumber) ne;
				int num = StringUtil.getInteger(in);

				IdentifierExpression ie = (IdentifierExpression) unit;
				String fn = ie.getName();
				int field = -1;
				if (fn.equals("hour")) {
					field = Calendar.HOUR;
				} else if (fn.equals("day")) {
					field = Calendar.DAY_OF_MONTH;
				} else if (fn.equals("week")) {
					field = Calendar.WEEK_OF_YEAR;
				} else if (fn.equals("month")) {
					field = Calendar.MONTH;
				} else if (fn.equals("year")) {
					field = Calendar.YEAR;
				} else if (fn.equals("minute")) {
					field = Calendar.MINUTE;
				} else if (fn.equals("second")) {
					field = Calendar.SECOND;
				}else{
					return new HiwiiException();
				}
				if (field == -1) {
					return new HiwiiException();
				}
				Calendar cal = (Calendar) time.clone();
				cal.add(field, num);
				TimeObject to = new TimeObject();
				to.setTime(cal);
				return to;
			}
			return new HiwiiException();
		}
		return null;
	}

//	public boolean beforeCentury(){
//		Calendar c0 = Calendar.getInstance();
//		c0.set(0, 0, 0, 0, 0, 0);
//		int cmp = time.compareTo(c0);
//		if(cmp < 0){
//			return true;
//		}
//		return false;
//	}
	
//	public boolean beforeTime(TimeObject arg){
//		return time.before(arg.getTime());
//	}
	
//	@Override
//	public JudgmentResult beforeEvent(HiwiiEvent he) {
//		if(he instanceof TimeObject){
//			TimeObject to = (TimeObject) he;
//			return EntityUtil.decide(beforeTime(to));
//		}
//		return null;
//	}

	@Override
	public String toString() {		
		return "time(\"" + value + "\")";
	}

	@Override
	public String getClassName() {		
		return "Time";
	}
}
