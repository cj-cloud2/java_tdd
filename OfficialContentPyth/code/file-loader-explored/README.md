# TDD File Loader (Python Version)

## 📄 Overview

This module is a Python conversion of the Java-based `FileLoader` system. It demonstrates how to **read a file** and **calculate its size** based on the content length, while showing how to **decouple logic from the file system** to create more testable and flexible code.

It shows how to progressively build a file loading utility using Test-Driven Development (TDD), with Git tags marking key milestones in the development process.

## Git Tags and Milestones

| Tag                                     | Description                                   |
|-----------------------------------------|-----------------------------------------------|
| `start_here`                            | Project scaffold with folders and empty files |
| `v1.0-code_in_main-read_text_file`      | File reading logic added                      |
| `v1.2-file_loading_in_its_own_class`    | Created `FileLoader` class in its own module  |
| `v1.3-moved_file_loading_into_function` | Refactored file loading logic into a function |
| `v1.4` (same as `main`)                 | Final clean version with full test coverage   |
|-----------------------------------------------------------------------------------------|

## Differences from the Java Project

- Removed Java-specific structures like interfaces and packages
- Uses Python’s `unittest` and `unittest.mock` instead of JUnit/Mockito
- Relies on duck typing instead of interface implementation
- Uses simple modules and folder-based structure
- Code is simplified using Python idioms like `with open(...)` and list comprehensions(yum!)

## Project Structure

```
tdd_file_loader/
├── app/
│   └── file_loader.py          # File loading logic
├── tests/
│   └── test_live_file_loader.py# Unit tests
│   └── test_file_loader.py     # Unit tests
└── main.py                     # Entry point for testing the module
```

## 📦 Module Structure

- `file_loader.py` – Contains the `FileLoader` class with two methods for loading files:
  - `load_file()` – Uses standard reading.
  - `load_file_with_func()` – Accepts a lambda/stub function to inject custom file loading logic.

- `test_live_file_loader.py` - Unit tests for the `FileLoader`, designed to:
  - Tests the original, IO-dependent usage of FileLoader:
  - Uses an actual file path like c:/tmp/KeyboardHandler.txt
  - Relies on the file being present with known content size

Serves as a reminder of why we avoid tightly coupling to external systems in unit tests
- `test_file_loader.py` – Unit tests for the `FileLoader`, designed to:
  - Avoid filesystem dependency
  - Demonstrate the power of injecting file loaders
  - Support mocking and stubbing as test techniques

## 🧪 Tests Explained

### ✅ `test_load_all_of_file_using_inbuilt_open_as_lambda`

- Simulates reading a real file using a lambda function that mimics reading from disk.
- Injects test data (e.g., `["Hello", "world"]`) instead of reading from a real file.
- Ensures `FileLoader` works even when using a different loader logic.

### ✅ `test_load_all_of_file_via_stub`

- Fully stubs the file loader logic with hardcoded lines.
- Demonstrates total decoupling from the filesystem.
- Ensures deterministic, fast, and reliable tests.

### ✅ `test_load_all_of_file_using_mock`
Uses a custom FakeFile class to simulate the behavior of a file reader.

- Mocks the read_all_lines() method to return a predefined list (e.g., ["Hello", "world"])
- Injects this behavior into FileLoader via a lambda

Purpose: Validates that FileLoader handles external dependencies via mocking — without touching the real file system.

Benefit: Highlights how to simulate external services/interfaces in Python cleanly using test doubles (fakes/mocks), helping reinforce decoupling and reliable tests.

## Running the Application

python main.py

## Running the Tests

python -m unittest discover -s tests

## 🚀 Benefits

- 🧱 **Decoupled Design**: Business logic separated from IO
- 🧪 **Testable Code**: No real file reads needed
- ⚡ **Fast Feedback**: Tests run quickly and consistently
- 🛠️ **Flexible**: Supports mock/stub/lambda strategies

## 📚 Summary

The Python `FileLoader` module shows how to bring good software design principles—like dependency injection and testability—to simple tasks like file reading. The addition of lambda and stub-based tests mirrors modern TDD (Test-Driven Development) practices and prepares your codebase for maintainability and scalability.

Each Git tag marks a meaningful step in the TDD process, evolving from a simple script to a modular and testable file loader.

