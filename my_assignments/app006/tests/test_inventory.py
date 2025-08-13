# -*- coding: utf-8 -*-
"""
Created on Wed Aug 13 05:40:13 2025

@author: Charudatta
"""
import unittest
from app.inventory import check_item_stock


class DummyAuditLog:
    def log(self, message):
        pass  # Does nothing (dummy)

def stub_get_item_data(item_id):
    if item_id == 101:
        return {"item_name": "Laptop", "quantity": 25, "owner": "Emily"}
    elif item_id == 102:
        return {"item_name": "Mouse", "quantity": 150, "owner": "Daniel"}
    return None

class TestInventory(unittest.TestCase):
    def test_item_quantity_basic(self):
        dummy_log = DummyAuditLog()
        qty = check_item_stock(101, stub_get_item_data, dummy_log)
        self.assertEqual(qty, 25)

    def test_item_quantity_second(self):
        dummy_log = DummyAuditLog()
        qty = check_item_stock(102, stub_get_item_data, dummy_log)
        self.assertEqual(qty, 150)
        
    def test_item_not_found(self):
        dummy_log = DummyAuditLog()
        qty = check_item_stock(999, stub_get_item_data, dummy_log)
        self.assertIsNone(qty)