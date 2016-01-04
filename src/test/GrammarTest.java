package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Grammar;
import main.Production;

public class GrammarTest {
	Grammar grammar;
	Grammar augmentedGrammar;
	
	@Before
	public void setUp() throws Exception {
		grammar = new Grammar();
		augmentedGrammar = new Grammar();
		augmentedGrammar.augment();
	
	}

	@Test
	public void augmentedGrammarTest() {
	}
	

}
