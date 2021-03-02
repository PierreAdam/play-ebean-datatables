package com.jackson42.play.ebeandatatables;

import com.jackson42.play.datatables.implementation.BasicPayload;
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
