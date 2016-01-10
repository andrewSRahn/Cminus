package compiler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
	static Grammar grammar;
	static Grammar augmentedGrammar;
	static Nullable nullable;
	static First first;
	static Follow follow;
	static Items items;
	static ParsingTable parsingTable;
	
	HashMap<String, HashSet<String>> firstMap;
	HashMap<String, HashSet<String>> followMap;
	ArrayList<ArrayList<Production>> itemsList;
	
	
	
	
	public static void main(String[] args) {
		grammar = new Grammar();
		augmentedGrammar = new Grammar();
		augmentedGrammar.augment();
		nullable = new Nullable(grammar);
		first = new First(grammar, nullable);
		follow = new Follow(grammar, first);
		items = new Items(augmentedGrammar);
		parsingTable = new ParsingTable(augmentedGrammar, follow, items);
		
		printMap(first.getFirstMap(), "first.txt");
		printMap(follow.getFollowMap(), "follow.txt");
		printList(items.getItems(), "items.txt");
		printParsingTable(parsingTable.getActionList(), parsingTable.getGoToList(), "table.txt");
		
		
		
		
	}
	


	private static void printParsingTable(ArrayList<HashMap<String, String>> actionList, ArrayList<HashMap<String, String>> goToList, String file) {
		if (actionList.size() != goToList.size()) {
			System.out.println("Action list and GoTo list lengths do not match.");
			throw new IllegalStateException();
		}
		
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
			Object[] rowArray = new String[columnSize];
			rowArray[0] = "State";
			for (int i = 1; i < augmentedGrammar.getTerminals().size() + 1; i++) {
				rowArray[i] = augmentedGrammar.getTerminals().get(i - 1);
			}
			for (int i = 0; i < augmentedGrammar.getNonterminals().size(); i++) {
				int arrayPosition = i + augmentedGrammar.getTerminals().size() + 1;
				rowArray[arrayPosition] = augmentedGrammar.getNonterminals().get(i);
			}

			writer.format(formatString, rowArray);
			
			
			// construct rest of rows and print
			int actionListIndex = augmentedGrammar.getTerminals().size() + 1;
			int goToListIndex = actionListIndex + augmentedGrammar.getTerminals().size() + 1;
			Object[] actionListKeys = Arrays.copyOfRange(rowArray, 1, actionListIndex);
			Object[] goToListKeys = Arrays.copyOfRange(rowArray, actionListIndex, goToListIndex);
			
			for (int i = 0; i < actionList.size(); i++) {
				rowArray = new String[columnSize];
				rowArray[0] = Integer.toString(i);
				
				HashMap<String, String> actionMap = actionList.get(i);
				HashMap<String, String> goToMap = goToList.get(i);
				 
				int index = 1;
				for (Object s: actionListKeys) {
					rowArray[index] = actionMap.get(s);
					index++;
				}
				for (Object s: goToListKeys) {
					rowArray[index] = goToMap.get(s);
					index++;
				}

				writer.format(formatString, rowArray);
			}
			
			
			

		    writer.close();
		} catch (IOException x) {
			System.out.println("Error writing " + file + ".");
			x.printStackTrace();
		}
	}


	private static void printList(ArrayList<ArrayList<Production>> items, String file) {
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


	private static void printMap(HashMap<String, HashSet<String>> map, String file) {		
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