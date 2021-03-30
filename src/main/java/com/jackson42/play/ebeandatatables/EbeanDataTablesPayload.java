/*
 * MIT License
 *
 * Copyright (c) 2021 Pierre Adam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.jackson42.play.ebeandatatables;

import com.jackson42.play.datatables.implementations.BasicPayload;
import io.ebean.ExpressionList;
import play.libs.typedmap.TypedKey;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * EbeanDataTablesPayload.
 *
 * @param <T> the type parameter
 * @author Pierre Adam
 * @since 21.03.01
 */
public class EbeanDataTablesPayload<T> extends BasicPayload {

    /**
     * The total match count key.
     */
    private final TypedKey<Integer> totalMatchCount;

    /**
     * The Extra query key.
     */
    private final TypedKey<Consumer<ExpressionList<T>>> extraQueryKey;

    /**
     * Instantiates a new Ebean data tables payload.
     */
    public EbeanDataTablesPayload() {
        this.totalMatchCount = TypedKey.create();
        this.extraQueryKey = TypedKey.create();
    }

    /**
     * Instantiates a new Ebean data tables payload.
     *
     * @param extraQuery the extra query
     */
    public EbeanDataTablesPayload(final Consumer<ExpressionList<T>> extraQuery) {
        this();
        this.put(this.extraQueryKey, extraQuery);
    }

    /**
     * Sets extra query.
     *
     * @param extraQuery the extra query
     */
    public void setExtraQuery(final Consumer<ExpressionList<T>> extraQuery) {
        this.put(this.extraQueryKey, extraQuery);
    }

    /**
     * Gets extra query.
     *
     * @return the extra query
     */
    public Optional<Consumer<ExpressionList<T>>> getExtraQuery() {
        return this.getOptional(this.extraQueryKey);
    }

    /**
     * Sets total match count.
     *
     * @param value the value
     */
    public void setTotalMatchCount(final Integer value) {
        this.put(this.totalMatchCount, value);
    }

    /**
     * Get the total match count.
     *
     * @return optional of the total match count
     */
    public Optional<Integer> getTotalMatchCount() {
        return this.getOptional(this.totalMatchCount);
    }
}
