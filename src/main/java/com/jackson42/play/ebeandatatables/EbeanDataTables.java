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

import com.jackson42.play.datatables.interfaces.PlayDataTables;
import io.ebean.ExpressionList;
import io.ebean.Finder;
import play.i18n.MessagesApi;

import java.util.function.Supplier;

/**
 * EbeanDataTablesQuery.
 *
 * @param <T> the type parameter
 * @author Pierre Adam
 * @since 21.03.01
 */
public interface EbeanDataTables<T> extends PlayDataTables<T, ExpressionList<T>, EbeanDataTablesPayload<T>> {

    /**
     * Instantiate a new EbeanDataTables.
     *
     * @param <T>                  the type parameter
     * @param tClass               the tClass
     * @param messagesApi          the messages api
     * @param initialQuerySupplier the initial query supplier
     * @return the ebean data tables
     */
    static <T> EbeanDataTables<T> create(final Class<T> tClass, final MessagesApi messagesApi, final Supplier<ExpressionList<T>> initialQuerySupplier) {
        return new EbeanDataTablesLogic<T>(tClass, messagesApi, initialQuerySupplier);
    }

    /**
     * Instantiate a new EbeanDataTables.
     *
     * @param <T>         the type parameter
     * @param tClass      the tClass
     * @param messagesApi the messages api
     * @return the ebean data tables
     */
    static <T> EbeanDataTables<T> create(final Class<T> tClass, final MessagesApi messagesApi) {
        return new EbeanDataTablesLogic<T>(tClass, messagesApi, null);
    }

    /**
     * Instantiate a new EbeanDataTables.
     *
     * @param <T>                  the type parameter
     * @param tClass               the t class
     * @param initialQuerySupplier the initial query supplier
     * @return the ebean data tables
     */
    static <T> EbeanDataTables<T> create(final Class<T> tClass, final Supplier<ExpressionList<T>> initialQuerySupplier) {
        return EbeanDataTables.create(tClass, null, initialQuerySupplier);
    }

    /**
     * Instantiate a new EbeanDataTables.
     *
     * @param <T>         the type parameter
     * @param finder      the finder
     * @param tClass      the t class
     * @param messagesApi the messages api
     * @return the ebean data tables
     */
    static <T> EbeanDataTables<T> create(final Finder<?, T> finder, final Class<T> tClass, final MessagesApi messagesApi) {
        return new EbeanDataTablesLogic<T>(finder, tClass, messagesApi);
    }

    /**
     * Instantiate a new EbeanDataTables.
     *
     * @param <T>    the type parameter
     * @param finder the finder
     * @param tClass the t class
     * @return the ebean data tables
     */
    static <T> EbeanDataTables<T> create(final Finder<?, T> finder, final Class<T> tClass) {
        return EbeanDataTables.create(finder, tClass, null);
    }

    /**
     * Instantiate a new EbeanDataTables.
     *
     * @param <T>    the type parameter
     * @param tClass the t class
     * @return the ebean data tables
     */
    static <T> EbeanDataTables<T> create(final Class<T> tClass) {
        return EbeanDataTables.create(new Finder<>(tClass), tClass, null);
    }
}
