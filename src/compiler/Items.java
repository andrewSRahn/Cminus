package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;

public class Items {
	Grammar augmentedGrammar;
	HashMap<Integer, ArrayList<Production>> items;
	
	public Items(Grammar augmentedGrammar) {
		this.augmentedGrammar = augmentedGrammar;
		this.items = constructItems();
	}
	
	

	public HashMap<Integer, ArrayList<Production>> constructItems() {
		ArrayList<ArrayList<Production>> items = new ArrayList<ArrayList<Production>>();
		ArrayList<Production> item0 = new ArrayList<Production>();
		
		Production p = new Production(augmentedGrammar.getProductions().get(0).getFull());
		p.addDot();
		item0.add(p);
		items.add(closure(item0));
		
		ArrayList<String> grammarSymbols = augmentedGrammar.getNonterminals();
		grammarSymbols.addAll(augmentedGrammar.getTerminals());
		
		boolean again = true;
		while (again) {
			again = false;
			
			for (String X: grammarSymbols) {
				for (ListIterator<ArrayList<Production>> listIterator = items.listIterator(); listIterator.hasNext();) {
					ArrayList<Production> currentItem = listIterator.next();
					ArrayList<Production> nextItem = goTo(currentItem, X);
					if (nextItem.isEmpty() == false) {
						if (items.contains(nextItem) == false) {
							again = true;
							listIterator.add(currentItem);
						}
					}
					System.out.println(nextItem);
				}
			}
		}
		
		System.out.println(items.size());

		

		
		
		return new HashMap<Integer, ArrayList<Production>>();
	}
	
	private Integer maxPlusOne(ConcurrentHashMap<Integer, ArrayList<Production>> items) {
		Integer max = -1;
		for (Integer i: items.keySet()) {
			if (i > max) {
				max = i;
			}
		}
		return max + 1;
	}



	public ArrayList<Production> closure(ArrayList<Production> i) {
		ArrayList<Production> j = i;
		
		boolean again = true;
		while (again) { 
			again = false;
			
			for (Production production: this.augmentedGrammar.getProductions()) {
				for (ListIterator<Production> listIterator = j.listIterator(); listIterator.hasNext();) {
					Production A = listIterator.next();
					String B = A.getTokenNextToDot();
					if (production.getLeft().equals(B)) {
						Production candidate = new Production(production.getFull());
						candidate.addDot();
						if(!j.contains(candidate)) {
							listIterator.add(candidate);
							again = true;
						}
					}
				}
			}
		}
		return j;
	}

	public ArrayList<Production> goTo(ArrayList<Production> items, String query) {
		ArrayList<Production> result = new ArrayList<Production>();
		for (Production p: items) {
			if (query.equals(p.getTokenNextToDot())) {
				result.add(p);
			}
		}
		return result;
	}
}
