/*
 * Copyright (c) 2020 Pierre Adam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 */

package com.jackson42.play.ebeandatatables.entities;

import com.jackson42.play.ebeandatatables.enumerations.OrderEnum;

import java.util.Locale;

/**
 * Order.
 *
 * @author Pierre Adam
 * @since 20.07.02
 */
public class Order {

    /**
     * The number of the column.
     */
    private int column;

    /**
     * The direction of sort.
     */
    private String dir;

    /**
     * Get the column.
     *
     * @return the column
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Set the column.
     *
     * @param column the column
     */
    public void setColumn(final int column) {
        this.column = column;
    }

    /**
     * Get the direction
     *
     * @return the direction
     */
    public String getDir() {
        return this.dir;
    }

    /**
     * Set the direction
     *
     * @param dir the direction
     */
    public void setDir(final String dir) {
        this.dir = dir;
    }

    /**
     * Get the order as an enum. Based on the direction
     *
     * @return the order
     */
    public OrderEnum getOrder() {
        try {
            return OrderEnum.valueOf(this.dir.toUpperCase(Locale.ENGLISH));
        } catch (final IllegalArgumentException ignore) {
            return OrderEnum.ASC;
        }
    }
}
