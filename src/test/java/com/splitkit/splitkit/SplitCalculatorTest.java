package com.splitkit.splitkit;



import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.junit.Before;
import org.junit.Test;

import com.splitkit.exception.SplitException;

public class SplitCalculatorTest {
	SplitCalculator splitCalculator = new SplitCalculator();
	List<Person> personList = new ArrayList<Person>();

	@Before
	public void before() {
		Person personA = new Person("A", 20.0f);
        personA.addExpense(10.0f);
        personList.add(personA);
        Person personB = new Person("B", 25.0f);
        personB.addExpense(12.0f);
        personList.add(personB);
        Person personC = new Person("C", 25.0f);
        personC.addExpense(15.0f);
        personList.add(personC);
        Person personD = new Person("D", 10.0f);
        personD.addExpense(28.0f);
        personList.add(personD);
        Person personE = new Person("E", 20.0f);
        personE.addExpense(35.0f);
        personList.add(personE);
	}
	
	@Test
	public void testComputeTotal() throws SplitException {
		assertEquals(100.0f,  splitCalculator.computeSplit(personList).totalExpenses, 10e-13);
	}
	
	@Test
	public void testComputeSplit_checkIfTheStreamsAreComputedCorrectly() throws SplitException {
		SplitDTO splitDTO = splitCalculator.computeSplit(personList);
		
//		assertEquals(0.0f, , 10e-13);
	}
	
}
