package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import compiler.Grammar;
import compiler.Parser;

public class ParserTest {
	Grammar augmentedGrammar;
	Parser parser;
	
	@Before
	public void setUp() throws Exception {
		augmentedGrammar = new Grammar();
		augmentedGrammar.augment();
		parser = new Parser();
	}

	@Test
	public void testClosure() {
	}

}
