package edu.csula.datascience.examples;

import io.searchbox.action.BulkableAction;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collection;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.common.collect.Lists;



/**
 * A quick example app to send data to elastic search on AWS
 */
public class JestExampleApp {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String indexName = "parking-db1";
        String typeName = "parking-violations";
        String awsAddress = "https://search-parking-violation-7k3m2jnh3brv2cbtrzlkz6phla.us-west-1.es.amazonaws.com/";
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
            .Builder(awsAddress)
            .multiThreaded(true)
            .build());
        JestClient client = factory.getObject();

        // as usual process to connect to data source, we will need to set up
        // node and client// to read CSV file from the resource folder
        File csv = new File(
            ClassLoader.getSystemResource("Parking_Finall.csv")
                .toURI()
        );

        try {
            // after reading the csv file, we will use CSVParser to parse through
            // the csv files
            CSVParser parser = CSVParser.parse(
                csv,
                Charset.defaultCharset(),
                CSVFormat.EXCEL.withHeader()
            );
            Collection<Park> temperatures = Lists.newArrayList();

            int count = 0;

            // for each record, we will insert data into Elastic Search
//            parser.forEach(record -> {
            for (CSVRecord record: parser) {
                // cleaning up dirty data which doesn't have time or temperature
                if (
                    !record.get("IssueDate").isEmpty() &&
                    !record.get("Violation").isEmpty()
                ) {
                    Park temp = new Park(record.get("Plate"), record
							.get("State"), record.get("LicenceType"), record
							.get("SummonsNumber"), record.get("IssueDate").toString(),
							record.get("ViolationTime"), record
									.get("Violation"),
							record.get("FineAmount"), record
									.get("PenaltyAmount"), record
									.get("InterestAmount"), record
									.get("AmountDue"), record
									.get("IssuingAgency"));

                    if (count < 500) {
                        temperatures.add(temp);
                        count ++;
                    } else {
                        try {
                            Collection<BulkableAction> actions = Lists.newArrayList();
                            temperatures.stream()
                                .forEach(tmp -> {
                                    actions.add(new Index.Builder(tmp).build());
                                });
                            Bulk.Builder bulk = new Bulk.Builder()
                                .defaultIndex(indexName)
                                .defaultType(typeName)
                                .addAction(actions);
                            client.execute(bulk.build());
                            count = 0;
                            temperatures = Lists.newArrayList();
                            System.out.println("Inserted 500 documents to cloud");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            Collection<BulkableAction> actions = Lists.newArrayList();
            temperatures.stream()
                .forEach(tmp -> {
                    actions.add(new Index.Builder(tmp).build());
                });
            Bulk.Builder bulk = new Bulk.Builder()
                .defaultIndex(indexName)
                .defaultType(typeName)
                .addAction(actions);
            client.execute(bulk.build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("We are done! Yay!");
    }

	static class Park {
		final String Plate;
		final String State;
		final String LicenceType;
		final String SummonsNumber;
		final String IssueDate;
		final String ViolationTime;
		final String Violation;
		final String FineAmount;
		final String PenaltyAmount;
		final String InterestAmount;
		final String AmountDue;
		final String IssuingAgency;

		public Park(String plate, String state, String licenceType,
				String summonsNumber, String issueDate, String violationTime,
				String violation, String fineAmount, String penaltyAmount,
				String interestAmount, String amountDue, String issuingAgency) {
			super();
			Plate = plate;
			State = state;
			LicenceType = licenceType;
			SummonsNumber = summonsNumber;
			IssueDate = issueDate;
			ViolationTime = violationTime;
			Violation = violation;
			FineAmount = fineAmount;
			PenaltyAmount = penaltyAmount;
			InterestAmount = interestAmount;
			AmountDue = amountDue;
			IssuingAgency = issuingAgency;
		}

	}
}
