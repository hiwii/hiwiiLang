package net.hiwii.system.util;

import java.util.ArrayList;

import net.hiwii.system.exception.ApplicationException;

public class SortedList<E extends Comparable<E>> extends ArrayList<E> {
	private static final long serialVersionUID = 1L;
	public void put(E e){
		int pos = ceiling(e);
		if(pos == -1){
			this.add(e);
		}else{
			this.add(pos, e);
		}
	}
	
	/**
	 * 小于等于给定元素的最大元素；如果不存在这样的元素，则返回-1
	 * @param e
	 * @return
	 * @throws ApplicationException
	 */
	public int floor(E e) throws ApplicationException{
		if(this.size() == 0){
			return -1;
		}else{
			int low = 0;
			int high = this.size() - 1;
			int mid = 0;
			if(this.get(high).compareTo(e) <= 0){
				return high;
			}else{//high > e,当出现多个连续相等值，取最后一个
				if(this.get(low).compareTo(e) <= 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						E val = this.get(mid);
						int cmp = val.compareTo(e);
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
	 * @param e
	 * @return
	 */
	public int ceiling(E e){
		if(this.size() == 0){
			return -1;
		}else{
			int low = 0;
			int high = this.size() - 1;
			int mid = 0;
			if(this.get(low).compareTo(e) >= 0){
				return low;
			}else{//low < e,当出现多个连续项相等，取第一个
				if(this.get(high).compareTo(e) >= 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						E val = this.get(mid);
						int cmp = val.compareTo(e);
						if(cmp >= 0){
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
	
	/**
	 * 严格小于给定元素的最大元素；如果不存在这样的元素，则返回-1
	 * @param e
	 * @return
	 */
	public int lower(E e){
		if(this.size() == 0){
			return -1;
		}else{
			int low = 0;
			int high = this.size() - 1;
			int mid = 0;
			if(this.get(high).compareTo(e) < 0){
				return high;
			}else{//high >= e,当出现多个连续相等值，取最后一个
				if(this.get(low).compareTo(e) < 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						E val = this.get(mid);
						int cmp = val.compareTo(e);
						if(cmp < 0){
							low = mid;
						}else{
							high = mid;
						}
					}
					return low;
				}else{//low >= e
					return -1;
				}
			}
		}	
	}
	
	/**
	 * 严格大于给定元素的最小元素；如果不存在这样的元素，则返回-1
	 * @param e
	 * @return
	 */
	public int higher(E e){
		if(this.size() == 0){
			return -1;
		}else{
			int low = 0;
			int high = this.size() - 1;
			int mid = 0;
			if(this.get(low).compareTo(e) > 0){
				return low;
			}else{//low <= e,当出现多个连续项相等，取第一个
				if(this.get(high).compareTo(e) > 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						E val = this.get(mid);
						int cmp = val.compareTo(e);
						if(cmp > 0){
							high = mid;
						}else{
							low = mid;
						}
					}
					return high;
				}else{//high <= e
					return -1;
				}
			}
		}
	}
	
}
