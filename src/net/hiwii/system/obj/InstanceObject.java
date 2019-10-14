package net.hiwii.system.obj;

import java.io.IOException;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.db.HiwiiDB;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.LocalHost;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.view.Entity;

public class InstanceObject extends Entity {
	private String uuid;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Override
	public Entity doIdentifierCalculation(String name) {
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			Entity ret = db.getInstanceProperty(uuid, name);
			if(ret != null) {
				return ret;
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
		return null;
	}
	@Override
	public Expression doIdentifierDecision(String name) {
		// TODO Auto-generated method stub
		return super.doIdentifierDecision(name);
	}
	
}
