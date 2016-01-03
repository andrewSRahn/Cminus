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
	public void closureTest() {
		ArrayList<Production> i = new ArrayList<Production>();
		
		Production seed = new Production(augmentedGrammar.getProductions().get(0).getFull());
		seed.addDot();
		i.add(seed);
		assertEquals(items.closure(i).size(), 12);
		
		i = new ArrayList<Production>();
		seed.incrementDot();
		i.add(seed);
		assertEquals(items.closure(i).size(), 1);
		
		i = new ArrayList<Production>();
		i.add(new Production("P :: L ."));
		i.add(new Production("L :: L . D"));
		assertEquals(items.closure(i).size(), 10);
		
	}
	
	@Test
	public void goToTest() {
		ArrayList<Production> item0 = new ArrayList<Production>();
		Production seed = new Production(augmentedGrammar.getProductions().get(0).getFull());
		seed.addDot();
		item0.add(seed);
		item0 = items.closure(item0);
		
		System.out.println(items.goTo(item0, "L"));
		
		assertEquals(items.goTo(item0, "P").size(), 1);
		assertEquals(items.goTo(item0, "L").size(), 2);
		assertEquals(items.goTo(item0, "D").size(), 1);
		assertEquals(items.goTo(item0, "Vd").size(), 1);
		assertEquals(items.goTo(item0, "Fn").size(), 1);
		assertEquals(items.goTo(item0, "Ts").size(), 4);
		assertEquals(items.goTo(item0, "int").size(), 1);
		assertEquals(items.goTo(item0, "void").size(), 1);
	}

}
