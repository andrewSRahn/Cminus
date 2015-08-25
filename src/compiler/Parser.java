package compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
		ArrayList<String> nonterminals;
		ArrayList<String> terminals;
		HashMap<String, ArrayList<String>> productions;
	
	public Parser() {
		Grammar grammar = new Grammar();
		this.nonterminals = grammar.nonterminals;
		this.terminals = grammar.terminals;
		this.productions = grammar.productions;
		
		
		
	}
	
	public boolean nullable(String a){
		boolean nullable;
		
		
		
		
		return nullable;
		
	}
	
	private ArrayList<String>rightTokenize(String input){
		String[] split = input.split("::");
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
