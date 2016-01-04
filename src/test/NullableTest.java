package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Grammar;
import main.Nullable;

public class NullableTest {
	Nullable nullable;
	Grammar grammar;
	@Before
	public void setUp() throws Exception {
		grammar = new Grammar();
		nullable = new Nullable(grammar);
	}

	@Test
	public void testNullableNonterminals() {
		assertTrue(nullable.nullable("Sl"));
		assertTrue(nullable.nullable("Ld"));
		assertTrue(nullable.nullable("Ar"));
		assertTrue(nullable.nullable("Sl Ld Ar"));
	}
	
	@Test
	public void testNonNullableNonterminals() {
		assertFalse(nullable.nullable("Pl"));
		assertFalse(nullable.nullable("Ar Pl"));
		assertFalse(nullable.nullable("Pl Ar"));
	}
	
	@Test
	public void testMixedNonterminalsAndTerminals() {
		assertTrue(nullable.nullable("empty"));
		assertFalse(nullable.nullable("empty {"));
		assertFalse(nullable.nullable("empty id"));
		assertTrue(nullable.nullable("empty Ar"));
		assertFalse(nullable.nullable("Vd id"));
	}
	
	@Test
	public void testOne() {
		assertFalse(nullable.nullable("L D"));
	}

}
