package net.hiwii.system.store;

/**
 * ���Ҽ�¼ʱ������LeafPosition���ڲ����������ҵ���һ��¼��
 * @author ha-wangzhenhai
 *
 */
public class LeafPosition {
	private BTreeNode node;
	private int position;
	
	public LeafPosition(BTreeNode node, int position) {
		this.node = node;
		this.position = position;
	}
	/**
	 * @return the node
	 */
	public BTreeNode getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(BTreeNode node) {
		this.node = node;
	}
	/**
	 * @return the pos
	 */
	public int getPosition() {
		return position;
	}
	/**
	 * @param pos the pos to set
	 */
	public void setPosition(int pos) {
		this.position = pos;
	}
	
}
