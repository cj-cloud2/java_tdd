from app.topic_score_writer import TopicScoreWriter
from app.topic_top_score import TopicTopScore
from app.file_writer import FileWriter

def main():
    # Create a list of topic scores
    top_scores = [
        TopicTopScore("Physics", 89),
        TopicTopScore("Math", 75),
        TopicTopScore("Art", 92)
    ]

    # Create the file writer
    file_writer = FileWriter()

    # Create the score writer with the file writer
    writer = TopicScoreWriter(file_writer)

    # Write the scores (only the first one will be written, per original Java logic)
    writer.write_scores(top_scores, filename="testfile.txt")

    print("Finished writing to file.")

if __name__ == "__main__":
    main()

