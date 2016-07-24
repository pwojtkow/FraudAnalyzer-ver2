package com.capgemimni.fraudanalyzer.ver2;

import java.util.List;
import com.google.common.collect.Multimap;

public interface FilterManager {

	public List<Transaction> filter (Multimap<Integer, Transaction> transactionsMap);

}
