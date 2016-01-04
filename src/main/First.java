package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class First {
	private HashMap<String, HashSet<String>> first;
	private Grammar grammar;
	private Nullable nullable;
	
	public First(Grammar grammar, Nullable nullable) {
		this.grammar = grammar;
		this.nullable = nullable;
		this.first = computeFirst();
	}
	
	public HashSet<String> first(String input) {
		ArrayList<String> t = tokenize(input);
		
		if (t.size() == 0) {
			System.out.println("public HashSet<String> solveFirst(String input)");
			System.out.println("invalid input");
			throw new IndexOutOfBoundsException();
		}
		else if (t.size() == 1) {
			return this.first.get(t.get(0));
		}
		else if (t.size() > 1 && nullable.nullable(t.get(0)) == false) {
			return this.first.get(t.get(0));
		}
		else {
			HashSet<String> firstOfpostx = first(getPostx0(t));
			HashSet<String> firstOfx0 = this.first.get(t.get(0));
			firstOfx0.addAll(firstOfpostx);
			return firstOfx0;
		}
	}
	
	private HashMap<String, HashSet<String>> computeFirst() {
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		
		HashSet<String> hs = new HashSet<String>();
		hs.add("empty");
		map.put("empty", hs);
		
		for (String s: grammar.getTerminals()) {
			HashSet<String> h = new HashSet<String>();
			h.add(s);
			map.put(s, h);
		}
		
		for (String s: grammar.getNonterminals()) {
			HashSet<String> h = new HashSet<String>();
			map.put(s, h);
		}
		
		boolean again = true;
		while (again) {
			again = false;
			
			for (Production p: grammar.getProductions()) {
				HashSet<String> firstOfx = map.get(p.getLeft());
				HashSet<String> firstOfx0 = map.get(p.getRightTokens().get(0));
				
				if (firstOfx.containsAll(firstOfx0) == false) {
					again = true;
				}
				
				firstOfx.addAll(firstOfx0);
				
				for (int i = 1; i < p.getRightTokens().size(); i++ ) {
					if (nullable.nullable(getPrei(p.getRightTokens(), i))) {
						HashSet<String> firstOfxi = map.get(p.getRightTokens().get(i));
						
						if (firstOfx.containsAll(firstOfxi) == false) {
							again = true;
						}
						
						firstOfx.addAll(firstOfxi);
					}
				}
			}
		}
		return map;
	}
	
	
	
	private ArrayList<String> tokenize(String a){
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
	
	private String getPrei(ArrayList<String> tokens, int i) {
		if (tokens.size() == 0) {
			System.out.println("private String getPrei(ArrayList<String> tokens)");
			System.out.println("tokens.size() == 0");
			throw new IndexOutOfBoundsException();
		}
		
		String s = "";
		for (int j = 0; j < i; j++) {
			s += tokens.get(j);
			if (j == i-1) {
				break;
			}
			s += " ";
		}
		return s;
	}
	
	private String getPostx0(ArrayList<String> tokens) {
		if (tokens.size() == 0) {
			System.out.println("private String getPostx0(ArrayList<String> tokens)");
			System.out.println("tokens.size() == 0");
			throw new IndexOutOfBoundsException();
		}
		
		String s = "";
		for (int i = 1; i < tokens.size(); i++) {
			s += tokens.get(i);
			if (i == tokens.size()-1) {
				break;
			}
			s += " ";
		}
		return s;
	}

	public HashMap<String, HashSet<String>> getFirstMap() {
		return first;
	}
	
}
