package com.splitkit.splitkit;
import java.util.ArrayList;
import java.util.List;

import com.splitkit.exception.SplitException;

/**
 *
 * @author Lukas Munteanu
 */
public class Logic {

	private SplitCalculator calculator = new SplitCalculator();
    
    public Logic(){
        
    }
    
    public SplitDTO computeSplit(List<Person> peopleList, int mode) throws SplitException{
    	List<Person> currentPeopleList = new ArrayList<Person>();
        for (Person person: peopleList){
            currentPeopleList.add(new Person(person));
        }
        switch (mode){
            case 0: 
                break;
            case 1: 
                float sipForEqualShares = 100.0f / (float)peopleList.size();
                for(Person person: currentPeopleList){
                    person.shareInPercent = sipForEqualShares;
                }
                break;
        }
        return this.calculator.computeSplit(currentPeopleList);
    }
}
