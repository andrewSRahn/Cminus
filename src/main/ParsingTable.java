package main;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
		this.actionList = constructActionList(augmentedGrammar, follow, items);
	}


	private ArrayList<HashMap<String, String>> constructActionList(Grammar augmentedGrammar, Follow follow, Items items) {
		ArrayList<HashMap<String, String>> actionList = new ArrayList<HashMap<String, String>>();
		HashMap<String, HashSet<String>> followMap = follow.getFollowMap();
		ArrayList<ArrayList<Production>> itemList = items.getItems();
		
		
		String startTerminal = augmentedGrammar.getNonterminals().get(augmentedGrammar.getNonterminals().size() - 1);
		
		for (int itemNumber = 0; itemNumber < itemList.size(); itemNumber++) {
			ArrayList<Production> item = itemList.get(itemNumber);
			HashMap<String, String> itemMap = new HashMap<String, String>();
			String itemMapValue = "";
			
			for (Production p: item) {
				String A = p.getLeft();
				String a = p.getTokenNextToDot();
				
				if (A.equals(startTerminal)) {
					itemMapValue = 
				}
				
			}
			
	
			
			
			
			actionList.add(itemMap);
		}
		
//		for (int i = 0; i < actionList.size(); i++) {
//			System.out.print(i + " ");
//			System.out.println(actionList.get(i));
//		}


		
		
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
