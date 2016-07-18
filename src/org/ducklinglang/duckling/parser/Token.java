package org.ducklinglang.duckling.parser;

public abstract class Token {
	private final int kind;
	private final String text;
	private final String value;
	protected Token(int kind, String text, String value) {
		super();
		this.kind = kind;
		this.text = text;
		this.value = value;
	}

}
