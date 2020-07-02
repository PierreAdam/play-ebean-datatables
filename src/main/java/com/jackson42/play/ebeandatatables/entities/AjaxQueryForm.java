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

import play.data.validation.Constraints;

import javax.validation.Valid;

/**
 * AjaxQueryForm.
 *
 * @author Pierre Adam
 * @since 20.07.02
 */
public class AjaxQueryForm {

    /**
     * The parameters.
     */
    @Valid
    @Constraints.Required
    private Parameters parameters;

    /**
     * Get the parameters.
     *
     * @return the parameters
     */
    public Parameters getParameters() {
        return this.parameters;
    }

    /**
     * Set the parameters
     *
     * @param parameters the parameters
     */
    public void setParameters(final Parameters parameters) {
        this.parameters = parameters;
    }
}
