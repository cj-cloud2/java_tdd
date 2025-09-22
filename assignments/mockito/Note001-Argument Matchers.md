Built-in matchers are used in Mockito to provide flexibility when stubbing method calls or verifying interactions. Instead of using a fixed argument, you can use a matcher to specify a range of acceptable values.

# List of common built-in matchers:

* **`any()`:** Matches any object of a specific class. For example, `any(String.class)` matches any `String` argument. You can also use `anyString()`, `anyInt()`, `anyList()`, etc., for convenience.
* **`eq()`:** Matches an argument that is equal to the given value. It's the equivalent of using `==` or `.equals()` in a standard Java comparison.
* **`endsWith()`:** Matches a `String` argument that ends with a specified suffix. For instance, `endsWith("world")` will match ` "hello world"` but not ` "hello"`.
* **`startsWith()`:** Matches a `String` argument that begins with a specified prefix.
* **`contains()`:** Matches a `String` argument that contains a specified substring.
* **`isA()`:** A more specific version of `any()`, it matches an argument that is an instance of a given class. For example, `isA(MyObject.class)` matches any instance of `MyObject`.
* **`isNull()`:** Matches a `null` argument.
* **`notNull()`:** Matches any argument that is not `null`.
* **`anyString()`:** A type-safe version of `any(String.class)`. It matches any `String` argument.
* **`anyInt()`:** Matches any `int` or `Integer` argument.
* **`anyDouble()`:** Matches any `double` or `Double` argument.
* **`anyBoolean()`:** Matches any `boolean` or `Boolean` argument.

***

### Using Matchers with `eq()`

A common mistake when using matchers is mixing them with regular values. When you use one matcher in a method call, all other arguments must also be matchers. The `eq()` matcher is specifically designed to handle this.

For example, this is **incorrect**:
`when(mockObject.doSomething("test", 123));`
*This will not work if you have a matcher as another argument. You must use `eq()` for the `int`.*

This is the **correct** way:
`when(mockObject.doSomething(anyString(), eq(123)));`
*Here, `anyString()` matches any `String` and `eq(123)` matches the integer `123`. This ensures all arguments are handled by matchers, which is a requirement of Mockito.*