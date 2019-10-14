package net.hiwii.context.path;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.view.Entity;

public class PathInfo extends Entity {
	private List<String> variables;
	private List<String> props;
	private List<String> assigns;
	private List<String> perceptions;
	private List<String> defines;
	private List<String> declares;
	
	public PathInfo() {
		variables = new ArrayList<String>();
		props = new ArrayList<String>();
		assigns = new ArrayList<String>();
		perceptions = new ArrayList<String>();
		defines = new ArrayList<String>();
		declares = new ArrayList<String>();
	}
	public List<String> getVariables() {
		return variables;
	}
	public void setVariables(List<String> variables) {
		this.variables = variables;
	}
	public List<String> getProps() {
		return props;
	}
	public void setProps(List<String> props) {
		this.props = props;
	}
	public List<String> getAssigns() {
		return assigns;
	}
	public void setAssigns(List<String> assigns) {
		this.assigns = assigns;
	}
	public List<String> getPerceptions() {
		return perceptions;
	}
	public void setPerceptions(List<String> perceptions) {
		this.perceptions = perceptions;
	}
	public List<String> getDefines() {
		return defines;
	}
	public void setDefines(List<String> defines) {
		this.defines = defines;
	}
	public List<String> getDeclares() {
		return declares;
	}
	public void setDeclares(List<String> declares) {
		this.declares = declares;
	}
}
