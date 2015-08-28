package compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
		ArrayList<String> nonterminals;
		ArrayList<String> terminals;
		ArrayList<Production> productions;
	
	public Parser() {
		Grammar grammar = new Grammar();
		this.nonterminals = Grammar.nonterminals;
		this.terminals = Grammar.terminals;
		this.productions = Grammar.productions;
	}
	
	public boolean nullable(String input){
		// calculating
		ArrayList<String> tokens = this.tokenize(input);
		HashMap<String, Boolean> nullable = new HashMap<String, Boolean>();
		
		for (String s: this.terminals) {
			nullable.put(s, new Boolean(false));
		}
		
		for (String s: this.nonterminals) {
			System.out.println(s);
			for (Production p: this.productions) {
				if (p.rightTokensContains("empty")) {
					System.out.println(p.toString());
					System.out.println("contains empty");
				}
			}
			
		}


		for (Production p: this.productions) {

	
		}
		return true;
		
	}
	
	
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

		System.out.println(this.nonterminals.size());
		System.out.println(this.terminals.size());
		int count = 0;
		for( String s: this.nonterminals){
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
			if (this.nonterminals.contains(current)) {
				position = i;
			}
		}
		if (position == -1){
			System.out.println("nonterminal not found");
		}
		return position;
		
	}
	

	
	

}
