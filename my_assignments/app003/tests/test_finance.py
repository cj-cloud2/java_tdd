# -*- coding: utf-8 -*-
"""
Created on Wed Aug 13 05:31:35 2025

@author: Charudatta
"""


import unittest
from app.finance import calculate_loan_interest

def stub_get_loan_data(loan_id):
    if loan_id == 501:
        return {"borrower": "Tyler", "amount": 100000, "interest_rate": 0.05}
    elif loan_id == 602:
        return {"borrower": "Linda", "amount": 200000, "interest_rate": 0.04}
    elif loan_id == 703:
        return {"borrower": "Patricia", "amount": 150000, "interest_rate": 0.045}
    return None

class TestFinance(unittest.TestCase):
    def test_interest_basic(self):
        interest = calculate_loan_interest(501, stub_get_loan_data)
        self.assertEqual(interest, 5000)

    def test_interest_lower_rate(self):
        interest = calculate_loan_interest(602, stub_get_loan_data)
        self.assertEqual(interest, 8000)

    def test_interest_not_found(self):
        interest = calculate_loan_interest(999, stub_get_loan_data)
        self.assertIsNone(interest)

if __name__ == "__main__":
    unittest.main()