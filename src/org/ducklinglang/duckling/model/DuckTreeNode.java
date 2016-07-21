package org.ducklinglang.duckling.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ducklinglang.duckling.parser.DuckToken;

public class DuckTreeNode {

	private List<DuckTreeNode> subNodes;
	private String name;

	private DuckTreeNode(String name, List<DuckTreeNode> subNodes) {
		this.name = name;
		this.subNodes = subNodes;
	}
	
	public static DuckTreeNode program(List<DuckTreeNode> decls) {
		return new DuckTreeNode("program", decls);
	}
	public static DuckTreeNode module(List<DuckTreeNode> decls) {
		return new DuckTreeNode("module", decls);
	}

	
	public static DuckTreeNode def(DuckToken name, DuckTreeNode def) {
		return new DuckTreeNode("def", Arrays.asList(name(name), def));
	}

	public static DuckTreeNode fun(List<DuckTreeNode> params, DuckTreeNode def) {
		return new DuckTreeNode("fun", params);
	}

	public static DuckTreeNode vardecl(DuckToken mode, DuckToken type, DuckToken name) {
		return new DuckTreeNode("vardecl", Arrays.asList(name(mode), name(type), name(name)));
	}
	
	private static DuckTreeNode name(DuckToken name) {
		return new DuckTreeNode(name.getValue(), Collections.EMPTY_LIST);
	}
	
	public String toString() {
		return name + subNodes.toString();
	}
}
