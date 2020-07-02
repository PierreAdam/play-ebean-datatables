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

import com.jackson42.play.ebeandatatables.entities.AjaxQueryForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;

import java.util.function.Function;

/**
 * PlayDatatable.
 *
 * @author Pierre Adam
 * @since 20.07.02
 */
public interface PlayEbeanDataTables {

    /**
     * Forge a form from the request and call the errorCallback or the successCallback according to the form.
     *
     * @param <RESULT>             the type of the return
     * @param request         the request
     * @param formFactory     the form factory
     * @param errorCallback   the on error
     * @param successCallback the callback
     * @return the r
     */
    default <RESULT> RESULT dataTablesAjaxRequest(final Http.Request request,
                                                  final FormFactory formFactory,
                                                  final Function<Form<AjaxQueryForm>, RESULT> errorCallback,
                                                  final Function<AjaxQueryForm, RESULT> successCallback) {
        final Form<AjaxQueryForm> boundForm = formFactory.form(AjaxQueryForm.class).bindFromRequest(request);

        if (boundForm.hasErrors()) {
            return errorCallback.apply(boundForm);
        }

        return successCallback.apply(boundForm.get());
    }
}
