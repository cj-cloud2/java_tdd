class StringTokeniser:
    def tokenise(self, input_val):
        """
        Tokenises a comma-separated string into a list of strings.
        
        :param input_val: The comma-separated input string.
        :return: List of tokens as strings.
        """
        if not isinstance(input_val, str):
            return []

        tokens = input_val.split(",")
        return [token.strip() for token in tokens if token.strip()]

