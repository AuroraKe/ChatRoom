package com.neu.test;

import java.awt.*;  
import javax.swing.*;  
import javax.swing.text.*;  
   
public class Styling  
{  
    public Styling()  
    {  
        String text = "To refer to locations within the sequence, the " +  
                      "coordinates used are the location between two " +  
                      "characters.\nAs the diagram below shows, a location " +  
                      "in a text document can be referred to as a position, " +  
                      "or an offset. This position is zero-based.";  
   
        SimpleAttributeSet aSet = new SimpleAttributeSet();   
        StyleConstants.setForeground(aSet, Color.blue);  
        StyleConstants.setBackground(aSet, Color.orange);  
        StyleConstants.setFontFamily(aSet, "lucida bright italic");  
        StyleConstants.setFontSize(aSet, 18);  
   
        SimpleAttributeSet bSet = new SimpleAttributeSet();  
        StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);  
        StyleConstants.setUnderline(bSet, true);  
        StyleConstants.setFontFamily(bSet, "lucida typewriter bold");  
        StyleConstants.setFontSize(bSet, 24);  
   
        JTextPane textPane = new JTextPane();  
        textPane.setText(text);  
        StyledDocument doc = textPane.getStyledDocument();  
        doc.setCharacterAttributes(105, doc.getLength()-105, aSet, false);  
        doc.setParagraphAttributes(0, 104, bSet, false);  
   
        JTextArea area = new JTextArea();
        area.setText(text);
        area.add(textPane);
        
        JFrame f = new JFrame();  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        f.add(new JScrollPane(textPane));  
        //f.add(new JScrollPane(area));
        f.setSize(400,400);  
        f.setLocation(200,200);  
        f.setVisible(true);  
    }  
   
    public static void main(String[] args)  
    {  
        new Styling();  
    }  
}  
