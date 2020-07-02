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

package com.jackson42.play.ebeandatatables;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jackson42.play.ebeandatatables.entities.AjaxQueryForm;
import com.jackson42.play.ebeandatatables.entities.Column;
import com.jackson42.play.ebeandatatables.entities.Order;
import com.jackson42.play.ebeandatatables.entities.Parameters;
import com.jackson42.play.ebeandatatables.enumerations.OrderEnum;
import io.ebean.ExpressionList;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.PagedList;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;
import play.libs.Json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * EbeanDataTableQuery.
 *
 * @param <T> the type parameter
 * @author Pierre Adam
 * @since 20.07.02
 */
public class EbeanDataTablesQuery<T extends Model> {

    /**
     * The potential prefixes of the getter in the target classes.
     */
    protected static final String[] METHOD_PREFIXES = {"get", "is", "has", "can"};

    /**
     * The finder of T
     */
    private final Finder<?, T> finder;

    /**
     * The class of T
     */
    private final Class<T> tClass;

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     */
    private final Map<String, Function<T, String>> fieldsDisplaySupplier;

    /**
     * The fields search handler. If set for a given field, the handler will be called when searching on that field.
     * If not set, the search will have no effect.
     */
    private final Map<String, BiConsumer<ExpressionList<T>, String>> fieldsSearchHandler;

    /**
     * The fields order handler. If set for a given field, the handler will be called when ordering on that field.
     * If not set, the search will be set to the name of the field followed by "ASC" or "DESC"
     */
    private final Map<String, BiConsumer<ExpressionList<T>, OrderEnum>> fieldsOrderHandler;

    /**
     * The global search supplier. If set, the handler will be called when a search not specific to a field is required.
     */
    private BiConsumer<ExpressionList<T>, String> globalSearchHandler;

    /**
     * The initial where condition. Is called on each forged request and should not contains orders or weird things.
     */
    private Consumer<ExpressionList<T>> where;

    /**
     * The initial query supplier allows you to create your own initial query.
     */
    private Supplier<ExpressionList<T>> initialQuerySupplier;

    /**
     * Constructor.
     *
     * @param finder the finder
     * @param tClass the tClass
     * @param where  the initial where close
     */
    public EbeanDataTablesQuery(final Finder<?, T> finder, final Class<T> tClass, final Consumer<ExpressionList<T>> where) {
        this.finder = finder;
        this.tClass = tClass;
        this.where = where;
        this.fieldsDisplaySupplier = new HashMap<>();
        this.fieldsSearchHandler = new HashMap<>();
        this.fieldsOrderHandler = new HashMap<>();
        this.globalSearchHandler = null;
        this.initialQuerySupplier = null;
    }

    /**
     * Constructor
     *
     * @param finder the finder
     * @param tClass the tClass
     */
    public EbeanDataTablesQuery(final Finder<?, T> finder, final Class<T> tClass) {
        this(finder, tClass, null);
    }

    /**
     * Constructor
     *
     * @param tClass the tClass
     */
    public EbeanDataTablesQuery(final Class<T> tClass) {
        this(new Finder<>(tClass), tClass, null);
    }

    /**
     * Add an object to a Json ArrayNode trying to solve the type.
     * If the object can't be solved, null is put on the array.
     *
     * @param data   the array
     * @param object the object
     */
    private static void addToArray(final ArrayNode data, final Object object) {
        if (object == null) {
            data.addNull();
        } else if (object instanceof String) {
            data.add((String) object);
        } else if (object instanceof Integer) {
            data.add((Integer) object);
        } else if (object instanceof Long) {
            data.add((Long) object);
        } else if (object instanceof Double) {
            data.add((Double) object);
        } else if (object instanceof UUID) {
            data.add(object.toString());
        } else if (object instanceof Boolean) {
            data.add((Boolean) object);
        } else if (object instanceof JsonNode) {
            data.add((JsonNode) object);
        } else if (object instanceof Enum) {
            final String tmp = object.toString();
            try {
                data.add(Integer.valueOf(tmp));
            } catch (final NumberFormatException ignore) {
                data.add(tmp);
            }
        } else if (object instanceof DateTime) {
            data.add(((DateTime) object).toString("dd/MM/yyyy"));
        } else {
            data.addNull();
        }
    }

    /**
     * The initial where condition. Is called on each forged request and should not contains orders or weird things.
     *
     * @param where the where handler
     */
    public void setWhere(final Consumer<ExpressionList<T>> where) {
        this.where = where;
    }

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     *
     * @param field         the field name
     * @param fieldSupplier the field display supplier
     */
    public void setFieldDisplaySupplier(final String field, final Function<T, String> fieldSupplier) {
        this.fieldsDisplaySupplier.put(field, fieldSupplier);
    }

    /**
     * The fields search handler. If set for a given field, the handler will be called when searching on that field.
     * If not set, the search will have no effect.
     *
     * @param field         the field name
     * @param searchHandler the field search handler
     */
    public void setSearchHandler(final String field, final BiConsumer<ExpressionList<T>, String> searchHandler) {
        this.fieldsSearchHandler.put(field, searchHandler);
    }

    /**
     * The fields order handler. If set for a given field, the handler will be called when ordering on that field.
     * If not set, the search will be set to the name of the field followed by "ASC" or "DESC"
     *
     * @param field        the field name
     * @param orderHandler the field order handler
     */
    public void setOrderHandler(final String field, final BiConsumer<ExpressionList<T>, OrderEnum> orderHandler) {
        this.fieldsOrderHandler.put(field, orderHandler);
    }

    /**
     * The global search supplier. If set, the handler will be called when a search not specific to a field is required.
     *
     * @param globalSearchHandler the global search handler
     */
    public void setGlobalSearchHandler(final BiConsumer<ExpressionList<T>, String> globalSearchHandler) {
        this.globalSearchHandler = globalSearchHandler;
    }

    /**
     * Set the initial query supplier allows you to create your own initial query.
     *
     * @param initialQuerySupplier the initial query supplier
     */
    public void setInitialQuerySupplier(final Supplier<ExpressionList<T>> initialQuerySupplier) {
        this.initialQuerySupplier = initialQuerySupplier;
    }

    /**
     * Get a paged list from the given parameters. Parameters SHOULD come from a form.
     *
     * @param parameters the parameters
     * @return the paged list
     * @see AjaxQueryForm
     * @see Parameters
     */
    public PagedList<T> getPagedList(final Parameters parameters) {
        return this.getPagedList(parameters, parameters.getIndexedColumns());
    }

    /**
     * Forge a query using the finder and the initial where provider.
     *
     * @return the query
     */
    private ExpressionList<T> forgeQuery() {
        final ExpressionList<T> query;

        if (this.initialQuerySupplier != null) {
            query = this.initialQuerySupplier.get();
        } else {
            query = this.finder.query().where();
        }

        if (this.where != null) {
            this.where.accept(query);
        }

        return query;
    }

    /**
     * Internal paged list
     *
     * @param parameters     the parameters
     * @param indexedColumns the columns indexed
     * @return the paged list
     */
    private PagedList<T> getPagedList(final Parameters parameters, final Map<Integer, Column> indexedColumns) {
        final ExpressionList<T> query = this.forgeQuery().setFirstRow(parameters.getStart()).setMaxRows(parameters.getLength()).where();

        if (parameters.getSearch() != null && parameters.getSearch().getValue() != null && !parameters.getSearch().getValue().isEmpty()) {
            this.globalSearchHandler.accept(query, parameters.getSearch().getValue());
        }

        for (int i = 0; i < indexedColumns.size(); i++) {
            final Column column = indexedColumns.get(i);

            if (column == null || column.getSearch() == null) {
                continue;
            }

            if (column.getSearch().getValue() != null && !column.getSearch().getValue().isEmpty()) {
                if (this.fieldsSearchHandler.containsKey(column.getName())) {
                    this.fieldsSearchHandler.get(column.getName()).accept(query, column.getSearch().getValue());
                }
            }
        }

        if (parameters.getOrder() != null) {
            for (final Order order : parameters.getOrder()) {
                final String columnName = indexedColumns.get(order.getColumn()).getName();
                if (this.fieldsOrderHandler.containsKey(columnName)) {
                    this.fieldsOrderHandler.get(columnName).accept(query, order.getOrder());
                } else {
                    query.order(String.format("%s %s", columnName, order.getOrder().name()));
                }
            }
        }

        return query.findPagedList();
    }

    /**
     * Build the Ajax result in the form of a Json ObjectNode. Parameters SHOULD come from a form.
     *
     * @param parameters the parameters
     * @return the Json ObjectNode
     * @see AjaxQueryForm
     * @see Parameters
     */
    public ObjectNode getAjaxResult(final Parameters parameters) {
        final Map<Integer, Column> indexedColumns = parameters.getIndexedColumns();
        final PagedList<T> pagedList = this.getPagedList(parameters, indexedColumns);
        final ObjectNode result = Json.newObject();
        final ArrayNode data = Json.newArray();

        for (final T t : pagedList.getList()) {
            data.add(this.objectToArrayNode(t, indexedColumns));
        }

        result.put("draw", parameters.getDraw());
        result.put("recordsTotal", this.forgeQuery().findCount());
        result.put("recordsFiltered", pagedList.getTotalCount());
        result.set("data", data);

        return result;
    }

    /**
     * Convert an object to an Array node using the indexed columns.
     *
     * @param t              the object
     * @param indexedColumns the indexed column
     * @return the array node
     */
    private ArrayNode objectToArrayNode(final T t, final Map<Integer, Column> indexedColumns) {
        final ArrayNode data = Json.newArray();

        for (int i = 0; i < indexedColumns.size(); i++) {
            final Column column = indexedColumns.get(i);
            if (column == null) {
                data.addNull();
                continue;
            }

            if (this.fieldsDisplaySupplier.containsKey(column.getName())) {
                data.add(this.fieldsDisplaySupplier.get(column.getName()).apply(t));
            } else {
                final Method method = this.methodForColumn(column);
                if (method == null) {
                    data.addNull();
                    continue;
                }

                try {
                    EbeanDataTablesQuery.addToArray(data, method.invoke(t));
                } catch (final IllegalAccessException | InvocationTargetException e) {
                    data.addNull();
                }
            }
        }

        return data;
    }

    /**
     * Try to solve a getter for a given column.
     *
     * @param column the column
     * @return the method or null
     */
    protected Method methodForColumn(final Column column) {
        for (final String methodPrefix : EbeanDataTablesQuery.METHOD_PREFIXES) {
            try {
                return this.tClass.getMethod(methodPrefix + StringUtils.capitalize(column.getName()));
            } catch (final NoSuchMethodException ignore) {
            }
        }

        try {
            return this.tClass.getMethod(column.getName());
        } catch (final NoSuchMethodException ignore) {
        }

        return null;
    }
}
