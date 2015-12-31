package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import compiler.Grammar;
import compiler.Items;
import compiler.Production;

public class ItemsTest {
	Grammar augmentedGrammar;
	Items items;
	
	@Before
	public void setUp() throws Exception {
		augmentedGrammar = new Grammar();
		augmentedGrammar.augment();
		items = new Items(augmentedGrammar);
	
	}
	@Test
	publiProductiont() {
		ArrayList<Production> i = new ArrayList<Production>();
		i.add()
		items.closure(Arrays.asList(new Production()))
	}

}
