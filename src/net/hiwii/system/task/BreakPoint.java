package net.hiwii.system.task;

import java.util.List;

import net.hiwii.context.AdverbContext;
import net.hiwii.view.Entity;

/**
 * 
 * @author Administrator
 * breakPoint information
 *
 */
public class BreakPoint {
	private int line;
	private Entity subject;
	private List<AdverbContext> adverbs;
	
	public BreakPoint() {
		this.line = 0;
	}
	public BreakPoint(int line) {
		this.line = line;
	}
}
