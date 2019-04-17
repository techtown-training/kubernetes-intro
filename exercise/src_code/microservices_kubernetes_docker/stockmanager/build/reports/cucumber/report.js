$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("features/Stocks.feature");
formatter.feature({
  "line": 1,
  "name": "Retrieving Stocks",
  "description": "",
  "id": "retrieving-stocks",
  "keyword": "Feature"
});
formatter.scenario({
  "line": 3,
  "name": "Should be able to get a list of all stocks",
  "description": "",
  "id": "retrieving-stocks;should-be-able-to-get-a-list-of-all-stocks",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 4,
  "name": "the application has been initialised with test data",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "the client gets all stocks",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "a list of 5 stocks will be returned",
  "keyword": "Then "
});
formatter.match({
  "location": "RestStepDefs.init()"
});
formatter.result({
  "duration": 110448471,
  "status": "passed"
});
formatter.match({
  "location": "RestStepDefs.getAllStocks()"
});
formatter.result({
  "duration": 386028320,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "5",
      "offset": 10
    }
  ],
  "location": "RestStepDefs.assertListOfStocksLength(int)"
});
formatter.result({
  "duration": 5237886,
  "status": "passed"
});
formatter.scenario({
  "line": 8,
  "name": "Should be able to get the correct SKU for the first stock",
  "description": "",
  "id": "retrieving-stocks;should-be-able-to-get-the-correct-sku-for-the-first-stock",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 9,
  "name": "the application has been initialised with test data",
  "keyword": "Given "
});
formatter.step({
  "line": 10,
  "name": "the client gets all stocks",
  "keyword": "When "
});
formatter.step({
  "line": 11,
  "name": "the stock at index 0 will have the sku 12345678",
  "keyword": "Then "
});
formatter.match({
  "location": "RestStepDefs.init()"
});
formatter.result({
  "duration": 273949,
  "status": "passed"
});
formatter.match({
  "location": "RestStepDefs.getAllStocks()"
});
formatter.result({
  "duration": 12856616,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "0",
      "offset": 19
    },
    {
      "val": "12345678",
      "offset": 39
    }
  ],
  "location": "RestStepDefs.assertStockHasSku(int,String)"
});
formatter.result({
  "duration": 1572001,
  "status": "passed"
});
});