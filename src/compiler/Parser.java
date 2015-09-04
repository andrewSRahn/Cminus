package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/*
 * Parser contains a grammar.
 * Also defines nullable, first, follow, goto, and closure. 
 * 
 */
public class Parser {
	
	public Parser() {
		@SuppressWarnings("unused")
		Grammar grammar = new Grammar();
	}
	
	/*
	 * Public method for first
	 */
	public HashSet<String> first(String input) {
		ArrayList<String> tokens = this.tokenize(input);
		
		if (tokens.size() == 1) {
			HashSet<String> first = new HashSet<String>();
			String a = tokens.get(0);
			
			if (Grammar.terminals.contains(a) || a.equals("empty")) {
				first.add(a);
				return first;
			}
			else if (Grammar.nonterminals.contains(a)) {
				for (Production p: Grammar.productions) {
					if (p.left.equals(a)) {
						first.addAll(this.first(p.right));
					}
				}
				return first;
			}
		}
		
		else if (tokens.size() > 1) {
			HashSet<String> first = new HashSet<String>();
			System.out.println("reached");
		}
		
		return new HashSet<String>();
		
	}
	
	/*
	 * Helper method for nullable.
	 * 
	 * Handles the calculation and stores into HashMap nullable.
	 * Handles in order of:  terminals, nonterminals, productions
	 */
	private boolean calculateNullable(String input){
		HashMap<String, Boolean> nullable = new HashMap<String, Boolean>();
		
		// empty string
		nullable.put("empty", new Boolean(true));
		
		// terminals
		for (String s: Grammar.terminals) {
			nullable.put(s, new Boolean(false));
		}

		// nonterminals
		for (Production p: Grammar.productions) {
			if (nullable.containsKey(p.left)) {
				if (nullable.get(p.left).booleanValue() == true) {
					continue;
				}
			}
			
			if (p.rightTokens.contains("empty")) {
				nullable.put(p.left, new Boolean(true));
			}
			else {
				nullable.put(p.left, new Boolean(false));
			}
		}
		
		// productions
		boolean again = true;
		while (again) {
			for (Production p: Grammar.productions) {
				int count = p.rightTokens.size();
				for (String s: p.rightTokens) {
					if (nullable.get(s) == null) {
						count--;
						continue;
					}
					if (nullable.get(s).booleanValue() == true) {
						count--;
					}
				}
				
				if (count < 1) {
					nullable.put(p.left, new Boolean(true));
					again = true;
				}
				else {
					again = false;
				}	
			}
		}
		return nullable.get(input);
	}
	
	/*
	 * Public access nullable method.
	 * Currently unsure about "return true" in last else.
	 */
	public boolean nullable(String input) {
		ArrayList<String> inputTokens = this.tokenize(input);
		
		for (String s: inputTokens) {
			try {
				this.calculateNullable(s);
			}
			catch (NullPointerException e){
				System.out.println("Nullable:  invalid input.");
				e.printStackTrace();
				return false;
			}
		}
		
		if (inputTokens.size() == 0) {
			return true;
		}
		else if (this.calculateNullable(inputTokens.get(0)) == false) {
			return false;
		}
		else {
			boolean nullable = true;
			
			for (int i = 0; i < inputTokens.size(); i++) {	
				if (this.calculateNullable(inputTokens.get(i)) == false) {
					nullable = false;
				}
			}
			
			if (nullable == true) {
				return true;
			}
			else {
				return false;
			}
		}
	
	}
	
	/*
	 * Helper method for parser
	 * Takes a String input and returns ArrayList<String> of tokens
	 */
	private ArrayList<String> tokenize(String a){
		ArrayList<String> value = new ArrayList<String>();
		String[] split = a.split(" ");
		for(String s: split){
			if(s.trim().length() < 1){
				continue;
			}
			else{
				value.add(s.trim());
			}
		}
		return value;
	}
}
