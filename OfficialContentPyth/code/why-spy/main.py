from app.data_clerk import DataClerk, FileLog

def main():
    # Create an instance of FileLog
    file_log = FileLog()

    # Create an instance of DataClerk with the file log
    clerk = DataClerk(file_log)

    # Call the process_data method
    clerk.process_data()

if __name__ == "__main__":
    main()

