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
				suspiciousClientIdList.addAll(transactionsMap.get(key));
			}
		}
		return suspiciousClientIdList;
	}
}
