package analytics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsQueryProcessor {

	static String QUERY_LOG_FILE = "./src/main/resources/logs/query_logs.txt";

	private static List<QueryLogResponse> queryLogResponse = new ArrayList<QueryLogResponse>();

	private static List<String> tables = new ArrayList<String>();

	private static AnalyticsWriter analyticsWriter = new AnalyticsWriter();

	private static String loggedInUser = "SDEY"; // Get LoggedIn User Here

	public AnalyticsQueryProcessor() {

		readQueryLog();
		tables.add("Employee");
		tables.add("Department");

	}

	public static void readQueryLog() {

		try (BufferedReader br = new BufferedReader(new FileReader(QUERY_LOG_FILE))) {
			String line;
			while ((line = br.readLine()) != null) {

				ArrayList<String> modifiedFields = new ArrayList<String>();

				String[] fields = line.split("~");
				for (String field : fields) {

					modifiedFields.add(field.substring(field.indexOf(":") + 1));
				}

				String user = modifiedFields.get(1);

				if (user.equalsIgnoreCase(loggedInUser)) { // needs to come from the current user

					String status = modifiedFields.get(2);
					String database = modifiedFields.get(4);

					QueryLogResponse response;

					if (status.equalsIgnoreCase("invalid")) {

						response = new QueryLogResponse(status, database, "", "");

					} else {

						String table = modifiedFields.get(5);
						String operation = modifiedFields.get(6);

						response = new QueryLogResponse(status, database, table, operation);
					}

					queryLogResponse.add(response);
				}
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void processCountQueries(String query) {

		query = query.replace(";", "");
		String[] fields = query.split(" ");
		String database = fields[2];
		List<QueryLogResponse> response = AnalyticsQueryPredicates.filterQueries(queryLogResponse,
				AnalyticsQueryPredicates.countQueries(database));
		String result = "User " + loggedInUser + " submitted " + response.size() + " queries on " + database;
		System.out.println(result);
		String message = "Query:" + query + ";" + "~" + "Result:" + result+"!";
		analyticsWriter.addAnalyticsLog(loggedInUser, message);

	}

	public void processCountQueriesByStatus(String query) {

		query = query.replace(";", "");
		String[] fields = query.split(" ");
		String status = fields[1];
		String database = fields[3];
		List<QueryLogResponse> response = AnalyticsQueryPredicates.filterQueries(queryLogResponse,
				AnalyticsQueryPredicates.countQueriesByStatus(database, status));

		String result = "User " + loggedInUser + " submitted " + response.size() + " " + status + " queries on "
				+ database;
		System.out.println(result);
		String message = "Query:" + query + ";" + "~" + "Result:" + result+"!";
		analyticsWriter.addAnalyticsLog(loggedInUser, message);
	}

	public void processCountQueriesByOperation(String query) {

		query = query.replace(";", "");
		String[] fields = query.split(" ");
		String operation = fields[1];
		String database = fields[3];
		String message = "Query:"+query+";"+"~";
		for(String table: tables) {
			List<QueryLogResponse> response = AnalyticsQueryPredicates.filterQueries(queryLogResponse,
					AnalyticsQueryPredicates.countQueriesByOperation(database, operation, table));
			String result = "User " + loggedInUser + " performed " + response.size() + " " + operation + " queries on "
					+ table + " in "+ database;
			System.out.println(result);
			message += result + "!";
		}
		analyticsWriter.addAnalyticsLog(loggedInUser, message);
	}

//	public static void main(String[] args) {
//
//		AnalyticsQueryProcessor ana = new AnalyticsQueryProcessor();
//
//		ana.processCountQueries("count queries db1;");
//
//		// List<QueryLogResponse> queryLogResponse1 =
//		// queryLogResponse.stream().filter().collect(Collectors.toList());
//
//		// System.out.println(queryLogResponse1.size());
//
//	}
}
