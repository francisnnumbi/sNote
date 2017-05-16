package fnn.smirl.note.util;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import java.util.regex.Pattern;

public interface Tokenize
{
 // Constant colors
 public static String RESERVED_KEYS_COLOR = "#03a9f4";
 public static String NUMBER_COLOR = "#1f5900";
 public static String INV_COMMA_COLOR = "#FF00FF78";
 public static String BLOCK_SIGN_COLOR = "#ff07a9";
 public static String COMMENT_BLOCK_COLOR = "#44ac3b";
 public static String BLOCK_NAME_COLOR = "#2620ff";
 public static String PRIMITIVE_TYPE_COLOR = "#b16600";
 
 // Patterns
 public static Pattern PRIMITIVE_TYPE = Pattern.compile("\\b(void|boolean|char|int|float|long|double)\\b");
 public static Pattern RESERVED_KEYS = Pattern.compile("\\b(public|class|interface|enum|private|static|final|protected|null|true|false)\\b");
 public static Pattern END = Pattern.compile("\\z");
 public static Pattern SPACE = Pattern.compile("\\s");
 public static Pattern NUMBER = Pattern.compile("[.|\\s]*?[0-9]+");
 public static Pattern INV_COMMA = Pattern.compile("\"([^\"]*)\"");
 public static Pattern BLOCK_SIGN = Pattern.compile("[=<>\\p{Punct}&&[^.?-]]");
 public static Pattern COMMENT_BLOCK = Pattern.compile("((//).+)|((?s)(/\\*([^*]|[\\s]|(\\*+([^*/]|[\\s])))*\\*+/))");
 public static Pattern BLOCK_NAME = Pattern.compile("(\\.|\\s+)?(\\w+)(\\s+)?[\\{|\\(|\\[]");
}
