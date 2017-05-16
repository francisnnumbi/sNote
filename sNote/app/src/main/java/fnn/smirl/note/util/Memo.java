package fnn.smirl.note.util;

public class Memo implements Comparable<Memo>
{

 public String header = null, body = null;
 public long dateId; public boolean done;
 
 public Memo(long dateId, String header, String body, boolean done){
	this.header = header;
	this.body = body;
	this.dateId = dateId;
	this.done = done;
 }

 @Override
 public String toString()
 {
	return dateId+"";
 }
 
 public boolean contains(String txt){
	return header.contains(txt) || body.contains(txt);
 }
 
 public boolean copy(Memo memo){
	dateId = memo.dateId;
	header = memo.header;
	body = memo.body;
	done = memo.done;
	return equals(memo);
 }
 
 public static Memo clone(Memo memo){
	return new Memo(memo.dateId, memo.header, memo.body, memo.done);
 }
 
 @Override
 public int compareTo(Memo p1)
 {
	// TODO: Implement this method
	return header.compareToIgnoreCase(p1.header);
 }

 @Override
 public boolean equals(Object o)
 {
	// TODO: Implement this method
	return header.compareToIgnoreCase(((Memo)o).header) == 0;
 }
 
}
