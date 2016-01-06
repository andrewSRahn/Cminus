package main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;


public class Parser {
	Grammar grammar;
	Grammar augmentedGrammar;
	Nullable nullable;
	First first;
	Follow follow;
	Items items;
	ParsingTable parsingTable;
	
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
		this.parsingTable = new ParsingTable(augmentedGrammar, follow, items);
		
		printMap(first.getFirstMap(), "first.txt");
		printMap(follow.getFollowMap(), "follow.txt");
		printList(items.getItems(), "items.txt");
		printParsingTable(parsingTable.getActionList(), parsingTable.getGoToList(), "table.txt");
		
		
		
		
	}


	private void printParsingTable(ArrayList<HashMap<String, String>> actionList, ArrayList<HashMap<String, String>> goToList, String file) {
		Path p = Paths.get(System.getProperty("user.dir"), "src", "output", file);
		
		try (Formatter writer = new Formatter(p.toFile())) {
			
			// constructing formatString
			int columnWidth = 0;
			for (String columnHeader: augmentedGrammar.getTerminals()) {
				if (columnHeader.length() > columnWidth) {
					columnWidth = columnHeader.length();
				}
			}
			columnWidth += 2;
			
			String columnWidthString = Integer.toString(columnWidth);
			String formatString = "";
			for (int i = 0; i < augmentedGrammar.getTerminals().size() + 1; i++) {
				formatString += "%" + columnWidthString + "s";
			}
			for (int i = 0; i < augmentedGrammar.getNonterminals().size(); i++) {
				formatString += "%" + columnWidthString + "s";
				if (i == augmentedGrammar.getNonterminals().size() - 1) {
					formatString += "\n";
				}
			}
			
			
			// construct row that contains column headers and print
			int columnSize = augmentedGrammar.getTerminals().size() + augmentedGrammar.getNonterminals().size() + 1;
			Object[] stringArray = new String[columnSize];
			stringArray[0] = "State";
			for (int i = 1; i < augmentedGrammar.getTerminals().size() + 1; i++) {
				stringArray[i] = augmentedGrammar.getTerminals().get(i - 1);
			}
			for (int i = 0; i < augmentedGrammar.getNonterminals().size(); i++) {
				int arrayPosition = i + augmentedGrammar.getTerminals().size() + 1;
				stringArray[arrayPosition] = augmentedGrammar.getNonterminals().get(i);
			}

			writer.format(formatString, stringArray);
			
			
			// construct rest of rows and print
			Object[] keyArray = stringArray.clone();
			if (actionList.size() != goToList.size()) {
				System.out.println("Action list and GoTo list do not match.");
				throw new IllegalStateException();
			}
			int rowSize = actionList.size();
			
			for (int i = 0; i < rowSize; i++) {
				stringArray = new String[columnSize];
				stringArray[0] = Integer.toString(i);
				
				for (int j = 0; j < actionList.size(); j++) {
					HashMap<String, String> actionListColumn = actionList.get(j);
					stringArray[j+1] = actionListColumn.get(keyArray[j+1]);
				}
				
				for (int j = 0; j < goToList.size(); j++) {
					HashMap<String, String> goToListColumn = goToList.get(j);
					int arrayPosition = j + actionList.size() + 1;
					stringArray[arrayPosition] = goToListColumn.get(keyArray[arrayPosition]);
				}
				
				
			}
			
			
			
			writer.format(formatString, stringArray);

			
			
			
		    	
		    writer.close();
		} catch (IOException x) {
			System.out.println("Error writing " + file + ".");
			x.printStackTrace();
		}
		
		
		
		
		
		
		
		
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