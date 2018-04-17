package com.neu.client;

import java.util.List;

import javax.swing.AbstractListModel;

class UUListModel extends AbstractListModel<String> {
	private static final long serialVersionUID = 2017092716063402L;
	private List<String> vector;

	public UUListModel(List<String> vector) {
		this.vector = vector;
	}

	/*
	 * 返回list的长度
	 */
	@Override
	public int getSize() {
		return vector.size();
	}

	/*
	 * 返回索引index的值
	 */
	@Override
	public String getElementAt(int index) {
		return vector.get(index);
	}

}
