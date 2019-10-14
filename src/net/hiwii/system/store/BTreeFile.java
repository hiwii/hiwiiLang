package net.hiwii.system.store;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hiwii.obj.file.FileObject;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;

/**
 * top 512 bytes for file header.
 * 16-19 4 bytes root page
 * 20-23 4 bytes free page
 * @author ha-wangzhenhai
 * 
 * BTreeFile����һ��512 byte root Page.ÿ������Page�ݶ�Ϊ4096.
 * ��root Page�⣬page���������ͣ�internalPage��leafPage��
 * internalPage������n��key��n+1 pointer�����һ��pointer��right pointer��
 * ÿ��pointer��ʾС�ڻ����key��pageָ�롣
 * leafPage����n��key��n��data��data��byte[]��ɡ�right pointerָ���Ҳ��leafPage
 * leafPage�����4���ֽڣ����ڼ�¼����leafPage��
 * û�����ݵ�pageΪfreePage������ʽ���ӡ�
 * overflow page���ڼ�¼����128�ֽڳ���key��value��
 * 
 * BTreeFile��key�����ظ�����key���ظ��������ظ�key�ǲ������ظ����е���β��ɾ����
 * ɾ����β��key��
 * String floorKey(key) ����С�ڵ���ָ��key�����keyֵ
 * String ceilingKey(key) ���ش��ڵ���ָ��key����Сkeyֵ
 * String maxKey(key) �������keyֵ
 * String minKey(key) ������Сkeyֵ
 * String lowerKey(key) ����С��ָ��key������keyֵ
 * String higherKey(key) ����С��ָ��key������keyֵ
 * 
 * public void insert(String key, byte[] val)
 * public byte[] search(String key)
 * public void delete(String key)
 *
 */
public class BTreeFile extends FileObject {
	private int m;
	private int pagesize;
	private Map<Long,Page> cache;
	private byte[] meta;//512 bytes
	private long root;
	
	public BTreeFile(File file) {
		super(file);
		cache = new HashMap<Long,Page>();
		try {
			readMeta();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m = 1;
		pagesize = 4096;
	}
	
	public long getRoot() {
		return root;
	}

	public void setRoot(long root) {
		this.root = root;
	}

	/**
	 * ����˼�룺node��Ϊ������ڡ�page������ϵͳ�С�
	 * node��key��value�������ڶ����У���ͨ�����屣����page�С�
	 * @param key
	 * @param val
	 * @throws ApplicationException
	 */
	public void insert(String key, byte[] val) throws ApplicationException{
//		BTreeNode node = getRootNode();
		BTreeNode node = searchLeafNode(key);
//		System.out.println("page num =" + node.getPagenum());
		insertLeaf(node, key, val);
	}
	
	public void delete(String key) throws ApplicationException{
//		BTreeNode node = searchLeafNode(key);
//		deleteKey(node, key);
		
		BTreeNode node = getRootNode();
		deleteNodeKey(node, key);
	}
	/**
	 * ���δ�ҵ����׳�����
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public byte[] search(String key) throws ApplicationException{
		BTreeNode node = searchLeafNode(key);//pg.getNode();
		if(node == null){
			throw new ApplicationException("not found!");
		}
		int pos = positionKey(node, key);
		if(pos < 0){
			throw new ApplicationException(key + ":not found!");
		}
		return getValue(node, pos);
		
	}
	
	public byte[] searchIdentifier(String key) throws ApplicationException{
		BTreeNode node = searchLeafNode(key);//pg.getNode();
		if(node == null){
			throw new ApplicationException("not found!");
		}
		int pos = positionKeyIdentifier(node, key);
		if(pos < 0 || !this.containsKey(node, pos, key)){
			throw new ApplicationException("not found!");
		}
		LeafPosition lp = new LeafPosition(node, pos);
		while(true){
			if(lp.getPosition() >= 0 && this.containsKey(lp.getNode(), lp.getPosition(), key)){
				pos = lp.getPosition();
				node = lp.getNode();
			}else{
				break;
			}
			lp = getNextPosition(node, pos);
		}
		return getValue(node, pos);
	}
	
	public void update(String key, byte[] val){
		
	}
	
	/**
	 * if not found, return null;
	 * @return
	 * @throws ApplicationException
	 */
	public BTreeNode searchLeafNode(String key) throws ApplicationException{
		BTreeNode node = getRootNode();
		
		while(!node.isLeaf()){
			long pnum = getChildPointer(node, key);
			node = getNode(pnum);//pg.getNode();
		}
		
		return node;
	}
	
	/**
	 * ���ش� set �д��ڵ��ڸ���Ԫ�ص���СԪ�أ����������������Ԫ�أ��򷵻� null��
	 * ��Ч�ʿ��ǣ�Ӧ��ͬʱ����value��node position��
	 * floor��higher��lower��ʱ���롣
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public KeyValue ceiling(String key) throws ApplicationException{
		BTreeNode node = getRootNode();
		while(!node.isLeaf()){
			long pnum = getChildPointer(node, key);
			node = getNode(pnum);//pg.getNode();
		}
		int pos = ceiling(node, key);
		if(pos < 1){
			LeafPosition lp = getNextPosition(node, node.getCells() - 1);
			if(lp == null){
				return null;
			}else{
				KeyValue kv = new KeyValue();
				kv.setKey(getKey(lp.getNode(), lp.getPosition()));
				kv.setValue(new String(getValue(lp.getNode(), lp.getPosition()), Charset.forName("UTF-8")));//
				return kv;
			}
		}else{
			KeyValue kv = new KeyValue();
			kv.setKey(getKey(node, pos));
			kv.setValue(new String(getValue(node, pos), Charset.forName("UTF-8")));
			return kv;
		}
	}
	
	/**
	 * ���ش� set ��С�ڵ��ڸ���Ԫ�ص����Ԫ�أ����������������Ԫ�أ��򷵻� null��
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public KeyValue floor(String key) throws ApplicationException{
		return null;
	}
	
	/**
	 * ���ش� set ���ϸ���ڸ���Ԫ�ص���СԪ�أ����������������Ԫ�أ��򷵻� null��
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public KeyValue higher(String key) throws ApplicationException{
		return null;
	}
	
	/**
	 * ���ش� set ���ϸ�С�ڸ���Ԫ�ص����Ԫ�أ����������������Ԫ�أ��򷵻� null��
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public KeyValue lower(String key) throws ApplicationException{
		return null;
	}
	
	/**
	 * ���ش� set �д��ڵ��ڸ���Ԫ�ص���СԪ�ص�next�����������������Ԫ�أ��򷵻� null��
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public String ceilingNext(String key) throws ApplicationException{
		return null;
	}
	
	/**
	 * ���ش� set �д��ڵ��ڸ���Ԫ�ص���СԪ�ص�Previous�����������������Ԫ�أ��򷵻� null��
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public String ceilingPrevious(String key) throws ApplicationException{
		return null;
	}
	
	/**
	 * ���ش� set ��С�ڵ��ڸ���Ԫ�ص����Ԫ�ص�next�����������������Ԫ�أ��򷵻� null��
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public String floorNext(String key) throws ApplicationException{
		return null;
	}
	
	/**
	 * ���ش� set ��С�ڵ��ڸ���Ԫ�ص����Ԫ�ص�Previous�����������������Ԫ�أ��򷵻� null��
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public String floorPrevious(String key) throws ApplicationException{
		return null;
	}
	
	/**
	 * ���δ�ҵ�������null��
	 * @param node
	 * @param pos
	 * @return
	 */
	public LeafPosition getNextPosition(BTreeNode node, int pos){
		int cells = node.getCells();
		if(pos < cells){
			return  new LeafPosition(node, pos + 1); 
		}else{
			try {
				byte[] data = getPage(node.getPagenum()).getData();
				long nextp = EntityUtil.unsignedInt(data, 15);
				if(nextp == 0){
					return null;
				}
				BTreeNode ret = getNode(nextp);
				if(ret.getCells() == 0){
					return null;
				}else{
					return new LeafPosition(ret, 0); 
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public LeafPosition getPreviousPosition(BTreeNode node, int pos){
		int cells = node.getCells();
		if(pos > 0){
			return  new LeafPosition(node, pos - 1); 
		}else{
			try {
				byte[] data = getPage(node.getPagenum()).getData();
				long lastp = EntityUtil.unsignedInt(data, 4092);
				if(lastp == 0){
					return null;
				}
				BTreeNode ret = getNode(lastp);
				if(ret.getCells() == 0){
					return null;
				}else{
					return new LeafPosition(ret, 0); 
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public BTreeNode getRootNode() throws ApplicationException{
		long pagenum = EntityUtil.unsignedInt(meta, 16);
		if(pagenum == 0){
			byte[] ptr = new byte[4];
			ptr = EntityUtil.getUnsignedInt(1);
			System.arraycopy(ptr, 0, meta, 16, 4);
			return newLeafNode();
		}
		return getNode(pagenum);
	}
	
	public Page getPage(long pagenum) throws ApplicationException{
		if(cache.containsKey(pagenum)){
			return cache.get(pagenum);
		}
		byte[] buf = new byte[4096];//m * pagesize
		long pos = (pagenum - 1) * 4096 + 512;
		int ret = read(buf, pos);
		if(ret <= 0){
			throw new ApplicationException("io err!");
		}

		Page pg = new Page();
		pg.setData(buf);
		pg.setNumber(pagenum);
		cache.put(pagenum, pg);
//		pg.setType(type)
		return pg;
	}
	
	public BTreeNode getNode(long pagenum) throws ApplicationException{
		Page pg = getPage(pagenum);
		byte[] data = pg.getData();
		
		BTreeNode bn = new BTreeNode();
		bn.setPagenum(pagenum);
		if(data[4] == 0){
			bn.setLeaf(true);
		}else{
			bn.setLeaf(false);
		}
		int pointer = EntityUtil.unsignedShort(data, 5);
		if(pointer != 0){
			//map<size,pointer>��˳���С��������
			List<CellPointer> chain = new ArrayList<CellPointer>();
			bn.setChain(chain);
			CellPointer parent = null;//if null, pointer = 5
//			int parent = 5;
			int child = 0;
			while(true){
				if(pointer == 0){//chain end
					break;
				}
				child = EntityUtil.unsignedShort(data, pointer - 1);
				int fsize = EntityUtil.unsignedShort(data, pointer - 3);
				CellPointer cp = new CellPointer();
				cp.setPointer(pointer);
				cp.setSize(fsize);
				cp.setParent(parent);
				bn.putChain(cp);
				
				parent = cp;
				pointer = child;
			}
		}
		int cells = EntityUtil.unsignedShort(data, 9);
		bn.setCells(cells);
//		List<Integer> ptrs = new ArrayList<Integer>();
//		for(int i=0;i<cells;i++){
//			ptrs.add(EntityUtil.unsignedShort(data, 19 + i * 2));
//		}
//		bn.setPointers(ptrs);
		return bn;
	}
	
	public long newPage() throws ApplicationException{
		try {
			byte[] data = new byte[4096];
			long end = size();
			long pagenum = (end - 512) / 4096 + 1;
			int cnt = write(data, end);
			Page pg = new Page();
			pg.setData(data);
			pg.setNumber(pagenum);
			cache.put(pagenum, pg);
			return pagenum;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException("io err!");
		}
	}
	
	public BTreeNode newLeafNode() throws ApplicationException{
		long pnum = newPage();

		BTreeNode node = new BTreeNode();
		node.setPagenum(pnum);
		node.setCells(0);
		node.setLeaf(true);

		Page pg = getPage(node.getPagenum());
		byte[] fp = EntityUtil.getUnsignedShort(4091);//free pointer����4byte����ָ��
		System.arraycopy(fp, 0, pg.getData(), 7, 2);
		return node;
	}
	
	public BTreeNode newInternalNode() throws ApplicationException{
		long pnum = newPage();

		BTreeNode node = new BTreeNode();
		node.setPagenum(pnum);
		node.setCells(0);
		node.setLeaf(false);

		Page pg = getPage(node.getPagenum());
		byte[] fp = EntityUtil.getUnsignedShort(4095);
//		byte[] fp = EntityUtil.getUnsignedShort(4091);//free pointer
		System.arraycopy(fp, 0, pg.getData(), 7, 2);
		return node;
	}
	
	/***************************************
	 * node������file container����ɣ���Ϊ��Ҫ����cache��ֻ�йؼ���Ϣ��node�С�
	 * key/data��Ϣ��page�С�
	 * ******************************************/
	public long getChildPointer(BTreeNode node, String key) throws ApplicationException{
		byte[] data = getPage(node.getPagenum()).getData();
		int pos = ceiling(node, key);//TODO �ظ�key
		if(pos < 0){
			return EntityUtil.unsignedInt(data, 15);
		}
		int ptr = EntityUtil.unsignedShort(data, 19 + pos * 2);
		return EntityUtil.unsignedInt(data, ptr + 1);//skip fragment flag
	}
	
	public long getChildPointerByIdentifier(BTreeNode node, String key) throws ApplicationException{
		byte[] data = getPage(node.getPagenum()).getData();
		int pos = ceiling(node, key);//TODO �ظ�key
		if(pos < 0){
			return EntityUtil.unsignedInt(data, 15);
		}
		int ptr = EntityUtil.unsignedShort(data, 19 + pos * 2);
		return EntityUtil.unsignedInt(data, ptr + 1);//skip fragment flag
	}
	
	public BTreeNode getChildNode(BTreeNode node, int pos) throws ApplicationException{
		byte[] data = getPage(node.getPagenum()).getData();
		if(pos < 0){
			long pnum = EntityUtil.unsignedInt(data, 15);
			return getNode(pnum);
		}
		int ptr1 = EntityUtil.unsignedShort(data, 19 + pos * 2);
		long pnum = EntityUtil.unsignedInt(data, ptr1 + 1);
		return getNode(pnum);
	}
	
	public byte[] getValue(BTreeNode node, int pos) throws ApplicationException{
		byte[] data = getPage(node.getPagenum()).getData();
		int ptr1 = EntityUtil.unsignedShort(data, 19 + pos * 2);
		int keysize, valsize;
		byte kflag, vflag;
		
		if(node.isLeaf()){
			kflag = data[ptr1 + 1];
			if((kflag & 0x80) == 0){
				keysize = kflag & 0x7f;
			}else{
				keysize = 4;//��Ҫ����overflow
			}
			vflag = data[ptr1 + 2];
			if((vflag & 0x80) == 0){
				valsize = vflag & 0x7f;
			}else{
				valsize = 4;//��Ҫ����overflow
			}
			byte[] ret = new byte[valsize];
			System.arraycopy(data, ptr1 + 3 + keysize, ret, 0, valsize);
			return ret;
		}else{
			throw new ApplicationException("not leaf!");
		}
	}
	
	public String getKey(BTreeNode node, int pos) throws ApplicationException{
		byte[] data = getPage(node.getPagenum()).getData();
		int ptr1 = EntityUtil.unsignedShort(data, 19 + pos * 2);
		int keysize, valsize;
		byte kflag;
		
		if(node.isLeaf()){
			kflag = data[ptr1 + 1];
			if((kflag & 0x80) == 0){
				keysize = kflag & 0x7f;
				String ret = new String(data, ptr1 + 3, keysize, Charset.forName("UTF-8"));
				return ret;
			}else{
				keysize = 4;//��Ҫ����overflow
			}
		}else{
			kflag = data[ptr1 + 5];
			if((kflag & 0x80) == 0){
				keysize = kflag & 0x7f;
				String ret = new String(data, ptr1 + 6, keysize, Charset.forName("UTF-8"));
				return ret;
			}else{
				keysize = 4;//��Ҫ����overflow
			}
		}
//		int clen = keysize + 5;
		return null;
	}
	
	/**
	 * ��������key's position.һ���������leaf key position.
	 * ����ظ�key��ȡ���һ����
	 * @throws ApplicationException 
	 */
	public int positionKey(BTreeNode node, String key) throws ApplicationException{
		int len = node.getCells();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
			if(compareKey(node, low, key) > 0 || compareKey(node, high, key) < 0){
				return -1;
			}
			while(low <= high){
				mid = (low + high) >> 1;
				if(mid == low){
					break;
				}
				if(compareKey(node, mid, key) <= 0){
					low = mid;
				}else{
					high = mid;
				}
			}
			return low;
		}
	}
	
	/**
	 * �����õ�key��ƥ�䣬�򷵻�-1.
	 * ��������key's position.һ���������leaf key position.
	 * ����ظ�key��ȡ���һ����
	 * @throws ApplicationException 
	 */
	public int positionKeyIdentifier(BTreeNode node, String key) throws ApplicationException{
		int len = node.getCells();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
//			if(compareKey(node, low, key) > 0 || compareKey(node, high, key) < 0){
//				return -1;
//			}
			while(low <= high){
				mid = (low + high) >> 1;
				if(mid == low){
					break;
				}
				if(compareKey(node, mid, key) <= 0){
					low = mid;
				}else{
					high = mid;
				}
			}
			if(this.containsKey(node, low, key)){
				return low;
			}
			return -1;
		}
	}
	
	/**
	 * key��node1 + pos1��ʾ��
	 * @param node
	 * @param node1
	 * @param pos1
	 * @param rightPtr��������ʱ��splitNode����ָ��ָ��rightPtr
	 * @throws ApplicationException
	 */
	public void insertInternal(BTreeNode node, BTreeNode node1, int pos1, long rightPtr) throws ApplicationException{
		Page pg = getPage(node.getPagenum());
		byte[] data = pg.getData();
		byte[] data1 = getPage(node1.getPagenum()).getData();
		
		int pos = ceiling(node, node1, pos1);
		if(pos < 0){
			pos = node.getCells();
		}else{
			if(this.compareKey(node, pos, node1, pos1) == 0){
				pos = pos + 1;
			}
		}
		
		//insert before found key
		int keysize;

		int ptr1 = EntityUtil.unsignedShort(data1, 19 + pos1 * 2);

		byte kflag;
		if(node1.isLeaf()){
			kflag = data1[ptr1 + 1];
		}else{
			kflag = data1[ptr1 + 5];
		}

		if((kflag & 0x80) == 0){
			keysize = kflag & 0x7f;
		}else{
			keysize = 4;//��Ҫ����overflow
		}
		int clen = keysize + 6;//cell length
		
		//����ʹ��top free
		int pointer = EntityUtil.unsignedShort(data, 7);
		int room = pointer - 19  - 2 * node.getCells();//EntityUtil.unsignedShort(data, pointer - 1);
		int cur = pointer - clen + 1;

		if(room >= clen + 2){//2 byte for pointer in top free
			byte[] ptr = EntityUtil.getUnsignedShort(pointer - clen);
			System.arraycopy(ptr, 0, data, 7, 2);
		}else{
			int idx = node.ceilingChain(clen);
			if(idx < 0){
				//split
				BTreeNode right = splitInternal(node);
				System.out.println("split");

				int cmp = compareKey(node1, pos1, node, node.getCells() - 1);
				if(cmp > 0){
					insertInternal(node, node1, pos1, rightPtr);
				}else{
					insertInternal(right, node1, pos1, rightPtr);
				}
				
				long parent = EntityUtil.unsignedInt(data, 11);
				if(parent == 0){
					BTreeNode in = newInternalNode();
//					in.setLeaf(false);
					byte[] ptr0 = EntityUtil.getUnsignedInt(in.getPagenum());
					System.arraycopy(ptr0, 0, meta, 16, 4);
					byte[] idata = getPage(in.getPagenum()).getData();
					idata[4] = 1;//node type
					//��ʱnode.getCells����Ч
					insertInternal(in, node, node.getCells(), right.getPagenum());

					System.arraycopy(ptr0, 0, getPage(node.getPagenum()).getData(), 11, 4);
					System.arraycopy(ptr0, 0, getPage(right.getPagenum()).getData(), 11, 4);
				}else{
					BTreeNode in = getNode(parent);
					insertInternal(in, node, node.getCells(), right.getPagenum());
				}
				return;
			}
			//insert space δ����
			room = node.getChain().get(idx).getSize();
			pointer = node.getChain().get(idx).getPointer();
			cur = pointer - clen + 1;
			int parent = 6;
			if(node.getChain().get(idx).getParent() != null){
				parent = node.getChain().get(idx).getParent().getPointer();
			}
			byte[] ptr = EntityUtil.getUnsignedShort(pointer - clen);
			byte[] downptr = new byte[2];
			System.arraycopy(data, pointer - 1, downptr, 0, 2);

			if(clen < room - 4){
				System.arraycopy(ptr, 0, data, parent - 1, 2);
				System.arraycopy(downptr, 0, data, pointer - clen - 1, 2);
				byte[] fsize = EntityUtil.getUnsignedShort(room - clen);
				System.arraycopy(fsize, 0, data, pointer - clen - 3, 2);
			}else{
				//top pointer set to zero
				System.arraycopy(downptr, 0, pg.getData(), parent - 1, 2);
			}
		}
		
		int n = node.getCells();
		insertCellPointer(node, pos, cur);
		cur = cur + 5;
		if(node1.isLeaf()){
			data[cur] = kflag;
			System.arraycopy(data1, ptr1 + 3, data, cur + 1, keysize);
		}else{
			System.arraycopy(data1, ptr1 + 5, data, cur, keysize + 1);
		}
		cur = cur - 4;//ptr + 1 + keysize + 1
		
		System.out.println("n=" + n + ",pos="+ pos);
		if(n == 0){
			byte[] pnum = EntityUtil.getUnsignedInt(node1.getPagenum());
			System.arraycopy(pnum, 0, data, cur, 4);
			pnum = EntityUtil.getUnsignedInt(rightPtr);
			System.arraycopy(pnum, 0, data, 15, 4);
		}else if(n == pos){
			System.arraycopy(data, 15, data, cur, 4);
			byte[] pnum = EntityUtil.getUnsignedInt(rightPtr);
			System.arraycopy(pnum, 0, data, 15, 4);
		}else{
			int ptr0 = EntityUtil.unsignedShort(data, 19 + (pos + 1) * 2);
			System.arraycopy(data, ptr0 + 1, data, cur, 4);
			byte[] pnum = EntityUtil.getUnsignedInt(rightPtr);
			System.arraycopy(pnum, 0, data, ptr0 + 1, 4);
		}
	}
	
	public void insertLeaf(BTreeNode node, String key, byte[] val) throws ApplicationException{
		Page pg = getPage(node.getPagenum());
		byte[] data = pg.getData();
		//insert before found key
		int keysize, valsize;
		byte kflag = 0, vflag = 0;
		
		int keylen = key.getBytes(Charset.forName("UTF-8")).length;
		if(keylen > 128){
			keysize = 4;//4 bytes page pointer
		}else{
			keysize = keylen;
			kflag = (byte) (keysize & 0xff);
		}
		if(val.length > 128){
			valsize = 4;
		}else{
			valsize = val.length;
			vflag = (byte) (valsize & 0xff);
		}
		int clen = keysize + valsize + 3;//cell length
		
		//����ʹ��top free
		int pointer = EntityUtil.unsignedShort(data, 7);
		int room = pointer - 19 - 2 * node.getCells();//EntityUtil.unsignedShort(data, pointer - 1);
		int cur = pointer - clen + 1;
		if(room >= clen + 2){//2 byte for pointer in top free
			byte[] ptr = EntityUtil.getUnsignedShort(pointer - clen);
			System.arraycopy(ptr, 0, data, 7, 2);
			
			data[cur] = 0;
		}else{
			int idx = node.ceilingChain(clen);
			if(idx < 0){
				//split
				BTreeNode right = split(node);
				System.out.println("split");

				int cmp = compareKey(right, 0, key);
				if(cmp < 0){
					insertLeaf(right, key, val);
				}else{
					insertLeaf(node, key, val);
				}
				
				long parent = EntityUtil.unsignedInt(data, 11);
				if(parent == 0){
					BTreeNode in = newLeafNode();
					in.setLeaf(false);
					byte[] ptr0 = EntityUtil.getUnsignedInt(in.getPagenum());
					System.arraycopy(ptr0, 0, meta, 16, 2);
					byte[] idata = getPage(in.getPagenum()).getData();
					idata[4] = 1;//node type
					insertInternal(in, node, node.getCells() - 1, right.getPagenum());

					System.arraycopy(ptr0, 0, getPage(node.getPagenum()).getData(), 11, 4);
					System.arraycopy(ptr0, 0, getPage(right.getPagenum()).getData(), 11, 4);
					
					ptr0 = EntityUtil.getUnsignedInt(right.getPagenum());
					System.arraycopy(ptr0, 0, getPage(node.getPagenum()).getData(), 15, 4);
				}else{
					BTreeNode in = getNode(parent);
					insertInternal(in, node, node.getCells() - 1, right.getPagenum());
					
					byte[] ptr0 = EntityUtil.getUnsignedInt(parent);
					System.arraycopy(ptr0, 0, getPage(right.getPagenum()).getData(), 11, 4);
					
					byte[] rightPtr = new byte[4];
					System.arraycopy(data, 15, rightPtr, 0, 4);
					ptr0 = EntityUtil.getUnsignedInt(right.getPagenum());
					System.arraycopy(ptr0, 0, data, 15, 4);
					System.arraycopy(rightPtr, 0, getPage(right.getPagenum()).getData(), 15, 4);
				}
				return;
			}
			room = node.getChain().get(idx).getSize();
			pointer = node.getChain().get(idx).getPointer();
			int parent = 6;//�� pointer - 1һ��
			if(node.getChain().get(idx).getParent() != null){
				parent = node.getChain().get(idx).getParent().getPointer();
			}

			byte[] ptr = EntityUtil.getUnsignedShort(pointer - clen);
			byte[] downptr = new byte[2];
			System.arraycopy(data, pointer - 1, downptr, 0, 2);

			if(clen < room - 4){
				System.arraycopy(ptr, 0, data, parent - 1, 2);
				System.arraycopy(downptr, 0, data, pointer - clen - 1, 2);
				byte[] fsize = EntityUtil.getUnsignedShort(room - clen);
				System.arraycopy(fsize, 0, data, pointer - clen - 3, 2);
				
				cur = pointer - clen + 1;
				data[cur] = 0;
			}else{
				//top pointer set to zero
				System.arraycopy(downptr, 0, data, parent - 1, 2);
				
				cur = pointer - room + 1;
				data[cur] = (byte) (room - clen);
			}
		}
		
		int pos = ceiling(node, key);
		if(pos < 0){
			pos = node.getCells();//add to end
		}else{
			if(this.compareKey(node, pos, key) == 0){
				pos = pos + 1;
			}
		}
		insertCellPointer(node, pos, cur);
		cur++;
		data[cur] = kflag;
		cur++;
		data[cur] = vflag;
		cur++;
		System.arraycopy(key.getBytes(Charset.forName("UTF-8")), 0, data, cur, keysize);
		cur = cur + keysize;//ptr + 1 + keysize + 1
		System.arraycopy(val, 0, data, cur, valsize);
		
	}
	
	public void insertCellPointer(BTreeNode node, int pos, int pointer) throws ApplicationException{
		int n = node.getCells();
		node.setCells(n + 1);
		byte[] len = EntityUtil.getUnsignedShort(n + 1);
		Page pg = getPage(node.getPagenum());
		System.arraycopy(len, 0, pg.getData(), 9, 2);
		if(pos < n){
			int psize = (n - pos) * 2;
			byte[] part = new byte[psize];
			System.arraycopy(pg.getData(), 19 + pos * 2, part, 0, psize);
			byte[] ptr = EntityUtil.getUnsignedShort(pointer);
			System.arraycopy(ptr, 0, pg.getData(), 19 + pos * 2, 2);
			System.arraycopy(part, 0, pg.getData(), 19 + (pos + 1) * 2, psize);
		}else{
			byte[] ptr = EntityUtil.getUnsignedShort(pointer);
			System.arraycopy(ptr, 0, pg.getData(), 19 + n * 2, 2);
		}
	}
	
	public void mergeFree(BTreeNode node, int pointer, int len) throws ApplicationException{
		Page pg = getPage(node.getPagenum());
		byte[] data = pg.getData();
		
		//���Ⱥϲ��Ҳ�space.�ڴ��Ż�����pointer����
		int right = pointer + len - 1;
		int ln = 0, rn = 0;
		int i = 0;
		int topfree = EntityUtil.unsignedShort(data, 7);
		int topsize = topfree - 19;
		boolean ftop = false;//if is neighbor of top free.
		boolean fr = false, fl = false;//fr for right, fl for left
		
		//�Ƿ��ٽ�top free
		if(pointer == topfree + 1){
			ftop = true;
		}
		for(CellPointer cp:node.getChain()){
			if(!fr && right == cp.getPointer() - cp.getSize()){
				fr = true;
				rn = i;
			}
			if(!fl && pointer == cp.getPointer() + 1){
				fl = true;
				ln = i;
			}
			if(fr && fl){
				break;
			}
			i++;
		}
		
		if(fr && fl){
			CellPointer ls = node.getChain().get(ln);
			CellPointer rs = node.getChain().get(rn);//right space
			int len1 = ls.getSize() + len + rs.getSize();
			byte[] newsize = EntityUtil.getUnsignedShort(len1);
			System.arraycopy(newsize, 0, data, rs.getPointer() - 3, 2);
			if(rs.getParent() == ls){
				CellPointer parent = ls.getParent();
				byte[] newptr = EntityUtil.getUnsignedShort(rs.getPointer());
				if(parent == null){
					System.arraycopy(newptr, 0, data, 5, 2);
				}else{
					System.arraycopy(newptr, 0, data, parent.getPointer() - 1, 2);
				}
				rs.setParent(ls.getParent());
				rs.setSize(len1);
				node.getChain().remove(ln);
			}else if(ls.getParent() == rs){
				byte[] child = new byte[2];
				System.arraycopy(data, ls.getPointer() - 1, child, 0, 2);
				System.arraycopy(child, 0, data, rs.getPointer() - 1, 2);

				for(CellPointer cp:node.getChain()){
					if(cp.getParent() == ls){
						cp.setParent(rs);
						break;
					}
				}
				rs.setSize(len1);
				node.getChain().remove(ln);
			}
			return;
		}
		if(ftop && fr){
			CellPointer rs = node.getChain().get(rn);//right space
			int len1 = rs.getSize() + len;
			len1 = topsize + len1;
			byte[] newptr = EntityUtil.getUnsignedShort(rs.getPointer());
			System.arraycopy(newptr, 0, data, 7, 2);

			byte[] child = new byte[2];
			System.arraycopy(data, rs.getPointer() - 1, child, 0, 2);
			if(rs.getParent() == null){
				System.arraycopy(child, 0, data, 5, 2);
			}else{					
				System.arraycopy(child, 0, data, rs.getParent().getPointer() - 1, 2);
			}
			for(CellPointer cp:node.getChain()){
				if(cp.getParent() == rs){
					cp.setParent(rs.getParent());
					break;
				}
			}
			node.getChain().remove(rn);
			return;
		}
		
		if(fr){
			CellPointer rs = node.getChain().get(rn);//right space
			int len1 = rs.getSize() + len;
			rs.setSize(len1);
			byte[] newsize = EntityUtil.getUnsignedShort(len1);
			System.arraycopy(newsize, 0, data, rs.getPointer() - 3, 2);
			return;
		}
		
		if(ftop){
			int ptr = topfree + len;
			byte[] newptr = EntityUtil.getUnsignedShort(ptr);
			System.arraycopy(newptr, 0, data, 7, 2);
			return;
		}
		if(fl){
			CellPointer ls = node.getChain().get(ln);//left space
			byte[] child = new byte[2];
			System.arraycopy(data, ls.getPointer() - 3, child, 0, 2);
			int len1 = ls.getSize() + len;
			byte[] newsize = EntityUtil.getUnsignedShort(len1);
			int ptr = pointer + len - 1;
			System.arraycopy(child, 0, data, ptr - 1, 2);//child at new pointer
			System.arraycopy(newsize, 0, data, ptr - 3, 2);
			
			byte[] newptr = EntityUtil.getUnsignedShort(ptr);
			if(ls.getParent() == null){
				System.arraycopy(newptr, 0, data, 5, 2);
			}else{
				System.arraycopy(newptr, 0, data, ls.getParent().getPointer() - 1, 2);
			}
			
			ls.setPointer(ptr);
			ls.setSize(len1);
			return;
		}
		
		byte[] child = new byte[2];
		System.arraycopy(data, 5, child, 0, 2);
		byte[] newsize = EntityUtil.getUnsignedShort(len);
		int ptr = pointer + len - 1;
		System.arraycopy(child, 0, data, ptr - 1, 2);//child at new pointer
		System.arraycopy(newsize, 0, data, ptr - 3, 2);
		byte[] newptr = EntityUtil.getUnsignedShort(ptr);
		System.arraycopy(newptr, 0, data, 5, 2);
		CellPointer cp0 = new CellPointer();
		cp0.setPointer(ptr);
		cp0.setSize(len);
		for(CellPointer cp:node.getChain()){
			if(cp.getParent() == null){
				cp.setParent(cp0);
				break;
			}
		}
		node.putChain(cp0);
	}
	
	public void deleteCell(BTreeNode node, int pos) throws ApplicationException{
		Page pg = getPage(node.getPagenum());
		byte[] data = pg.getData();
		int ptr = EntityUtil.unsignedShort(data, 19 + pos * 2);
		//get pointer data size
		int clen = 0;
		byte frag = data[ptr];
		
		if(node.isLeaf()){
			byte kflag = data[ptr + 1];
			byte vflag = data[ptr + 2];
			int keysize, valsize;
			if((kflag & 0x80) == 0){
				keysize = kflag & 0x7f;
			}else{
				keysize = 4;//��Ҫ����overflow
			}
			
			if((vflag & 0x80) == 0){
				valsize = vflag & 0x7f;
			}else{
				valsize = 4;
			}
			clen = keysize + valsize + 3 + frag; 
		}else{
			byte kflag = data[ptr + 5];
			int keysize;
			if((kflag & 0x80) == 0){
				keysize = kflag & 0x7f;
			}else{
				keysize = 4;//��Ҫ����overflow
			}
			clen = keysize + 6 + frag; 
		}
		

		//updata cell number and cell pointers
		int n = node.getCells();
		node.setCells(n - 1);
		byte[] len = EntityUtil.getUnsignedShort(n - 1);
		System.arraycopy(len, 0, pg.getData(), 9, 2);
		System.out.println("n = " + n);
		if(pos < n - 1){
			int psize = (n - pos - 1) * 2;
			System.arraycopy(data, 19 + (pos + 1) * 2, data, 19 + pos * 2, psize);
		}else{
			
		}
		
		mergeFree(node, ptr, clen);
	}
	
//	/**
//	 * �����ظ�key��ɾ�����һ��
//	 * @param node
//	 * @param key
//	 * @return
//	 * @throws ApplicationException
//	 */
//	public int deleteKey(BTreeNode node, String key) throws ApplicationException{
//		int pos = ceiling(node, key);
//		if(pos < 0){
//			return -1;
//		}
//		int cmp = compareKey(node, pos, key);
//		if(cmp != 0){
//			return -1;
//		}
//		
//		deleteCell(node, pos);
//
//		return 0;
//	}
	
	/**
	 * ��internal nodeɾ��key��
	 * 1����¼pos����ȡnode���������á�
	 * 2,���ɾ���ɹ������child is leaf��cells==0��ɾ����ֻ��right pointer.
	 * ���child is internal��cells==0��ɾ�������һ���ڵ�.
	 * �ж�internal node cells is 0��������������leaf node cells is 0��
	 * 3�����node�Ǹ��ڵ���cells==0��ɾ�����ڵ㣬���µĸ��ڵ���child(right pointer node)
	 * @param node
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public int deleteNodeKey(BTreeNode node, String key) throws ApplicationException{
		int pos = ceiling(node, key);
		if(node.isLeaf()){
			if(pos < 0){		
				return -1;
			}else{
				int cmp = compareKey(node, pos, key);
				if(cmp != 0){
					return -1;
				}
				deleteCell(node, pos);
				return 1;
			}
		}
		
		BTreeNode child = getChildNode(node, pos);
		int ret = deleteNodeKey(child, key);
		
		Page pg = getPage(node.getPagenum());
		byte[] data = pg.getData();
		if(ret > 0 && hasZeroCells(child)){
			boolean relink = false;
			if(pos < 0 && node.getCells() > 0){
				int ptr = EntityUtil.unsignedShort(data, 19 + (node.getCells() - 1) * 2);
				System.arraycopy(data, ptr + 1, data, 15, 4);
				deleteCell(node, node.getCells() - 1);
				relink = true;
			}else if(pos >= 0){
				deleteCell(node, pos);
				relink = true;
			}
			
			if(relink){
				//re link leaf node 
			}
			long top = EntityUtil.unsignedInt(meta, 16);
			if(pg.getNumber() == top && node.getCells() == 0){
				BTreeNode only = getChildNode(node, -1);
				while(only.getCells() == 0 && !only.isLeaf()){
					only = getChildNode(node, -1);
				}
				byte[] ptr = EntityUtil.getUnsignedInt(only.getPagenum()); 
				System.arraycopy(ptr, 0, meta, 16, 4);
			}
		}
		return ret;
	}
	
	public boolean hasZeroCells(BTreeNode node)throws ApplicationException{
		BTreeNode n = node;
		while(!n.isLeaf()){
			if(n.getCells() > 0){
				return false;
			}
			n = getChildNode(n, -1);
		}
		return (n.getCells() == 0);
	}
		
	public BTreeNode split(BTreeNode node) throws ApplicationException{
		Page pg = getPage(node.getPagenum());
		byte[] data = pg.getData();
		BTreeNode node1 = newLeafNode();
		Page pg1 = getPage(node1.getPagenum());
		byte[] data1 = pg1.getData();
		long rpage = EntityUtil.unsignedInt(data, 15);
		
		int cnt = node.getCells();
		int mid = cnt >> 1;//0--mid - 1���£�mid--n-1�½��ڵ�.�������������һ��
		int end = 4091;
		int pos = 0;
		
		int remain = cnt - mid;
		//for test
//		int keysize11, ptr11;
//		String key11 = "";
		for(int i = remain;i < cnt;i++){
			int ptr = EntityUtil.unsignedShort(data, 19 + i * 2);
//			byte frag = data[ptr];
			byte kflag = data[ptr + 1];
			byte vflag = data[ptr + 2];
			int keysize, valsize;
			if((kflag & 0x80) == 0){
				keysize = kflag & 0x7f;
			}else{
				keysize = 4;//��Ҫ����overflow
			}

			if((vflag & 0x80) == 0){
				valsize = vflag & 0x7f;
			}else{
				valsize = 4;
			}
			int clen = keysize + valsize + 3;
			int ptr1 = end - clen + 1;
			data1[ptr1] = 0;//����ʡ��
			System.arraycopy(data, ptr + 1, data1, ptr1 + 1, clen - 1);
			byte[] pc = EntityUtil.getUnsignedShort(ptr1);//cell pointer
			System.arraycopy(pc, 0, data1, 19 + pos * 2, 2);
			pos++;
			end = end - clen;
			
			mergeFree(node, ptr, clen);
//			deleteCell(node, i);
		}
		
		//for test
//		String key11 = new String(data, ptr11 + 2, keysize11);
//		System.out.println("key = " + key11 + ", left =" + node.getPagenum() + "right =" + node1.getPagenum());
		System.out.println("left =" + node.getPagenum() + ",right =" + node1.getPagenum());
		
		byte[] cells =  EntityUtil.getUnsignedShort(mid);
		System.arraycopy(cells, 0, data1, 9, 2);

		byte[] ptr = EntityUtil.getUnsignedShort(end);
		System.arraycopy(ptr, 0, data1, 7, 2);
		node1.setCells(mid);
		
		cells = EntityUtil.getUnsignedShort(remain);
		System.arraycopy(cells, 0, data, 9, 2);
		node.setCells(remain);
		System.out.println("remain =" + remain + ",mid =" + mid);
		
		//update left and right page pointer
		System.arraycopy(data, 15, data1, 15, 4);
		byte[] left = EntityUtil.getUnsignedInt(node.getPagenum());
		System.arraycopy(left, 0, data1, 4092, 4);
		byte[] addr = EntityUtil.getUnsignedInt(node1.getPagenum());
		System.arraycopy(addr, 0, data, 15, 4);

		if(rpage != 0){
			byte[] rdata = getPage(rpage).getData();
			System.arraycopy(addr, 0, rdata, 4092, 4);
		}
		
		return node1;
	}
	
	public BTreeNode splitInternal(BTreeNode node) throws ApplicationException{
		Page pg = getPage(node.getPagenum());
		byte[] data = pg.getData();
		BTreeNode node1 = newInternalNode();
//		node1.setLeaf(false);
		Page pg1 = getPage(node1.getPagenum());
		byte[] data1 = pg1.getData();
		data1[4] = 1;
		
		int cnt = node.getCells();
		int mid = (cnt + 1) >> 1;
		int end = 4095;
		int pos = 0;
		/* 0--mid���£�remain--n-1�½��ڵ�.internal���Ѻ�,right pointer���Ƶ��Ҳࡣ
		 * ���Ѻ����internal����mid+1���ڵ㣬mid + 1��pointer��
		 * ������һ��pointer����right�����һ���ڵ�ȡ��
		 * �Ҳ�internal��cnt - mid - 1���ڵ㣬cnt - mid��pointer��
		 */
		int remain = cnt + 1 - mid;

		for(int i = remain;i < cnt;i++){
			int ptr = EntityUtil.unsignedShort(data, 19 + i * 2);
			byte kflag = data[ptr + 5];
			int keysize;
			if((kflag & 0x80) == 0){
				keysize = kflag & 0x7f;
			}else{
				keysize = 4;//��Ҫ����overflow
			}

			int clen = keysize + 6;//no fragment,4 byte pointer + 2 flag
			int ptr1 = end - clen + 1;
			System.arraycopy(data, ptr + 1, data1, ptr1 + 1, clen - 1);
			data[ptr] = 0;
			byte[] pc = EntityUtil.getUnsignedShort(ptr1);//cell pointer
			System.arraycopy(pc, 0, data1, 19 + pos * 2, 2);
			pos++;
			end = end - clen;
			
			mergeFree(node, ptr, clen);
		}
		
		System.out.println("left =" + node.getPagenum() + ",right =" + node1.getPagenum());
		
		//update node1
		byte[] cells =  EntityUtil.getUnsignedShort(cnt - remain);
		System.arraycopy(cells, 0, data1, 9, 2);

		byte[] ptr = EntityUtil.getUnsignedShort(end);
		System.arraycopy(ptr, 0, data1, 7, 2);
		System.arraycopy(data, 15, data1, 15, 4);
		node1.setCells(cnt - remain);
		
		//update node
		cells = EntityUtil.getUnsignedShort(remain - 1);
		System.arraycopy(cells, 0, data, 9, 2);
		node.setCells(remain - 1);
		
		int pointer = EntityUtil.unsignedShort(data, 19 + 2 * (remain - 1));
		System.arraycopy(data, pointer + 1, data, 15, 4);
		return node1;
	}
	/**
	 * �Ƚ�key�����С�ڸ���key������<0
	 * @param node
	 * @param pos
	 * @param key
	 */
	public int compareKey(BTreeNode node, int pos, String key) throws ApplicationException{
		Page pg = getPage(node.getPagenum());
		byte[] data = pg.getData();
		byte[] ptr = new byte[2];
		int pointer = EntityUtil.unsignedShort(pg.getData(), 19 + pos * 2);
		byte flag = 0;
		if(node.isLeaf()){
			flag = data[pointer + 1];
		}else{
			flag = data[pointer + 5];
		}
		if((flag & 0x00) == 0){
			int size = flag & 0x7f;
			String key0 = null;
			if(node.isLeaf()){
				key0 = new String(data, pointer + 3, size, Charset.forName("UTF-8"));
			}else{
				key0 = new String(data, pointer + 6, size, Charset.forName("UTF-8"));
			}
			return key0.compareTo(key);
		}else{
			//�ֲ���ȡ��key���бȽ�
		}
		return -1;
	}
	
	/**
	 * ����node/position key�Ƿ����ָ��key��
	 * @return
	 */
	public boolean containsKey(BTreeNode node, int pos, String key){
		Page pg = null;
		try {
			pg = getPage(node.getPagenum());
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = pg.getData();
//		byte[] ptr = new byte[2];
		int pointer = EntityUtil.unsignedShort(pg.getData(), 19 + pos * 2);
		byte flag = 0;
		if(node.isLeaf()){
			flag = data[pointer + 1];
		}else{
			flag = data[pointer + 5];
		}
		if((flag & 0x00) == 0){
			int size = flag & 0x7f;
			String key0 = null;
			if(node.isLeaf()){
				key0 = new String(data, pointer + 3, size, Charset.forName("UTF-8"));
			}else{
				key0 = new String(data, pointer + 6, size, Charset.forName("UTF-8"));
			}
			if(key0.length() < key.length()){
				return false;
			}
			if(key0.substring(0, key.length()).equals(key)){
				return true;
			}
		}else{
			//�ֲ���ȡ��key���бȽ�
		}
		return false;
	}
	/**
	 * С�ڵ��ڸ���Ԫ�ص����Ԫ�أ����������������Ԫ�أ��򷵻�-1
	 * ��pos=high��ֵС��key���򷵻�ֵ>getCells()
	 * @param node
	 * @param key
	 * @return
	 * @throws ApplicationException
	 */
	public int floor(BTreeNode node, String key) throws ApplicationException{
		int len = node.getCells();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
			if(compareKey(node, high, key) < 0){
				return high + 1;
			}else{//high > e,�����ֶ���������ֵ��ȡ���һ��
				if(compareKey(node, low, key) <= 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						if(compareKey(node, mid, key) <= 0){
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
	 * @throws ApplicationException 
	 */
	public int ceilingTop(BTreeNode node, String key) throws ApplicationException{
		int len = node.getCells();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
			if(compareKey(node, low, key) >= 0){
				return low;
			}else{//low < key,�����ֶ����������ȣ�ȡ��һ��
				if(compareKey(node, high, key) >= 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						if(compareKey(node, mid, key) >= 0){
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
	 * ���ڵ��ڸ���Ԫ�ص���СԪ�أ�������ڶ��ƥ��Ԫ�أ�ȡ���һ����
	 * @param key
	 * @return
	 * @throws ApplicationException 
	 */
	public int ceiling(BTreeNode node, String key) throws ApplicationException{
		int len = node.getCells();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
			if(compareKey(node, low, key) > 0){
				return low;
			}else{//low < key,�����ֶ����������ȣ�ȡ���һ��
				if(compareKey(node, high, key) > 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						if(compareKey(node, mid, key) > 0){
							high = mid;
						}else{
							low = mid;
						}
					}
					return high;
				}else if(compareKey(node, high, key) == 0){
					return high;
				}else{
					return -1;
				}
			}
		}
	}
	
	/**
	 * ����ƥ��Identifier
	 * @param key
	 * @return
	 * @throws ApplicationException 
	 */
	public int ceilingIdentifier(BTreeNode node, String key) throws ApplicationException{
		int len = node.getCells();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0, pos = 0;
			if(compareKey(node, low, key) > 0){
				pos = low;
			}else{//low < key,�����ֶ����������ȣ�ȡ���һ��
				if(compareKey(node, high, key) > 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						if(compareKey(node, mid, key) > 0){
							high = mid;
						}else{
							low = mid;
						}
					}
					pos = high;
				}else if(compareKey(node, high, key) == 0){
					pos = high;
				}else{
					pos = -1;
				}
			}
			if(this.containsKey(node, pos, key)){
				return pos;
			}else{
				return -1;
			}
		}
	}
	
	/**
	 * �Ƚ�key�����С�ڸ���key������<0
	 * @param node
	 * @param pos
	 * @param key
	 */
	public int compareKey(BTreeNode node, int pos, BTreeNode node1, int pos1) throws ApplicationException{
		Page pg = getPage(node.getPagenum());
		byte[] data = pg.getData();
		Page pg1 = getPage(node1.getPagenum());
		byte[] data1 = pg1.getData();

		int pointer1 = EntityUtil.unsignedShort(data1, 19 + pos1 * 2);
		
		byte flag = 0;
		if(node1.isLeaf()){
			flag = data1[pointer1 + 1];
		}else{
			flag = data1[pointer1 + 5];
		}
		if((flag & 0x00) == 0){
			int size = flag & 0x7f;
			String key0 = null;
			if(node1.isLeaf()){
				key0 = new String(data1, pointer1 + 3, size, Charset.forName("UTF-8"));
			}else{
				key0 = new String(data1, pointer1 + 6, size, Charset.forName("UTF-8"));
			}
			return compareKey(node, pos, key0);
		}else{
			//�ֲ���ȡ��key���бȽ�
		}
		return -1;
	}
	
	public int floor(BTreeNode node, BTreeNode node1, int pos1) throws ApplicationException{
		int len = node.getCells();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
			if(compareKey(node, high, node1, pos1) <= 0){
				return high;
			}else{//high > e,�����ֶ���������ֵ��ȡ���һ��
				if(compareKey(node, low, node1, pos1) <= 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						if(compareKey(node, mid, node1, pos1) <= 0){
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
	 * @throws ApplicationException 
	 */
	public int ceiling(BTreeNode node, BTreeNode node1, int pos1) throws ApplicationException{
		int len = node.getCells();
		if(len == 0){
			return -1;
		}else{
			int low = 0;
			int high = len - 1;
			int mid = 0;
			if(compareKey(node, low, node1, pos1) > 0){
				return low;
			}else{//low < key,�����ֶ����������ȣ�ȡ��һ��
				if(compareKey(node, high, node1, pos1) > 0){
					while(low <= high){
						mid = (low + high) >> 1;
						if(mid == low){
							break;
						}
						if(compareKey(node, mid, node1, pos1) > 0){
							high = mid;
						}else{
							low = mid;
						}
					}
					return high;
				}else if(compareKey(node, high, node1, pos1) == 0){
					return high;
				}else{
					return -1;
				}
			}
		}
	}
	/**************IO operate**********************/
	public void flush(){
		writeMeta();
		for(Long num:cache.keySet()){
			Page pg = cache.get(num);
			long pos = (num - 1) * 4096 + 512;
			int cnt = write(pg.getData(), pos);
		}
	}
	
	public void readMeta() throws IOException{
		meta = new byte[512];//todo by direct buffer
		if(size() == 0){
			write(meta, 0);
			return;
		}
		read(meta, 0);
	}
	
	public void writeMeta(){
		write(meta, 0);
	}
}
