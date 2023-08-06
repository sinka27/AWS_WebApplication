package org.pom;

import java.util.HashMap;
import java.util.Map;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class PomLambda {

    // Create an instance of AmazonDynamoDB client
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    private static final String tableName = "PowerofMathDataBase";

    // Define the handler function that the Lambda service will use as an entry point
    public Map<String, Object> lambdaHandler(Map<String, Integer> input){
        // Extract the two numbers from the Lambda service's input object
        int base = input.get("base");
        int exponent = input.get("exponent");

        // Calculate the math result
        double mathResult = Math.pow(base, exponent);

        // Store the current time in a human-readable format
        String now = java.time.ZonedDateTime.now().toString();

        // Write the result and time to the DynamoDB table and save the response in a variable
        Table table = dynamoDB.getTable(tableName);
        Item item = new Item().withPrimaryKey("ID", String.valueOf(mathResult))
                .withString("LatestGreetingTime", now);
        table.putItem(item);

        // Create a response object
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 200);
        response.put("body", "Your result is " + mathResult);
        return response;
    }

    //method to get Item from DynamoDb
    public String getItem(Map<String, String> input){
        String id = input.get("ID");
        Table table = dynamoDB.getTable(tableName);
        Item item = table.getItem("ID", id);
        return item.getString("LatestGreetingTime");
    }

    //method to delete Item from DynamoDb
    public String deleteItem(Map<String, String> input){
        String id = input.get("ID");
        Table table = dynamoDB.getTable(tableName);
        table.deleteItem("ID", id);
        return "Item deleted";
    }
}