package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Nullable {
	Grammar grammar;
	
	public Nullable(Grammar grammar) {
		this.grammar = grammar;
	}
	
	private boolean computeNullable(String input){
		HashMap<String, Boolean> nullable = new HashMap<String, Boolean>();
		
		// empty string
		nullable.put("empty", new Boolean(true));
		
		// terminals
		for (String s: grammar.getTerminals()) {
			nullable.put(s, new Boolean(false));
		}

		// nonterminals
		for (Production p: grammar.getProductions()) {
			if (nullable.containsKey(p.getLeft())) {
				if (nullable.get(p.getLeft()).booleanValue() == true) {
					continue;
				}
			}
			
			if (p.getRightTokens().contains("empty")) {
				nullable.put(p.getLeft(), new Boolean(true));
			}
			else {
				nullable.put(p.getLeft(), new Boolean(false));
			}
		}
		
		// productions
		boolean again = true;
		while (again) {
			for (Production p: grammar.getProductions()) {
				int count = p.getRightTokens().size();
				for (String s: p.getRightTokens()) {
					if (nullable.get(s) == null) {
						count--;
						continue;
					}
					if (nullable.get(s).booleanValue() == true) {
						count--;
					}
				}
				
				if (count < 1) {
					nullable.put(p.getLeft(), new Boolean(true));
					again = true;
				}
				else {
					again = false;
				}	
			}
		}
		return nullable.get(input);
	}
	
	public boolean nullable(String input) {
		ArrayList<String> inputTokens = tokenize(input);
		
		for (String s: inputTokens) {
			try {
				this.computeNullable(s);
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
		else if (this.computeNullable(inputTokens.get(0)) == false) {
			return false;
		}
		else {
			boolean nullable = true;
			
			for (int i = 0; i < inputTokens.size(); i++) {	
				if (this.computeNullable(inputTokens.get(i)) == false) {
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
	
	public ArrayList<String> tokenize(String a){
		if (a.length() == 0) {
			System.out.println("private ArrayList<String> tokenize(String a)");
			System.out.println("a.length() == 0");
			throw new IndexOutOfBoundsException();
		}
		
		String[] split = a.split(" ");
		ArrayList<String> value = new ArrayList<String>();
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
