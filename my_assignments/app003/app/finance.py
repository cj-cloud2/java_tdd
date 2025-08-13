# -*- coding: utf-8 -*-
"""
Created on Wed Aug 13 05:30:52 2025

@author: Charudatta
"""


def calculate_loan_interest(loan_id, get_loan_data):
    """
    Calculate simple interest (1 year) on a loan by fetching loan data.

    Args:
        loan_id (int): Unique ID of the loan.
        get_loan_data (function): Function that returns a dict containing:
                                  "amount" (principal float/int),
                                  "interest_rate" (float).

    Returns:
        int: Interest amount rounded down to nearest integer.
        None: If loan not found or required data missing.
    """
    loan = get_loan_data(loan_id)
    if not loan or "amount" not in loan or "interest_rate" not in loan:
        return None

    principal = loan["amount"]
    rate = loan["interest_rate"]

    if not isinstance(principal, (int, float)) or not isinstance(rate, (int, float)):
        raise ValueError("Loan amount and interest rate must be numeric")

    interest = principal * rate
    return int(interest)