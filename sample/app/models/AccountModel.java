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

import enumeration.Role;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

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
     * The Uid.
     */
    @Column(name = "uid", nullable = false, unique = true)
    private final UUID uid;

    /**
     * The First name.
     */
    @Column(name = "first_name", nullable = false, unique = false)
    private String firstName;

    /**
     * The Last name.
     */
    @Column(name = "last_name", nullable = false, unique = false)
    private String lastName;

    /**
     * The Email.
     */
    @Column(name = "email", nullable = false, unique = false)
    private String email;

    /**
     * Is the Email validated.
     */
    @Column(name = "email_validated", nullable = false, unique = false)
    private boolean emailValidated;

    /**
     * The Role.
     */
    @Column(name = "role", nullable = false, unique = false)
    private Role role;

    /**
     * Instantiates a new Account model.
     *
     * @param firstName      the first name
     * @param lastName       the last name
     * @param email          the email
     * @param emailValidated the email validated
     * @param role           the role
     */
    public AccountModel(final String firstName, final String lastName, final String email, final boolean emailValidated, final Role role) {
        this.uid = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.emailValidated = emailValidated;
        this.role = role;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public UUID getUid() {
        return this.uid;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
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

    /**
     * Is email validated boolean.
     *
     * @return the boolean
     */
    public boolean isEmailValidated() {
        return this.emailValidated;
    }

    /**
     * Sets email validated.
     *
     * @param emailValidated the email validated
     */
    public void setEmailValidated(final boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(final Role role) {
        this.role = role;
    }
}
