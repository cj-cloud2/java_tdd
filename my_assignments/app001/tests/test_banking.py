# -*- coding: utf-8 -*-
"""
Created on Wed Aug 13 05:11:41 2025

@author: Charudatta
"""


import unittest
from app.banking import get_current_balance

def stub_get_account_data(account_id):
    if account_id == 111:
        return {"name": "Mary", "balance": 12000}
    elif account_id == 222:
        return {"name": "Patricia", "balance": 24750}
    elif account_id == 333:
        return {"name": "Jennifer", "balance": 8000}
    return None

class TestBanking(unittest.TestCase):
    def test_balance_basic(self):
        bal = get_current_balance(111, stub_get_account_data)
        self.assertEqual(bal, 12000)

    def test_balance_large(self):
        bal = get_current_balance(222, stub_get_account_data)
        self.assertEqual(bal, 24750)

    def test_balance_not_found(self):
        bal = get_current_balance(999, stub_get_account_data)
        self.assertIsNone(bal)

if __name__ == "__main__":
    unittest.main()