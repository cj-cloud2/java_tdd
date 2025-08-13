# -*- coding: utf-8 -*-
"""
Created on Wed Aug 13 05:38:21 2025

@author: Charudatta
"""


def check_item_stock(item_id, get_item_data, audit_log):
    """
    Retrieves current stock level for an item.

    Args:
        item_id (int): Unique item ID.
        get_item_data (function): Stubbed function returning dict with:
                                  "quantity" (int) and other item metadata.
        audit_log (object): Has .log(message) method.

    Returns:
        int: Quantity in stock if found.
        None: If not found or missing 'quantity'.
    """
    item = get_item_data(item_id)
    if not item or "quantity" not in item:
        return None
    quantity = item["quantity"]
    if not isinstance(quantity, int):
        raise ValueError("Item quantity must be an integer")
    audit_log.log(f"Checked stock for {item_id}: {quantity}")
    return quantity