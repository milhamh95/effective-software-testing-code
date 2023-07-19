package com.estc.java.ch1.chapter1;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class PlanningPokerTest {
	@Test
	void rejectNullInput() {
		PlanningPoker planningPoker = new PlanningPoker();
		var exception = assertThrows(IllegalArgumentException.class, () -> planningPoker.identifyExtremes(null));
		assertEquals("Estimates can't be null", exception.getMessage());
	}
	
	@Test
	void rejectEmptyList() {
		PlanningPoker planningPoker = new PlanningPoker();
		var exception = assertThrows(IllegalArgumentException.class, () -> planningPoker.identifyExtremes(List.of()));
		assertEquals("there has to be more than 1 estimate in the list", exception.getMessage());
	}
	
	@Test
	void rejectSingleEstimate() {
		PlanningPoker planningPoker = new PlanningPoker();
		Estimate estimate = new Estimate("eleanor", 1);
		var exception = assertThrows(IllegalArgumentException.class, () -> planningPoker.identifyExtremes(List.of(estimate)));
		assertEquals("there has to be more than 1 estimate in the list", exception.getMessage());
	}
	
	@Test
	void twoEstimates() {
		List<Estimate> list = Arrays.asList(
				new Estimate("mauriico", 10),
				new Estimate("frank", 5)
		);
		
		List<String> devs = new PlanningPoker().identifyExtremes(list);
		
		assertThat(devs).containsExactlyInAnyOrder("mauriico", "frank");
	}
	
	@Property
	void estimatesInAnyOrder(@ForAll("estimates") List<Estimate> estimates) {
		estimates.add(new Estimate("lowestimate", 1));
		estimates.add(new Estimate("highestimate", 100));
		Collections.shuffle(estimates);
		
		List<String> devs = new PlanningPoker().identifyExtremes(estimates);
		
		assertThat(devs).containsExactlyInAnyOrder("lowestimate", "highestimate");
	}
	
	@Provide
	Arbitrary<List<Estimate>> estimates() {
		Arbitrary<String> names = Arbitraries.strings().withCharRange('a', 'z').ofLength(5);
		Arbitrary<Integer> values = Arbitraries.integers().between(2, 99);
		
		Arbitrary<Estimate> estimates = Combinators.combine(names, values)
				.as(Estimate::new);
		
		return estimates.list().ofMinSize(1);
	}
}
