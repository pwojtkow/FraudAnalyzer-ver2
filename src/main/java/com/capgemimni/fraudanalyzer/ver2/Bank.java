package com.capgemimni.fraudanalyzer.ver2;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import com.capgemimni.fraudanalyzer.ver2.filters.FilterBigAmountTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterByUserID;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterFrequentTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterSmallAmountTransactions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Bank implements FraudAnalyzer {

	private FilterManager filterManager;
	private List<Transaction> suspiciousTransactions;

	public List<Transaction> analyze(List<Transaction> transactionsList, DateTime date) {
		List<Transaction> ignoredCliendIdList = filterByDate(transactionsList, date);
		Multimap<Integer, Transaction> transactionsMap = ArrayListMultimap.create();
		putListIntoMap(ignoredCliendIdList, transactionsMap);
		int counter = 0;

		while (filterManager != null) {
			if (counter == 0) {
				filterManager = new FilterByUserID();
			} else if (counter == 1) {
				filterManager = new FilterSmallAmountTransactions();
			} else if (counter == 2) {
				filterManager = new FilterBigAmountTransactions();
			} else if (counter == 3) {
				filterManager = new FilterFrequentTransactions();
				//TODO ADD LAST 5 FILTER
				//TODO CREATE TESTS FOR FILTERS
			} else {
				filterManager = null;
			}
			addSuspiciousTransactionToList(date, transactionsMap);
			counter++;
		}
		return suspiciousTransactions;
	}

	private boolean addSuspiciousTransactionToList(DateTime date, Multimap<Integer, Transaction> transactionsMap) {
		return suspiciousTransactions.addAll(filterManager.filter(transactionsMap, date));
	}

	/**
	 * @param ignoredCliendIdList
	 * @param transactionsMap
	 *            where key is a senderID, and value is a Transaction
	 */
	private void putListIntoMap(List<Transaction> ignoredCliendIdList, Multimap<Integer, Transaction> transactionsMap) {
		Integer serdnerId;
		for (int i = 0; i < ignoredCliendIdList.size(); i++) {
			serdnerId = ignoredCliendIdList.get(i).getSenderId();
			transactionsMap.put(serdnerId, ignoredCliendIdList.get(i));
		}
	}

	private List<Transaction> filterByDate(List<Transaction> transactionsList, DateTime date) {
		List<Transaction> ignoredCliendIdList;

		// filter transaction list to date given as a method argument
		ignoredCliendIdList = transactionsList.stream().filter(t -> t.getDate().toLocalDate().compareTo(date.toLocalDate()) == 0)
				.collect(Collectors.toList());
		return ignoredCliendIdList;
	}

}
