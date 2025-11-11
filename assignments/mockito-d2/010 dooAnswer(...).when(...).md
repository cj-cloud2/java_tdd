# doAnswer(...).when(...)

The `doAnswer().when()` method in Mockito is a powerful stubbing technique that allows you to define custom behavior for mocked methods, providing far more flexibility than simple return values or exceptions.

## Core Functionality

The `doAnswer()` method enables you to specify complex logic that executes when a mocked method is called. Unlike `thenReturn()` which simply returns a predefined value, `doAnswer()` accepts an `Answer` interface implementation that can access method arguments, perform calculations, trigger callbacks, and return dynamic results based on the invocation context.

## Syntax and Structure

The basic syntax follows this pattern:

```java
doAnswer(new Answer<ReturnType>() {
    @Override
    public ReturnType answer(InvocationOnMock invocation) throws Throwable {
        // Custom logic here
        return result;
    }
}).when(mockObject).methodToStub();
```

Using lambda expressions for cleaner code:

```java
doAnswer(invocation -> {
    // Custom behavior
    return result;
}).when(mockObject).methodToStub();
```


## Key Use Cases

### Stubbing Void Methods

When working with void methods, `doAnswer()` is particularly valuable since you cannot use `when().thenReturn()` :

```java
@Test
public void testVoidMethodWithCallback() {
    BookService service = mock(BookService.class);
    
    doAnswer(invocation -> {
        BookServiceCallback callback = (BookServiceCallback) invocation.getArguments()[0];
        callback.onSuccess("Effective Java");
        return null; // void methods return null
    }).when(service).queryBookTitle(any(BookServiceCallback.class));
    
    service.queryBookTitle(callback);
    // Verify callback was triggered
}
```


### Dynamic Return Values Based on Arguments

You can inspect method arguments and return different values accordingly :

```java
@Test
public void testDynamicUserRole() {
    UserService service = mock(UserService.class);
    
    doAnswer(invocation -> {
        String userId = (String) invocation.getArguments()[0];
        if ("admin".equals(userId)) {
            return "ADMINISTRATOR";
        } else if ("user".equals(userId)) {
            return "REGULAR_USER";
        }
        return "GUEST";
    }).when(service).getUserRole(anyString());
    
    assertEquals("ADMINISTRATOR", service.getUserRole("admin"));
    assertEquals("REGULAR_USER", service.getUserRole("user"));
    assertEquals("GUEST", service.getUserRole("unknown"));
}
```


### Capturing and Modifying Arguments

`doAnswer()` allows you to capture arguments and perform operations on them :

```java
@Test
public void testArgumentCapture() {
    CustomerDao dao = mock(CustomerDao.class);
    
    doAnswer(invocation -> {
        Customer customer = (Customer) invocation.getArguments()[0];
        customer.setId(123); // Simulate database ID assignment
        return customer;
    }).when(dao).save(any(Customer.class));
    
    Customer newCustomer = new Customer("John", "john@example.com");
    Customer savedCustomer = dao.save(newCustomer);
    
    assertEquals(123, savedCustomer.getId());
}
```


## Complex Behavioral Examples

### Database Service with Connection Logic

```java
@Test
public void testDatabaseConnectionBehavior() {
    DatabaseService dbService = mock(DatabaseService.class);
    AtomicInteger connectionAttempts = new AtomicInteger(0);
    
    doAnswer(invocation -> {
        int attempts = connectionAttempts.incrementAndGet();
        if (attempts <= 2) {
            throw new DatabaseException("Connection failed");
        }
        return true; // Success on third attempt
    }).when(dbService).connect();
    
    // First two calls should fail
    assertThrows(DatabaseException.class, () -> dbService.connect());
    assertThrows(DatabaseException.class, () -> dbService.connect());
    // Third call should succeed
    assertTrue(dbService.connect());
}
```


### File Processing with Progress Tracking

```java
@Test
public void testFileProcessingWithProgress() {
    FileProcessor processor = mock(FileProcessor.class);
    List<String> progressLog = new ArrayList<>();
    
    doAnswer(invocation -> {
        String filename = (String) invocation.getArguments()[0];
        ProgressCallback callback = (ProgressCallback) invocation.getArguments()[1];
        
        // Simulate processing steps
        callback.onProgress(25, "Reading " + filename);
        progressLog.add("25% - Reading " + filename);
        
        callback.onProgress(50, "Parsing " + filename);
        progressLog.add("50% - Parsing " + filename);
        
        callback.onProgress(100, "Completed " + filename);
        progressLog.add("100% - Completed " + filename);
        
        return "Processing completed for " + filename;
    }).when(processor).processFile(anyString(), any(ProgressCallback.class));
    
    ProgressCallback callback = mock(ProgressCallback.class);
    String result = processor.processFile("data.txt", callback);
    
    assertEquals("Processing completed for data.txt", result);
    assertEquals(3, progressLog.size());
}
```


## Advanced Patterns

### Chain of Responsibility Simulation

```java
@Test
public void testChainOfResponsibility() {
    AuthService authService = mock(AuthService.class);
    
    doAnswer(invocation -> {
        String token = (String) invocation.getArguments()[0];
        
        // Simulate validation chain
        if (token.startsWith("expired_")) {
            throw new TokenExpiredException("Token has expired");
        }
        if (token.startsWith("invalid_")) {
            throw new InvalidTokenException("Invalid token format");
        }
        if (token.startsWith("valid_")) {
            return new User(token.substring(6)); // Extract username
        }
        
        throw new UnauthorizedException("Authentication failed");
    }).when(authService).authenticate(anyString());
    
    // Test various scenarios
    assertThrows(TokenExpiredException.class, 
        () -> authService.authenticate("expired_12345"));
    assertThrows(InvalidTokenException.class, 
        () -> authService.authenticate("invalid_xyz"));
    
    User user = authService.authenticate("valid_john");
    assertEquals("john", user.getUsername());
}
```


### State Management Simulation

```java
@Test
public void testStatefulBehavior() {
    PaymentGateway gateway = mock(PaymentGateway.class);
    Map<String, Double> accountBalances = new HashMap<>();
    accountBalances.put("ACC123", 1000.0);
    accountBalances.put("ACC456", 500.0);
    
    doAnswer(invocation -> {
        String account = (String) invocation.getArguments()[0];
        Double amount = (Double) invocation.getArguments()[1];
        
        Double currentBalance = accountBalances.get(account);
        if (currentBalance == null) {
            throw new AccountNotFoundException("Account not found: " + account);
        }
        
        if (currentBalance < amount) {
            throw new InsufficientFundsException("Insufficient balance");
        }
        
        // Update balance
        accountBalances.put(account, currentBalance - amount);
        return new Transaction(account, amount, "SUCCESS");
        
    }).when(gateway).processPayment(anyString(), anyDouble());
    
    // Test successful payment
    Transaction tx1 = gateway.processPayment("ACC123", 200.0);
    assertEquals("SUCCESS", tx1.getStatus());
    assertEquals(800.0, accountBalances.get("ACC123"));
    
    // Test insufficient funds
    assertThrows(InsufficientFundsException.class, 
        () -> gateway.processPayment("ACC456", 600.0));
}
```

The `doAnswer().when()` pattern provides unmatched flexibility for creating sophisticated mock behaviors that closely simulate real-world scenarios, making it invaluable for comprehensive unit testing.

