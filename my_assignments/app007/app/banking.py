# -*- coding: utf-8 -*-
"""
Created on Wed Aug 13 05:46:50 2025

@author: Charudatta
"""


def process_bank_transaction(transaction_id, get_transaction_data, audit_log):
    """
    Process a bank transaction by fetching its details and logging the event.

    Args:
        transaction_id (int): Unique ID of the transaction.
        get_transaction_data (function): Function returning dict:
                                         "amount" (float/int).
        audit_log (object): Object with a .log(msg) method.

    Returns:
        int: Transaction amount rounded down if numeric.
        None: If transaction not found or missing amount.
    """
    transaction = get_transaction_data(transaction_id)
    if not transaction or "amount" not in transaction:
        return None
    amount = transaction["amount"]
    if not isinstance(amount, (int, float)):
        raise ValueError("Transaction amount must be numeric")
    audit_log.log(f"Processed transaction {transaction_id} for amount {amount}")
    return int(amount)