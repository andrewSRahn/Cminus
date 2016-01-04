package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import main.Production;

public class ProductionTest {
	static Production p0;
	static Production p1;
	static Production p2;
	static Production p3;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		p0 = new Production("P :: L");
		p1 = new Production("Sl :: empty");
		p2 = new Production("Ss :: if ( E ) S else S");
		p3 = new Production("P' :: P");
	}

	@Test
	public void constructorTest() {
		assertEquals("P :: L", 
				p0.getFull());
		
		assertEquals("Sl :: empty", 
				p1.getFull());
		
		assertEquals("Ss :: if ( E ) S else S",
				p2.getFull());
		
	}
	
	@Test
	public void addDotTest() {
		p0.addDot();
		assertEquals(p0.toString(),
				"P :: . L");
		assertEquals(p0.getRight(),
				". L");
		assertEquals(p0.getRightTokens(),
				Arrays.asList(".", "L"));
		
		p1.addDot();
		assertEquals(p1.toString(),
				"Sl :: .");
		assertEquals(p1.getRight(),
				".");
		assertEquals(p1.getRightTokens(),
				Arrays.asList("."));
		
		p2.addDot();
		assertEquals(p2.toString(),
				"Ss :: . if ( E ) S else S");
		assertEquals(p2.getRight(),
				". if ( E ) S else S");
		assertEquals(p2.getRightTokens(),
				Arrays.asList(".", "if", "(", "E", ")", "S", "else", "S"));
		
		p3.addDot();
		System.out.println(p3);
	}
	
	@Test
	public void incrementDotTest(){
		p0.incrementDot();
		assertEquals(p0.getFull(),
				"P :: L .");
		assertEquals(p0.getRight(),
				"L .");
		assertEquals(p0.getRightTokens(),
				Arrays.asList("L", "."));
		
		p2.incrementDot();
		assertEquals(p2.getFull(),
				"Ss :: if . ( E ) S else S");
		assertEquals(p2.getRight(),
				"if . ( E ) S else S");
		assertEquals(p2.getRightTokens(),
				Arrays.asList("if", ".", "(", "E", ")", "S", "else", "S"));
		
		
	}
	
	

}
