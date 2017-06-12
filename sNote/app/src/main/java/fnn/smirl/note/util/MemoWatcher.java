package fnn.smirl.note.util;
import android.text.*;
import android.text.style.*;

public class MemoWatcher implements TextWatcher
{
	private Editable editable = null;
	private String currText = "";

	@Override
	public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
		// TODO: Implement this method
	}

	@Override
	public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
		// TODO: Implement this method
	}

	@Override
	public void afterTextChanged(Editable p1) {
		// TODO: Implement this method
		editable = p1;
		String _text = editable.toString();
		if(currText.equals(_text)){
			return;
		}else{
			currText = _text;
		}
				
		// do the highlighting now
		EditorUtils.formatEditor(editable);
	}

}
