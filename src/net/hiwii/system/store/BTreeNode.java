package net.hiwii.system.store;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import net.hiwii.system.exception.ApplicationException;

/**
 * page header:
 * 0-3	4 bytes ��־���
 * 4	1 byte :page type;
 * 5-6	2 byte :deleted free cell
 * 7-8	2 byte :top free cell
 * 9-10 2 byte :cells number n
 * 11-14	4 bytes parent page number
 * 15-18	4 bytes right page number
 * ����leafNode
 * 19-n*2+19 n*2 bytes cell pointer
 * ����internalNode
 * 19�Ժ� 2 bytes cell pointer + 4 bytes page pointer
 * 
 * cell structure:
 * ����leafNode
 * 1 byte fragment flag + 1 byte key flag + 1 byte value flag + key + value + fragment
 * ����internalNode
 * 1 byte fragment flag + 4 byte page pointer + 1 byte key flag + key + fragment
 * fragment flag value:0--zero fragment,1--3��ʾ1--3��byte fragment
 * 
 * key��ʾ����
 * 1 flag + key;//not overflow���ռ��129λ
 *  or
 * 1 flag + pointer(4);//overflow
 * ��flag��λΪ1��ʾ��overflow,�θ�λ1��ʾfull overflow��ʣ��6λ��ʾpart overflowҳ��pointer��
 * ��flag��λΪ0��û��overflow������7λ��ʾkey���ȡ�
 * ����key����Ϊ�գ����key����ռ��2 bytes�����ռ��129 bytes����Ϊ128 bytes.
 * value�ṹͬkey��
 * key+value����cell��
 * index btree����value����Ϊ0.��ˣ�key+value��Сֵ2+1�����ֵ129+129��
 * new record��Ѱ��һ������ʵ�free cell�洢�����γ�����fragment��
 * fragment number��󲻳���128*3
 * 
 * free�ռ��Ϊ���࣬һ���ǿ���ҳ�涥�˵�free space��һ����ɾ�����free space��
 * ����ҳ�涥�˵�free space��С��Ҫ����cell��number��������
 * free cell����chain��
 * chain�ڵ㣺
 * 2 bytes point to next free cell
 * 2 byte length
 * С��4 bytes�γ�fragment
 * free cell point to end cell free block
 * if free cell is zero, no free cell.
 * 
 * page number��1��ʼ��
 * ���parent/child page number = 0����ʾָ����Ч��
 * 7   2 fragment number �ݲ�ʹ��
 */
public class BTreeNode{
	private boolean leaf;
	private int cells;//number of cell pointer
	//key���С��128������node�У����������ڴ洢�С�
	private String[] keys;
	private List<Integer> pointers;//number of cell pointer
	private int freePtr;//point to free cell.
	private long parent;
	private long pagenum;
	private int fragment;
	//chain����size��С��������
	private List<CellPointer> chain;
	private NavigableMap<Integer,Integer> ptrIndex;
	
	public BTreeNode() {
		chain = new ArrayList<CellPointer>();
		ptrIndex = new TreeMap<Integer,Integer>();
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public int getFreePtr() {
		return freePtr;
	}

	public void setFreePtr(int freePtr) {
		this.freePtr = freePtr;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public long getPagenum() {
		return pagenum;
	}

	public void setPagenum(long pagenum) {
		this.pagenum = pagenum;
	}

	public int getFragment() {
		return fragment;
	}

	public void setFragment(int fragment) {
		this.fragment = fragment;
	}

	public int getCells() {
		return cells;
	}

	public void setCells(int cells) {
		this.cells = cells;
	}

	public List<Integer> getPointers() {
		return pointers;
	}

	public void setPointers(List<Integer> pointers) {
		this.pointers = pointers;
	}

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public List<CellPointer> getChain() {
		return chain;
	}

	public void setChain(List<CellPointer> chain) {
		this.chain = chain;
	}

	public int containKey(String key){
		return -1;
	}
	/**
	 * ��page���ҵ�key������ʹ�ö��ַ���
	 * @param key
	 */
	public byte[] find(String key) throws ApplicationException{
//		int pos = floor(key)
		return null;
	}
	
	public int floor(String key){
		int len = getCells();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
			if(getKey(high).compareTo(key) <= 0){
				return high;
			}else{//high > e,�����ֶ���������ֵ��ȡ���һ��
				if(getKey(low).compareTo(key) <= 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						String val = getKey(mid);
						int cmp = val.compareTo(key);
						if(cmp <= 0){
							low = mid;
						}else{
							high = mid;
						}
					}
					return low;
				}else{//low > e
					return -1;
				}
			}
		}
	}
	
	/**
	 * ���ڵ��ڸ���Ԫ�ص���СԪ�أ����������������Ԫ�أ��򷵻�-1
	 * @param key
	 * @return
	 */
	public int ceiling(String key){
		int len = getCells();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
			if(getKey(low).compareTo(key) > 0){
				return low;
			}else{//low < key,�����ֶ����������ȣ�ȡ�����һ��
				if(getKey(high).compareTo(key) > 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						String val = getKey(mid);
						int cmp = val.compareTo(key);
						if(cmp > 0){
							high = mid;
						}else{
							low = mid;
						}
					}
					return high;
				}else if(getKey(high).compareTo(key) == 0){
					return high;
				}else{
					return -1;
				}
			}
		}
	}
	
	public int getPointer(int index){
		return chain.get(index).getPointer();
	}
	
	public int floorChain(int size){
		int len = chain.size();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
			if(chain.get(high).getSize() <= size){
				return high;
			}else{//high > e,�����ֶ���������ֵ��ȡ���һ��
				if(chain.get(low).getSize() <= size){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						if(chain.get(mid).getSize() <= size){
							low = mid;
						}else{
							high = mid;
						}
					}
					return low;
				}else{//low > e
					return -1;
				}
			}
		}
	}
	
	/**
	 * ���ڵ��ڸ���Ԫ�ص���СԪ�أ����������������Ԫ�أ��򷵻�-1
	 * @param key
	 * @return
	 */
	public int ceilingChain(int size){
		int len = chain.size();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
			if(chain.get(low).getSize() >= size){
				return low;
			}else{
				if(chain.get(high).getSize() >= size){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						if(chain.get(mid).getSize() <= size){
							high = mid;
						}else{
							low = mid;
						}
					}
					return high;
				}else{
					return -1;
				}
			}
		}
	}
	
	public void putChain(CellPointer cp){
		int pos = ceilingChain(cp.getSize());
		if(pos < 0){
			chain.add(cp);
		}else{
			chain.add(pos, cp);
		}
	}
	public String getKey(int pos){
		return "test";
	}
	
	public byte[] getValue(int pos){
		return null;
	}

}
