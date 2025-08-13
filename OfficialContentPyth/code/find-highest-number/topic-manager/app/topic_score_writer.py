from app.file_writer import FileWriter

class TopicScoreWriter:
    def __init__(self, file_writer):
        self._file_writer = file_writer

    def write_scores(self, top_scores, filename="output.txt"):
        if top_scores:
            tts = top_scores[0]
            data_to_write = f"{tts.get_topic_name()}, {tts.get_top_score()}"
            self._file_writer.write_line(data_to_write, filename)
