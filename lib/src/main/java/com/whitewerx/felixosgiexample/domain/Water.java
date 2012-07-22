package com.whitewerx.felixosgiexample.domain;

import java.util.ArrayList;
import java.util.List;

public class Water {

	private List<Element> chemicalFormula;

	public Water() {
		chemicalFormula = new ArrayList<Element>(3);
		chemicalFormula.add(new Hydrogen());
		chemicalFormula.add(new Hydrogen());
		chemicalFormula.add(new Oxygen());
	}
	
	/**
	 * For testing
	 */
	protected Water(List<Element> chemicalFormula) {
		this.chemicalFormula = chemicalFormula;
	}

	/**
	 * Compresses the formula from E'E'E" to E'2E", e.g. HHO to H2O.
	 */
	public String chemicalFormula() {
		StringBuilder formula = new StringBuilder();
		int count = 0;
		Element lastSeen = null;
		for (Element e : chemicalFormula) {
			if (lastSeen == null)
				lastSeen = e;
			
			if( lastSeen.equals(e)) {
				count++;
				continue;
			}
				
			formula.append(lastSeen.symbol());
			if(count>1)
				formula.append(count);
			count = 1;
			lastSeen = e;
		}
		formula.append(lastSeen.symbol());
		if (count > 1)
			formula.append(count);

		return formula.toString();
	}
}
