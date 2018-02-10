/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolistgradedexample;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author shree
 */
public class Item <E extends Comparable>  implements Serializable, Comparable<Item <E>>{
    private String label;
    private Date deadLine;
    private SimpleDateFormat fr = new SimpleDateFormat("MM-dd-yyyy");
    private boolean isCompleted;
    private boolean inProgress;
    private String startDate;
    private String completeDate;

    public Item(String label, Date deadLine) {
        this.label = label;
        this.deadLine = deadLine;
        this.isCompleted = false;
        this.inProgress = false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.label);
        hash = 79 * hash + Objects.hashCode(this.deadLine);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        if (!Objects.equals(this.deadLine, other.deadLine)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if(isInProgress()){
            return getLabel() + " by "+ getDeadLine() + "(started on "+ getStartDate() + " ).";
        } 
        else if(isIsCompleted()){
             return getLabel() + " by "+ getDeadLine() + "(finished on "+ getCompleteDate() + " ).";
        }
        
        return getLabel() + " by " + getDeadLine();
    }

    public String getLabel() {
        return label;
    }

    public String getDeadLine() {
        return fr.format(deadLine);
    }

    public boolean isIsCompleted() {
        return this.isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }
    
    public int completeDateComparator(String completedOn){
        
        return (-1)* (this.startDate.compareTo(completedOn));
    }
    
     public int startDateComparator(String startedOn){
        
        return (this.getDeadLine().compareTo(startedOn));
    }

    @Override
    public int compareTo(Item<E> o) {
       return this.label.compareToIgnoreCase(o.label); 
    } 
    
}
