package main;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ParsingTable {
	private ArrayList<HashMap<String, String>> actionList;
	private ArrayList<HashMap<String, String>> goToList;
	private Grammar augmentedGrammar;
	private Follow follow;
	private Items items;
	
	
	public ParsingTable(Grammar augmentedGrammar, Follow follow, Items items) {
		this.augmentedGrammar = augmentedGrammar;
		this.follow = follow;
		this.items = items;
		this.actionList = constructActionList(augmentedGrammar, follow, items);
		this.goToList = constructGoToList(augmentedGrammar, items);
	}
	
	private ArrayList<HashMap<String, String>> constructGoToList(Grammar augmentedGrammar, Items items) {
		ArrayList<HashMap<String, String>> goToList = new ArrayList<HashMap<String, String>>();
		ArrayList<ArrayList<Production>> itemList = items.getItems();
		
		for (int itemNumber = 0; itemNumber < itemList.size(); itemNumber++) {
			ArrayList<Production> item = itemList.get(itemNumber);
			HashMap<String, String> itemMap = new HashMap<String, String>();
			
			for (String key: augmentedGrammar.getNonterminals()) {
				ArrayList<Production> nextItem = items.goTo(item, key);
				if (nextItem.isEmpty() == false) {
					int nextItemNumber = items.getItemNumber(nextItem, itemList);
					itemMap.put(key, Integer.toString(nextItemNumber));
				}
				else {
					itemMap.put(key, "");
				}

			}
			goToList.add(itemMap);
		}
		
		return goToList;
	}

	private ArrayList<HashMap<String, String>> constructActionList(Grammar augmentedGrammar, Follow follow, Items items) {
		ArrayList<HashMap<String, String>> actionList = new ArrayList<HashMap<String, String>>();
		HashMap<String, HashSet<String>> followMap = follow.getFollowMap();
		ArrayList<ArrayList<Production>> itemList = items.getItems();
		
		
		//helper variables used in algorithm
		String startTerminal = augmentedGrammar.getNonterminals().get(augmentedGrammar.getNonterminals().size() - 1);
		Production finalProduction = new Production(augmentedGrammar.getProductions().get(0).getFull());
		finalProduction.addDot();
		finalProduction.incrementDot();

		
		for (int itemNumber = 0; itemNumber < itemList.size(); itemNumber++) {
			ArrayList<Production> item = itemList.get(itemNumber);
			HashMap<String, String> itemMap = new HashMap<String, String>();
			
			
			for (Production p: item) {
				String A = p.getLeft();
				String a = p.getTokenNextToDot();
				
				for (String key: augmentedGrammar.getTerminals()) {
					if (key.equals(a)) {
						ArrayList<Production> nextItem = items.goTo(item, a);
						int nextItemNumber = items.getItemNumber(nextItem, itemList);
						String value = "s" + Integer.toString(nextItemNumber) + "/";
						itemMap.put(key, value);
					}
					else {
						itemMap.put(key, "");
					}
				}
				
				if (a == null 
						&& !A.equals(startTerminal)) {
					for (String key: followMap.get(A)) {
						String value = itemMap.getOrDefault(key, "");
						value = value.concat("r" + "/");
						itemMap.put(key, value);
					}
				}
				
				if (p.getFull().equals(finalProduction.getFull())) {
					String value = itemMap.getOrDefault("$", "");
					value = value.concat("acc" + "/");
					itemMap.put("$", value);
				}
			}
			actionList.add(itemMap);
		}
		
		for (int i = 0; i < actionList.size(); i++) {
			for (String key: actionList.get(i).keySet()) {
				String value = actionList.get(i).get(key);
				if (value.length() > 0) {
					actionList.get(i).put(key, value.substring(0, (value.length() - 1)));
				}
			}
		}
		return actionList;
	}




	public ArrayList<HashMap<String, String>> getActionList() {
		return actionList;
	}




	public ArrayList<HashMap<String, String>> getGoToList() {
		// TODO Auto-generated method stub
		return goToList;
	}
	
	
	
	
	
}
