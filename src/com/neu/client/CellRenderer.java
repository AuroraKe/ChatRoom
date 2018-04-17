package com.neu.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class CellRenderer extends JLabel implements ListCellRenderer<Object> {

	private static final long serialVersionUID = 2017092716063401L;

	public CellRenderer() {
		setOpaque(true);
	}

	/*
	 * @param list ���ڻ��Ƶ�JList
	 * 
	 * @param value list.getModel().getElementAt(index)���ص�ֵ
	 * 
	 * @param index ��Ԫ������
	 * 
	 * @param isSelected �Ƿ�ѡ�У�true��ʾѡ�У�false��ʾδѡ��
	 * 
	 * @param cellHasFocus ��������ڵ�Ԫ�����򷵻�true
	 */
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // ���ñ߿�Ϊ5�Ŀհ�
		if (value != null) {
			setText(value.toString());
			setIcon(new ImageIcon("images\\1.jpg"));
		}
		// isSelected��ʾ�Ƿ�ѡ�У�true��ʾѡ�У�false��ʾδѡ��
		if (isSelected) {
			setBackground(new Color(255, 255, 153));
			setForeground(Color.BLACK);
		} else {
			setBackground(Color.WHITE);
			setForeground(Color.BLACK);
		}
		setEnabled(list.isEnabled()); // list.isEnabled()�����������ã��򷵻� true��
		// ���򷵻�false
		setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
		setOpaque(true);
		return this;
	}
}
