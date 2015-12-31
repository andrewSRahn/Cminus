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


public class Parser {
	Grammar grammar;
	Grammar augmentedGrammar;
	Nullable nullable;
	First first;
	Follow follow;
	Items items;
	
	HashMap<String, HashSet<String>> firstMap;
	HashMap<String, HashSet<String>> followMap;
	HashMap<Integer, ArrayList<Production>> itemsMap;
	
	
	
	
	public Parser() {
		this.grammar = new Grammar();
		this.augmentedGrammar = new Grammar();
		this.augmentedGrammar.augment();
		this.nullable = new Nullable(grammar);
		this.first = new First(grammar, nullable);
		this.follow = new Follow(grammar, first);
		
		printMap(first.getFirstMap(), "first.txt");
		printMap(follow.getFollowMap(), "follow.txt");
		
		
		
		
	}


	private void printMap(HashMap<String, HashSet<String>> map, String file) {		
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
	

	

}