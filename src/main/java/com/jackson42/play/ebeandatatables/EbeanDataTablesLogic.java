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

import com.jackson42.play.datatables.entities.Parameters;
import com.jackson42.play.datatables.entities.internal.DataSource;
import com.jackson42.play.datatables.enumerations.OrderEnum;
import com.jackson42.play.datatables.implementation.SimplePlayDataTables;
import io.ebean.ExpressionList;
import io.ebean.Finder;
import io.ebean.PagedList;
import play.i18n.MessagesApi;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * EbeanDataTableQuery.
 *
 * @param <T> the type parameter
 * @author Pierre Adam
 * @since 20.07.02
 */
public class EbeanDataTablesLogic<T> extends SimplePlayDataTables<T, ExpressionList<T>, EbeanDataTablesPayload<T>> implements EbeanDataTables<T> {

    /**
     * Constructor.
     *
     * @param tClass               the tClass
     * @param messagesApi          the messages api
     * @param initialQuerySupplier the initial query supplier
     */
    protected EbeanDataTablesLogic(final Class<T> tClass, final MessagesApi messagesApi, final Supplier<ExpressionList<T>> initialQuerySupplier) {
        super(tClass, messagesApi, initialQuerySupplier);
    }

    /**
     * Constructor.
     *
     * @param finder      the finder
     * @param tClass      the tClass
     * @param messagesApi the messages api
     */
    protected EbeanDataTablesLogic(final Finder<?, T> finder, final Class<T> tClass, final MessagesApi messagesApi) {
        super(tClass, messagesApi, () -> finder.query().where());
    }

    @Override
    protected void setPagination(final ExpressionList<T> query, final int startElement, final int numberOfElement) {
        query.setFirstRow(startElement).setMaxRows(numberOfElement).where();
    }

    @Override
    protected void fallbackOrderHandler(final ExpressionList<T> query, final String columnName, final OrderEnum order) {
        query.order(String.format("%s %s", columnName, order.name()));
    }

    @Override
    protected void fallbackSearchHandler(final ExpressionList<T> query, final String columnName, final String value) {
    }

    @Override
    protected DataSource<T> dataSourceFromProvider(final ExpressionList<T> query, final EbeanDataTablesPayload<T> payload) {
        final PagedList<T> pagedList = query.findPagedList();
        final List<T> entities = pagedList.getList();
        return new DataSource<>(payload.getTotalMatchCount().orElse(pagedList.getTotalCount()), pagedList.getTotalCount(), entities);
    }

    @Override
    protected void preSearchHook(final ExpressionList<T> query, final EbeanDataTablesPayload<T> payload, final Parameters parameters) {
        final Optional<Consumer<ExpressionList<T>>> extraQuery = payload.getExtraQuery();
        extraQuery.ifPresent(extra -> extra.accept(query));
        payload.setTotalMatchCount(query.findCount());
    }

    @Override
    protected void postSearchHook(final ExpressionList<T> query, final EbeanDataTablesPayload<T> payload, final Parameters parameters) {
    }

    @Override
    protected void preOrderHook(final ExpressionList<T> query, final EbeanDataTablesPayload<T> payload, final Parameters parameters) {
    }

    @Override
    protected void postOrderHook(final ExpressionList<T> query, final EbeanDataTablesPayload<T> payload, final Parameters parameters) {
    }

    @Override
    protected EbeanDataTablesPayload<T> getDefaultPayload() {
        return new EbeanDataTablesPayload<>();
    }
}
