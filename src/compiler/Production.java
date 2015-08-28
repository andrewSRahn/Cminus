package compiler;

import java.util.ArrayList;

public class Production {
	String full;
	String left;
	String right;
	ArrayList<String> rightTokens;
	
	public Production(String full) {
		this.full = full;
		this.left = full.split("::")[0];
		this.right = full.split("::")[1].trim();
		this.rightTokens = new ArrayList<String>();
		String[] split = right.split(" ");
		for (String s: split) {
			this.rightTokens.add(s);
		}
	}
	
	public boolean rightTokensContains(String query) {
		for (String s: rightTokens) {
			if (s.equals(query)) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return full;
	}
	
	
}
