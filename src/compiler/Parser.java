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
	HashMap<String, HashSet<String>> first;
	
	
	public Parser() {
		Grammar grammar = new Grammar();
		
	}
	
	private void printFirst(HashSet<String> first) {
		
	}
	
	
	/*
	 * Helper method for first
	 * Handles the calculation and stores into HashMap first.
	 * Handles in order of:  terminals, nonterminals, productions
	 */
	private HashSet<String> calculateFirst(String input) {
		HashMap<String, HashSet<String>> first = 
				new HashMap<String, HashSet<String>>();
		
		//terminals
		for (String s: Grammar.terminals) {
			HashSet<String> a = new HashSet<String>();
			a.add(s);
			first.put(s, a);
		}
		
		//nonterminals
		for (String s: Grammar.nonterminals) {
			first.put(s, new HashSet<String>());
		}
		
		//productions
		boolean again = true;
		while (again) {
			again = false;
			for (Production p: Grammar.productions) {
				// handling production with "empty"
				if (first.get(p.rightTokens.get(0)) == null) {
					continue;
				}
				
				HashSet<String> set = first.get(p.left);
				set.addAll(first.get(p.rightTokens.get(0)));
				
				
				String query = "";
				for (int i = 2; i < p.rightTokens.size() + 1; i++) {
					query += p.rightTokens.get(i-2);
					if (this.nullable(query)) {
						HashSet<String> old = set;
						set.addAll(first.get(p.rightTokens.get(i-1)));
						if (!set.equals(old)) {
							again = true;
						}
					}
					
					if (i == p.rightTokens.size()) {
						continue;
					}
					query += " ";
				}
			}
		}
		
		
		return first.get(input);
	}
	
	/*
	 * Public access first method.
	 */
	public HashSet<String> first(String input) {
		ArrayList<String> tokens = this.tokenize(input);

		
		int position = -1;
		for (int i = 0; i < tokens.size(); i++) {
			if (this.nullable(tokens.get(i))) {
				continue;
			}
			if (!this.nullable(tokens.get(i))) {
				position = i;
				break;
			}
		}
		
		
		HashSet<String> set = this.calculateFirst(tokens.get(0));
		
		for (int i = 0; i <= position; i++) {
			set.addAll(this.calculateFirst(tokens.get(i)));
			
		}
		
		
		return set;
		
	}
	
	/*
	 * Helper method for nullable.
	 * 
	 * Handles the calculation and stores into HashMap nullable.
	 * Handles in order of:  terminals, nonterminals, productions
	 */
	private boolean calculateNullable(String input){
		HashMap<String, Boolean> nullable = new HashMap<String, Boolean>();
		
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
			if( s.trim().length() < 1){
				continue;
			}
			else{
				value.add(s);
			}
		}
		return value;
	}
	
	private ArrayList<String>rightTokenize(String production){
		String[] split = production.split("::");
		split = split[1].split(" ");
		
		ArrayList<String> value = new ArrayList<String>();
		for(String s: split){
			if( s.trim().length() < 1 ){
				System.out.println("hit");
				continue;
				
			}
			else {
				value.add(s);
			}
			
		}
		System.out.println(value.toString());
		
		
		return value;
	}
	
	private int nonterminalCount(ArrayList<String> tokens){

		System.out.println(Grammar.nonterminals.size());
		System.out.println(Grammar.terminals.size());
		int count = 0;
		for( String s: Grammar.nonterminals){
			if(tokens.contains(s)){
				count += 1;
			}
		}
		return count;
		
	}
	
	private int nonterminalPosition(ArrayList<String> tokens){
		int position = -1;
		for (int i = 0; i < tokens.size(); i++) {
			String current = tokens.get(i);
			if (Grammar.nonterminals.contains(current)) {
				position = i;
			}
		}
		if (position == -1){
			System.out.println("nonterminal not found");
		}
		return position;
		
	}
	

	
	

}
