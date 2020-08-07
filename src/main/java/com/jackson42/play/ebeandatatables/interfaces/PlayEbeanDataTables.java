/*
 * Copyright (C) 2014 - 2020 PayinTech, SAS - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.jackson42.play.ebeandatatables.interfaces;

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
