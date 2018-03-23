package com.splitkit.splitkit;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.splitkit.exception.SplitException;

/**
 *
 * @author Lukas Munteanu
 */
public class SplitCalculator {
	// final static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getName());
    private float totalExpenses = 0.0f;

    public SplitCalculator() {

    }
    
    private void computeTotal(List<Person> peopleList){
        float totalExpenses = 0.0f;
        for(Person curPerson: peopleList){
            totalExpenses += curPerson.getExpenses();
        }
        this.totalExpenses = totalExpenses;
        logger.debug("Total Expenses: %.2f%n", this.totalExpenses);
    }
    
    private void computeShares(List<Person> peopleList){
    	peopleList.forEach(person->{
    		person.computeShare(totalExpenses);
    		person.computeResidual();});
    }
    
    private List<MoneyStream> computeMoneyStreams(List<Person> peopleList){
        ArrayList<MoneyStream> streams = new ArrayList<MoneyStream>();
        deptorComparator compDep = new deptorComparator();
        receiverComparator compRec = new receiverComparator();
        PriorityQueue<Person> receiverQueue = new PriorityQueue<Person>(compRec);
        PriorityQueue<Person> deptorQueue = new PriorityQueue<Person>(compDep);
        
        float totalDept = 0.0f;         // the sum of all peoples dept 
        for(Person person: peopleList){
            if(person.residual > 0.0f){
                person.residual = abs(person.residual);
                receiverQueue.add(person);
                totalDept += person.residual;
            }else if(person.residual < 0.0f){
                person.residual = abs(person.residual);
                deptorQueue.add(person);
            }
        }
        
        if(logger.isDebugEnabled()){
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("[*] TotalDept = %.2f%n", totalDept));
            sb.append(String.format("===== People in reciever Queue ======%n"));
            for(Person person: receiverQueue){
                sb.append(String.format("%s with %.2f\n", person.name, person.residual));
            }
            sb.append(String.format("===== People in deptor Queue ======%n"));
            for(Person person: deptorQueue){
                sb.append(String.format("%s with %.2f\n", person.name, person.residual));
            }
            logger.debug(sb.toString());
        }
        
        Person deptorHead;
        Person receiverHead;
        int counter = 0;
        while(totalDept > 0.0f && counter < 10){
            deptorHead = deptorQueue.peek();
            receiverHead = receiverQueue.peek();
            float tmp = deptorHead.residual - receiverHead.residual;
            // if they are approximately equal
            if(tmp < 10e-6f && tmp > -10e-6f){
                streams.add(new MoneyStream(deptorHead.name, receiverHead.name, receiverHead.residual));
                totalDept -= receiverHead.residual;
                logger.debug("Subtracting %.2f from totalDept", receiverHead.residual);
                deptorQueue.remove();
                receiverQueue.remove();
                logger.debug("Removing Receiver %s  and Deptor %s | TotalDept: %.2f", receiverHead.name, deptorHead.name, totalDept);
            }
            // if deptor has more depth than the receiver gets
            else if (tmp > 10e-6){
                streams.add(new MoneyStream(deptorHead.name, receiverHead.name, receiverHead.residual));
                totalDept -= receiverHead.residual;
                deptorHead.residual -= receiverHead.residual;
                receiverQueue.remove();
                deptorQueue.remove();
                deptorQueue.add(deptorHead);
                logger.debug("Removing Receiver %s and dequeueing Deptor %s | TotalDept: %.2f", receiverHead.name, deptorHead.name, totalDept);
            }
            // if receiver gets more money than deptor has depth
            else if(tmp < -10e-6){
                streams.add(new MoneyStream(deptorHead.name, receiverHead.name, deptorHead.residual));
                totalDept -= deptorHead.residual;
                receiverHead.residual -= deptorHead.residual;
                receiverQueue.remove();
                receiverQueue.add(receiverHead);
                deptorQueue.remove();
                logger.debug("Removing Deptor %s and dequeueing Receiver %s | TotalDept: %.2f", deptorHead.name, receiverHead.name, totalDept);
            }
            counter ++;
        }
        logger.debug("Done: TotalDept = %.2f", totalDept);
        return streams;
    }
    
    /**
     * Computes how the total expenses of all people have to be split
     * @param peopleList List of Person objects among which the expenses are split
     * @return String that shows the money that has to be exchanged between people in the peopleList
     * @throws splitkit.splitCalculator.SplitException 
     */
    public SplitDTO computeSplit(List<Person> peopleList) throws SplitException{
        // check if the shares are set correctly
        float sipCounter = 0.0f;
        for (Person person: peopleList){
            sipCounter += person.shareInPercent;
        }
            logger.debug("Sum of sips: %.2f", sipCounter);
        if(abs(sipCounter - 100.0f) > 2e-2){
            throw new SplitException("Shares are not correct!");
        }
        this.computeTotal(peopleList);
        this.computeShares(peopleList);
        if(logger.isDebugEnabled()){
            StringBuilder sb = new StringBuilder();
            for (Person curPerson: peopleList){
                sb.append(curPerson.varsToString());
            }
            logger.debug(sb.toString());
        }
        List<MoneyStream> moneyStreams = this.computeMoneyStreams(peopleList);
        return new SplitDTO(moneyStreams, this.totalExpenses);
    }
    
    /**
     * Comparator used for priority queue
     */
    private class deptorComparator implements Comparator<Person> {
        public int compare(Person personA, Person personB){
            return (int)(personB.residual - personA.residual);
        }
    }
    
    /**
     * Comparator used for priority queue
     */
    private class receiverComparator implements Comparator<Person> {
        public int compare(Person personA, Person personB){
            return (int)(personA.residual - personB.residual);
        }
    }
}
