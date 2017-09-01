package com.a.android.wheretogo.search;

import com.a.android.wheretogo.item.Item;
import com.a.android.wheretogo.item.Item2;
import com.a.android.wheretogo.item.Item3;

import java.util.List;

public interface OnCategorySearchListener {
	public void onSuccess(List<Item> itemList, List<Item2> itemList5, List<Item3> itemList6);
	public void onFail();
}
