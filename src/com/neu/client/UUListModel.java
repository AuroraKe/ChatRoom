package com.neu.client;

import java.util.Vector;

import javax.swing.AbstractListModel;

class UUListModel extends AbstractListModel {
	private static final long serialVersionUID = 2017092716063402L;
	private Vector<String> vector;

	public UUListModel(Vector<String> vector) {
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
	public Object getElementAt(int index) {
		return vector.get(index);
	}

}
