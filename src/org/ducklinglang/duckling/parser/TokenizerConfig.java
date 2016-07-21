package org.ducklinglang.duckling.parser;

import java.util.Collection;

import org.nuthatchery.pgf.config.TokenizerConfigBase;
import org.nuthatchery.pgf.tokens.CategoryStore;

public class TokenizerConfig extends TokenizerConfigBase {

	@Override
	protected String cfgIdentifierRegex() {
		return DEFAULT_ID_REGEX;
	}

	@Override
	protected CatSet cfgInitialCategorySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String cfgKeywordRegex() {
		return DEFAULT_KW_REGEX;
	}

	@Override
	protected Collection<String> cfgKeywords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean cfgUseDefaultLiteralClassification() {
		return true;
	}

	@Override
	protected void moreCategories(CategoryStore store) {
		
	}

}
