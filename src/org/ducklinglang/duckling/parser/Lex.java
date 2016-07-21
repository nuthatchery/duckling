package org.ducklinglang.duckling.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.nuthatchery.pgf.tokens.Category;
import static org.ducklinglang.duckling.parser.DuckToken.*;

public class Lex {
	enum Mode {
		IDENTIFIER, NUMBER, STRING, REST, OPERATOR, SPACE, SIMPLE
	};

	private CharInput input;
	private List<DuckToken> buffer = new ArrayList<DuckToken>();
	private StringBuilder inBuffer = new StringBuilder();
	private int startPos;
	private static final Category[] SYMBOLS = new Category[] { //
			SPACE_HORIZ, OP_BANG, DBL_QUOTE, HASH, OP_DOLLAR, //
			OP_PERCENT, OP_AMPERSAND, SGL_QUOTE, LEFT_PAREN, RIGHT_PAREN, //
			OP_STAR, OP_PLUS, COMMA, OP_MINUS, DOT, OP_SLASH, //
			DIGIT, DIGIT, DIGIT, DIGIT, DIGIT, //
			DIGIT, DIGIT, DIGIT, DIGIT, DIGIT, //
			COLON, SEMI, OP_LT, OP_EQ, OP_GT, OP_QUESTION, OP_AT, //
			LETTER, LETTER, LETTER, LETTER, LETTER, //
			LETTER, LETTER, LETTER, LETTER, LETTER, //
			LETTER, LETTER, LETTER, LETTER, LETTER, //
			LETTER, LETTER, LETTER, LETTER, LETTER, //
			LETTER, LETTER, LETTER, LETTER, LETTER, LETTER, //
			LEFT_BRACKET, BACKSLASH, RIGHT_BRACKET, OP_HAT, UNDERSCORE, BACK_QUOTE, //
			LETTER, LETTER, LETTER, LETTER, LETTER, //
			LETTER, LETTER, LETTER, LETTER, LETTER, //
			LETTER, LETTER, LETTER, LETTER, LETTER, //
			LETTER, LETTER, LETTER, LETTER, LETTER, //
			LETTER, LETTER, LETTER, LETTER, LETTER, LETTER, //
			LEFT_BRACE, OP_BAR, RIGHT_BRACE, OP_TILDE, null };

	public Lex(Reader reader) {
		input = new CharInput(reader);
	}

	public DuckToken nextToken() throws IOException {
		Mode mode = Mode.SIMPLE;

		while (true) {
			int i = input.get();
			char c = (char) i;

			Category cat = GLYPH;
			if (i > 0x20 && i < 0x7f) {
				cat = SYMBOLS[i - 0x20];
			} else if (Character.isDigit(i)) {
				cat = DIGIT;
			} else if (Character.isWhitespace(i)) {
				cat = SPACE;
			} else if (Character.isAlphabetic(i)) {
				cat = LETTER;
			} else if (i < 0) {
				cat = STOP;
			}

			switch (mode) {
			case IDENTIFIER:
				if (cat.isSubCatOf(LETTER)) {
					inBuffer.append(c);
					continue;
				} else {
					input.pushback(i);
					return finish(ID, i);
				}
			case NUMBER:
				if (cat.isSubCatOf(DIGIT)) {
					inBuffer.append(c);
					continue;
				} else {
					input.pushback(i);
					return finish(INT, i);
				}
			case OPERATOR:
				if (cat.isSubCatOf(OP)) {
					inBuffer.append(c);
					continue;
				} else {
					input.pushback(i);
					return finish(OP, i);
				}
			case SPACE:
				if (cat.isSubCatOf(SPACE)) {
					inBuffer.append(c);
					continue;
				} else {
					input.pushback(i);
					return finish(SPACE, i);
				}
			case REST:
				if (c == '\n' || cat == STOP) {
					input.pushback(i);
					return finish(COMMENT, i);
				} else {
					inBuffer.append(c);
					continue;
				}
			case SIMPLE:
				if (cat.isSubCatOf(LETTER)) {
					mode = Mode.IDENTIFIER;
					input.pushback(c);
					startPos = input.getPos();
				} else if (cat.isSubCatOf(DIGIT)) {
					mode = Mode.NUMBER;
					input.pushback(c);
					startPos = input.getPos();
				} else if (cat.isSubCatOf(SPACE)) {
					mode = Mode.SPACE;
					input.pushback(c);
					startPos = input.getPos();
				} else if (cat.isSubCatOf(OP)) {
					mode = Mode.OPERATOR;
					input.pushback(c);
					startPos = input.getPos();
				} else if (cat == HASH) {
					startPos = input.getPos() - 1;
					mode = Mode.REST;
				} else if (cat == DBL_QUOTE) {
					startPos = input.getPos() - 1;
					mode = Mode.STRING;
				} else if (cat == STOP) {
					return null;
				} else {
					String data = String.valueOf(c);
					return new DuckToken(cat, data, data, input.getPos() - 1, 1);
				}
				break;
			case STRING:
				if (cat.isSubCatOf(DBL_QUOTE) || cat == STOP) {
					return finish(STRING, i);
				} else if (cat.isSubCatOf(BACKSLASH)) {
					inBuffer.append(input.get());
					continue;
				} else {
					inBuffer.append(c);
					continue;
				}
			default:
				break;

			}
		}

	}

	private DuckToken finish(Category cat, int i) {
		String data = inBuffer.toString();
		inBuffer.setLength(0);
		return new DuckToken(cat, data, data, startPos, input.getPos() - startPos);
	}

	public static void main(String[] args) throws IOException {
		StringReader reader = new StringReader(
				"a + \"(b ++@++,...\" public static void main(String[] args) throws IOException { #cdef\n fdasjkln\n dnsajkl l dakls ");

		Lex lex = new Lex(reader);

		DuckToken tok = lex.nextToken();
		while (tok != null) {
			System.out.println(tok);
			tok = lex.nextToken();
		}
	}

	static class CharInput {
		Reader reader;
		int lookahead = -1;
		int pos = 0;

		public CharInput(Reader reader) {
			this.reader = reader;
		}

		public int get() throws IOException {
			int c = lookahead;
			if (c < 0)
				c = reader.read();
			lookahead = -1;
			pos++;
			return c;
		}

		public int look() throws IOException {
			if (lookahead < 0)
				lookahead = reader.read();
			return lookahead;
		}

		public void pushback(int c) {
			// if(lookahead >= 0)
			// throw new IOException("Pushback buffer full!");
			--pos;
			lookahead = c;
		}

		public int getPos() {
			return pos;
		}
	}

}
