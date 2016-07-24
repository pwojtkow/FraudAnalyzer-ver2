package com.capgemimni.fraudanalyzer.ver2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import com.capgemimni.fraudanalyzer.ver2.filters.FilterBigAmountTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterByUserID;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterFrequentTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterFrequentTransactionsToOne;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterSmallAmountTransactions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Bank implements FraudAnalyzer {

	private FilterManager filterManager;
	private List<Transaction> suspiciousTransactions;
	private Multimap<Integer, Transaction> transactionsMap = ArrayListMultimap.create();
	
	public List<Transaction> analyze(List<Transaction> transactionsList, DateTime date) {
		suspiciousTransactions = new ArrayList<Transaction>();
		List<Transaction> correctDateTransactionsList = filterByDate(transactionsList, date);
		putListIntoMap(correctDateTransactionsList, transactionsMap);
		
		removeIgnoredTransactions(transactionsMap);
		
		useFilters(transactionsMap);
		
		return suspiciousTransactions;
	}

		public void removeIgnoredTransactions(Multimap<Integer, Transaction> transactionsMap) {
			
			final int FIRST_IGNORED_CLIENT_ID = 101;
			final int SECOND_IGNORED_CLIENT_ID = 606;
			
			if (transactionsMap.containsKey(FIRST_IGNORED_CLIENT_ID)) {
				transactionsMap.removeAll(FIRST_IGNORED_CLIENT_ID);
			}
			if (transactionsMap.containsKey(SECOND_IGNORED_CLIENT_ID)) {
				transactionsMap.removeAll(SECOND_IGNORED_CLIENT_ID);
			}
		}
	
	private void useFilters(Multimap<Integer, Transaction> transactionsMap) {
		int counter = 0;
		int numberOfFilters = 5;
		
		while (counter < numberOfFilters) {
			if (counter == 0) {
				filterManager = new FilterByUserID();
			} else if (counter == 1) {
				filterManager = new FilterSmallAmountTransactions();
			} else if (counter == 2) {
				filterManager = new FilterBigAmountTransactions();
			} else if (counter == 3) {
				filterManager = new FilterFrequentTransactionsToOne();
			} else if (counter == 4) {
				filterManager = new FilterFrequentTransactions();
			} else {
				filterManager = null;
			}
			
			suspiciousTransactions.addAll(filterManager.filter(transactionsMap));
			counter++;
		}
	}

	private void putListIntoMap(List<Transaction> ignoredCliendIdList, Multimap<Integer, Transaction> transactionsMap) {
		Integer serdnerId;
		for (int i = 0; i < ignoredCliendIdList.size(); i++) {
			serdnerId = ignoredCliendIdList.get(i).getSenderId();
			transactionsMap.put(serdnerId, ignoredCliendIdList.get(i));
		}
	}

	private List<Transaction> filterByDate(List<Transaction> transactionsList, DateTime date) {
		List<Transaction> correctDateTransactionList;
		
		correctDateTransactionList = transactionsList.stream().filter(t -> t.getDate().toLocalDate().compareTo(date.toLocalDate()) == 0)
				.collect(Collectors.toList());
		return correctDateTransactionList;
	}

	public Multimap<Integer, Transaction> getTransactionsMap() {
		return transactionsMap;
	}
}
