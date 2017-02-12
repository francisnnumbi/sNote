package fnn.smirl.note.util;
import android.content.*;
import android.app.*;
import fnn.smirl.note.R;
import android.os.Bundle;
import android.text.Html;

public class Sharing
{
	
	
	public static boolean share(Activity context, String title, String editor){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/*");
		
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
	 intent.putExtra(Intent.EXTRA_TITLE, title);
		intent.putExtra(Intent.EXTRA_TEXT, editor);
		
		context.startActivity(
		Intent.createChooser(intent, context.getResources().getString(R.string.sharing_statement)));
		return true;
	}
}
