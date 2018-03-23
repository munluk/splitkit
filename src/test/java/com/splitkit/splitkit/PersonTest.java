package com.splitkit.splitkit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PersonTest{
	Person myPerson = new Person("David", 10.0f);
	
	List<Person> peopleList = new ArrayList<Person>();
	
	@Before
	public void before() {
		myPerson.addExpense(25.0f);
		myPerson.addExpense(10.0f);
		myPerson.computeShare(100.0f);
		
		Person personA = new Person("A", 20.0f);
	    personA.addExpense(10.0f);
	    peopleList.add(personA);
	    Person personB = new Person("B", 25.0f);
	    personB.addExpense(12.0f);
	    peopleList.add(personB);
	    Person personC = new Person("C", 25.0f);
	    personC.addExpense(15.0f);
	    peopleList.add(personC);
	    Person personD = new Person("D", 10.0f);
	    personD.addExpense(28.0f);
	    peopleList.add(personD);
	    Person personE = new Person("E", 20.0f);
	    personE.addExpense(35.0f);
	    peopleList.add(personE);
	    
	}
	
	@Test
	public void testComputeTotalExpenses() {
		assertEquals(35.0f, myPerson.getExpenses(), 10e-13);
	}
	
	@Test
	public void testComputeShare() {
		assertEquals(10.0f, myPerson.share, 10e-13);
	}
	
	@Test
	public void testComputeResidual_ResidualPositive() {
		myPerson.computeResidual();
		
		assertEquals(25.0f, myPerson.residual, 10e-13);
	}
	
	@Test
	public void testComputeResidual_ResidualNegative() {
		Person person = new Person(myPerson);
		person.setExpenses(5.0f);
		person.computeResidual();
		
		assertEquals(-5.0f, person.residual, 10e-13);
	}
	
	@Test
	public void testGetPerson_NoPersonInList() {
		List<Person> people = Collections.emptyList();
		
		assertEquals(null, Person.getPerson("Hans", people));
	}
	
	@Test
	public void testGetPerson_PersonNotInList() {
        assertEquals(null, Person.getPerson("Hans", peopleList));
	}
	
	@Test
	public void testGetPerson_PersonInList() {
		assertNotNull("A", Person.getPerson("A", peopleList).name);
		assertEquals("A", Person.getPerson("A", peopleList).name);
	}
	
}
