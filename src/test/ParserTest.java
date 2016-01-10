package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import compiler.First;
import compiler.Follow;
import compiler.Grammar;
import compiler.Items;
import compiler.Main;
import compiler.Nullable;
import compiler.ParsingTable;

public class ParserTest {
	private Grammar augmentedGrammar;
	private Nullable nullable;
	private First first;
	private Follow follow;
	private Items items;
	private ParsingTable parsingTable;
	
	@Before
	public void setUp() throws Exception {
		augmentedGrammar = new Grammar();
		augmentedGrammar.augment();
		nullable = new Nullable(augmentedGrammar);
		first = new First(augmentedGrammar, nullable);
		follow = new Follow(augmentedGrammar, first);
		items = new Items(augmentedGrammar);
		parsingTable = new ParsingTable(augmentedGrammar, follow, items);
	}

	@Test
	public void test() {
	}

}
