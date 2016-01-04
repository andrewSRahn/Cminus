package main;

import java.util.ArrayList;
import java.util.ListIterator;

public class Items {
	Grammar augmentedGrammar;
	ArrayList<ArrayList<Production>> items;
	
	public Items(Grammar augmentedGrammar) {
		this.augmentedGrammar = augmentedGrammar;
		this.items = constructItems();
	}
	
	public ArrayList<ArrayList<Production>> constructItems() {
		ArrayList<ArrayList<Production>> items = new ArrayList<ArrayList<Production>>();
		ArrayList<Production> item0 = new ArrayList<Production>();
		
		Production p = new Production(augmentedGrammar.getProductions().get(0).getFull());
		p.addDot();
		item0.add(p);
		items.add(closure(item0));
		
		ArrayList<String> grammarSymbols = new ArrayList<String>();
		grammarSymbols.addAll(augmentedGrammar.getNonterminals());
		grammarSymbols.addAll(augmentedGrammar.getTerminals());
		
		boolean again = true;
		while (again) {
			again = false;
			
			for (String X: grammarSymbols) {
				for (ListIterator<ArrayList<Production>> listIterator = items.listIterator(); listIterator.hasNext();) {
					ArrayList<Production> currentItem = listIterator.next();
					ArrayList<Production> nextItem = goTo(currentItem, X);
					if (nextItem.isEmpty() == false) {
						if (doesContainDuplicateItem(items, nextItem) == false) {
							again = true;
							listIterator.add(nextItem);
						}
					}
				}
			}
		}
		
		return items;
	}
	
	private boolean doesContainDuplicateItem(
			ArrayList<ArrayList<Production>> items,
			ArrayList<Production> item) {
		for (ArrayList<Production> i: items) {
			if (i.containsAll(item)) {
				return true;
			}
		}
		return false;
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
				Production o = new Production(p.getFull());
				o.incrementDot();
				result.add(o);
			}
		}
		
		return closure(result);
	}
	
	public ArrayList<ArrayList<Production>> getItems() {
		return items;
	}
}
