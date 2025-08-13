import os
import sys

mpath =os.path.abspath(os.path.join(os.path.dirname(__file__), "..","..", "find-highest_number"))
sys.path.insert(0, mpath)

from app.topic_top_score import TopicTopScore
from final.app.highest_number_finder import HighestNumberFinder


class TopicManager:
    def __init__(self, highest_number_finder=None):
        if highest_number_finder is None:
            highest_number_finder = HighestNumberFinder()
        self._highest_number_finder = highest_number_finder

    def find_topic_high_scores(self, topic_scores_list):
        top_scores = []

        for ts in topic_scores_list:
            top_score = self._highest_number_finder.find_highest_number(ts.get_scores())
            top_scores.append(TopicTopScore(ts.get_topic_name(), top_score))

        return top_scores

