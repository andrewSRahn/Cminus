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
		System.out.println(p.first("E"));
		System.out.println(p.first("id"));
		
	}

}
