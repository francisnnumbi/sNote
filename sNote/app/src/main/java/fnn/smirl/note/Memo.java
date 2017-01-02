package fnn.smirl.note;

public class Memo implements Comparable<Memo>
{

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
 
 

 private String header = null, body = null;
 
 public Memo(String header, String body){
	this.header = header;
	this.body = body;
 };

 public void setHeader(String header)
 {
	this.header = header;
 }

 public String getHeader()
 {
	return header;
 }

 public void setBody(String body)
 {
	this.body = body;
 }

 public String getBody()
 {
	return body;
 }

 @Override
 public String toString()
 {
	// TODO: Implement this method
	return header;
 }
 
 
}
