package cordova.plugin.ekyc;

import java.lang.String;



public class PersonalDetailsModelClass{
    PersonalDetailsModelClass(){}
    private String quotationNumber, proposerName, adhaarNumber, planName;

    public void setQuotationNumber(String quotationNumber){
        this.quotationNumber = quotationNumber;
    }

    public void setProposerName(String proposerName){
        this.proposerName = proposerName;
    }

    public void setAdhaarNumber(String adhaarNumber){
        this.adhaarNumber = adhaarNumber;
    }

    public void setPlanName(String planName){
        this.planName = planName;
    }

    public String getQuotationNumber(){
        return this.quotationNumber;
    }

    public String getProposerName(){
        return this.proposerName;
    }

    public String getAdhaarNumber(){
        return this.adhaarNumber;
    }

    public String getPlanName(){
        return this.planName;
    }
    
}

