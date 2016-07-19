package com.capgemimni.fraudanalyzer.ver2.filters;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import com.capgemimni.fraudanalyzer.ver2.FilterManager;
import com.capgemimni.fraudanalyzer.ver2.Transaction;
import com.google.common.collect.Multimap;

public class FilterFrequentTransactions extends FilterManager {

	private static final int TRANSACION_QUANTITY_LIMIT_PER_DAY = 5;

	@Override
	public List<Transaction> filter(Multimap<Integer, Transaction> transactionsMap, DateTime date) {
		List<Transaction> suspiciousClientIdList = new ArrayList<Transaction>();
		int numberOfSendTransactions = 0;

		for (Integer key : transactionsMap.keySet()) {
			numberOfSendTransactions = transactionsMap.get(key).size();
			if (numberOfSendTransactions > TRANSACION_QUANTITY_LIMIT_PER_DAY) {
				if (areTransactionsToTheSameRecievier(transactionsMap, numberOfSendTransactions, key)) {
					suspiciousClientIdList.addAll(transactionsMap.get(key));
				}
			}
		}
		for (int i = 0; i <= suspiciousClientIdList.size(); i++) {
			transactionsMap.removeAll(suspiciousClientIdList.get(i));
		}
		return suspiciousClientIdList;
	}

	private boolean areTransactionsToTheSameRecievier(Multimap<Integer, Transaction> transactionsMap,
			int numberOfTransactions, Integer key) {
		Transaction[] bufforArray;
		bufforArray = createBufforArray(transactionsMap, numberOfTransactions, key);
		int counter = 0;
		
		for (int i = 0; i < bufforArray.length - 1; i++) {
			if (bufforArray[i + 1].equals(bufforArray[i])) {
				counter++;
			}
		}
		if (counter == bufforArray.length - 1) {
			return true;
		} else {
			return false;
		}
	}

	private Transaction[] createBufforArray(Multimap<Integer, Transaction> transactionsMap, int numberOfTransactionsForOneClient,
			Integer key) {
		Transaction[] bufforArray;
		bufforArray = transactionsMap.get(key).toArray(new Transaction[numberOfTransactionsForOneClient]);
		return bufforArray;
	}

}
