package compiler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;

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
	HashMap<String, HashSet<String>> first;
	HashMap<String, HashSet<String>> follow;
	
	Grammar augmentedGrammar;
	HashMap<Integer, ArrayList<ProductionWithDot>> items;
	
	
	public Parser() {
		this.grammar = new Grammar();
		this.first = computeFirst();
		this.follow = computeFollow();
		printMap(this.first, "first.txt");
		printMap(this.follow, "follow.txt");
		
		this.augmentedGrammar = new Grammar();
		this.augmentedGrammar.augment();
		
		this.items = computeItems();
		
		
		
		
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
					if (solveNullable(getPrei(p.getRightTokens(), i))) {
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
		
		for (String s: grammar.getNonterminals()) {
			map.put(s, new HashSet<String>());
		}
		
		HashSet<String> buffer = new HashSet<String>();
		buffer.add("$");
		map.put(grammar.getProductions().get(0).getLeft(), buffer);
		
		boolean again = true;
		while (again) {
			again = false;
			for (String x: grammar.getNonterminals()) {
				for (Production p: grammar.getProductions()) {
					if (p.getRightTokens().contains(x)) {
						
						int indexOfx = p.getRightTokens().indexOf(x);
						
						
						if (indexOfx == p.getRightTokens().size() - 1) {
							HashSet<String> followOfx = map.get(x);
							
							if (followOfx.containsAll(map.get(p.getLeft())) == false) {
								again = true;
							}
							
							followOfx.addAll(map.get(p.getLeft()));
							map.put(x, followOfx);
						}
						else if (indexOfx < p.getRightTokens().size() - 1) {
							String beta = p.getRightTokens().get(indexOfx + 1);
							if (grammar.getTerminals().contains(beta)) {
								HashSet<String> followOfx = map.get(x);
								
								if (followOfx.contains(beta) == false) {
									again = true;
								}
								
								followOfx.add(beta);
								map.put(x, followOfx);
							}
							else {
								HashSet<String> followOfx = map.get(x);
								HashSet<String> firstOfbeta = this.first.get(beta);
								firstOfbeta.remove("empty");
								
								if (followOfx.containsAll(firstOfbeta) == false) {
									again = true;
								}
								
								followOfx.addAll(firstOfbeta);
								map.put(x, followOfx);
							}
						}
					}
				}
			}
		}
		return map;
	}

/*-------------------------------------------------------------------------------------------------------
 * Section V: Item Sets
 * 
 * 
 * 
 */
	
	private HashMap<Integer, ArrayList<Production>> computeItems() {
		HashMap<Integer, ArrayList<Production>> items = new HashMap<Integer, ArrayList<Production>>();
		ArrayList<Production> item0 = new ArrayList<Production>();
		
		System.out.println(items.toString());
		
		return new  HashMap<Integer, ArrayList<Production>>();
	}
	
	private ArrayList<Production> closure(ArrayList<Production> i) {
		ArrayList<Production> j = i;
		
		boolean again = true;
		while (again) { 
			again = false;
			
			for (Production b: this.augmentedGrammar.getProductions()){
				for (ListIterator<ProductionWithDot> a = j.listIterator(); a.hasNext();) {
					ProductionWithDot c = a.next();
					String Btoken = c.getTokenNextOfDot();
					Production Bproduction = null;
					
					if (b.getLeft().equals(Btoken)) {
						Bproduction = b;
					}
					else {
						continue;
					}
					
					ProductionWithDot Bitem = new ProductionWithDot(Bproduction);
					
					boolean flag = false;
					for (ProductionWithDot x: j) {
						if (Bitem.getFull().equals(x.getFull())) {
							flag = true;
						}
					}
					
					if (flag == true) {
						continue;
					}
					else {
						a.add(Bitem);
						again = true;
					}
				}
			}
		}
		return j;
	}

	private ArrayList<ProductionWithDot> goTo(ArrayList<ProductionWithDot> i, String token) {
		ArrayList<ProductionWithDot> j = new ArrayList<ProductionWithDot>();
		
		for (ProductionWithDot k: i) {
			if (token.equals(k.getTokenNextOfDot())) {
				ProductionWithDot l = new ProductionWithDot(k);
				l.shiftDot();

				if (itemCheck(i, l) == false) {
					j.add(l);
				}
			}
		}
		
		j = closure(j);
		
		return j;
	}


/*-------------------------------------------------------------------------------------------------------
 * Section XI: Utilities
 * 
 * private void printMap(HashMap<String, HashSet<String>> map)
 * private ArrayList<String> tokenize(String a)
 * private String getPostx0(ArrayList<String> tokens)
 * private String getPrei(ArrayList<String> tokens)
 * 
 */
	public void printMap(HashMap<String, HashSet<String>> map, String file) {		
		Path p = Paths.get(System.getProperty("user.dir"), "src", "output", file);
		
		try (BufferedWriter writer = Files.newBufferedWriter(p)) {
		    for (String s: grammar.getNonterminals()) {
		    	writer.write(s + ": " + map.get(s));
		    	writer.newLine();
		    }
		    writer.close();
		} catch (IOException x) {
			System.out.println("Error writing " + file + ".");
			x.printStackTrace();
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

	public String getPostx0(ArrayList<String> tokens) {
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
	
	public String getPrei(ArrayList<String> tokens, int i) {
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
	
	public String getPosti(ArrayList<String> tokens, int i) {
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
	
	public boolean itemCheck(ArrayList<ProductionWithDot> a, ProductionWithDot b) {
		for (ProductionWithDot c: a) {
			if (c.equals(b)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean itemsCheck(HashMap<Integer, ArrayList<ProductionWithDot>> a, ArrayList<ProductionWithDot> b) {
		for (ArrayList<ProductionWithDot> c: a.values()) {
			if (b.containsAll(c)) {
				return true;
			}
			
		}
		return false;
	}
}