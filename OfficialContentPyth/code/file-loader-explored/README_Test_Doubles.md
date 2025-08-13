# 🧪 Test Doubles in Unit Testing

A **test double** is a general term used in software testing to describe any object that **stands in for a real component** during a test. It's a “double” because it takes the place of the real thing — much like a stunt double in a movie.

## 🔁 Types of Test Doubles

| Type      | Purpose                                       | Example Use Case                              |
|-----------|-----------------------------------------------|-----------------------------------------------|
| **Dummy** | Passed around but never actually used         | Placeholder for parameters that aren't used   |
| **Fake**  | Has working implementation but simplified     | In-memory DB, fake file loader                |
| **Stub**  | Returns predefined responses to method calls  | Always returns `89` when asked for top score  |
| **Mock**  | Pre-programmed with expectations; verifies interactions| Check if `write_line("Physics, 89")` was called |
| **Spy**   | Records how it was used for later verification| Tracks how many times a method was called     |

## 🧠 Why Use Test Doubles?

- ✅ Isolate the **unit under test** from external dependencies (e.g., file system, database, network)
- ⚡ Avoid **slow**, **unreliable**, or **unavailable** real systems
- 🔁 Reproduce **edge cases** or **failure conditions** with control
- 🔍 Focus on **behavioral verification** (e.g., “Did this method get called?”)

---

### 🛠 Example in Context

In our `FileLoader` project, we use:
- A **fake** or **lambda** to simulate file content
- A **mock** to verify that `write_line()` or `clear_the_log()` was called
- These remove the need for a real file and make tests faster and more reliable


