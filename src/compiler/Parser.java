package compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
	private Grammar augmentedGrammar;
	private Nullable nullable;
	private First first;
	private Follow follow;
	private Items items;
	private ParsingTable parsingTable;
	
	private ArrayList<HashMap<String, String>> action;
	private ArrayList<HashMap<String, String>> goTo;
	private ArrayList<String> stack;
	private ArrayList<String> input;
	
	
	public Parser() {
		augmentedGrammar = new Grammar();
		augmentedGrammar.augment();
		nullable = new Nullable(augmentedGrammar);
		first = new First(augmentedGrammar, nullable);
		follow = new Follow(augmentedGrammar, first);
		items = new Items(augmentedGrammar);
		parsingTable = new ParsingTable(augmentedGrammar, follow, items);
		action = parsingTable.getActionList();
		goTo = parsingTable.getGoToList();
		stack = new ArrayList<String>();
		input = new ArrayList<String>();
		
	}
	
	public String parse() {
		return "";
	}
	
	private void readSource(String file) {
		
	}
	
	
	
}
