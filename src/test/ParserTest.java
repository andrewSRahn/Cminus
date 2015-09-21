package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;
import compiler.ProductionWithDot;
import compiler.Parser;
import compiler.Production;

public class ParserTest {
	
	@Test
	public void testItemsCheck() {
		HashMap<Integer, ArrayList<ProductionWithDot>> items = new HashMap<Integer, ArrayList<ProductionWithDot>>();
		ArrayList<ProductionWithDot> item0 = new ArrayList<ProductionWithDot>();
		ArrayList<ProductionWithDot> item1 = new ArrayList<ProductionWithDot>();
		ArrayList<ProductionWithDot> item2 = new ArrayList<ProductionWithDot>();
		ArrayList<ProductionWithDot> item3 = new ArrayList<ProductionWithDot>();
		ArrayList<ProductionWithDot> item4 = new ArrayList<ProductionWithDot>();
		
		
		ProductionWithDot i0 = new ProductionWithDot(new Production("P' :: P"));
		ProductionWithDot i1 = new ProductionWithDot(new Production("P :: L"));
		ProductionWithDot i2 = new ProductionWithDot(new Production("L :: L D"));
		ProductionWithDot i3 = new ProductionWithDot(new Production("L :: L D"));
		
		item0.addAll(Arrays.asList(i0, i1, i2));
		item1.addAll(Arrays.asList(i0, i1, i2));
		item2.addAll(Arrays.asList(i0, i1));
		item3.addAll(Arrays.asList(i1, i2, i0));
		item4.addAll(Arrays.asList(i0, i1, i3));
		
		items.put(0, item0);
		
		Parser p = new Parser();
		
		assertTrue("Items by my definition should contain item0", p.itemsCheck(items, item0));
		assertTrue("Items should recognize that item1 is a duplicate of item0", p.itemsCheck(items, item1));
		assertFalse("Items should recognize that item2 is not a duplicate", p.itemsCheck(items, item2));
		assertTrue("Items should recognize that item3 is a disordered duplicate", p.itemsCheck(items, item3));
		assertTrue(p.itemsCheck(items, item4));
	}
	
	@Test
	public void testItemCheck() {
		ProductionWithDot i0 = new ProductionWithDot(new Production("P' :: P"));
		ProductionWithDot i1 = new ProductionWithDot(new Production("P :: L"));
		ProductionWithDot i2 = new ProductionWithDot(new Production("L :: L D"));
		ProductionWithDot i3 = new ProductionWithDot(new Production("L :: L D"));
		
		ArrayList<ProductionWithDot> item0 = new ArrayList<ProductionWithDot>();
		
		item0.addAll(Arrays.asList(i0, i1));
		
		Parser p = new Parser();
		assertTrue(p.itemCheck(item0, i0));
		assertFalse(p.itemCheck(item0, i2));
	}
	
}
