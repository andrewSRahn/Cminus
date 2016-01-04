package main;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

public class ParsingTable {
	private ArrayList<HashMap<String, String>> actionList;
	private ArrayList<HashMap<String, Integer>> goToList;
	private Grammar augmentedGrammar;
	private Follow follow;
	private Items items;
	
	
	public ParsingTable(Grammar augmentedGrammar, Follow follow, Items items) {
		this.augmentedGrammar = augmentedGrammar;
		this.follow = follow;
		this.items = items;
		actionList = constructActionList(augmentedGrammar, items);
	}


	private ArrayList<HashMap<String, String>> constructActionList(Grammar augmentedGrammar, Items items) {
		ArrayList<ArrayList<Production>> itemList = items.getItems();
		ArrayList<HashMap<String, String>> actionList = new ArrayList<HashMap<String, String>>();
		
		
		
		for (int itemNumber = 0; itemNumber < itemList.size(); itemNumber++) {
			ArrayList<Production> item = itemList.get(itemNumber);
			HashMap<String, String> itemMap = new HashMap<String, String>();
			String entry = "";
			
			for (String s: augmentedGrammar.getTerminals()) {
				for (Production p: item) {
					if (s.equals(p.getTokenNextToDot())) {
						entry = getItemNumber(itemList, items.goTo(item, s));
					}
				}
				if (entry.length() == 0) {
					entry = " ";
				}
			}
			
			
			
			actionList.add(itemMap);
		}
		
		for (int i = 0; i < actionList.size(); i++) {
			System.out.print(i + " ");
			System.out.println(actionList.get(i));
		}


		
		
		return new ArrayList<HashMap<String,String>>();
		
	}


	private String getItemNumber(ArrayList<ArrayList<Production>> items, ArrayList<Production> item) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).containsAll(item)) {
				return Integer.toString(i);
			}
		}
		
		System.out.println("Given item");
		for(Production p: item) {
			System.out.println(p);
		}
		throw new InvalidParameterException();
	}
	
	
	
	
	
}
