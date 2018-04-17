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
	public String getElementAt(int index) {
		return vector.get(index);
	}

}
