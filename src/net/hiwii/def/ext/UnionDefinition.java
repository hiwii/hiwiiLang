package net.hiwii.def.ext;

import java.util.List;

import net.hiwii.def.Definition;

public class UnionDefinition extends Definition {
	private List<Definition> elements;

	public List<Definition> getElements() {
		return elements;
	}

	public void setElements(List<Definition> elements) {
		this.elements = elements;
	}
	
}
