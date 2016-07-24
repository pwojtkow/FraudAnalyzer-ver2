package com.capgemimni.fraudanalyzer.ver2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.capgemimni.fraudanalyzer.ver2.filters.FilterBigAmountTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterByUserIDTest;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterFrequentTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterFrequentTransactionsToOne;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterSmallAmountTransactions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import junit.framework.Assert;

public class FraudAnalyzerTest {

	List<Transaction> startingTransactionList;
	TransactionBuilder builder;
	DateTime dateToCheck;
	Bank bank;
	Multimap<Integer, Transaction> transactionsMap;
	FilterManager filter;

	@Before
	public void creatingObjects() {
		bank = new Bank();
		startingTransactionList = new ArrayList<Transaction>();
		builder = new TransactionBuilder();
		transactionsMap = ArrayListMultimap.create();
	}

	@Test
	public void shouldFilterByDate() {
		// given
		dateToCheck = new DateTime(2016, 6, 17, 12, 30);
		DateTime dateNotToCheck = new DateTime(2015, 1, 7, 3, 10);
		Transaction trans1 = builder.date(dateToCheck).id(1).moneyAmount(100000).recipientId(2).senderId(1).build();
		Transaction trans2 = builder.date(dateToCheck).id(2).moneyAmount(10).recipientId(3).senderId(1).build();
		Transaction trans3 = builder.date(dateToCheck).id(3).moneyAmount(100).recipientId(4).senderId(1).build();
		Transaction trans4 = builder.date(dateNotToCheck).id(4).moneyAmount(100000).recipientId(3).senderId(4).build();
		Transaction trans5 = builder.date(dateNotToCheck).id(5).moneyAmount(10).recipientId(4).senderId(5).build();
		
		// when
		startingTransactionList.add(trans1);
		startingTransactionList.add(trans2);
		startingTransactionList.add(trans3);
		startingTransactionList.add(trans4);
		startingTransactionList.add(trans5);
		int size = bank.analyze(startingTransactionList, dateToCheck).size();
		
		// then
		Assert.assertEquals(3, size);
	}
	
}
