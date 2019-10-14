package net.hiwii.system.util;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.system.runtime.RuntimePool;
import net.hiwii.view.Entity;

public class SortUtil {
	private Expression cognition;
	private boolean asc;
	private RuntimePool pool;
	
//	public SortUtil(Expression cognition, boolean asc, RuntimePool pool) {
//		this.cognition = cognition;
//		this.asc = asc;
//		this.pool = pool;
//	}

	public SortUtil(boolean asc) {
		this.asc = asc;
	}

	public Expression getCognition() {
		return cognition;
	}

	public void setCognition(Expression cognition) {
		this.cognition = cognition;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public void quickSort(List<Entity> data,int i,int j){
		if(i >= j){
			return;
		}
        Entity pivot = data.get(j);
        int k = partition(data,i,j,pivot);
        quickSort(data,i,k-1);
        quickSort(data,k+1,j);
        
    }
	
	public void swap(List<Entity> data,int i,int j){
		Entity temp = data.get(i);
		data.set(i, data.get(j));
		data.set(j, temp);
	}

	private int partition(List<Entity> data, int left, int right,Entity pivot) {

		String op0 = "LT", op1 = "GT";
		if(!asc){
			op0 = "GT";
			op1 = "LT";
		}
		int leftPtr = left - 1;
		int rightPtr = right;
		//added
		List<Entity> arg = new ArrayList<Entity>();
		arg.add(pivot);
		while(true){
			while(true){
				Expression ret = data.get(++leftPtr).doFunctionDecision(op0, arg);
				if(ret instanceof JudgmentResult){
					JudgmentResult jr = (JudgmentResult) ret;
					if(!jr.isResult()){
						break;
					}
				}else{
					return -1;
				}
			}
			while(true){
				if(!(rightPtr > 0)){
					break;
				}
				Expression ret = data.get(--rightPtr).doFunctionDecision(op1, arg);
				if(ret instanceof JudgmentResult){
					JudgmentResult jr = (JudgmentResult) ret;
					if(!jr.isResult()){
						break;
					}
				}else{
					return -1;
				}
			}
			if(leftPtr >= rightPtr)
				break;
			else
				swap(data,leftPtr,rightPtr);
		}
		swap(data,leftPtr,right);
		return leftPtr;

//		return -1;
	}
	
//	public Entity toCognition(Entity ent, Expression expr){
//		if(expr == null){
//			return ent;
//		}else{
//			return ent.toCognize(expr, null);
//		}
//	}
	
	public static void Random(List<Entity> list){
		int size = list.size();
		for(Entity en:list){
			int r = (int) (Math.random() * size);
			if(r > 0){
				Entity temp = list.get(0);
				list.set(0, list.get(r));
				list.set(r, temp);
			}
		}
	}

}
