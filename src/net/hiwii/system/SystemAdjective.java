package net.hiwii.system;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.view.Entity;

public class SystemAdjective extends Entity {
	public static List<String> array = new ArrayList<String>();
		
	static{
		array.add("precise");
	}
	
	public static boolean contains(String str){
		return array.contains(str);
	}
}
