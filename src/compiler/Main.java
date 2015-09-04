/*
 * Cminus compiler 
 * 
 * Grammar
 * Parser
 * 
 * 
 */

package compiler;



public class Main {

	public static void main(String[] args) {
		Parser p = new Parser();
		System.out.println(p.first("Ss"));
		System.out.println(p.first("empty"));
		System.out.println(p.first("void"));
	}

}
