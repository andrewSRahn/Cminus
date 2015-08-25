package compiler;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Grammar {
	public static ArrayList<String> terminals = new ArrayList<String>();
	public static ArrayList<String> nonterminals = new ArrayList<String>();
	public static HashMap<String, ArrayList<String>> productions = new HashMap<String, ArrayList<String>>();
	
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
				nonterminals.add(s.next());
			}
		} catch (FileNotFoundException e) {
			System.out.println("nonterminal read has failed");
			e.printStackTrace();
		}
		
		try {
			s = new Scanner(new BufferedReader(new FileReader("src\\input\\terminals.txt")));
			while (s.hasNext()) {
				terminals.add(s.next());
			}
		} catch (FileNotFoundException e) {
			System.out.println("terminal read has failed");
			e.printStackTrace();
		}
		
		try {
			s = new Scanner(new BufferedReader(new FileReader("src\\input\\productions.txt")));
			while (s.hasNext()) {
				String current = s.nextLine();
				String[] split = current.split("::");
				
				String left = split[0];
				String right = split[1];
				
				if(productions.containsKey(left)){
					ArrayList<String> old = productions.get(left);
					old.add(right);
					productions.put(left, old);
				}
				else{
					ArrayList<String> o = new ArrayList<String>();
					o.add(right);
					productions.put(left, o);
				}
				
			}
		} catch (FileNotFoundException e) {
			System.out.println("production read has failed");
			e.printStackTrace();
		}
		s.close();
	}

	

}
