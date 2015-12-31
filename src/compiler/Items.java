package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class Items {
	Grammar augmentedGrammar;
	HashMap<Integer, ArrayList<Production>> items;
	
	public Items(Grammar augmentedGrammar) {
		this.augmentedGrammar = augmentedGrammar;
		this.items = constructItems();
	}
	
	

	private HashMap<Integer, ArrayList<Production>> constructItems() {
		HashMap<Integer, ArrayList<Production>> items = new HashMap<Integer, ArrayList<Production>>();
		ArrayList<Production> item0 = new ArrayList<Production>();
		
		Production p = new Production(augmentedGrammar.getProductions().get(0));
		
		p.addDot();

		
		item0.add(p);
		
		
		closure(item0);
		
		
		return new  HashMap<Integer, ArrayList<Production>>();
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
					
					System.out.println(production.getRightTokens());
					
					if (production.getLeft().equals(B)) {
						Production candidate = new Production(production);
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

	private ArrayList<Production> goTo(ArrayList<Production> i, String token) {
		ArrayList<Production> j = new ArrayList<Production>();
		
		
		
		return j;
	}
}
