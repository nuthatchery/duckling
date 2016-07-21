package org.ducklinglang.duckling.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.ducklinglang.duckling.model.DuckTreeNode;
import static org.ducklinglang.duckling.model.DuckTreeNode.*;
import org.nuthatchery.pgf.tokens.Category;

public class Parser {

	public static void main(String[] args) throws IOException {
		Reader r = new FileReader("src/org/ducklinglang/duckling/parser/example.dkl");
		Parser p = new Parser(r);
		System.out.println(p.parseProgram());
	}
	private DuckToken lookAhead = null;

	private Lex lex;

	public Parser(Reader reader) {
		lex = new Lex(reader);
	}

	private boolean atEnd() throws IOException {
		return look() == null;
	}

	private void expect(Category cat) throws IOException {
		DuckToken tok = look();
		if (tok.hasSubCatOf(cat))
			get();
		else
			parseError("Expected " + cat, tok);
	}

	private void expect(String s, Category cat) throws IOException {
		DuckToken tok = look();
		if (tok.hasSubCatOf(cat) && tok.getValue().equals(s))
			get();
		else
			parseError("Expected ...", tok);
	}

	private DuckToken get() throws IOException {
		DuckToken tok = look();
		lookAhead = null;
		return tok;
	}

	private DuckToken get(Category cat) throws IOException {
		DuckToken tok = look();
		if (!tok.hasSubCatOf(cat))
			parseError("Expected " + cat, tok);
		lookAhead = null;
		return tok;
	}

	private DuckToken look() throws IOException {
		if (lookAhead == null)
			do {
				lookAhead = lex.nextToken();
			} while (lookAhead != null && lookAhead.hasSubCatOf(DuckToken.SPACE));

		return lookAhead;
	}

	private boolean lookingAt(Category cat) throws IOException {
		DuckToken tok = look();
		return tok != null && tok.hasSubCatOf(cat);
	}

	private DuckTreeNode parseDecl() throws IOException {
		DuckToken head = get(DuckToken.ID);

		switch (head.getValue()) {
		case "def":
			return parseDef(head);
		default:
			parseError("Invalid start of declaration", head);
		}

		return null;
	}

	private DuckTreeNode parseDef(DuckToken head) throws IOException {
		DuckToken name = get(DuckToken.ID);

		expect("=", DuckToken.OP);

		DuckToken kind = get(DuckToken.ID);

		switch (kind.getValue()) {
		case "module":
			return def(name, parseModule());
		case "fun":
			return def(name, parseFun());
		}
		return null;
	}

	private void parseError(String string, DuckToken head) throws IOException {
		throw new IOException(string + ": " + head);
	}

	private DuckTreeNode parseFun() throws IOException {
		DuckToken type = null;
		if (lookingAt(DuckToken.ID)) {
			type = get(DuckToken.ID);
		}

		expect(DuckToken.LEFT_PAREN);
		List<DuckTreeNode> params = new ArrayList<>();
		if (!lookingAt(DuckToken.RIGHT_PAREN)) {
			while (true) {
				DuckToken var = get(DuckToken.ID);
				params.add(vardecl(var, var, var));
				if (lookingAt(DuckToken.COMMA)) {
					get(DuckToken.COMMA);
				} else {
					break;
				}
			}
		}

		expect(DuckToken.RIGHT_PAREN);

		return fun(params, null);
	}

	private DuckTreeNode parseModule() throws IOException {
		expect(DuckToken.LEFT_BRACE);

		List<DuckTreeNode> decls = new ArrayList<>();
		while (!lookingAt(DuckToken.RIGHT_BRACE)) {
			DuckTreeNode decl = parseDecl();
			if (decl != null)
				decls.add(decl);
		}

		expect(DuckToken.RIGHT_BRACE);
		return module(decls);
	}

	public DuckTreeNode parseProgram() throws IOException {
		List<DuckTreeNode> decls = new ArrayList<>();
		while (!atEnd()) {
			DuckTreeNode decl = parseDecl();
			if (decl != null)
				decls.add(decl);
		}

		return program(decls);
	}
}
