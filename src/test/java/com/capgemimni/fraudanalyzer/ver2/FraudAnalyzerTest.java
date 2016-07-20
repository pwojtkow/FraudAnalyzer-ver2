package com.capgemimni.fraudanalyzer.ver2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.capgemimni.fraudanalyzer.ver2.filters.FilterBigAmountTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterByUserID;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterFrequentTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterFrequentTransactionsToOne;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterIgnoredTransactions;
import com.capgemimni.fraudanalyzer.ver2.filters.FilterSmallAmountTransactions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import junit.framework.Assert;

//TODO write testcas'y

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
		Transaction trans1 = builder.date(dateToCheck).build();
		Transaction trans2 = builder.date(dateToCheck).build();
		Transaction trans3 = builder.date(dateNotToCheck).build();
		Transaction trans4 = builder.date(dateNotToCheck).build();
		// when
		startingTransactionList.add(trans1);
		startingTransactionList.add(trans2);
		startingTransactionList.add(trans3);
		startingTransactionList.add(trans4);
		int size = bank.filterByDate(startingTransactionList, dateToCheck).size();
		// then
		Assert.assertEquals(2, size);
	}

	@Test
	public void shouldPutListIntoMap() {
		// given
		Transaction trans1 = builder.id(1).build();
		Transaction trans2 = builder.id(2).build();
		Transaction trans3 = builder.id(3).build();
		Transaction trans4 = builder.id(4).build();
		// when
		startingTransactionList.add(trans1);
		startingTransactionList.add(trans2);
		startingTransactionList.add(trans3);
		startingTransactionList.add(trans4);
		bank.putListIntoMap(startingTransactionList, transactionsMap);
		int size = transactionsMap.size();
		// then
		Assert.assertEquals(4, size);
	}

	@Test
	public void shouldFilterByUserId() {
		// given
		filter = new FilterByUserID();
		Transaction trans1 = builder.senderId(542).build();
		Transaction trans2 = builder.senderId(1052).build();
		Transaction trans3 = builder.senderId(2103).build();
		Transaction trans4 = builder.senderId(4).build();
		// when
		putTransactionIntoMap(trans1);
		putTransactionIntoMap(trans2);
		putTransactionIntoMap(trans3);
		putTransactionIntoMap(trans4);
		// date no important here
		int size = filter.filter(transactionsMap, dateToCheck).size();
		// then
		Assert.assertEquals(3, size);
	}

	@Test
	public void shouldFilterBySmallAmount() {
		// given
		filter = new FilterSmallAmountTransactions();
		Transaction trans1 = builder.senderId(1).moneyAmount(1000).date(dateToCheck).build();
		Transaction trans2 = builder.senderId(1).moneyAmount(2000).date(dateToCheck).build();
		Transaction trans3 = builder.senderId(1).moneyAmount(3000).date(dateToCheck).build();
		Transaction trans4 = builder.senderId(1).moneyAmount(1).date(dateToCheck).build();
		Transaction trans5 = builder.senderId(99).moneyAmount(1).date(dateToCheck).build();
		int expectedNumberSuspectedTransactions = 4;
		// when
		putTransactionIntoMap(trans1);
		putTransactionIntoMap(trans2);
		putTransactionIntoMap(trans3);
		putTransactionIntoMap(trans4);
		putTransactionIntoMap(trans5);
		int returningArraySize = filter.filter(transactionsMap, dateToCheck).size();
		// then
		Assert.assertEquals(expectedNumberSuspectedTransactions, returningArraySize);
	}

	@Test
	public void shouldFilterByBigAmount() {
		// given
		filter = new FilterBigAmountTransactions();
		Transaction trans1 = builder.senderId(1).moneyAmount(5000).date(dateToCheck).build();
		Transaction trans2 = builder.senderId(1).moneyAmount(5000).date(dateToCheck).build();
		Transaction trans3 = builder.senderId(1).moneyAmount(1).date(dateToCheck).build();
		Transaction trans4 = builder.senderId(99).moneyAmount(1).date(dateToCheck).build();
		int expectedNumberSuspectedTransactions = 3;
		// when
		putTransactionIntoMap(trans1);
		putTransactionIntoMap(trans2);
		putTransactionIntoMap(trans3);
		putTransactionIntoMap(trans4);
		int returningArraySize = filter.filter(transactionsMap, dateToCheck).size();
		// then
		Assert.assertEquals(expectedNumberSuspectedTransactions, returningArraySize);
	}

	@Test
	public void shouldFilterFrequentsTransactionsToOne() {
		// given
		filter = new FilterFrequentTransactionsToOne();
		Transaction trans1 = builder.id(1).senderId(1).recipientId(2).date(dateToCheck).build();
		Transaction trans2 = builder.id(2).senderId(1).recipientId(2).date(dateToCheck).build();
		Transaction trans3 = builder.id(3).senderId(1).recipientId(2).date(dateToCheck).build();
		Transaction trans4 = builder.id(4).senderId(1).recipientId(2).date(dateToCheck).build();
		Transaction trans5 = builder.id(5).senderId(1).recipientId(2).date(dateToCheck).build();
		int expectedNumberSuspectedTransactions = 5;
		// when
		putTransactionIntoMap(trans1);
		putTransactionIntoMap(trans2);
		putTransactionIntoMap(trans3);
		putTransactionIntoMap(trans4);
		putTransactionIntoMap(trans5);
		int returningArraySize = filter.filter(transactionsMap, dateToCheck).size();
		// then
		Assert.assertEquals(expectedNumberSuspectedTransactions, returningArraySize);
	}
	
	@Test
	public void shouldFilterFrequentsTransactions() {
		// given
		filter = new FilterFrequentTransactions();
		Transaction trans1 = builder.id(1).senderId(1).recipientId(2).date(dateToCheck).build();
		Transaction trans2 = builder.id(2).senderId(1).recipientId(3).date(dateToCheck).build();
		Transaction trans3 = builder.id(3).senderId(1).recipientId(3).date(dateToCheck).build();
		Transaction trans4 = builder.id(4).senderId(1).recipientId(4).date(dateToCheck).build();
		Transaction trans5 = builder.id(5).senderId(1).recipientId(4).date(dateToCheck).build();
		Transaction trans6 = builder.id(6).senderId(1).recipientId(5).date(dateToCheck).build();
		int expectedNumberSuspectedTransactions = 6;
		// when
		putTransactionIntoMap(trans1);
		putTransactionIntoMap(trans2);
		putTransactionIntoMap(trans3);
		putTransactionIntoMap(trans4);
		putTransactionIntoMap(trans5);
		putTransactionIntoMap(trans6);
		int returningArraySize = filter.filter(transactionsMap, dateToCheck).size();
		// then
		Assert.assertEquals(expectedNumberSuspectedTransactions, returningArraySize);
	}

	@Test
	public void shouldFilterIgnoredTransactions() {
		// given
				filter = new FilterIgnoredTransactions();
				Transaction trans1 = builder.id(1).senderId(101).build();
				Transaction trans2 = builder.id(2).senderId(606).build();
				Transaction trans3 = builder.id(3).senderId(606).build();
				Transaction trans4 = builder.id(4).senderId(5).build();
				Transaction trans5 = builder.id(5).senderId(4).build();
				int expectedNumberIgnoredTransactions = 3;
				// when
				putTransactionIntoMap(trans1);
				putTransactionIntoMap(trans2);
				putTransactionIntoMap(trans3);
				putTransactionIntoMap(trans4);
				putTransactionIntoMap(trans5);
				int sizeBeforeFilter = transactionsMap.size();
				filter.filter(transactionsMap, dateToCheck);
				int sizeAfterFilter = transactionsMap.size();
				// then
				Assert.assertEquals((sizeBeforeFilter - expectedNumberIgnoredTransactions), sizeAfterFilter);
	}
	
	private void putTransactionIntoMap(Transaction trans1) {
		transactionsMap.put(trans1.getSenderId(), trans1);
	}
}
