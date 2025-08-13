# -*- coding: utf-8 -*-
"""
Created on Wed Aug 13 05:24:45 2025

@author: Charudatta
"""


import unittest
from app.payroll import calculate_net_salary

def stub_get_employee_details(employee_id):
    if employee_id == 101:
        return {"name": "Jack", "gross": 50000}
    elif employee_id == 202:
        return {"name": "Raymond", "gross": 65000}
    elif employee_id == 303:
        return {"name": "Dennis", "gross": 80000}
    return None

class TestPayrollDeduction(unittest.TestCase):
    def test_salary_basic(self):
        net = calculate_net_salary(101, stub_get_employee_details)
        self.assertEqual(net, 39000)  

    def test_salary_high(self):
        net = calculate_net_salary(202, stub_get_employee_details)
        self.assertEqual(net, 50700)  

    def test_salary_not_found(self):
        net = calculate_net_salary(999, stub_get_employee_details)
        self.assertIsNone(net)

if __name__ == "__main__":
    unittest.main()