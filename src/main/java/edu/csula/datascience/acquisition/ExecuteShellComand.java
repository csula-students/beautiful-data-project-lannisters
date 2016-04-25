package edu.csula.datascience.acquisition;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;

public class ExecuteShellComand implements Collector<Simple, Mock> {

	public static void main(String[] args) {

		ExecuteShellComand obj = new ExecuteShellComand();

		obj.save(null);
	}

	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}

	@Override
	public Collection<Simple> mungee(Collection<Mock> src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Collection<Simple> data) {
		// TODO Auto-generated method stub

		ExecuteShellComand obj = new ExecuteShellComand();

		for (int i = 1; i < 4; i++) {
			String command = "mongoimport -d parking -c parking" + i
					+ " --type csv --file src/main/resources/parking" + i
					+ ".csv --headerline";

			obj.executeCommand(command);

			System.out.println("finished");
		}

	}

}