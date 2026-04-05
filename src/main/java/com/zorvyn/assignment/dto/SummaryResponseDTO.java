package com.zorvyn.assignment.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SummaryResponseDTO {
    private double totalIncome;
    private double totalExpense;
    private double netBalance;
    private long recordCount;    
    private long incomeCount;    
    private long expenseCount;     
    private double averageIncome;
    private double averageExpense;
    Map<String, Double> categoryTotals;
    
}


    

