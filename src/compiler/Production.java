package compiler;

import java.util.ArrayList;

/*
 * Sample production  		Ss :: if ( E ) S else S
 * 				full 		Ss :: if ( E ) S else S
 * 				left		Ss
 * 				right		if ( E ) S else S
 * 				rightTokens	[if, (, E, ), S, else, S]
 * 
 */
public class Production {
	String full;
	String left;
	String right;
	ArrayList<String> rightTokens;
	
	public Production(String full) {
		this.full = full;
		this.left = full.split("::")[0].trim();
		this.right = full.split("::")[1].trim();
		this.rightTokens = new ArrayList<String>();
		String[] split = right.split(" ");
		for (String s: split) {
			this.rightTokens.add(s);
		}
	}
	public String toString() {
		return full;
	}
	
	
}
