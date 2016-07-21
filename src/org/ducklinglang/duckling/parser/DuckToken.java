package org.ducklinglang.duckling.parser;

import org.nuthatchery.pgf.tokens.Category;
import org.nuthatchery.pgf.tokens.CategoryStore;
import org.nuthatchery.pgf.tokens.DataToken;

public class DuckToken extends DataToken {
	private final String value;
	private int length;
	private int offset;
	private static final CategoryStore store = new CategoryStore();

	static {
		store.declare("TOKEN");
		store.declare("TXT");
		store.declare("SPC");
		store.declare("CTRL");

		store.declare("TXT", "TOKEN");
		store.declare("SPC", "TOKEN");
		store.declare("CTRL", "TOKEN");


		store.declare("BEGIN", "CTRL");
		store.declare("END", "CTRL");
		 
		store.declare("LITERAL", "TXT");
		store.declare("KEYWORD", "LITERAL");
		store.declare("GRP", "LITERAL");
		store.declare("PAR", "GRP");
		store.declare("BRC", "GRP");
		store.declare("BRT", "GRP");
		store.declare("LGRP", "GRP");
		store.declare("RGRP", "GRP");
		store.declare("PUNCT", "LITERAL");
	}
	
	public static final Category LETTER = store.declare("LETTER", "TXT");
	public static final Category UNDERSCORE = store.declare("UNDERSCORE", "LETTER");
	public static final Category DIGIT = store.declare("DIGIT", "TXT");
	public static final Category GLYPH = store.declare("GLYPH", "TXT");

	public static final Category START = store.declare("START", "CTRL");
	public static final Category STOP = store.declare("STOP", "CTRL");

	public static final Category ID = store.declare("ID", "TXT");
	public static final Category INT = store.declare("ID", "TXT");
	public static final Category STRING = store.declare("STRING", "TXT");
	public static final Category OP = store.declare("OP", "LITERAL");
	public static final Category OP_BANG = store.declare("!", "OP");
	public static final Category OP_LT = store.declare("<", "OP");
	public static final Category OP_GT = store.declare(">", "OP");
	public static final Category OP_PLUS = store.declare("+", "OP");
	public static final Category OP_MINUS = store.declare("-", "OP");
	public static final Category OP_STAR = store.declare("*", "OP");
	public static final Category OP_SLASH = store.declare("/", "OP");
	public static final Category OP_AMPERSAND = store.declare("&", "OP");
	public static final Category OP_DOLLAR = store.declare("$", "OP");
	public static final Category OP_EQ = store.declare("=", "OP");
	public static final Category OP_QUESTION = store.declare("?", "OP");
	public static final Category OP_PERCENT = store.declare("%", "OP");
	public static final Category OP_HAT = store.declare("^", "OP");
	public static final Category OP_BAR = store.declare("|", "OP");
	public static final Category OP_TILDE = store.declare("~", "OP");
	public static final Category OP_AT = store.declare("@", "OP");

	public static final Category SPACE = store.category("SPC");
	public static final Category SPACE_HORIZ = store.declare("WS", "SPC");
	public static final Category SPACE_VERT = store.declare("NL", "SPC");
	public static final Category COMMENT = store.declare("COM", "SPC");

	public static final Category DBL_QUOTE = store.declare("DBL_QUOTE", "PUNCT");
	public static final Category SGL_QUOTE = store.declare("SGL_QUOTE", "PUNCT");
	public static final Category BACK_QUOTE = store.declare("BACK_QUOTE", "PUNCT");
	public static final Category HASH = store.declare("HASH", "PUNCT");
	public static final Category BACKSLASH = store.declare("BACKSLASH", "PUNCT");

	public static final Category LEFT_PAREN = store.declare("LPAR", "PAR", "LGRP");
	public static final Category RIGHT_PAREN = store.declare("RPAR", "PAR", "RGRP");
	public static final Category LEFT_BRACE = store.declare("LBRC", "BRC", "LGRP");
	public static final Category RIGHT_BRACE = store.declare("RBRC", "BRC", "RGRP");
	public static final Category LEFT_BRACKET = store.declare("LBRT", "BRT", "LGRP");
	public static final Category RIGHT_BRACKET = store.declare("RBRT", "BRT", "RGRP");

	public static final Category COMMA = store.declare("COMMA", "PUNCT");
	public static final Category SEMI = store.declare("SEMI", "PUNCT");
	public static final Category COLON = store.declare("COLON", "PUNCT");
	public static final Category DOT = store.declare("DOT", "PUNCT");

	static {
		store.compile();
	}
	protected DuckToken(Category cat, String text, String value, int offset, int length) {
		super(text, cat);
		this.value = value;
		this.offset = offset;
		this.length = length;
	}
	public String getValue() {
		return value;
	}

}
