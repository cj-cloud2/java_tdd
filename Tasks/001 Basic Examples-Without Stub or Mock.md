# TDD Examples:
Here are five progressively challenging TDD (Test-Driven Development) examples and tasks from finance, banking, and IoT domains. These focus on typical business logic scenarios and avoid the use of stubs or mocks, ensuring tests directly interact with implemented functionality.These examples cover different domains and introduce a wide range of business logic, data management, and validation concepts essential for TDD mastery.


# TDD Approach (Lifecycle) as below should be used to solve these tasks:
- **Red:** The initial failing test.
- **Green:** The minimal service class code to make the test pass.
- **Refactor:** Suggestions for cleanup and improvements.



**How to teach with these:**

- Write each test method (red), see the failure.
- Add/update the implementation for green.
- Refactor if required for readability, reuse, or performance after all tests are green.
- No mocks or stubs used; logic is fully verifiable.





Do these as coding exercises :

- Start each task by writing its simplest (failing) test.
- Write minimal code to pass the test.
- Refactor while continually testing.
- No mocking or stubbing—interact with real code and structures.

This approach reinforces the core TDD cycle and essential business logic skills common in finance, banking, and IoT domains.

---








### 1. Finance – Currency Converter (Easy)

**Task:**
Write a function that converts an amount from one currency to another, given a static exchange rate.

- **Test 1:** Converting \$100 to Euros at a rate of 0.9 should return €90.
- **Test 2:** Converting \$0 should return €0.
- **Test 3:** Converting a negative amount should throw an error.

*Goal:* Focus on correct calculation and handling invalid input.
## 1. Finance – Currency Converter

**CurrencyConverter.java**
**CurrencyConverterTest.java**






### 2. Banking – Simple Interest Calculator (Moderate)

**Task:**
Implement a function to calculate simple interest.

- **Test 1:** Principal \$1,000, rate 5%, time 3 years → Interest = \$150.
- **Test 2:** Principal \$0 should return \$0 interest.
- **Test 3:** Negative interest rate should throw an error.

*Goal:* Reinforces understanding of input validation and arithmetic logic.
## 2. Banking – Simple Interest Calculator

**SimpleInterestCalculator.java**
**SimpleInterestCalculatorTest.java**








### 3. IoT – Temperature Threshold Alert (Moderate)

**Task:**
Implement a class that stores temperature readings and raises an alert if a threshold is crossed.

- **Test 1:** Adding a reading above the threshold triggers the alert status to true.
- **Test 2:** Adding readings below the threshold does not trigger the alert.
- **Test 3:** Resetting the alert restores alert status to false.

*Goal:* Practice state management in classes and basic event triggering.
## 3. IoT – Temperature Threshold Alert

**TemperatureMonitor.java**
**TemperatureMonitorTest.java**












### 4. Finance – Monthly Budget Tracker (Hard)

**Task:**
Create a budget tracker that tracks expenses and determines if the spending exceeds a monthly limit.

- **Test 1:** Adding expenses keeps total within the limit – should return isOverBudget as false.
- **Test 2:** Total expenses exactly at the limit – should return isOverBudget as false.
- **Test 3:** Adding an expense that pushes the total over the limit – should return isOverBudget as true.
- **Test 4:** Removing an expense brings the total back within the limit – should return isOverBudget as false.

*Goal:* Manage collections/lists and implement business rule checks.

## 4. Finance – Monthly Budget Tracker

**BudgetTracker.java**
**BudgetTrackerTest.java**







### 5. Banking – Transaction History Filter (Challenging)

**Task:**
Create a class for maintaining a list of transactions with methods to filter transactions by date range and type (withdrawal/deposit).

- **Test 1:** Adding several transactions, filter by date range returns only those within the range.
- **Test 2:** Filter by type (deposit) returns only deposit transactions.
- **Test 3:** Edge case: No transactions in date range returns an empty list.
- **Test 4:** Filtering with invalid dates throws an error.

*Goal:* Emphasize data filtering, list operations, and error handling.
## 5. Banking – Transaction History Filter

**Transaction.java**
**TransactionHistory.java**
**TransactionHistoryTest.java**
