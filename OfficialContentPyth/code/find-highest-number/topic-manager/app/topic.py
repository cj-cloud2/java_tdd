class Topic:
    def __init__(self, topic_name: str, score: int):
        self._topic_name = topic_name
        self._score = score

    def get_topic_name(self) -> str:
        return self._topic_name

    def get_score(self) -> int:
        return self._score

