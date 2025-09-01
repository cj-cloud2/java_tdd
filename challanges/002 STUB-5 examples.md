
# TDD Stub Examples

Here are 5 examples of Difficulty Level 4 tasks using stubs (simple hand-written Java classes) in JUnit 5, including the detailed requirements, complete annotated test class, stub explanations, and service class implementations. These are modeled to require multi-step logic, conditional flows, and to validate integration with a stubbed collaborator.

**Summary of Stubbing Usage**
For every example, a hand-crafted stub class replaces real external dependencies, providing controlled, test-specific behavior. This enables deterministic, focused logic testing of the service in JUnit 5, where the unit interacts with external dependencies whose responses are simulated by the stub object. Each test method’s comments explain its precise role and the stub’s use within.



### Example 1: User Notification Filtering

**Task Requirement:**
Implement a `NotificationService` that filters notifications for a given user based on their user settings. The filtering logic should skip notifications marked as “Do Not Disturb” in settings. User settings come from a dependency `UserSettingsProvider`. Create unit tests using a hand-crafted stub for `UserSettingsProvider`.

#### Test Class
 NotificationServiceTest
    // Purpose: Verifies filtering out "DND" notifications according to stubbed settings.    
    // Purpose: Ensures all notifications are shown if "Do Not Disturb" is off in stub.
    
// STUB: Hardcoded to simulate settings
class UserSettingsProviderStub implements UserSettingsProvider 
// Stub Explanation:
// 'UserSettingsProviderStub' is used instead of the real settings provider. 
// It's a simple Java class that allows the test to simulate different user settings.
// Where: Used directly in test methods, injected into service.
// How: Constructor parameter determines simulated DND setting.



#### Service and Stub Implementation
NotificationService

// Interface for collaboration
interface UserSettingsProvider {
    boolean isDoNotDisturbEnabled(String username);
}











### Example 2: Payment Processing Fee Calculation

**Task Requirement:**
Write `PaymentProcessor` that adds a transaction fee to payments based on user’s account type from an external dependency `AccountTypeProvider`. Write tests using a stub for `AccountTypeProvider` to simulate different account types.

#### Test Class
 PaymentProcessorTest
    // Tests correct fee for premium account
    // Tests correct fee for standard account


class AccountTypeProviderStub implements AccountTypeProvider
    // Stub used where fee depends on account type. The stub class returns the type set in its constructor.



#### Service and Stub
class PaymentProcessor 
interface AccountTypeProvider {
    String getAccountType(String userId);
}













### Example 3: Item Restocking Logic

**Task Requirement:**
Create an `InventoryManager` that checks if items need to be restocked using the `SupplierService`. If stock is below threshold, it should request restock. Use a stub for `SupplierService` to simulate item availability.

#### Test Class
class InventoryManagerTest
    // Ensures restocking occurs when supply is available
    // Ensures no restock if supplier is out of stock

class SupplierServiceStub implements SupplierService 
    // Stub Explanation: SupplierServiceStub's constructor sets supplied availability. Used directly to inject simulated supplier status.
 

#### Service and Stub

class InventoryManager
interface SupplierService {
    boolean hasStock(String item);
    void order(String item, int quantity);
}







