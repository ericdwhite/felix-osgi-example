package com.whitewerx.felixosgiexample.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class WaterTest {

	Mockery context = new Mockery();

	@Test
	public void shouldHaveAChemicalFormula() {
		
		final Element hydrogen = context.mock(Element.class, "h");
		final Element oxygen = context.mock(Element.class, "o");
		
		context.checking(new Expectations() {{
			one(hydrogen).symbol();
			will(returnValue("h"));
			
			one(oxygen).symbol();
			will(returnValue("o"));
		}});
		
		List<Element> chemicalFormula = new ArrayList<Element>(3);
		chemicalFormula.add(hydrogen);
		chemicalFormula.add(hydrogen);
		chemicalFormula.add(oxygen);
		
		Water h2o = new Water(chemicalFormula);
		String formula = h2o.chemicalFormula();
		assertThat(formula, equalTo("h2o"));
		
		context.assertIsSatisfied();
	}
}
