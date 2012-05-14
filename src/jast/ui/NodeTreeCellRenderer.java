/*
 * Copyright (C) 2002 Thomas Dudziak, Jan Wloka
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package jast.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.TreeCellRenderer;

public class NodeTreeCellRenderer extends JLabel implements TreeCellRenderer
{
    private boolean _drawsFocusBorderAroundIcon;
    private boolean _isSelected;
    private boolean _hasFocus;
    private Color   _textSelectionColor;
    private Color   _textNonSelectionColor;
    private Color   _backgroundSelectionColor;
    private Color   _backgroundNonSelectionColor;
    private Color   _borderSelectionColor;

    public NodeTreeCellRenderer()
    {
        setHorizontalAlignment(JLabel.LEFT);

        setTextSelectionColor(UIManager.getColor("Tree.selectionForeground"));
        setTextNonSelectionColor(UIManager.getColor("Tree.textForeground"));
        setBackgroundSelectionColor(UIManager.getColor("Tree.selectionBackground"));
        setBackgroundNonSelectionColor(UIManager.getColor("Tree.textBackground"));
        setBorderSelectionColor(UIManager.getColor("Tree.selectionBorderColor"));

        Boolean value = (Boolean)UIManager.get("Tree.drawsFocusBorderAroundIcon");

        _drawsFocusBorderAroundIcon = (value != null) && value.booleanValue();
    }

    public Color getBackgroundNonSelectionColor()
    {
        return _backgroundNonSelectionColor;
    }

    public Color getBackgroundSelectionColor()
    {
        return _backgroundSelectionColor;
    }

    public Color getBorderSelectionColor()
    {
        return _borderSelectionColor;
    }

    private int getLabelStart()
    {
        Icon currentIcon = getIcon();

        if ((currentIcon != null) && (getText() != null))
        {
            return currentIcon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
        }
        else
        {
            return 0;
        }
    }

    public Dimension getPreferredSize()
    {
        Dimension retDimension = super.getPreferredSize();

        if (retDimension != null)
        {
            retDimension = new Dimension(retDimension.width + 3,
                                         retDimension.height);
        }
        return retDimension;
    }

    public Color getTextNonSelectionColor()
    {
        return _textNonSelectionColor;
    }

    public Color getTextSelectionColor()
    {
        return _textSelectionColor;
    }

    public Component getTreeCellRendererComponent(JTree   tree,
                                                  Object  value,
                                                  boolean isSelected,
                                                  boolean isExpanded,
                                                  boolean isLeaf,
                                                  int     row,
                                                  boolean hasFocus)
    {
        if (!(value instanceof NodeTreeItem))
        {
            return this;
        }

        String stringValue = tree.convertValueToText(value,
                                                     isSelected,
                                                     isExpanded,
                                                     isLeaf,
                                                     row,
                                                     hasFocus);

        _hasFocus = hasFocus;
        setText(stringValue);
        setForeground(isSelected ?
                          getTextSelectionColor() :
                          getTextNonSelectionColor());

        NodeTreeItem node = (NodeTreeItem)value;

        if (node.isTagged())
        {
            setForeground(NodeTreeItem.TAG_COLOR);
        }

        // There needs to be a way to specify disabled icons.
        if (tree.isEnabled())
        {
            setEnabled(true);
            setIcon(node.getIcon());
        }
        else
        {
            setEnabled(false);
            setDisabledIcon(node.getIcon());
        }
        setComponentOrientation(tree.getComponentOrientation());
        _isSelected = isSelected;

        return this;
    }

    public void paint(Graphics graphics)
    {
        Color backColor;

        if (_isSelected)
        {
            backColor = getBackgroundSelectionColor();
        }
        else
        {
            backColor = getBackgroundNonSelectionColor();
            if (backColor == null)
            {
                backColor = getBackground();
            }
        }

        int imageOffset = -1;

        if (backColor != null)
        {
            Icon currentIcon = getIcon();

            imageOffset = getLabelStart();
            graphics.setColor(backColor);
            if (getComponentOrientation().isLeftToRight())
            {
                graphics.fillRect(imageOffset,
                                  0,
                                  getWidth() - 1 - imageOffset,
                                  getHeight());
            }
            else
            {
                graphics.fillRect(0,
                                  0,
                                  getWidth() - 1 - imageOffset,
                                  getHeight());
            }
        }
        if (_hasFocus)
        {
            if (_drawsFocusBorderAroundIcon)
            {
                imageOffset = 0;
            }
            else if (imageOffset == -1)
            {
                imageOffset = getLabelStart();
            }

            Color backSelColor = getBorderSelectionColor();

            if (backSelColor != null)
            {
                graphics.setColor(backSelColor);
                if (getComponentOrientation().isLeftToRight())
                {
                    graphics.drawRect(imageOffset,
                                      0,
                                      getWidth() - 1 - imageOffset,
                                      getHeight() - 1);
                }
                else
                {
                    graphics.drawRect(0,
                                      0,
                                      getWidth() - 1 - imageOffset,
                                      getHeight() - 1);
                }
            }
        }
        super.paint(graphics);
    }

    public void setBackground(Color color)
    {
        if (color instanceof ColorUIResource)
        {
            color = null;
        }
        super.setBackground(color);
    }

    public void setBackgroundNonSelectionColor(Color color)
    {
        _backgroundNonSelectionColor = color;
    }

    public void setBackgroundSelectionColor(Color color)
    {
        _backgroundSelectionColor = color;
    }

    public void setBorderSelectionColor(Color color)
    {
        _borderSelectionColor = color;
    }

    public void setFont(Font font)
    {
        if (font instanceof FontUIResource)
        {
            font = null;
        }
        super.setFont(font);
    }

    public void setTextNonSelectionColor(Color color)
    {
        _textNonSelectionColor = color;
    }

    public void setTextSelectionColor(Color color)
    {
        _textSelectionColor = color;
    }
}
