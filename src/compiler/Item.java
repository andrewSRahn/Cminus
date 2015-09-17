package compiler;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/*
 * Sample production  			Ss :: . if ( E ) S else S
 * 				full 			Ss :: . if ( E ) S else S
 * 				left			Ss
 * 				right			. if ( E ) S else S
 * 				rightTokens		[., if, (, E, ), S, else, S]
 * 				
 *				dotLocation 	0
 *				tokenNextOfDot	if
*/

public class Item {
	String full;
	String left;
	String right;
	ArrayList<String> rightTokens;
	
	Integer dotLocation;
	String tokenNextOfDot;
	
	public Item(Production x) {
		this.left = x.left;
		this.full = (x.left + " :: . ");
		this.right = ". ";
		
		this.rightTokens = new ArrayList<String>();
		this.rightTokens.add(".");
		
		
		for (int i = 0; i < x.rightTokens.size(); i++) {
			this.full += x.rightTokens.get(i);
			this.right += x.rightTokens.get(i);
			this.rightTokens.add(x.rightTokens.get(i));
			if (i == x.rightTokens.size()-1) {
				continue;
			}
			this.full += " ";
			this.right += " ";
		}
		
		this.dotLocation = 0;
		
		if (this.dotLocation == this.rightTokens.size() - 1) {
			this.tokenNextOfDot = null;
		}
		else {
			this.tokenNextOfDot = this.rightTokens.get(dotLocation.intValue() + 1);
		}
	}
	
	public void shiftDot() {
		if (this.dotLocation == this.rightTokens.size() - 1) {
			System.out.println("Can't shift dot of ItemProduction");
			System.out.println(this.full);
			throw new IndexOutOfBoundsException();
		}
		
		
	}

	public String toString() {
		return full;
	}
}
