package test;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import compiler.First;
import compiler.Grammar;
import compiler.Nullable;

public class FirstTest {
	Grammar grammar;
	First first;
	Nullable nullable;
	@Before
	public void setUp() throws Exception {
		grammar = new Grammar();
		nullable = new Nullable(grammar);
		first = new First(grammar, nullable);
		
	}

	@Test
	public void testOne() {
		HashSet<String> hs = new HashSet<String>();
		hs.add("void");
		hs.add("int");
		assertEquals(first.first("P"), hs);
		assertEquals(first.first("L"), hs);
		assertEquals(first.first("D"), hs);
	}
	
	@Test
	public void testTwo() {
		HashSet<String> hs = new HashSet<String>();
		hs.add("{");
		assertEquals(first.first("Cs"), hs);
	}
	
	@Test
	public void testThree(){
		HashSet<String> hs = new HashSet<String>();
		hs.add("if");
		assertEquals(first.first("Ss"), hs);
	}
	
	@Test
	public void testFour(){
		HashSet<String> hs = new HashSet<String>();
		hs.add("id");
		assertEquals(first.first("C"), hs);
	}

}
