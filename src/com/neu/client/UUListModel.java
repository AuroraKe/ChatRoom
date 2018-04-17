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
	 * ����list�ĳ���
	 */
	@Override
	public int getSize() {
		return vector.size();
	}

	/*
	 * ��������index��ֵ
	 */
	@Override
	public Object getElementAt(int index) {
		return vector.get(index);
	}

}
