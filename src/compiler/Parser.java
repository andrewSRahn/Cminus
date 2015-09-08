package compiler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/*
 * Parser contains a grammar.
 * Also defines nullable, first, follow, goto, and closure. 
 * 
 */

/*-------------------------------------------------------------------------------------------------------
 * Section I:  Parser Attributes and Constructor
 * 
 * 
 */
public class Parser {
	Grammar grammar;
	Grammar augmentedGrammar;
	HashMap<String, HashSet<String>> first;
	HashMap<String, HashSet<String>> follow;
	
	
	public Parser() {
		this.grammar = new Grammar();
		augmentGrammar();
		this.first = computeFirst();
		this.follow = computeFollow();

		printMap(this.first, "first.txt");
		printMap(this.follow, "follow.txt");
	}
	

	
/*-------------------------------------------------------------------------------------------------------
 * Section II: Nullable
 * 
 * private boolean computeNullable(String input)
 * public boolean solveNullable(String input)
 */
	
	private boolean computeNullable(String input){
		HashMap<String, Boolean> nullable = new HashMap<String, Boolean>();
		
		// empty string
		nullable.put("empty", new Boolean(true));
		
		// terminals
		for (String s: grammar.terminals) {
			nullable.put(s, new Boolean(false));
		}

		// nonterminals
		for (Production p: grammar.productions) {
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
			for (Production p: grammar.productions) {
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
	
	public boolean solveNullable(String input) {
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

/*-------------------------------------------------------------------------------------------------------
 * Section III: First
 * 
 * private HashMap<String, HashSet<String>> computeFirst();
 * public HashSet<String> solveFirst(String input);
 */
	
	private HashMap<String, HashSet<String>> computeFirst() {
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		
		HashSet<String> hs = new HashSet<String>();
		hs.add("empty");
		map.put("empty", hs);
		
		for (String s: grammar.terminals) {
			HashSet<String> h = new HashSet<String>();
			h.add(s);
			map.put(s, h);
		}
		
		for (String s: grammar.nonterminals) {
			HashSet<String> h = new HashSet<String>();
			map.put(s, h);
		}
		
		boolean again = true;
		while (again) {
			again = false;
			
			for (Production p: grammar.productions) {
				HashSet<String> firstOfx = map.get(p.left);
				HashSet<String> firstOfx0 = map.get(p.rightTokens.get(0));
				
				if (firstOfx.containsAll(firstOfx0) == false) {
					again = true;
				}
				
				firstOfx.addAll(firstOfx0);
				
				for (int i = 1; i < p.rightTokens.size(); i++ ) {
					if (solveNullable(getPrei(p.rightTokens, i))) {
						HashSet<String> firstOfxi = map.get(p.rightTokens.get(i));
						
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
	
	public HashSet<String> solveFirst(String input) {
		ArrayList<String> t = tokenize(input);
		
		if (t.size() == 0) {
			System.out.println("public HashSet<String> solveFirst(String input)");
			System.out.println("invalid input");
			throw new IndexOutOfBoundsException();
		}
		else if (t.size() == 1) {
			return this.first.get(t.get(0));
		}
		else if (t.size() > 1 && solveNullable(t.get(0)) == false) {
			return this.first.get(t.get(0));
		}
		else {
			HashSet<String> firstOfpostx = solveFirst(getPostx0(t));
			HashSet<String> firstOfx0 = this.first.get(t.get(0));
			firstOfx0.addAll(firstOfpostx);
			return firstOfx0;
		}
		
		
	}
/*-------------------------------------------------------------------------------------------------------
 * Section IV: Follow
 * 
 * private HashMap<String, HashSet<<String>> computeFollow()
 * 
 */

	private HashMap<String, HashSet<String>> computeFollow() {
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		
		for (String s: grammar.nonterminals) {
			map.put(s, new HashSet<String>());
		}
		
		HashSet<String> buffer = new HashSet<String>();
		buffer.add("$");
		map.put(grammar.productions.get(0).left, buffer);
		
		boolean again = true;
		while (again) {
			again = false;
			
			for (Production p: grammar.productions) {
				for (int i = 0; i < p.rightTokens.size() - 1; i++) {
					String xi = p.rightTokens.get(i);
					String xposti = getPosti(p.rightTokens, i);
					
					if (grammar.nonterminals.contains(xi)) {
						HashSet<String> followOfxi = map.get(xi);
						HashSet<String> firstOfxposti = solveFirst((xposti));
						
						if (followOfxi.containsAll(firstOfxposti) == false) {
							again = true;
						}
						
						followOfxi.addAll(firstOfxposti);
						map.put(xi, followOfxi);
					}
				}
				
				for (int i = p.rightTokens.size() - 2; i > -1; i--) {
					String xi = p.rightTokens.get(i);
					String xposti = getPosti(p.rightTokens, i);
					
					if (grammar.nonterminals.contains(xi) &&
							solveNullable(xposti) == true) {
						HashSet<String> followOfxi = map.get(xi);
						HashSet<String> followOfx = map.get(p.left);
						
						if (followOfxi.containsAll(followOfx)) {
							again = true;
						}
						
						followOfxi.addAll(followOfx);
					}
				}
			}
			
		}

		
		return map;
	}

/*-------------------------------------------------------------------------------------------------------
 * Section V: Item Sets
 * 
 * private void augmentGrammar() 
 * 
 */
	
	private void augmentGrammar() {
		this.augmentedGrammar = new Grammar();
		
		String l = this.augmentedGrammar.productions.get(0).left + "'";
		l += " :: ";
		l += this.augmentedGrammar.productions.get(0).left;
		
		Production p = new Production(l);
		
		this.augmentedGrammar.productions.add(0, p);
	}
	
	
	
/*-------------------------------------------------------------------------------------------------------
 * Section XI: Utilities
 * 
 * private void printMap(HashMap<String, HashSet<String>> map)
 * private ArrayList<String> tokenize(String a)
 * private String getPostx0(ArrayList<String> tokens)
 * private String getPrei(ArrayList<String> tokens)
 */
	private void printMap(HashMap<String, HashSet<String>> map, String file) {		
		Path p = Paths.get(System.getProperty("user.dir"), "src", "output", file);
		
		try (BufferedWriter writer = Files.newBufferedWriter(p)) {
		    for (String s: grammar.nonterminals) {
		    	writer.write(s + ": " + map.get(s));
		    	writer.newLine();
		    }
		    writer.close();
		} catch (IOException x) {
			System.out.println("Error writing " + file + ".");
			x.printStackTrace();
		}
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
	
	private String getPosti(ArrayList<String> tokens, int i) {
		if (tokens.size() == 0) {
			System.out.println("private String getPosti(ArrayList<String> tokens, int i)");
			System.out.println("tokens.size() == 0");
			throw new IndexOutOfBoundsException();
		}
		
		String s = "";
		for (int j = i + 1; j < tokens.size(); j++) {
			s += tokens.get(j);
			if (j == tokens.size()-1) {
				break;
			}
			s += " ";
		}
		return s;
	}
}