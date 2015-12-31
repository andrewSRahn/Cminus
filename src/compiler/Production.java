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
	
	public Production(Production production) {
		this.full = production.getFull();
		this.left = production.getLeft();
		this.right = production.getRight();
		this.rightTokens = production.getRightTokens();
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
	
	public String getTokenNextToDot() {
		if (getRightTokens().indexOf(".") == -1) {
			System.out.println("Cannot getTokenNextToDot.  No dot contained in production.");
			System.out.println(getFull());
			throw new IllegalStateException();
		}
		
		if (getRightTokens().indexOf(".") == getRightTokens().size() -1) {
			return null;
		}
		else {
			return getRightTokens().get(getRightTokens().indexOf(".") + 1);
		}
	}
	
	public void setFull(String full) {
		this.full = full;
	}

	public void setLeft(String left) {
		this.left = left;
	}
	
	public void setRight(ArrayList<String> rightTokens) {
		this.right = "";
		for (int i = 0; i < rightTokens.size(); i++) {
			this.right += rightTokens.get(i);
			if (i == rightTokens.size() - 1) {
				break;
			}
			this.right += " ";
		}
	}
	
	public void setRightTokens(ArrayList<String> rightTokens) {
		this.rightTokens = rightTokens;
	}
	
	public void addDot() {
		if (getRightTokens().contains(".")) {
			System.out.println("Cannot addDot.  Dot already contained in rightTokens");
			System.out.println(full);
			System.out.println(getRightTokens().toString());
			System.out.println(getRight());
			throw new IllegalStateException();
		}
		
		if (getRightTokens().contains("empty")) {
			getRightTokens().remove(getRightTokens().indexOf("empty"));
		}
		
		getRightTokens().add(0, ".");
		
		setRight(getRightTokens());
		setFull(getLeft() + " :: " + getRight());
	}
	
	public void incrementDot() {
		if (getRightTokens().indexOf(".") == - 1) {
			System.out.println("Cannot incrementDot.  Dot not contained in rightTokens");
			System.out.println(full);
			throw new IllegalStateException();
		}
		
		if (getRightTokens().indexOf(".") == getRightTokens().size() - 1) {
			System.out.println("Cannot incrementDot.  Dot at right-most position");
			System.out.println(full);
			throw new IllegalStateException();
		}
		
		int index = getRightTokens().indexOf(".");
		getRightTokens().remove(".");
		getRightTokens().add(index+1, ".");
		setRight(getRightTokens());
		setFull( getLeft() + " :: " + getRight());
	}
}
