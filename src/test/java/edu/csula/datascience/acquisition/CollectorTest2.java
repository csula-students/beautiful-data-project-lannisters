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
public class CollectorTest2 {

	@Test
	public void mungee() throws Exception {

		Reader in = new FileReader("src/main/resources/parktest1.csv");

		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
		List<Object> count = new ArrayList<>();

		for (CSVRecord record : records) {

			count.add(record);
			System.out.println("state" + record.get("State").toString());
			// Check all data is for State NewYork or not
			Assert.assertEquals(record.get("State").toString().equals("NY"),
					true);
		}
		//checks for null Entries
		Assert.assertEquals(count.get(8).equals(null), false);

	}
}