## Purpose

The `FileLoader` class is a simple utility that reads the contents of a text file and
computes the total number of characters. This mirrors the functionality of its Java
equivalent but is refactored to follow Pythonic conventions.

## Key Features

- **load_file(filename)**: Reads a file from the filesystem.
- **load_file_with_func(func)**: Allows injection of custom line-loading logic. This is especially useful for **unit testing**, where we want to avoid actual file I/O.
- **get_lines()**: Returns the raw lines read from the file.

## Why `load_file_with_func`?

In the original Java code, testing relied on reading a specific file path which might not exist in all environments. This caused fragile tests. By introducing a functional interface (or callable in Python), we decouple the file reading from the logic that processes it, allowing tests to supply mock data.

## Example

def fake_loader(_):
    return ["Hello", "World"]

loader = FileLoader("unused.txt")
size = loader.load_file_with_func(fake_loader)
assert size == 10

