package com.splitkit.splitkit;

public class MoneyStream {
	public String org;
    public String dst;
    float amount = 0.0f;
    
    public MoneyStream(String org, String dst, float amount){
        this.org = org;
        this.dst = dst;
        this.amount = amount;
    }
    
    public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}
