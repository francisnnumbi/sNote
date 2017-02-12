package fnn.smirl.note.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fnn.smirl.note.NoteActivity;
import fnn.smirl.note.R;
import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Memo> {

 public NoteAdapter(Context ctxt, ArrayList<Memo> memos) {
	super(ctxt, 0, memos);
 }

 @Override
 public View getView(final int position, View convertView, ViewGroup parent) {
	// TODO: Implement this method
	final Memo memo = getItem(position);
	if (convertView == null) {
	 convertView = LayoutInflater.from(getContext())
		.inflate(R.layout.list_template, parent, false);
	}

	TextView tv_header = (TextView)convertView.findViewById(R.id.tv_header);
	tv_header.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View p1) {
		 // TODO: Implement this method
		 edit(memo);
		} 
	 });
	TextView tv_body = (TextView)convertView.findViewById(R.id.tv_body);
	convertView.setBackgroundColor(Color.WHITE);
	tv_header.setTextColor(Color.parseColor("#FF4A494C"));
	tv_body.setTextColor(Color.GRAY);

	tv_header.setText(position + 1 + ". " + memo.getHeader());
	tv_body.setText(memo.getBody());

	return convertView;
 }

 private void edit(Memo memo) {
	Intent intent = new Intent(getContext(), NoteActivity.class);
	intent.putExtra(getContext().getResources().getString(R.string.header), memo.getHeader());
	intent.putExtra(getContext().getResources().getString(R.string.body), memo.getBody());
	getContext().startActivity(intent);
 }

}
