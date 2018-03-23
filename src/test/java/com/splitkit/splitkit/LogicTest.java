package com.splitkit.splitkit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.splitkit.exception.SplitException;

public class LogicTest {
	private Logic logic = new Logic();
	List<Person> personList = new ArrayList<>();
	
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
	
	@Test(expected = SplitException.class)  
	public void 
	testComputeSplit_withEmptyListAsArgument() throws SplitException {
		int splitMode = 0;
		logic.computeSplit(Collections.emptyList(), splitMode);
	}
	
	@Test
	public void testComputeSplit_checkIfResidualsAfterSplitAreCorrect() throws SplitException {
		int splitMode = 0;
		SplitDTO splitDTO = logic.computeSplit(personList, splitMode);
		
		Person personA = Person.getPerson("A", splitDTO.processedPeopleList);
		Person personB = Person.getPerson("B", splitDTO.processedPeopleList);
		Person personC = Person.getPerson("C", splitDTO.processedPeopleList);
		Person personD = Person.getPerson("D", splitDTO.processedPeopleList);
		Person personE = Person.getPerson("E", splitDTO.processedPeopleList);
		
		assertEquals(-10.0f, personA.residual, 10e-13);
		assertEquals(-13.0f, personB.residual, 10e-13);
		assertEquals(-10.0f, personC.residual, 10e-13);
		assertEquals(18.0f, personD.residual, 10e-13);
		assertEquals(15.0f, personE.residual, 10e-13);
	}
	
	@Test
	public void testComputeSplit_checkIfResidualsAfterEqualSplitAreCorrect() throws SplitException {
		int splitMode = 1;
		SplitDTO splitDTO = logic.computeSplit(personList, splitMode);
		
		Person personA = Person.getPerson("A", splitDTO.processedPeopleList);
		Person personB = Person.getPerson("B", splitDTO.processedPeopleList);
		Person personC = Person.getPerson("C", splitDTO.processedPeopleList);
		Person personD = Person.getPerson("D", splitDTO.processedPeopleList);
		Person personE = Person.getPerson("E", splitDTO.processedPeopleList);
		
		float equalShare = splitDTO.totalExpenses / ((float)splitDTO.processedPeopleList.size());
		
		assertEquals(personA.getExpenses()-equalShare, personA.residual, 10e-13);
		assertEquals(personB.getExpenses()-equalShare, personB.residual, 10e-13);
		assertEquals(personC.getExpenses()-equalShare, personC.residual, 10e-13);
		assertEquals(personD.getExpenses()-equalShare, personD.residual, 10e-13);
		assertEquals(personE.getExpenses()-equalShare, personE.residual, 10e-13);
	}
}
