package net.hiwii.system.store;

import net.hiwii.system.exception.ApplicationException;

/**
 * key可以存放在node中最多128 bytes
 * Cell结构：key + pointer
 * pointer保存在页面，key如果小于128保存在页面，超过则保存在overflow页面
 * pointer:page number(4 bytes)
 * @author Administrator
 *
 */
public class InternalNode extends BTreeNode {
	public InternalNode(){
		setLeaf(false);
	}
	/**
	 * return pointer if locate key.
	 * @param key
	 * @return
	 */
	public int searchKey(String key){
		return 0;
	}
	@Override
	public int containKey(String key) {
		int ret = ceiling(key);
		return ret;
	}
	@Override
	public byte[] find(String key) throws ApplicationException {
		int ret = ceiling(key);
		if(ret < 0){
			throw new ApplicationException("not found!");
		}
		
		return super.find(key);
	}
	public void split(){
		
	}
	public void merge(){
		
	}
	
	public boolean needSplit(){
		return false;
	}
}
