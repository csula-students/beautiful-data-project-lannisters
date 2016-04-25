package edu.csula.datascience.acquisition;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * A test case to show how to use Collector and Source
 */
public class CollectorTest1 {

	@Test
	public void mungee() throws Exception {

		Reader in = new FileReader("src/main/resources/parktest1.csv");

		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
		List<Object> count = new ArrayList<>();

		for (CSVRecord record : records) {
			count.add(record);
		}
		System.out.println("count" + count.size());
		// Test no of entries
		Assert.assertEquals(count.size(), 11);
		// Test entry 9 and 10 are same or not
		Assert.assertEquals(count.get(3).equals(count.get(4)), false);
	}
}