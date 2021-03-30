/*
 * MIT License
 *
 * Copyright (c) 2020 Pierre Adam
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

package datatables;

import com.jackson42.play.ebeandatatables.EbeanDataTablesLogic;
import io.ebean.Expr;
import models.AccountModel;
import play.i18n.MessagesApi;
import views.html.ActionsRender;
import views.html.EmailRender;

/**
 * AccountDatatables.
 *
 * @author Pierre Adam
 * @since 20.11.26
 */
public class AccountDatatables extends EbeanDataTablesLogic<AccountModel> {

    /**
     * Instantiates a new Account datatables.
     *
     * @param messagesApi the messages api
     */
    public AccountDatatables(final MessagesApi messagesApi) {
        super(AccountModel.find, AccountModel.class, messagesApi);

        // If you want to restrict your query to something specific, like for example a table
        // that show all the active account but ignore in every case the ones that are disabled;
        // you can use the method setInitialQuerySupplier to forge the initial query yourself.
        // ! THIS IS OPTIONAL ! //
        this.setInitProviderConsumer(query -> {
            // You can add here something like .eq("active", true) or whatever you want to filter on.
        });

        // The fields id, uid, firstName, lastName, email and role are in the model and will be solved automatically by default.
        // You can however override the default behavior. For example to anonymize data.
        // Example
        this.setFieldDisplaySupplier("uid", accountModel -> {
            // Let's anonymize all the uid of all account with a first name that start with the letter a.
            if (accountModel.getFirstName().toLowerCase().startsWith("a")) {
                return accountModel.getUid().toString().replaceAll("[a-z0-9]", "*");
            } else {
                return accountModel.getUid().toString();
            }
        });

        // We can also customize the rendering of a field using a view specially made for that field.
        this.setFieldDisplayHtmlSupplier("email",
                (accountModel, context) -> EmailRender.render(accountModel, context.getRequest()));

        // On our view, you define a field called "actions" that does not exists on the model.
        // Defining a custom field allows you to create calculated values on the fly or give some extra content.
        // Right now, we're going to give the "actions" field an HTML content that will allow us to give
        // an edition link and a deletion link to the table.
        this.setFieldDisplayHtmlSupplier("actions", (accountModel, context) -> {
            // To get the full advantage of the template, we render a view as our result.
            context.getPayload(); // The Payload for the current request.
            context.getMessages(); // The messages for the current request. (If MessagesApi was given in the constructor)
            context.getRequest(); // The current request
            return ActionsRender.render(accountModel, context.getRequest());
        });

        // The method setOrderHandler allows you to set a custom way to order a column.
        // This is useful if you want to order a field that is not in your model.
        // This is totally optional if your field does exists in your model and can be ordered by your database.
        this.setOrderHandler("email", (query, orderEnum) -> {
            // This is the equivalent of what EbeanDataTablesQuery will do on it's own !
            query.order(String.format("email %s", orderEnum.name()));
        });

        // The default search handler will only search in the field itself. You can however customize the search handler for a column.
        // In the page, we declared an input with the id "searchNameOrEmail" but we need to put it on a column of the Datatable.
        // So we used the email as our "entry point"
        this.setSearchHandler("email", (query, searchTerm) -> {
            query.or(
                    Expr.ilike("firstName", String.format("%%%s%%", searchTerm)),
                    Expr.or(
                            Expr.ilike("firstName", String.format("%%%s%%", searchTerm)),
                            Expr.ilike("email", String.format("%%%s%%", searchTerm))
                    )
            );
        });
    }
}
