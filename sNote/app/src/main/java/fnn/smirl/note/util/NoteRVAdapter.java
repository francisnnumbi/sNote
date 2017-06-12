package fnn.smirl.note.util;

import android.view.*;
import android.widget.*;
import fnn.smirl.note.*;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatTextView;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import android.support.v7.widget.*;
import android.graphics.*;
import android.view.View.*;
import android.widget.ActionMenuView.*;

public class NoteRVAdapter extends RecyclerView.Adapter<NoteRVAdapter.MemoHolder> {

	private Memo _memo;
	private Context ctx;

	public NoteRVAdapter(Context ctx) {
		this.ctx = ctx;

	 }

	@Override
	public NoteRVAdapter.MemoHolder onCreateViewHolder(ViewGroup p1, int p2) {
		// TODO: Implement this method
		View v = LayoutInflater.from(ctx)
		 .inflate(R.layout.list_template, p1, false);

		return new MemoHolder(v);
	 }

	@Override
	public void onBindViewHolder(NoteRVAdapter.MemoHolder p1, int p2) {
		// TODO: Implement this method
		Memo m = MainActivity.memos.subList.get(p2);
		p1. tv_header.setText((p2 + 1) + " : " + m.header);
		p1.tv_body.setText(m.body);
		p1.cb_done.setChecked(m.done);
	 }

	@Override
	public int getItemCount() {
		// TODO: Implement this method
		return MainActivity.memos.subList.size();
	 }

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		// TODO: Implement this method
		super.onAttachedToRecyclerView(recyclerView);
	 }

	private void edit(Memo memo) {
		Intent intent = new Intent(MainActivity.ACTIVITY, NoteActivity.class);
		intent.putExtra("new", false);
		intent.putExtra("date_id", memo.dateId);
		MainActivity.ACTIVITY.startActivity(intent);
	 }

	public void save_changes(Memo memo) {
		MainActivity.addRecord(memo);
	 }


	class MemoHolder extends RecyclerView.ViewHolder {
		CardView cv;
		AppCompatTextView tv_header, tv_body;
		CheckBox cb_done;

		public MemoHolder(final View v) {
			super(v);
			cv = (CardView)v.findViewById(R.id.note_cv);
			cv.setShadowPadding(0, 0, 10, 10);
			cv.setCardElevation(10);
			cv.setCardBackgroundColor(Color.LTGRAY);

			tv_header = (AppCompatTextView)v.findViewById(R.id.tv_header);
			tv_body = (AppCompatTextView)v.findViewById(R.id.tv_body);

			cb_done = (CheckBox)v.findViewById(R.id.cb_done);

			cb_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
				 @Override
				 public void onCheckedChanged(CompoundButton p1, boolean p2) {
					 try {
						 _memo = MainActivity.memos.subList.get(getAdapterPosition());
						 _memo.done = p2;
						 MainActivity.store();
						} catch (Exception e) {}
					 // MainActivity.replaceRecord(mm, memo);
					}
				});

			tv_header.setOnClickListener(new OnClickListener(){

				 @Override
				 public void onClick(View p1) {
					 // TODO: Implement this method
					 _memo = MainActivity.memos.subList.get(getAdapterPosition());
					 edit(_memo);
					} 
				});
			//
			v.setOnLongClickListener(new OnLongClickListener(){

				 @Override
				 public boolean onLongClick(View p1) {
					 // TODO: Implement this method
					 showPop(p1, getAdapterPosition());
					 return false;
					}
				});
		 }

		private void showPop(View v, final int position) {
			final  android.widget.PopupMenu pop = new android.widget.PopupMenu(ctx, v);
			pop.inflate(R.menu.popup_menu);
			pop.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener(){

				 @Override
				 public boolean onMenuItemClick(MenuItem p1) {
					 // TODO: Implement this method
					 switch (p1.getItemId()) {
						 case R.id.pop_up_menu_new:
							MainActivity.newMemo(MainActivity.ACTIVITY);
							pop.dismiss();
							break;
						 case R.id.pop_up_menu_edit:
							_memo = MainActivity.memos.subList.get(position);
							edit(_memo);
							pop.dismiss();
							break;
						 case R.id.pop_up_menu_delete:
							_memo = MainActivity.memos.subList.get(position);
							MainActivity.deleteRecord(_memo);
							pop.dismiss();
							break;	
						}
					 return false;
					}
				});
			pop.show();
		 }
	 }
 }
