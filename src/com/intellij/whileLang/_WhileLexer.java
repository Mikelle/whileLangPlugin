/* The following code was generated by JFlex 1.4.3 on 01.11.15 17:02 */

package com.intellij.whileLang;
import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static com.intellij.whileLang.psi.WhileTypes.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 01.11.15 17:02 from the specification file
 * <tt>C:/Users/Mikhail/Desktop/whileLangPlugin/src/com/intellij/whileLang/_WhileLexer.flex</tt>
 */
public class _WhileLexer implements FlexLexer {
  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\1\1\1\1\0\1\1\1\1\22\0\1\1\13\0\1\12"+
    "\1\0\1\3\1\0\12\2\1\4\1\11\1\0\1\5\3\0\2\10"+
    "\1\34\1\10\1\37\7\10\1\36\1\40\1\35\4\10\1\41\6\10"+
    "\4\0\1\7\1\0\1\20\1\10\1\31\1\21\1\17\1\25\1\10"+
    "\1\22\1\15\1\10\1\32\1\23\1\10\1\26\1\24\1\30\1\10"+
    "\1\14\1\27\1\16\1\33\1\10\1\13\3\10\57\0\1\6\12\0"+
    "\1\6\4\0\1\6\5\0\27\6\1\0\37\6\1\0\u01ca\6\4\0"+
    "\14\6\16\0\5\6\7\0\1\6\1\0\1\6\201\0\5\6\1\0"+
    "\2\6\2\0\4\6\10\0\1\6\1\0\3\6\1\0\1\6\1\0"+
    "\24\6\1\0\123\6\1\0\213\6\10\0\236\6\11\0\46\6\2\0"+
    "\1\6\7\0\47\6\110\0\33\6\5\0\3\6\55\0\53\6\43\0"+
    "\2\6\1\0\143\6\1\0\1\6\17\0\2\6\7\0\2\6\12\0"+
    "\3\6\2\0\1\6\20\0\1\6\1\0\36\6\35\0\131\6\13\0"+
    "\1\6\30\0\41\6\11\0\2\6\4\0\1\6\5\0\26\6\4\0"+
    "\1\6\11\0\1\6\3\0\1\6\27\0\31\6\107\0\1\6\1\0"+
    "\13\6\127\0\66\6\3\0\1\6\22\0\1\6\7\0\12\6\17\0"+
    "\7\6\1\0\7\6\5\0\10\6\2\0\2\6\2\0\26\6\1\0"+
    "\7\6\1\0\1\6\3\0\4\6\3\0\1\6\20\0\1\6\15\0"+
    "\2\6\1\0\3\6\16\0\2\6\23\0\6\6\4\0\2\6\2\0"+
    "\26\6\1\0\7\6\1\0\2\6\1\0\2\6\1\0\2\6\37\0"+
    "\4\6\1\0\1\6\23\0\3\6\20\0\11\6\1\0\3\6\1\0"+
    "\26\6\1\0\7\6\1\0\2\6\1\0\5\6\3\0\1\6\22\0"+
    "\1\6\17\0\2\6\43\0\10\6\2\0\2\6\2\0\26\6\1\0"+
    "\7\6\1\0\2\6\1\0\5\6\3\0\1\6\36\0\2\6\1\0"+
    "\3\6\17\0\1\6\21\0\1\6\1\0\6\6\3\0\3\6\1\0"+
    "\4\6\3\0\2\6\1\0\1\6\1\0\2\6\3\0\2\6\3\0"+
    "\3\6\3\0\14\6\26\0\1\6\64\0\10\6\1\0\3\6\1\0"+
    "\27\6\1\0\12\6\1\0\5\6\3\0\1\6\32\0\2\6\6\0"+
    "\2\6\43\0\10\6\1\0\3\6\1\0\27\6\1\0\12\6\1\0"+
    "\5\6\3\0\1\6\40\0\1\6\1\0\2\6\17\0\2\6\22\0"+
    "\10\6\1\0\3\6\1\0\51\6\2\0\1\6\20\0\1\6\21\0"+
    "\2\6\30\0\6\6\5\0\22\6\3\0\30\6\1\0\11\6\1\0"+
    "\1\6\2\0\7\6\72\0\60\6\1\0\2\6\14\0\7\6\72\0"+
    "\2\6\1\0\1\6\2\0\2\6\1\0\1\6\2\0\1\6\6\0"+
    "\4\6\1\0\7\6\1\0\3\6\1\0\1\6\1\0\1\6\2\0"+
    "\2\6\1\0\4\6\1\0\2\6\11\0\1\6\2\0\5\6\1\0"+
    "\1\6\25\0\4\6\40\0\1\6\77\0\10\6\1\0\44\6\33\0"+
    "\5\6\163\0\53\6\24\0\1\6\20\0\6\6\4\0\4\6\3\0"+
    "\1\6\3\0\2\6\7\0\3\6\4\0\15\6\14\0\1\6\21\0"+
    "\46\6\1\0\1\6\5\0\1\6\2\0\53\6\1\0\u014d\6\1\0"+
    "\4\6\2\0\7\6\1\0\1\6\1\0\4\6\2\0\51\6\1\0"+
    "\4\6\2\0\41\6\1\0\4\6\2\0\7\6\1\0\1\6\1\0"+
    "\4\6\2\0\17\6\1\0\71\6\1\0\4\6\2\0\103\6\45\0"+
    "\20\6\20\0\125\6\14\0\u026c\6\2\0\21\6\1\0\32\6\5\0"+
    "\113\6\25\0\15\6\1\0\4\6\16\0\22\6\16\0\22\6\16\0"+
    "\15\6\1\0\3\6\17\0\64\6\43\0\1\6\4\0\1\6\103\0"+
    "\130\6\10\0\51\6\1\0\1\6\5\0\106\6\12\0\35\6\63\0"+
    "\36\6\2\0\5\6\13\0\54\6\25\0\7\6\70\0\27\6\11\0"+
    "\65\6\122\0\1\6\135\0\57\6\21\0\7\6\67\0\36\6\15\0"+
    "\2\6\12\0\54\6\32\0\44\6\51\0\3\6\12\0\44\6\153\0"+
    "\4\6\1\0\4\6\3\0\2\6\11\0\300\6\100\0\u0116\6\2\0"+
    "\6\6\2\0\46\6\2\0\6\6\2\0\10\6\1\0\1\6\1\0"+
    "\1\6\1\0\1\6\1\0\37\6\2\0\65\6\1\0\7\6\1\0"+
    "\1\6\3\0\3\6\1\0\7\6\3\0\4\6\2\0\6\6\4\0"+
    "\15\6\5\0\3\6\1\0\7\6\164\0\1\6\15\0\1\6\20\0"+
    "\15\6\145\0\1\6\4\0\1\6\2\0\12\6\1\0\1\6\3\0"+
    "\5\6\6\0\1\6\1\0\1\6\1\0\1\6\1\0\4\6\1\0"+
    "\13\6\2\0\4\6\5\0\5\6\4\0\1\6\64\0\2\6\u0a7b\0"+
    "\57\6\1\0\57\6\1\0\205\6\6\0\4\6\3\0\2\6\14\0"+
    "\46\6\1\0\1\6\5\0\1\6\2\0\70\6\7\0\1\6\20\0"+
    "\27\6\11\0\7\6\1\0\7\6\1\0\7\6\1\0\7\6\1\0"+
    "\7\6\1\0\7\6\1\0\7\6\1\0\7\6\120\0\1\6\u01d5\0"+
    "\2\6\52\0\5\6\5\0\2\6\4\0\126\6\6\0\3\6\1\0"+
    "\132\6\1\0\4\6\5\0\51\6\3\0\136\6\21\0\33\6\65\0"+
    "\20\6\u0200\0\u19b6\6\112\0\u51cd\6\63\0\u048d\6\103\0\56\6\2\0"+
    "\u010d\6\3\0\20\6\12\0\2\6\24\0\57\6\20\0\31\6\10\0"+
    "\106\6\61\0\11\6\2\0\147\6\2\0\4\6\1\0\4\6\14\0"+
    "\13\6\115\0\12\6\1\0\3\6\1\0\4\6\1\0\27\6\35\0"+
    "\64\6\16\0\62\6\76\0\6\6\3\0\1\6\16\0\34\6\12\0"+
    "\27\6\31\0\35\6\7\0\57\6\34\0\1\6\60\0\51\6\27\0"+
    "\3\6\1\0\10\6\24\0\27\6\3\0\1\6\5\0\60\6\1\0"+
    "\1\6\3\0\2\6\2\0\5\6\2\0\1\6\1\0\1\6\30\0"+
    "\3\6\2\0\13\6\7\0\3\6\14\0\6\6\2\0\6\6\2\0"+
    "\6\6\11\0\7\6\1\0\7\6\221\0\43\6\35\0\u2ba4\6\14\0"+
    "\27\6\4\0\61\6\u2104\0\u016e\6\2\0\152\6\46\0\7\6\14\0"+
    "\5\6\5\0\1\6\1\0\12\6\1\0\15\6\1\0\5\6\1\0"+
    "\1\6\1\0\2\6\1\0\2\6\1\0\154\6\41\0\u016b\6\22\0"+
    "\100\6\2\0\66\6\50\0\14\6\164\0\5\6\1\0\207\6\44\0"+
    "\32\6\6\0\32\6\13\0\131\6\3\0\6\6\2\0\6\6\2\0"+
    "\6\6\2\0\3\6\43\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\1\1\4\1\5\1\6"+
    "\15\4\1\3\1\7\3\4\1\10\5\4\1\11\1\12"+
    "\1\13\1\14\14\4\1\15\1\4\1\16\5\4\1\17"+
    "\1\20\1\21\1\22\1\23\1\4\1\24\1\25\1\4"+
    "\1\26\1\27\1\30\2\4\1\31";

  private static int [] zzUnpackAction() {
    int [] result = new int[71];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\42\0\104\0\146\0\210\0\252\0\42\0\42"+
    "\0\314\0\356\0\u0110\0\u0132\0\u0154\0\u0176\0\u0198\0\u01ba"+
    "\0\u01dc\0\u01fe\0\u0220\0\u0242\0\u0264\0\u0286\0\42\0\u02a8"+
    "\0\u02ca\0\u02ec\0\252\0\u030e\0\u0330\0\u0352\0\u0374\0\u0396"+
    "\0\252\0\252\0\252\0\252\0\u03b8\0\u03da\0\u03fc\0\u041e"+
    "\0\u0440\0\u0462\0\u0484\0\u04a6\0\u04c8\0\u04ea\0\u050c\0\u052e"+
    "\0\252\0\u0550\0\252\0\u0572\0\u0594\0\u05b6\0\u05d8\0\u05fa"+
    "\0\252\0\252\0\252\0\252\0\252\0\u061c\0\252\0\252"+
    "\0\u063e\0\252\0\252\0\252\0\u0660\0\u0682\0\252";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[71];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\2\1\5\1\2\1\6\1\2"+
    "\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\17\2\6\1\20\1\21\1\22\1\23\1\24"+
    "\3\6\1\25\5\6\43\0\1\3\42\0\1\4\1\26"+
    "\43\0\1\27\36\0\1\6\4\0\2\6\2\0\27\6"+
    "\2\0\1\6\4\0\2\6\2\0\1\6\1\30\5\6"+
    "\1\31\17\6\2\0\1\6\4\0\2\6\2\0\4\6"+
    "\1\32\22\6\2\0\1\6\4\0\2\6\2\0\12\6"+
    "\1\33\14\6\2\0\1\6\4\0\2\6\2\0\1\6"+
    "\1\34\5\6\1\35\17\6\2\0\1\6\4\0\2\6"+
    "\2\0\10\6\1\36\2\6\1\37\13\6\2\0\1\6"+
    "\4\0\2\6\2\0\13\6\1\40\13\6\2\0\1\6"+
    "\4\0\2\6\2\0\11\6\1\41\15\6\2\0\1\6"+
    "\4\0\2\6\2\0\1\6\1\42\4\6\1\43\20\6"+
    "\2\0\1\6\4\0\2\6\2\0\2\6\1\44\2\6"+
    "\1\45\21\6\2\0\1\6\4\0\2\6\2\0\11\6"+
    "\1\46\15\6\2\0\1\6\4\0\2\6\2\0\17\6"+
    "\1\47\7\6\2\0\1\6\4\0\2\6\2\0\1\6"+
    "\1\50\25\6\2\0\1\6\4\0\2\6\2\0\22\6"+
    "\1\51\4\6\2\0\1\26\41\0\1\6\4\0\2\6"+
    "\2\0\2\6\1\52\24\6\2\0\1\6\4\0\2\6"+
    "\2\0\2\6\1\53\24\6\2\0\1\6\4\0\2\6"+
    "\2\0\5\6\1\54\21\6\2\0\1\6\4\0\2\6"+
    "\2\0\20\6\1\55\6\6\2\0\1\6\4\0\2\6"+
    "\2\0\4\6\1\56\22\6\2\0\1\6\4\0\2\6"+
    "\2\0\14\6\1\57\12\6\2\0\1\6\4\0\2\6"+
    "\2\0\6\6\1\60\20\6\2\0\1\6\4\0\2\6"+
    "\2\0\6\6\1\61\20\6\2\0\1\6\4\0\2\6"+
    "\2\0\10\6\1\62\16\6\2\0\1\6\4\0\2\6"+
    "\2\0\3\6\1\63\23\6\2\0\1\6\4\0\2\6"+
    "\2\0\2\6\1\64\24\6\2\0\1\6\4\0\2\6"+
    "\2\0\11\6\1\65\15\6\2\0\1\6\4\0\2\6"+
    "\2\0\23\6\1\66\3\6\2\0\1\6\4\0\2\6"+
    "\2\0\3\6\1\67\23\6\2\0\1\6\4\0\2\6"+
    "\2\0\10\6\1\70\16\6\2\0\1\6\4\0\2\6"+
    "\2\0\6\6\1\71\20\6\2\0\1\6\4\0\2\6"+
    "\2\0\4\6\1\72\22\6\2\0\1\6\4\0\2\6"+
    "\2\0\13\6\1\73\13\6\2\0\1\6\4\0\2\6"+
    "\2\0\4\6\1\74\22\6\2\0\1\6\4\0\2\6"+
    "\2\0\15\6\1\75\11\6\2\0\1\6\4\0\2\6"+
    "\2\0\14\6\1\76\12\6\2\0\1\6\4\0\2\6"+
    "\2\0\15\6\1\77\11\6\2\0\1\6\4\0\2\6"+
    "\2\0\16\6\1\100\10\6\2\0\1\6\4\0\2\6"+
    "\2\0\23\6\1\101\3\6\2\0\1\6\4\0\2\6"+
    "\2\0\4\6\1\102\22\6\2\0\1\6\4\0\2\6"+
    "\2\0\4\6\1\103\22\6\2\0\1\6\4\0\2\6"+
    "\2\0\4\6\1\104\22\6\2\0\1\6\4\0\2\6"+
    "\2\0\24\6\1\105\2\6\2\0\1\6\4\0\2\6"+
    "\2\0\25\6\1\106\1\6\2\0\1\6\4\0\2\6"+
    "\2\0\26\6\1\107";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1700];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;
  private static final char[] EMPTY_BUFFER = new char[0];
  private static final int YYEOF = -1;
  private static java.io.Reader zzReader = null; // Fake

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\4\1\2\11\16\1\1\11\60\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[71];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** this buffer may contains the current text array to be matched when it is cheap to acquire it */
  private char[] zzBufferArray;

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /* user code: */
  public _WhileLexer() {
    this((java.io.Reader)null);
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public _WhileLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 1588) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart(){
    return zzStartRead;
  }

  public final int getTokenEnd(){
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end,int initialState){
    zzBuffer = buffer;
    zzBufferArray = com.intellij.util.text.CharArrayUtil.fromSequenceWithoutCopying(buffer);
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzPushbackPos = 0;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBufferArray != null ? zzBufferArray[zzStartRead+pos]:zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;
    char[] zzBufferArrayL = zzBufferArray;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL)
            zzInput = (zzBufferArrayL != null ? zzBufferArrayL[zzCurrentPosL++] : zzBufferL.charAt(zzCurrentPosL++));
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = (zzBufferArrayL != null ? zzBufferArrayL[zzCurrentPosL++] : zzBufferL.charAt(zzCurrentPosL++));
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 6: 
          { return COMMA;
          }
        case 26: break;
        case 22: 
          { return WRITE;
          }
        case 27: break;
        case 25: 
          { return COMMENT;
          }
        case 28: break;
        case 8: 
          { return IF;
          }
        case 29: break;
        case 5: 
          { return SEP;
          }
        case 30: break;
        case 1: 
          { return com.intellij.psi.TokenType.BAD_CHARACTER;
          }
        case 31: break;
        case 24: 
          { return FALSE;
          }
        case 32: break;
        case 15: 
          { return READ;
          }
        case 33: break;
        case 20: 
          { return SKIP;
          }
        case 34: break;
        case 14: 
          { return NOT;
          }
        case 35: break;
        case 19: 
          { return ENDP;
          }
        case 36: break;
        case 7: 
          { return ASSIGN;
          }
        case 37: break;
        case 13: 
          { return AND;
          }
        case 38: break;
        case 21: 
          { return PROC;
          }
        case 39: break;
        case 9: 
          { return DO;
          }
        case 40: break;
        case 23: 
          { return WHILE;
          }
        case 41: break;
        case 4: 
          { return ID;
          }
        case 42: break;
        case 12: 
          { return FI;
          }
        case 43: break;
        case 10: 
          { return OR;
          }
        case 44: break;
        case 17: 
          { return THEN;
          }
        case 45: break;
        case 18: 
          { return ELSE;
          }
        case 46: break;
        case 11: 
          { return OD;
          }
        case 47: break;
        case 3: 
          { return NUMBER;
          }
        case 48: break;
        case 16: 
          { return TRUE;
          }
        case 49: break;
        case 2: 
          { return com.intellij.psi.TokenType.WHITE_SPACE;
          }
        case 50: break;
        default:
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            return null;
          }
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
