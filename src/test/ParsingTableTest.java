package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import compiler.First;
import compiler.Follow;
import compiler.Grammar;
import compiler.Items;
import compiler.Nullable;
import compiler.ParsingTable;
import compiler.Production;

public class ParsingTableTest {
	ParsingTable parsingTable;
	Grammar augmentedGrammar;
	Nullable nullable;
	First first;
	Follow follow;
	Items items;
	
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
