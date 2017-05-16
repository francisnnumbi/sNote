package fnn.smirl.note.util;
import java.util.ArrayList;

public class Memos {
 public ArrayList<Memo> list 
 = new ArrayList<Memo>();
 public ArrayList<Memo> subList 
 = new ArrayList<Memo>();

 public boolean add(Memo memo) {
	if (!list.contains(memo)) {
	 return list.add(memo);
	} else {
	 memo.header = memo.header + "(" + memo.dateId + ")";
	 return list.add(memo);
	}
 }

 public Memo get(long dateId) {
	for (int i = 0; i < list.size(); i++) {
	 Memo m = list.get(i);
	 if (m.dateId == dateId)return m;
	}
	return null;
 }

 public boolean replace(Memo oldMemo, Memo newMemo) {
	if (list.contains(oldMemo)) {
	 list.remove(oldMemo);
	 return list.add(newMemo);
	}
	return false;

 }

 public void subList(String criteria) {
	subList = new ArrayList<Memo>();
	for (int i =0; i < list.size(); i++) {
	 if (list.get(i).contains(criteria)) {
		subList.add(list.get(i));
	 }
	}
 }


 public boolean remove(Memo memo) {
	for (int i = 0; i < list.size(); i++) {
	 if (list.get(i).dateId == memo.dateId) {
	  list.remove(i);
		return true;
	 } 
	}
	return false;
 }

}
