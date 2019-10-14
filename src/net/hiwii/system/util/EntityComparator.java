package net.hiwii.system.util;

import java.util.Comparator;

import net.hiwii.view.Entity;

/**
 * 通过实现comparator，可以使用java提供的排序api
 * @author ha-wangzhenhai
 *
 */
public class EntityComparator implements Comparator<Entity> {

	@Override
	public int compare(Entity arg0, Entity arg1) {
//		return arg0.toCompare(arg1);
		return 0;
	}

}
