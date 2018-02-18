# Spring Batch CSV Reader Example

This is a sample Spring Batch application reading from a CSV and writing to MySQL (h2) db.

## Technologies
* Spring Batch
* Spring Data JPA

## Running
Requires that a data.csv file be in src/main/resources

`gradle clean build && gradle bootRun`

## Running example tests
Processes example data.csv in src/test/resources

`gradle test`