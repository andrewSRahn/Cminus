package main;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Grammar {
	private ArrayList<String> terminals = new ArrayList<String>();
	private ArrayList<String> nonterminals = new ArrayList<String>();
	private ArrayList<Production> productions = new ArrayList<Production>();
	
	public Grammar() {
		this.read();
	}
	
	private void read(){	
	//////////////////////////////////////////////////////////////
	// reads in nonterminals, then terminals, then productions
	//////////////////////////////////////////////////////////////
	
		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader("src\\input\\nonterminals.txt")));
			while (s.hasNext()) {
				getNonterminals().add(s.next());
			}
		} catch (FileNotFoundException e) {
			System.out.println("nonterminal read has failed");
			e.printStackTrace();
		}
		
		try {
			s = new Scanner(new BufferedReader(new FileReader("src\\input\\terminals.txt")));
			while (s.hasNext()) {
				getTerminals().add(s.next());
			}
		} catch (FileNotFoundException e) {
			System.out.println("terminal read has failed");
			e.printStackTrace();
		}
		
		try {
			s = new Scanner(new BufferedReader(new FileReader("src\\input\\productions.txt")));
			while (s.hasNext()) {
				getProductions().add(new Production(s.nextLine()));
			}
		} catch (FileNotFoundException e) {
			System.out.println("production read has failed");
			e.printStackTrace();
		}
		s.close();
	}
	
	
	public void augment() {
		String l = this.getProductions().get(0).getLeft() + "'";
		getNonterminals().add(l);
		l += " :: ";
		l += this.getProductions().get(0).getLeft();
		Production p = new Production(l);
		getProductions().add(0, p);
		
	}

	public ArrayList<String> getTerminals() {
		return terminals;
	}

	public void setTerminals(ArrayList<String> terminals) {
		this.terminals = terminals;
	}

	public ArrayList<Production> getProductions() {
		return productions;
	}

	public void setProductions(ArrayList<Production> productions) {
		this.productions = productions;
	}

	public ArrayList<String> getNonterminals() {
		return nonterminals;
	}

	public void setNonterminals(ArrayList<String> nonterminals) {
		this.nonterminals = nonterminals;
	}
}
