class TopicScores:
    def __init__(self, topic_name, scores):
        self._topic_name = topic_name
        self._scores = scores

    def get_topic_name(self):
        return self._topic_name

    def get_scores(self):
        return self._scores

