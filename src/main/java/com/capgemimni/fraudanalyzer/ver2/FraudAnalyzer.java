package com.capgemimni.fraudanalyzer.ver2;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.Multimap;

public interface FraudAnalyzer {

	public List<Transaction> analyze(List<Transaction> transactionsList, DateTime date);
	
}