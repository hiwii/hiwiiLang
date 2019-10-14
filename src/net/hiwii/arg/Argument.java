package net.hiwii.arg;

import java.io.IOException;

import com.sleepycat.je.DatabaseException;

import net.hiwii.system.exception.ApplicationException;
import net.hiwii.view.Entity;

public class Argument extends Entity {
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean doAccept(Entity ent) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		return true;
	}
}
