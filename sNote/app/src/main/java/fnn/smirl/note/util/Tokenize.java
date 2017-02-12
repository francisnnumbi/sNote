package fnn.smirl.note.util;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import java.util.regex.Pattern;

public interface Tokenize
{
 // Constant colors
 public static String reservedKeysColor = "#b11d01";
 public static String numberColor = "#1f5900";
 public static String invCommaColor = "#20aeff";
 public static String blockSignColor = "#ff07a9";
 public static String commentBlockColor = "#44ac3b";
 public static String blockNameColor = "#2620ff";
 public static String primitiveTypeColor = "#b16600";
 
 
 public static Pattern primitiveType = Pattern.compile("\\b(void|boolean|char|int|float|long|double)\\b");
 public static Pattern reservedKeys = Pattern.compile("\\b(public|class|interface|enum|private|static|final|protected|null|true|false)\\b");
 public static Pattern end = Pattern.compile("\\z");
 public static Pattern space = Pattern.compile("\\s");
 public static Pattern number = Pattern.compile("[.|\\s]*?[0-9]+");
 public static Pattern invComma = Pattern.compile("\"([^\"]*)\"");
 public static Pattern blockSign = Pattern.compile("[=<>\\p{Punct}&&[^.?-]]");
 public static Pattern commentBlock = Pattern.compile("((//).+)|((?s)(/\\*([^*]|[\\s]|(\\*+([^*/]|[\\s])))*\\*+/))");
 public static Pattern blockName = Pattern.compile("(\\.|\\s+)?(\\w+)(\\s+)?[\\{|\\(|\\[]");
}
