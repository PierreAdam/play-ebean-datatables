package controllers;

import com.jackson42.play.ebeandatatables.entities.Parameters;
import com.jackson42.play.ebeandatatables.interfaces.PlayEbeanDataTables;
import com.typesafe.config.Config;
import datatables.AccountDatatables;
import models.AccountModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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
     * The Account datatables.
     */
    private final AccountDatatables accountDatatables;

    /**
     * Instantiates a new Home controller.
     *
     * @param config      the config
     * @param formFactory the form factory
     * @param messagesApi the messages api
     */
    @Inject
    public HomeController(final Config config,
                          final FormFactory formFactory,
                          final MessagesApi messagesApi) {
        this.config = config;
        this.formFactory = formFactory;
        this.accountDatatables = new AccountDatatables(messagesApi);
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

                    // Finally, we return the json data according to the request parameters.
                    return Results.ok(this.accountDatatables.getAjaxResult(request, parameters));
                });
    }

    /**
     * Post datatables account result async.
     * <p>
     * Use completion stage to be non-blocking in case you have some data that take time to be processed.
     * An even more efficient way would be to assign a specific thread pool.
     * See https://www.playframework.com/documentation/2.8.x/ThreadPools
     * </p>
     *
     * @param request the request
     * @return the result
     */
    public CompletionStage<Result> POST_DatatablesAccountAsync(final Http.Request request) {
        return this.dataTablesAjaxRequest(request, this.formFactory,
                boundForm -> {
                    // On error callback
                    this.logger.error("The form is invalid : {}", boundForm.errors());
                    return CompletableFuture.completedFuture(Results.badRequest());
                },
                form -> {
                    // On success callback

                    return CompletableFuture.supplyAsync(form::getParameters) // Retrieve the parameters from the form.
                            .thenApply(parameters -> this.accountDatatables.getAjaxResult(request, parameters)) // Get the result from the form parameters.
                            .thenApply(Results::ok); // Return the result.
                });
    }
}
