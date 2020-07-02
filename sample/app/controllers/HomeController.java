package controllers;

import com.jackson42.play.ebeandatatables.EbeanDataTablesQuery;
import com.jackson42.play.ebeandatatables.PlayEbeanDataTables;
import com.jackson42.play.ebeandatatables.entities.Parameters;
import com.typesafe.config.Config;
import io.ebean.Expr;
import models.AccountModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.accountActions;

import javax.inject.Inject;

/**
 * Sample controller.
 */
public class HomeController extends Controller implements PlayEbeanDataTables {

    /**
     * The Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * The Config.
     */
    private final Config config;

    /**
     * The Form factory.
     */
    private final FormFactory formFactory;

    /**
     * Instantiates a new Home controller.
     *
     * @param config      the config
     * @param formFactory the form factory
     */
    @Inject
    public HomeController(final Config config,
                          final FormFactory formFactory) {
        this.config = config;
        this.formFactory = formFactory;
    }

    /**
     * Get the index page.
     *
     * @param request the request
     * @return the result
     */
    public Result GET_Index(final Http.Request request) {
        // If the database is empty, building the sample data
        if (AccountModel.find.query().where().findCount() == 0) {
            this.config.getStringList("sample-data").forEach(name -> {
                final String email = name.replace(" ", ".").trim().toLowerCase() + "@mail.com";
                final AccountModel account = new AccountModel(name, email);
                account.save();
            });
        }

        // Distribute the page
        return Results.ok(views.html.index.render());
    }

    /**
     * Post datatables account result.
     *
     * @param request the request
     * @return the result
     */
    public Result POST_DatatablesAccount(final Http.Request request) {
        return this.dataTablesAjaxRequest(request, this.formFactory,
                boundForm -> {
                    // On error callback
                    this.logger.error("The form is invalid : {}", boundForm.errors());
                    return Results.badRequest();
                },
                form -> {
                    // On success callback

                    // Retrieve the parameters from the form.
                    final Parameters parameters = form.getParameters();

                    // Build the EbeanDatatablesQuery from the model finder and the model class.
                    final EbeanDataTablesQuery<AccountModel> edt = new EbeanDataTablesQuery<>(AccountModel.find, AccountModel.class);

                    // If you want to restrict your query to something specific, like for example a table
                    // that show all the active account but ignore in every case the ones that are disabled;
                    // you can use the method setInitialQuerySupplier to forge the initial query yourself.
                    // ! THIS IS OPTIONAL ! //
                    edt.setWhere(query -> {
                        // You can add here something like .eq("active", true) or whatever you want to filter on.
                    });

                    // The fields id, name and email are in the model and will be solved automatically by default.
                    // You can however override the default behavior. For example to anonymize data.
                    // Example
                    edt.setFieldDisplaySupplier("email", accountModel -> {
                        // Let's anonymize all the emails that start with the letter a.
                        if (accountModel.getEmail().startsWith("a")) {
                            return accountModel.getEmail().replaceAll("[a-z0-9]", "*");
                        } else {
                            return accountModel.getEmail();
                        }
                    });

                    // On our view, you define a field called "actions" that does not exists on the model.
                    // Defining a custom field allows you to create calculated values on the fly or give some extra content.
                    // Right now, we're going to give the "actions" field an HTML content that will allow us to give
                    // an edition link and a deletion link to the table.
                    edt.setFieldDisplaySupplier("actions", accountModel -> {
                        // To get the full advantage of the template, we render a view and get the body of the view as our result.
                        return accountActions.render(accountModel, request).body();
                    });

                    // The method setOrderHandler allows you to set a custom way to order a column.
                    // This is useful if you want to order a field that is not in your model.
                    // This is totally optional if your field does exists in your model and can be ordered by your database.
                    edt.setOrderHandler("email", (query, orderEnum) -> {
                        // This is the equivalent of what EbeanDataTablesQuery will do on it's own !
                        query.order(String.format("email %s", orderEnum.name()));
                    });

                    // The default search handler will only search in the field itself. You can however customize the search handler for a column.
                    // In the page, we declared an input with the id "searchNameOrEmail" but we need to put it on a column of the Datatable.
                    // So we used the email as our "entry point"
                    edt.setSearchHandler("email", (query, searchTerm) -> {
                        query.or(
                                Expr.ilike("name", String.format("%%%s%%", searchTerm)),
                                Expr.ilike("email", String.format("%%%s%%", searchTerm))
                        );
                    });

                    // Finally, we return the json data according to the request parameters.
                    return Results.ok(edt.getAjaxResult(parameters));
                });
    }
}
