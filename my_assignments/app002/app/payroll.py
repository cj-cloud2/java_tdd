# -*- coding: utf-8 -*-
"""
Created on Wed Aug 13 05:24:02 2025

@author: Charudatta
"""


def calculate_net_salary(employee_id, get_employee_details):
    """
    Calculate the net salary for an employee.

    Formula:
        net = gross - 20% tax - 2% PF

    Args:
        employee_id (int): The employee's unique ID.
        get_employee_details (function): Dependency injection for fetching employee data.

    Returns:
        int: Net salary rounded down to the nearest integer.
        None: If employee not found or invalid data.
    """
    details = get_employee_details(employee_id)
    if not details or "gross" not in details:
        return None

    gross_salary = details["gross"]
    if not isinstance(gross_salary, (int, float)):
        raise ValueError("Gross salary must be a number")

    tax_amount = gross_salary * 0.20  # 20% tax
    pf_amount = gross_salary * 0.02   # 2% PF
    net_salary = gross_salary - tax_amount - pf_amount
    return int(net_salary)