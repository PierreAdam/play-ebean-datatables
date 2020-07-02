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

package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * AccountModel.
 *
 * @author Pierre Adam
 * @since 20.07.02
 */
@Entity
public class AccountModel extends Model {

    /**
     * Helpers to request model.
     */
    public static final Finder<Long, AccountModel> find = new Finder<>(AccountModel.class);

    /**
     * The Id.
     */
    @Id
    private Long id;

    /**
     * The Name.
     */
    @Column(name = "name", nullable = false, unique = false)
    private String name;

    /**
     * The Email.
     */
    @Column(name = "email", nullable = false, unique = false)
    private String email;

    /**
     * Instantiates a new Account model.
     *
     * @param name  the name
     * @param email the email
     */
    public AccountModel(final String name, final String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(final String email) {
        this.email = email;
    }
}
