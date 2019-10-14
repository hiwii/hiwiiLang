package net.hiwii.system.store;

import net.hiwii.system.exception.ApplicationException;

/**
 * key���Դ����node�����128 bytes
 * Cell�ṹ��key + pointer
 * pointer������ҳ�棬key���С��128������ҳ�棬�����򱣴���overflowҳ��
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
