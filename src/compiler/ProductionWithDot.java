package compiler;

import java.util.ArrayList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/*
 * Sample production  			Ss :: . if ( E ) S else S
 * 				full 			Ss :: . if ( E ) S else S
 * 				left			Ss
 * 				right			. if ( E ) S else S
 * 				rightTokens		[., if, (, E, ), S, else, S]
 * 				
 *				dotLocation 	0
 *				tokenNextOfDot	if
*/

public class ProductionWithDot {
	String full;
	String left;
	String right;
	ArrayList<String> rightTokens;
	
	Integer dotLocation;
	String tokenNextOfDot;
	
	public ProductionWithDot(Production x) {
		this.left = x.left;
		this.full = (x.left + " :: . ");
		this.right = ". ";
		
		this.rightTokens = new ArrayList<String>();
		this.rightTokens.add(".");
		
		
		for (int i = 0; i < x.rightTokens.size(); i++) {
			this.full += x.rightTokens.get(i);
			this.right += x.rightTokens.get(i);
			this.rightTokens.add(x.rightTokens.get(i));
			if (i == x.rightTokens.size()-1) {
				continue;
			}
			this.full += " ";
			this.right += " ";
		}
		
		this.dotLocation = new Integer(0);
		
		if (this.dotLocation.intValue() == this.rightTokens.size() - 1) {
			this.tokenNextOfDot = null;
		}
		else {
			this.tokenNextOfDot = this.rightTokens.get(dotLocation.intValue() + 1);
		}
	}
	
	public ProductionWithDot(ProductionWithDot i) {
		this.full = i.full;
		this.left = i.left;
		this.right = i.right;
		this.rightTokens = i.rightTokens;
		this.dotLocation = i.dotLocation;
		this.tokenNextOfDot = i.tokenNextOfDot;
	}
	
	public void shiftDot() {
		if (this.dotLocation.intValue() == this.rightTokens.size() - 1) {
			System.out.println("Can't shift dot of ItemProduction");
			System.out.println(this.full);
			throw new IndexOutOfBoundsException();
		}
		
		
		this.rightTokens.remove(this.dotLocation.intValue());
		this.dotLocation = new Integer(this.dotLocation.intValue() + 1);
		this.rightTokens.add(this.dotLocation.intValue(), ".");
		
		if (this.dotLocation.intValue() == this.rightTokens.size() - 1) {
			this.tokenNextOfDot = null;
		}
		else {
			this.tokenNextOfDot = this.rightTokens.get(this.dotLocation.intValue()+1);
		}

		this.full = (this.left + " :: ");
		this.right = "";
		for (int i = 0; i < this.rightTokens.size(); i++) {
			this.right += this.rightTokens.get(i);
			this.full += this.rightTokens.get(i);
			if (i == this.rightTokens.size() -1) {
				continue;
			}
			this.right += " ";
			this.full += " ";
		}
	}

	public String toString() {
		return full;
	}
	
	public boolean equals(Object other) {
		if (other == null) { 
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other.getClass() != getClass()) {
			return false;
		}
		
		ProductionWithDot i = (ProductionWithDot) other;
		return new EqualsBuilder()
				.append(this.full, i.full)
				.isEquals();
	}
	
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(this.full)
				.append(this.rightTokens)
				.append(this.tokenNextOfDot)
				.append(this.dotLocation)
				.toHashCode();
	}
	
	
	
}
