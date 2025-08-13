
def get_current_balance(account_id, get_account_data):
    """
    Return the current balance for a given account.

    Args:
        account_id (int): Unique account identifier.
        get_account_data (function): Stub/service for fetching account data.

    Returns:
        int: Account balance, rounded if float.
        None: If account not found or invalid data.
    """
    details = get_account_data(account_id)
    if not details or "balance" not in details:
        return None
    balance = details["balance"]
    if not isinstance(balance, (int, float)):
        raise ValueError("Balance must be a number")
    return int(balance)
