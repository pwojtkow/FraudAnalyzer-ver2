package com.capgemimni.fraudanalyzer.ver2.filters;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import com.capgemimni.fraudanalyzer.ver2.FilterManager;
import com.capgemimni.fraudanalyzer.ver2.FilterStrategy;
import com.capgemimni.fraudanalyzer.ver2.Transaction;
import com.google.common.collect.Multimap;

public class FilterByUserID extends FilterManager {
	
	private static final int FIRST_SUSPICIOUS_CLINET_ID = 542;
	private static final int SECOND_SUSPICIOUS_CLINET_ID = 1052;
	private static final int THIRD_SUSPICIOUS_CLINET_ID = 2103;
	
	List<Integer> suspiciousClientsID;
	
	public FilterByUserID(){
		suspiciousClientsID.add(FIRST_SUSPICIOUS_CLINET_ID);
		suspiciousClientsID.add(SECOND_SUSPICIOUS_CLINET_ID);
		suspiciousClientsID.add(THIRD_SUSPICIOUS_CLINET_ID);
	}
	
	public List<Transaction> filter(Multimap<Integer,Transaction> transactionsMap, DateTime date) {
		List<Transaction> suspiciousTransactions = new ArrayList<Transaction>();
		for(int i = 0; i < suspiciousClientsID.size(); i++) {
			Integer suspiciousKey = suspiciousClientsID.get(i);
			if(transactionsMap.containsKey(suspiciousKey)) {
				suspiciousTransactions.addAll(transactionsMap.get(suspiciousKey));
			}
		}
		return null;
	}
}
