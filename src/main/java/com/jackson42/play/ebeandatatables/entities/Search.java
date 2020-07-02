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

/**
 * ColumnSearchForm.
 *
 * @author Pierre Adam
 * @since 20.07.02
 */
public class Search {

    /**
     * The name to search.
     */
    private String value;

    /**
     * Is the search a regex.
     */
    private boolean regex;

    /**
     * Get the name.
     *
     * @return the name
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Set the name
     *
     * @param value the name
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Is the query a regex
     *
     * @return true if regex
     */
    public boolean isRegex() {
        return this.regex;
    }

    /**
     * Set is a regex.
     *
     * @param regex true if is a regex
     */
    public void setRegex(final boolean regex) {
        this.regex = regex;
    }
}
