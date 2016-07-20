package com.capgemimni.fraudanalyzer.ver2.filters;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import com.capgemimni.fraudanalyzer.ver2.FilterManager;
import com.capgemimni.fraudanalyzer.ver2.Transaction;
import com.google.common.collect.Multimap;

public class FilterSmallAmountTransactions extends FilterManager {

	private static final int TRANSACION_QUANTITY_LIMIT_PER_DAY = 3;
	private static final int MONEY_TRANSFER_LIMIT_PER_DAY = 5000;

	@Override
	public List<Transaction> filter(Multimap<Integer, Transaction> transactionsMap, DateTime date) {
		List<Transaction> suspiciousClientIdList = new ArrayList<Transaction>();
		int numberOfTransactions = 0;
		int sumOfTransactions = 0;

		for (Integer key : transactionsMap.keySet()) {
			numberOfTransactions = transactionsMap.get(key).size();
			if (numberOfTransactions > TRANSACION_QUANTITY_LIMIT_PER_DAY) {
				sumOfTransactions = countSumOfTransactionsAmounts(transactionsMap, numberOfTransactions, sumOfTransactions, key);
				if (sumOfTransactions > MONEY_TRANSFER_LIMIT_PER_DAY) {
					suspiciousClientIdList.addAll(transactionsMap.get(key));
				}
			}
		}
		return suspiciousClientIdList;
	}

	private int countSumOfTransactionsAmounts(Multimap<Integer, Transaction> transactionsMap, int numberOfTransactionsForOneClient,
			int sumOfTransactions, Integer key) {
		Transaction[] bufforArray;
		bufforArray = createBufforArray(transactionsMap, numberOfTransactionsForOneClient, key);
		for (int i = 0; i < bufforArray.length; i++) {
			sumOfTransactions += bufforArray[i].getMoneyAmount();
		}
		return sumOfTransactions;
	}

	private Transaction[] createBufforArray(Multimap<Integer, Transaction> transactionsMap, int numberOfTransactionsForOneClient,
			Integer key) {
		Transaction[] bufforArray;
		bufforArray = transactionsMap.get(key).toArray(new Transaction[numberOfTransactionsForOneClient]);
		return bufforArray;
	}

}
