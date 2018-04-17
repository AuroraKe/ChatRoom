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
	 * @param list 正在绘制的JList
	 * 
	 * @param value list.getModel().getElementAt(index)返回的值
	 * 
	 * @param index 单元格索引
	 * 
	 * @param isSelected 是否被选中，true表示选中，false表示未选中
	 * 
	 * @param cellHasFocus 如果焦点在单元格上则返回true
	 */
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 设置边框为5的空白
		if (value != null) {
			setText(value.toString());
			setIcon(new ImageIcon("images\\1.jpg"));
		}
		// isSelected表示是否选中，true表示选中，false表示未选中
		if (isSelected) {
			setBackground(new Color(255, 255, 153));
			setForeground(Color.BLACK);
		} else {
			setBackground(Color.WHITE);
			setForeground(Color.BLACK);
		}
		setEnabled(list.isEnabled()); // list.isEnabled()如果组件已启用，则返回 true；
		// 否则返回false
		setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
		setOpaque(true);
		return this;
	}
}
