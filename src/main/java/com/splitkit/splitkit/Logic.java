package com.splitkit.splitkit;
import java.util.ArrayList;
import com.splitkit.exception.SplitException;
import javafx.collections.ObservableList;

/**
 *
 * @author Lukas Munteanu
 */
public class Logic {
    
    private SplitCalculator calculator = new SplitCalculator();
            
    public Logic(){
        
    }
    
    public SplitDTO computeSplit(ObservableList<Person> peopleList, int mode) throws SplitException{
        ArrayList<Person> newPeopleList = new ArrayList<Person>();
        for (Person person: peopleList){
            newPeopleList.add(new Person(person));
        }
//        String outputString;
        switch (mode){
            case 0: // standard split using sips
                break;
            case 1: // split equally -> overwrite the sips
                float sip = 100.0f / (float)peopleList.size();
                for(Person person: newPeopleList){
                    person.shareInPercent = sip;
                }
                break;
        }
//        outputString = this.calculator.computeSplit(newPeopleList);
//        return outputString;
        return this.calculator.computeSplit(newPeopleList);
    }
}
