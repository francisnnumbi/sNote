package fnn.smirl.note;
import android.os.*;
import android.text.*;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import fnn.smirl.note.util.Memo;
import fnn.smirl.note.util.Tokenize;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.text.style.ParagraphStyle;
import android.text.style.LeadingMarginSpan;
import fnn.smirl.note.util.Sharing;
import android.widget.TextView.BufferType;

public class NoteActivity extends Activity {

 String tempHeader = "";
 Pattern pattern = Pattern.compile("");
 Handler handler;
 EditText note_title = null;
 EditText note_body = null;
 Spannable spannable;

 private boolean runFormat = true;

 ClipboardManager mClipboard;
 ClipData mClip;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.note_view);
	Bundle bundle = getIntent().getExtras();
	String header = bundle.getString(getResources().getString(R.string.header));
	tempHeader = header;
	String body = bundle.getString(getResources().getString(R.string.body));
	note_title = (EditText)findViewById(R.id.note_title);
	note_title.setText(header);
	note_body = (EditText)findViewById(R.id.note_body);
	note_body.setText(body);

	mClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

	note_body.addTextChangedListener(new TextWatcher(){

		@Override
		public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
		 // TODO: Implement this method
		// runFormat = true;
		 
		}

		@Override
		public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
		 // TODO: Implement this method

		}

		@Override
		public void afterTextChanged(Editable p1) {
		 // TODO: Implement this method
		 
		 runFormat = true;
		}

	 });

 }

 @Override
 protected void onPostCreate(Bundle savedInstanceState) {
	// TODO: Implement this method
	super.onPostCreate(savedInstanceState);

	handler = new Handler(Looper.getMainLooper());
	handler.postDelayed(new Runnable(){

		@Override
		public void run() {
		 // TODO: Implement this method
		 synchronized (note_body) {
			if (runFormat) {
			 int pos1 = note_body.getSelectionStart();
			 int pos2 = note_body.getSelectionEnd();
			 refreshFormat();
			 note_body.setSelection(pos1, pos2);
			 runFormat = false;
			}
			handler.postDelayed(this, 1000);
		 } 
		}
	 }, 100);

 }

 @Override
 public void onBackPressed() {
	cancel_changes();
 }


 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.n_a_menu, menu);
	return true;
 }

 @Override
 public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	 case R.id.item_select_all:
		note_body.selectAll();
		break;
	 case R.id.item_copy:
		mClip = ClipData.newPlainText("text", 
																	note_body.getEditableText()
																	.subSequence(note_body.getSelectionStart(), note_body.getSelectionEnd()));
		mClipboard.setPrimaryClip(mClip);
		break;
	 case R.id.item_share:
		// here
		spannable = new SpannableString(note_body.getText().toString());
		Sharing.share(this, note_title.getText().toString(), Html.toHtml(spannable));
		// end here
		break;
	 case R.id.item_save:
		save_changes();
		break;
	 case R.id.item_delete:
		delete_memo();
		break;
	 case R.id.item_paste:
		mClip = mClipboard.getPrimaryClip();
		ClipData.Item ite = mClip.getItemAt(0);
		note_body.getEditableText()
		 .insert(note_body.getSelectionStart(), ite.getText());
		break;
	}
	return true;
 }

 public void save_changes() {
	if (tempHeader != null) {
	 MainActivity.deleteRecord(tempHeader);
	}
	String df = note_title.getText().toString();
	if (df == "" || df == " ") df = tempHeader;
	if (df != "") {
	 Memo memo = new Memo(note_title.getText().toString(),
												note_body.getText().toString());
	 MainActivity.addRecord(memo);
	 Toast.makeText(this, getResources().getString(R.string.notif_memo_saved),
									Toast.LENGTH_LONG)
		.show();
	} else {
	 Toast.makeText(this, getResources().getString(R.string.notif_memo_not_saved),
									Toast.LENGTH_LONG)
		.show();
	}
 }

 public void delete_memo() {
	MainActivity.deleteRecord(note_title.getText().toString());
	Toast.makeText(this, getResources().getString(R.string.notif_memo_deleted),
								 Toast.LENGTH_LONG)
	 .show();
	finish();
 }

 public void cancel_changes() {
	finish();
 }

 private void refreshFormat() {
//	SpannableStringBuilder ssb = new SpannableStringBuilder();
//	ssb.append(note_body.getText().toString());
	spannable = new SpannableString(note_body.getText().toString());
	Matcher matcher = Tokenize.end.matcher(spannable);
	formatEditor(matcher, spannable);

 }

 private void formatEditor(Matcher mat, Spannable ssb) {

	while (mat.find()) {
	 mat.usePattern(Tokenize.space);
	 mat.find();

	 mat.usePattern(Tokenize.number);
	 while (mat.find()) {
		ssb.setSpan(new StyleSpan(Typeface.BOLD),
								mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

	 }

	 mat.usePattern(Tokenize.blockName);
	 while (mat.find()) {
		ssb.setSpan(new StyleSpan(Typeface.BOLD),
								mat.start(), mat.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor(Tokenize.blockNameColor)),
								mat.start(), mat.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);								

	 }

	 mat.usePattern(Tokenize.reservedKeys);
	 while (mat.find()) {
		ssb.setSpan(new StyleSpan(Typeface.BOLD),
								mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor(Tokenize.reservedKeysColor)),
								mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);								

	 }
	 mat.usePattern(Tokenize.primitiveType);
	 while (mat.find()) {
		ssb.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
								mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor(Tokenize.primitiveTypeColor)),
								mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);								

	 }


	 mat.usePattern(Tokenize.blockSign);
	 while (mat.find()) {
		ssb.setSpan(new StyleSpan(Typeface.BOLD),
								mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor(Tokenize.blockSignColor)),
								mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);						

	 }

	 mat.usePattern(Tokenize.invComma);
	 while (mat.find()) {
		ssb.setSpan(new StyleSpan(Typeface.ITALIC),
								mat.start() + 1, mat.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor(Tokenize.invCommaColor)),
								mat.start() + 1, mat.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);						


	 }
	 mat.usePattern(Tokenize.commentBlock);
	 while (mat.find()) {
		ssb.setSpan(new StyleSpan(Typeface.ITALIC),
								mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor(Tokenize.commentBlockColor)),
								mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);						

	 }

	 mat.usePattern(Tokenize.end);
	 if (mat.find()) {
		note_body.setText(ssb, BufferType.SPANNABLE);
		break;
	 }
	}
 }
}
