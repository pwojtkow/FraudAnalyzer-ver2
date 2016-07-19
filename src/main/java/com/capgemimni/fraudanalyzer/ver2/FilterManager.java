package com.capgemimni.fraudanalyzer.ver2;

import java.util.List;
import org.joda.time.DateTime;
import com.google.common.collect.Multimap;

public abstract class FilterManager implements FilterStrategy {

	public abstract List<Transaction> filter (
			Multimap<Integer, Transaction> transactionsMap, DateTime date);

}
