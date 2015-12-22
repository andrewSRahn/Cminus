package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import compiler.Production;
import compiler.ProductionWithDot;

public class ProductionTest {
	static Production p0;
	static Production p1;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		p0 = new Production("P :: L");
		p1 = new Production("L :: L D");
	}

	@Test
	public void constructorTest() {
		assertEquals("P :: . L", 
				p0.toString());
		assertEquals("L :: . L D", 
				p1.toString());
	}
	
	@Test
	public void shiftDotTest() {
		p0.shiftDot();
		assertEquals("Check shiftDot",
				p0.toString(),
				"P :: L .");
		
		p1.shiftDot();
		assertEquals("Check shiftDot",
				p1.toString(),
				"L :: L . D");
		
		p1.shiftDot();
		assertEquals("Check shiftDot",
				p1.toString(),
				"L :: L D .");
	}
	
	@Test
	public void tokenNextOfDotTest() {
		assertEquals("check tokenNextOfdot",
				p0.getTokenNextOfDot(),
				"L");
		p0.shiftDot();
	}

}
