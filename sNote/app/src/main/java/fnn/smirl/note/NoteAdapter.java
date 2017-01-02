package fnn.smirl.note;

import android.content.*;
import android.view.*;
import android.view.ContextMenu;
import android.widget.*;
import android.graphics.Color;
import android.view.View.OnClickListener;
import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Memo>
{

 public NoteAdapter(Context ctxt, ArrayList<Memo> memos){
	super(ctxt, 0, memos);
 }

 @Override
 public View getView(final int position, View convertView, ViewGroup parent)
 {
	// TODO: Implement this method
	final Memo memo = getItem(position);
	if (convertView == null){
	 convertView = LayoutInflater.from(getContext())
		.inflate(R.layout.list_template, parent, false);
	}

	TextView tv_header = (TextView)convertView.findViewById(R.id.tv_header);
	tv_header.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View p1)
		{
		 // TODO: Implement this method
		 edit(memo);
		} 
	 });
	TextView tv_body = (TextView)convertView.findViewById(R.id.tv_body);

	if (position % 2 == 0){
	 convertView.setBackgroundColor(Color.WHITE);
	 tv_header.setTextColor(Color.DKGRAY);
	 tv_body.setTextColor(Color.GRAY);
	}else{
	 convertView.setBackgroundColor(Color.GRAY);
	 tv_header.setTextColor(Color.WHITE);
	 tv_body.setTextColor(Color.WHITE);
	}

	tv_header.setText(position + 1 + ". " + memo.getHeader());
	tv_body.setText(memo.getBody());

	return convertView;
 }

 private void edit(Memo memo){
	Intent intent = new Intent(getContext(), NoteActivity.class);
	intent.putExtra(getContext().getResources().getString(R.string.header), memo.getHeader());
	intent.putExtra(getContext().getResources().getString(R.string.body), memo.getBody());
	getContext().startActivity(intent);
 }

}
