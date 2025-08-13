class TopicTopScore:
    def __init__(self, topic_name, score):
        self._topic_name = topic_name
        self._top_score = score

    def get_topic_name(self):
        return self._topic_name

    def get_top_score(self):
        return self._top_score

    # Overloading the == operator - will now compare two TopicTopScore objects
    # based on their topic_name (case insensitive) and top_score values.
    def __eq__(self, other):
        if not isinstance(other, TopicTopScore):
            return False
        return (self._topic_name.lower() == other._topic_name.lower()
                and self._top_score == other._top_score)

    def __repr__(self):
        return f"TopicTopScore(topic_name='{self._topic_name}', top_score={self._top_score})"
