package com.splitkit.splitkit;

import java.util.List;

public class SplitDTO {
	
	public List<MoneyStream> moneyStreams;
	public List<Person> processedPeopleList;
	public float totalExpenses = 0.0f;
	
	public SplitDTO(List<MoneyStream> moneyStreams, List<Person> processedPeopleList, float totalExpenses) {
		super();
		this.moneyStreams = moneyStreams;
		this.totalExpenses = totalExpenses;
		this.processedPeopleList = processedPeopleList;
	}

	public String asString() {
		if(moneyStreams.isEmpty()) {
			return "No transactions necessary!";
		}else {
			StringBuilder sb = new StringBuilder();
	        sb.append(String.format("Total expenses: %.2f%n%n", this.totalExpenses));
	        moneyStreams.stream().forEach(moneyStream->{
	        	sb.append(String.format("%s pays %s an amount of %.2f%n", moneyStream.org, moneyStream.dst, moneyStream.amount));
	        	});
	        
	        return sb.toString();
		}
	}
}
