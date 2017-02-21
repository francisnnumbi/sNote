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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import fnn.smirl.note.MainActivity;
import android.content.res.Resources;
import android.widget.Toast;

public class NoteAdapter extends ArrayAdapter<Memo> {
 private Context ctx;
 private Resources res;

 public NoteAdapter(Context ctxt, ArrayList<Memo> memos) {
	super(ctxt, 0, memos);
	this.ctx = ctxt;
	res = ctxt.getResources();

 }

 @Override
 public View getView(final int position, View convertView, ViewGroup parent) {
	// TODO: Implement this method
	final Memo memo = getItem(position);

	if (convertView == null) {
	 convertView = LayoutInflater.from(ctx)
		.inflate(R.layout.list_template, parent, false);
	}

	TextView tv_header = (TextView)convertView.findViewById(R.id.tv_header);
	TextView tv_body = (TextView)convertView.findViewById(R.id.tv_body);
	convertView.setBackgroundColor(Color.WHITE);
	tv_header.setTextColor(Color.parseColor("#FF4A494C"));
	tv_body.setTextColor(Color.GRAY);

	tv_header.setText(position + 1 + ". " + memo.header);
	tv_body.setText(memo.body);

	CheckBox cb_done = (CheckBox)convertView.findViewById(R.id.cb_done);
	cb_done.setChecked(memo.done);
//	if(memo.done){
//	cb_done.setBackgroundDrawable(res.getDrawable(
//	android.R.drawable.checkbox_on_background));
//	}else{
//	 cb_done.setBackgroundDrawable(res.getDrawable(
//													android.R.drawable.checkbox_off_background));
//	}
	
	cb_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton p1, boolean p2) {
		 Memo mm = Memo.clone(memo);
		 memo.done = p2;
		 MainActivity.replaceRecord(memo, mm);
		}
	 });

	tv_header.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View p1) {


		 Toast.makeText(ctx, memo.toString(), Toast.LENGTH_SHORT)
			.show();
		 // TODO: Implement this method
		 edit(memo);
		} 
	 });
	this.setNotifyOnChange(true);
	return convertView;
 }



 private void edit(Memo memo) {
	Intent intent = new Intent(MainActivity.ACTIVITY, NoteActivity.class);
	intent.putExtra(res.getString(R.string.header), memo.header);
	intent.putExtra(res.getString(R.string.body), memo.body);
	intent.putExtra("done", memo.done);
	intent.putExtra("date_id", memo.dateId);
	MainActivity.ACTIVITY.startActivity(intent);
 }

 public void save_changes(Memo memo) {
	MainActivity.addRecord(memo);
 }

}
