package fnn.smirl.note.util;
import java.util.regex.*;
import android.text.*;
import android.text.style.*;
import android.graphics.*;
import java.util.*;

public class EditorUtils implements Tokenize {

	public static void formatEditor(Editable ssb) {
		Matcher mat0 = NUMBER.matcher(ssb);
		Matcher mat1 = BLOCK_NAME.matcher(ssb);
		Matcher mat2 = RESERVED_KEYS.matcher(ssb);
		Matcher mat3 = PRIMITIVE_TYPE.matcher(ssb);
		Matcher mat4 = BLOCK_SIGN.matcher(ssb);
		Matcher mat5 = INV_COMMA.matcher(ssb);
		Matcher mat6 = COMMENT_BLOCK.matcher(ssb);
		//	while (!mat.hitEnd()) {
//			mat.usePattern(SPACE);
//			mat.find();
		StyleSpan[] ss = ssb.getSpans(0, ssb.toString().length(), StyleSpan.class);
		for (StyleSpan as : ss)ssb.removeSpan(as);

		ForegroundColorSpan[] dss = ssb.getSpans(0, ssb.toString().length(), ForegroundColorSpan.class);
		for (ForegroundColorSpan as : dss)ssb.removeSpan(as);

		//mat.usePattern(NUMBER);
		while (mat0.find()) {
			ssb.setSpan(new StyleSpan(Typeface.BOLD),
			mat0.start(), mat0.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
	//	mat.usePattern(BLOCK_NAME);
		while (mat1.find()) {
			ssb.setSpan(new StyleSpan(Typeface.BOLD),
			mat1.start(), mat1.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor(BLOCK_NAME_COLOR)),
			mat1.start(), mat1.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);								

		}

		//mat.usePattern(RESERVED_KEYS);
		while (mat2.find()) {
			ssb.setSpan(new StyleSpan(Typeface.BOLD),
			mat2.start(), mat2.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor(RESERVED_KEYS_COLOR)),
			mat2.start(), mat2.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);								

		}
		//mat.usePattern(PRIMITIVE_TYPE);
		while (mat3.find()) {
			ssb.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
			mat3.start(), mat3.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor(PRIMITIVE_TYPE_COLOR)),
			mat3.start(), mat3.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);								

		}


		//mat.usePattern(BLOCK_SIGN);
		while (mat4.find()) {
			ssb.setSpan(new StyleSpan(Typeface.BOLD),
			mat4.start(), mat4.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor(BLOCK_SIGN_COLOR)),
			mat4.start(), mat4.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);						

		}

	//	mat.usePattern(INV_COMMA);
		while (mat5.find()) {
			ssb.setSpan(new StyleSpan(Typeface.ITALIC),
			mat5.start() + 1, mat5.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor(INV_COMMA_COLOR)),
			mat5.start() + 1, mat5.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);						


		}
	//	mat.usePattern(COMMENT_BLOCK);
		while (mat6.find()) {
			ssb.setSpan(new StyleSpan(Typeface.ITALIC),
			mat6.start(), mat6.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor(COMMENT_BLOCK_COLOR)),
			mat6.start(), mat6.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);						

		}

//			mat.usePattern(END);
//			if(mat.find()) {
//			}
//		}
	}
}
