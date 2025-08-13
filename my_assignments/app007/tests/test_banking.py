# -*- coding: utf-8 -*-
"""
Created on Wed Aug 13 05:46:02 2025

@author: Charudatta
"""


import unittest
from app.banking import process_bank_transaction

class DummyAuditLog:
    def log(self, message):
        pass

def stub_get_transaction_data(transaction_id):
    if transaction_id == 1001:
        return {"account_holder": "Sandra", "amount": 1500}
    elif transaction_id == 1002:
        return {"account_holder": "Matthew", "amount": 3200}
    elif transaction_id == 1003:
        return {"account_holder": "Ashley", "amount": 2700}
    return None

class TestBanking(unittest.TestCase):
    def test_transaction_basic(self):
        dummy_log = DummyAuditLog()
        amount = process_bank_transaction(1001, stub_get_transaction_data, dummy_log)
        self.assertEqual(amount, 1500)

    def test_transaction_second(self):
        dummy_log = DummyAuditLog()
        amount = process_bank_transaction(1002, stub_get_transaction_data, dummy_log)
        self.assertEqual(amount, 3200)

    def test_transaction_not_found(self):
        dummy_log = DummyAuditLog()
        amount = process_bank_transaction(9999, stub_get_transaction_data, dummy_log)
        self.assertIsNone(amount)

if __name__ == "__main__":
    unittest.main()