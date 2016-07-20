package com.capgemimni.fraudanalyzer.ver2;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import com.capgemimni.fraudanalyzer.ver2.filters.FilterBigAmountTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterByUserID;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterFrequentTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterFrequentTransactionsToOne;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterIgnoredTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterSmallAmountTransactions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Bank implements FraudAnalyzer {

	private FilterManager filterManager;
	private List<Transaction> suspiciousTransactions;
	private Multimap<Integer, Transaction> transactionsMap = ArrayListMultimap.create();
	
	public List<Transaction> analyze(List<Transaction> transactionsList, DateTime date) {
		List<Transaction> correctDateTransactionsList = filterByDate(transactionsList, date);
		putListIntoMap(correctDateTransactionsList, transactionsMap);
		useFilters(date, transactionsMap);
		return suspiciousTransactions;
	}
	
	private void useFilters(DateTime date, Multimap<Integer, Transaction> transactionsMap) {
		int counter = 0;

		while (filterManager != null) {
			if (counter == 0) {
				filterManager = new FilterIgnoredTransactions();
			} else if (counter == 1) {
				filterManager = new FilterByUserID();
			} else if (counter == 2) {
				filterManager = new FilterSmallAmountTransactions();
			} else if (counter == 3) {
				filterManager = new FilterBigAmountTransactions();
			} else if (counter == 4) {
				filterManager = new FilterFrequentTransactionsToOne();
			} else if (counter == 5) {
				filterManager = new FilterFrequentTransactions();
			} else {
				filterManager = null;
			}
			addSuspiciousTransactionToList(date, transactionsMap);
			counter++;
		}
	}

	private boolean addSuspiciousTransactionToList(DateTime date, Multimap<Integer, Transaction> transactionsMap) {
		return suspiciousTransactions.addAll(filterManager.filter(transactionsMap, date));
	}

	/**
	 * @param ignoredCliendIdList
	 * @param transactionsMap
	 *            where key is a senderID, and value is a Transaction
	 */
	public void putListIntoMap(List<Transaction> ignoredCliendIdList, Multimap<Integer, Transaction> transactionsMap) {
		Integer serdnerId;
		for (int i = 0; i < ignoredCliendIdList.size(); i++) {
			serdnerId = ignoredCliendIdList.get(i).getSenderId();
			transactionsMap.put(serdnerId, ignoredCliendIdList.get(i));
		}
	}

	public List<Transaction> filterByDate(List<Transaction> transactionsList, DateTime date) {
		List<Transaction> correctDateTransactionList;

		// filter transaction list to date given as a method argument
		correctDateTransactionList = transactionsList.stream().filter(t -> t.getDate().toLocalDate().compareTo(date.toLocalDate()) == 0)
				.collect(Collectors.toList());
		return correctDateTransactionList;
	}

	public Multimap<Integer, Transaction> getTransactionsMap() {
		return transactionsMap;
	}
}
