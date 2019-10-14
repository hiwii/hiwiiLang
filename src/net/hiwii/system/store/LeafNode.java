package net.hiwii.system.store;

public class LeafNode extends BTreeNode {
	public LeafNode(){
		setLeaf(true);
	}
	public byte[] find(String key){
		int ret = floor(key);
		if(ret < 0){
			return null;
		}else{
			return getField(ret);
		}
	}
	
	public byte[] getField(int pos){
		return new byte[10];
	}
}
