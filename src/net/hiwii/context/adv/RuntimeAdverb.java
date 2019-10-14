package net.hiwii.context.adv;

import java.util.List;

import net.hiwii.context.AdverbContext;
import net.hiwii.context.RuntimeContext;
import net.hiwii.view.Entity;

public class RuntimeAdverb extends Entity {
	private List<AdverbContext> adverb;
	private RuntimeContext context;
	
	public List<AdverbContext> getAdverb() {
		return adverb;
	}
	public void setAdverb(List<AdverbContext> adverb) {
		this.adverb = adverb;
	}
	public RuntimeContext getContext() {
		return context;
	}
	public void setContext(RuntimeContext context) {
		this.context = context;
	}
}
