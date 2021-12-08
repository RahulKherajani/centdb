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

               case "create":

                   query = "CREATE TABLE Persons (PersonID int, Name string, Gender boolean, Salary double, PRIMARY KEY (PersonID));";
                   query =query.toLowerCase();
                   String[] words = query.split(" ");

                    if(words[1]=="database") {
                        db.createDatabase(words[2]);
                    }

                    else if(words[2]=="table"){

                       query =query.toLowerCase();
                       String CREATE_QUERY_OUTER = "CREATE\\sTABLE\\s(\\w+)\\s";

                       String CREATE_QUERY_INNER = "\\(((?:\\w+\\s\\w+\\(?[0-9]*\\)?,?)+)\\);";

                       Pattern pattern3 = Pattern.compile(CREATE_QUERY_OUTER + CREATE_QUERY_INNER);

                       Matcher matcher3 = pattern3.matcher(query);
                       matcher3.find();
                       boolean match = matcher3.matches ();
                       if(match == true) {

                           String columnName = matcher3.group(1);
                           String tableName = matcher3.group(2);
                           String condition[] = matcher3.group(4).split(" ");
                            //create

                       }
                    }
                   else {
                       System.out.println("Invalid query !!");
                   }
                   break;

                case "use":
                    query = query.replaceAll(";", "");
                    String[] words1 = query.split(" ");
                    String db_name = words1[1];
                    db.useDatabase(db_name);
                    break;


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

                    ArrayList<String[]> row_values = new ArrayList<>();
                    row_values.add(rows);

                    Table table1 = new Table();
                    table1.setColumns(columns);
                    table1.setRows(row_values);

                    db.insertTable(table,table1);
                    break;

               case "select":

                    //query = "SELECT * FROM Customers WHERE Country = 'Mexico';
                    query =query.toLowerCase();

                    Pattern pattern1 = Pattern.compile("select\\s+(.*?)\\s*from\\s+(.*?)\\s*(where\\s(.*?)\\s*)?;", Pattern.DOTALL);
                    Matcher matcher1 = pattern1.matcher(query);
                    matcher1.find();
                    boolean match = matcher1.matches ();
                    if(match == true) {

                        String columnName = matcher1.group(1);
                        String tableName = matcher1.group (2);
                        String[] condition = matcher1.group (4).split(" ");

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
                        String[] columnNameAndValue = matcher2.group(2).split("=");
                        String[] condition = matcher2.group (3).split(" ");

                        WhereCondition whereCondition=new WhereCondition();
                        whereCondition.setColumn(condition[0]);
                        whereCondition.setOperation(Operation.valueOf(condition[1]));
                        whereCondition.setValue(condition[2].replaceAll("\'",""));

                        db.updateTable(tableName,columnNameAndValue[0].trim(),columnNameAndValue[1].trim(),whereCondition);
                    }
                    else {
                        System.out.println("Invalid query !!");
                    }
                    break;


                case "delete":

                    // query = "DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';";
                    query =query.toLowerCase();

                    Pattern pattern5 = Pattern.compile("delete\\s(.*?)from\\s(.*?)where\\s(.*?)?;");
                    Matcher matcher5 = pattern5.matcher(query);
                    boolean match3 = matcher5.matches ();

                    if(match3 == true) {
                        String tableName = matcher5.group (2);
                        String condition[] = matcher5.group (3).split(" ");

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

                case "drop":
                    //query=DROP TABLE table_name;
                    query=query.toLowerCase();
                    Pattern pattern4 = Pattern.compile("drop\\s(.*?)table\\s(.*?)?;");
                    Matcher matcher4 = pattern4.matcher(query);
                    boolean match4 = matcher4.matches();

                    if (match4) {
                        String tableName = matcher4.group(2);
                        db.dropTable(tableName);
                    }
                    else {
                        System.out.println("Invalid query");
                    }

            }


    }
}
