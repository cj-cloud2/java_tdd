from app.string_tokeniser import StringTokeniser

def main():
    input_text = ",cleese"
    tokeniser = StringTokeniser()
    tokens = tokeniser.tokenise(input_text)
    print("Tokens:", tokens)

if __name__ == "__main__":
    main()

