package services;

import Constants.Operation;
import model.Column;
import model.Table;
import model.WhereCondition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParsingServicesImpl implements QueryParsingServices {

    @Override
    public void queryParsing(String query) throws IOException {

        String query_type = query.replaceAll (" .*", "");
        query_type = query_type.toLowerCase ();
        DatabaseServices db= new DatabaseServicesImpl();


            switch(query_type){

              /* case "create":
                    query = query.replaceAll(";", "")
                            .replaceAll(",", " ")
                            .replaceAll("[^a-zA-Z ]", "")
                            .toLowerCase();

                    String[] words = query.split(" ");

                    if(words[1]=="database") {
                        db.createDatabase(words[2]);
                    }
                    else if(words[2]=="table"){
                        Table table1 = new Table();
                        //set column names
                        Column col = new Column();
                        ArrayList<Column> columns = new Column<>();
                        col.setColumnName();
                        db.createTable(words[2],);
                    }
                    else
                    {
                        System.out.println("Invalid Query");
                    }
                    break;


*/
                case "use":
                    query = query.replaceAll(";", "");
                    String[] words1 = query.split(" ");
                    String db_name = words1[1];
                    db.useDatabase(db_name);
                    break;

/*
                case "insert":
                    Pattern pattern = Pattern.compile("insert into\\s(.*?)\\s(.*?)\\svalues\\s(.*?);", Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(query);
                    matcher.find();

                    String table = matcher.group(1);
                    String[] columns = matcher.group(2)
                            .replaceAll("[\\[\\](){}]","")
                            .replace(" ","")
                            .split(",");
                    String[] rows = matcher.group(3)
                            .replaceAll("[\\[\\](){}]","")
                            .replace(" ","")
                            .replace("'","")
                            .replace("\"","")
                            .split(",");
                    ArrayList<String[]> row_values = (ArrayList<String[]>) Arrays.asList(rows);

                    Table table1 = new Table();
                    table1.setColumns(columns);
                    table1.setRows(row_values);

                    db.insertTable(table,table1);
                    break;

  */              case "select":

                    //query = "SELECT * FROM Customers WHERE Country = 'Mexico';
                    query =query.toLowerCase();

                    Pattern pattern1 = Pattern.compile("select\\s+(.*?)\\s*from\\s+(.*?)\\s*(where\\s(.*?)\\s*)?;", Pattern.DOTALL);
                    Matcher matcher1 = pattern1.matcher(query);
                    matcher1.find();
                    boolean match = matcher1.matches ();
                    if(match == true) {

                        String columnName = matcher1.group(1);
                        String tableName = matcher1.group (2);
                        String condition[] = matcher1.group (4).split(" ");

                        WhereCondition whereCondition=new WhereCondition();
                        whereCondition.setColumn(condition[0]);
                        whereCondition.setOperation(Operation.valueOf(condition[1]));
                        whereCondition.setValue(condition[2].replaceAll("\'",""));

                        db.selectTable(tableName,columnName,whereCondition);
                    }
                    else {
                        System.out.println("Invalid query !!");
                    }
                    break;

                case "update":

                    //UPDATE Customers SET ContactName='Juan' WHERE Country = 'Mexico';
                    query=query.toLowerCase();
                    Pattern pattern2 = Pattern.compile("update\\s(.*?)set\\s(.*?)where\\s(.*?)?;");
                    Matcher matcher2 = pattern2.matcher(query);
                    boolean match2 = matcher2.matches ();

                    if(match2) {
                        String tableName = matcher2.group (1);
                        String[] column_name_and_value = matcher2.group(2).split("=");
                        String[] condition = matcher2.group (3).split(" ");

                        WhereCondition whereCondition=new WhereCondition();
                        whereCondition.setColumn(condition[0]);
                        whereCondition.setOperation(Operation.valueOf(condition[1]));
                        whereCondition.setValue(condition[2].replaceAll("\'",""));

                        db.updateTable(tableName,column_name_and_value[0].trim(),column_name_and_value[1].trim(),whereCondition);
                    }
                    else {
                        System.out.println("Invalid query !!");
                    }
                    break;

                case "delete":

                    // query = "DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';";
                    query =query.toLowerCase();

                    Pattern pattern = Pattern.compile("delete\\s(.*?)from\\s(.*?)where\\s(.*?)?;");
                    Matcher matcher = pattern.matcher(query);
                    boolean match3 = matcher.matches ();

                    if(match3 == true) {
                        String tableName = matcher.group (2);
                        String condition[] = matcher.group (3).split(" ");

                        WhereCondition whereCondition=new WhereCondition();
                        whereCondition.setColumn(condition[0]);
                        whereCondition.setOperation(Operation.valueOf(condition[1]));
                        whereCondition.setValue(condition[2].replaceAll("\'",""));

                        db.deleteTable(tableName,whereCondition);
                    }
                    else {
                        System.out.println("Invalid query !!");
                    }
                    break;
            }


    }
}
