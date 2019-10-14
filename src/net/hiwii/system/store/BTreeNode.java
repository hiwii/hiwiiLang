package net.hiwii.system.store;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import net.hiwii.system.exception.ApplicationException;

/**
 * page header:
 * 0-3	4 bytes 日志序号
 * 4	1 byte :page type;
 * 5-6	2 byte :deleted free cell
 * 7-8	2 byte :top free cell
 * 9-10 2 byte :cells number n
 * 11-14	4 bytes parent page number
 * 15-18	4 bytes right page number
 * 对于leafNode
 * 19-n*2+19 n*2 bytes cell pointer
 * 对于internalNode
 * 19以后 2 bytes cell pointer + 4 bytes page pointer
 * 
 * cell structure:
 * 对于leafNode
 * 1 byte fragment flag + 1 byte key flag + 1 byte value flag + key + value + fragment
 * 对于internalNode
 * 1 byte fragment flag + 4 byte page pointer + 1 byte key flag + key + fragment
 * fragment flag value:0--zero fragment,1--3表示1--3个byte fragment
 * 
 * key表示方法
 * 1 flag + key;//not overflow，最长占用129位
 *  or
 * 1 flag + pointer(4);//overflow
 * 当flag高位为1表示有overflow,次高位1表示full overflow，剩余6位表示part overflow页内pointer。
 * 当flag高位为0，没有overflow，余下7位表示key长度。
 * 由于key不能为空，因此key最少占用2 bytes，最多占用129 bytes。简化为128 bytes.
 * value结构同key。
 * key+value构成cell。
 * index btree允许value长度为0.因此，key+value最小值2+1，最大值129+129。
 * new record将寻找一个最合适的free cell存储，以形成最少fragment。
 * fragment number最大不超过128*3
 * 
 * free空间分为两类，一个是靠近页面顶端的free space，一个是删除后的free space。
 * 靠近页面顶端的free space大小需要根据cell的number做出调整
 * free cell构成chain。
 * chain节点：
 * 2 bytes point to next free cell
 * 2 byte length
 * 小于4 bytes形成fragment
 * free cell point to end cell free block
 * if free cell is zero, no free cell.
 * 
 * page number从1开始。
 * 如果parent/child page number = 0，表示指针无效。
 * 7   2 fragment number 暂不使用
 */
public class BTreeNode{
	private boolean leaf;
	private int cells;//number of cell pointer
	//key如果小于128保存在node中，超过保存在存储中。
	private String[] keys;
	private List<Integer> pointers;//number of cell pointer
	private int freePtr;//point to free cell.
	private long parent;
	private long pagenum;
	private int fragment;
	//chain按照size从小到大排序
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
	 * 在page中找到key，可以使用二分法。
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
			}else{//high > e,当出现多个连续相等值，取最后一个
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
	 * 大于等于给定元素的最小元素；如果不存在这样的元素，则返回-1
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
			}else{//low < key,当出现多个连续项相等，取第最后一个
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
			}else{//high > e,当出现多个连续相等值，取最后一个
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
	 * 大于等于给定元素的最小元素；如果不存在这样的元素，则返回-1
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
