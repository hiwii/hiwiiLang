package net.hiwii.user;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.StringExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.view.Entity;

public class User extends Peer {
	private String userid;
//	private String userName;
	private String password;
	public User() {
		setClassName("User");
	}
	//	private List<String> group;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
//	public String getUserName() {
//		return userName;
//	}
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("userid")) {
			return new StringExpression(userid);
		}
		return null;
	}
	@Override
	public Expression doFunctionDecision(String name, List<Entity> args) {
		if(name.equals("EQ")) {
			if(args.size() != 1) {
				return new HiwiiException();
			}
			if(args.get(0) instanceof User) {
				User to = (User) args.get(0);
				if(to.getUserid().equals(userid)) {
					return EntityUtil.decide(true);
				}
			}
			return EntityUtil.decide(true);
		}
		return null;
	}
	@Override
	public String toString() {
		return "User [userid=" + userid + "]";
	}
}
