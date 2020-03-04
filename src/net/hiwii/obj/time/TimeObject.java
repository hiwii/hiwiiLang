package net.hiwii.obj.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.NumberUnit;
import net.hiwii.expr.StringExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.syntax.number.RealNumber;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.view.Entity;

public class TimeObject extends HiwiiEvent {
	private Calendar time;

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("toString")) {
			return new StringExpression(this.toString());
		}
		if(name.equals("millis")) {
			return new IntegerNumber(String.valueOf(time.getTimeInMillis()));
		}
		return null;
	}

	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
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
//				if (!(abs instanceof SimpleDefinition)) {
//					return new HiwiiException();
//				}
//				SimpleDefinition sd = (SimpleDefinition) abs;
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
		}else if(name.equals("format")) {
			if (args.size() != 1) {
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof StringExpression)) {
				return new HiwiiException();
			}
			StringExpression fmt = (StringExpression) args.get(0);
			try {
				String str = format(fmt.getValue());
				return new StringExpression(str);
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
		}
		return null;
	}

	public boolean beforeCentury(){
		Calendar c0 = Calendar.getInstance();
		c0.set(0, 0, 0, 0, 0, 0);
		int cmp = time.compareTo(c0);
		if(cmp < 0){
			return true;
		}
		return false;
	}
	
	public boolean beforeTime(TimeObject arg){
		return time.before(arg.getTime());
	}
	
	@Override
	public JudgmentResult beforeEvent(HiwiiEvent he) {
		if(he instanceof TimeObject){
			TimeObject to = (TimeObject) he;
			return EntityUtil.decide(beforeTime(to));
		}
		return null;
	}

	@Override
	public String format(String arg) throws ApplicationException {
		SimpleDateFormat sdf = new SimpleDateFormat(arg);
		return sdf.format(time.getTime());
	}

	@Override
	public String toString() {
		if (time != null) {
			try {
				if(beforeCentury()){
					return "-" + format("yyyy.MM.dd HH:mm:ss");
				}else{
					return format("yyyy.MM.dd HH:mm:ss");
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
}
