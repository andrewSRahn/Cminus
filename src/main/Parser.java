package main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Parser {
	Grammar grammar;
	Grammar augmentedGrammar;
	Nullable nullable;
	First first;
	Follow follow;
	Items items;
	
	HashMap<String, HashSet<String>> firstMap;
	HashMap<String, HashSet<String>> followMap;
	ArrayList<ArrayList<Production>> itemsList;
	
	
	
	
	public Parser() {
		this.grammar = new Grammar();
		this.augmentedGrammar = new Grammar();
		this.augmentedGrammar.augment();
		this.nullable = new Nullable(grammar);
		this.first = new First(grammar, nullable);
		this.follow = new Follow(grammar, first);
		this.items = new Items(augmentedGrammar);
		
		printMap(first.getFirstMap(), "first.txt");
		printMap(follow.getFollowMap(), "follow.txt");
		printList(items.getItems(), "items.txt");
		
		
		
		
	}


	private void printList(ArrayList<ArrayList<Production>> items, String file) {
		Path p = Paths.get(System.getProperty("user.dir"), "src", "output", file);
		
		try (BufferedWriter writer = Files.newBufferedWriter(p)) {
		    for (int i = 0; i < items.size(); i++) {
		    	writer.write("Item " + i);
		    	writer.newLine();

		    	for (Production production: items.get(i)) {
		    		writer.write(production.getFull());
		    		writer.newLine();
		    	}
		    	
		    	writer.write("==========================");
		    	writer.newLine();
		    	
		    }
		    writer.close();
		} catch (IOException x) {
			System.out.println("Error writing " + file + ".");
			x.printStackTrace();
		}
		
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