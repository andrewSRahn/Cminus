package compiler;

import java.security.InvalidParameterException;
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
	private String full;
	private String left;
	private String right;
	private ArrayList<String> rightTokens;
	
	public Production(String full) {
		if (full.contains("::") == false) {
			System.out.println("Item constructor:  incorrect input");
			System.out.println(full);
			throw new InvalidParameterException();
		}
		
		this.full = full;
		this.setLeft(full.split("::")[0].trim());
		this.right = full.split("::")[1].trim();
		this.setRightTokens(new ArrayList<String>());
		

		String[] split = right.split(" ");
		for (String s: split) {
			this.getRightTokens().add(s);
		}
	}
	
	public String toString() {
		return full;
	}

	public String getFull() {
		return full;
	}
	
	public String getLeft() {
		return left;
	}
	
	public String getRight() {
		return right;
	}
	
	public ArrayList<String> getRightTokens() {
		return rightTokens;
	}

	public void setLeft(String left) {
		this.left = left;
	}
	
	public void setRightTokens(ArrayList<String> rightTokens) {
		this.rightTokens = rightTokens;
	}
	
	public void initDot() {
		if (getRightTokens().contains(".")) {
			System.out.println("Cannot initDot.  Dot already contained in rightTokens");
			System.out.println(full);
			throw new InvalidParameterException();
		}
		getRightTokens().add(0, ".");
		String newRight = "";
		for (String s: getRightTokens()) {
			newRight += (s + " ");
		}
		
		
	}
}
