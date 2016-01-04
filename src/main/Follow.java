package main;

import java.util.HashMap;
import java.util.HashSet;

public class Follow {
	private Grammar grammar;
	private First first;
	private HashMap<String, HashSet<String>> follow;
	
	public Follow(Grammar grammar, First first) {
		this.grammar = grammar;
		this.first = first;
		this.follow = computeFollow();
	}
	
	private HashMap<String, HashSet<String>> computeFollow() {
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		
		for (String s: grammar.getNonterminals()) {
			map.put(s, new HashSet<String>());
		}
		
		HashSet<String> buffer = new HashSet<String>();
		buffer.add("$");
		map.put(grammar.getProductions().get(0).getLeft(), buffer);
		
		boolean again = true;
		while (again) {
			again = false;
			for (String x: grammar.getNonterminals()) {
				for (Production p: grammar.getProductions()) {
					if (p.getRightTokens().contains(x)) {
						
						int indexOfx = p.getRightTokens().indexOf(x);
						
						
						if (indexOfx == p.getRightTokens().size() - 1) {
							HashSet<String> followOfx = map.get(x);
							
							if (followOfx.containsAll(map.get(p.getLeft())) == false) {
								again = true;
							}
							
							followOfx.addAll(map.get(p.getLeft()));
							map.put(x, followOfx);
						}
						else if (indexOfx < p.getRightTokens().size() - 1) {
							String beta = p.getRightTokens().get(indexOfx + 1);
							if (grammar.getTerminals().contains(beta)) {
								HashSet<String> followOfx = map.get(x);
								
								if (followOfx.contains(beta) == false) {
									again = true;
								}
								
								followOfx.add(beta);
								map.put(x, followOfx);
							}
							else {
								HashSet<String> followOfx = map.get(x);
								HashSet<String> firstOfbeta = this.first.first(beta);
								firstOfbeta.remove("empty");
								
								if (followOfx.containsAll(firstOfbeta) == false) {
									again = true;
								}
								
								followOfx.addAll(firstOfbeta);
								map.put(x, followOfx);
							}
						}
					}
				}
			}
		}
		return map;
	}

	public HashMap<String, HashSet<String>> getFollowMap() {
		return follow;
	}
	


}
