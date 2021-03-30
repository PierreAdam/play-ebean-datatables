# Play-Ebean-Datatables

[![Latest release](https://img.shields.io/github/v/release/PierreAdam/play-ebean-datatables)](https://github.com/PierreAdam/play-ebean-datatables/releases/latest)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/347f98262ae146ae86ac6b4c6c2844da)](https://www.codacy.com/gh/PierreAdam/play-ebean-datatables/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PierreAdam/play-ebean-datatables&amp;utm_campaign=Badge_Grade)
[![Code Climate maintainability](https://img.shields.io/codeclimate/maintainability-percentage/PierreAdam/play-ebean-datatables)](https://codeclimate.com/github/PierreAdam/play-ebean-datatables)
[![Snyk Vulnerabilities](https://img.shields.io/snyk/vulnerabilities/github/PierreAdam/play-ebean-datatables)](https://snyk.io/test/github/PierreAdam/play-ebean-datatables?targetFile=pom.xml)
![JDK](https://img.shields.io/badge/JDK-1.8+-blue.svg)
[![Build Status](https://travis-ci.com/PierreAdam/play-ebean-datatables.svg?branch=master)](https://travis-ci.com/PierreAdam/play-ebean-datatables)
[![GitHub license](https://img.shields.io/github/license/PierreAdam/play-ebean-datatables)](https://raw.githubusercontent.com/PierreAdam/play-ebean-datatables/master/LICENSE)

Play-Ebean-Datatables is a library for play framework that allows you to easily
integrate [Datatables](https://datatables.net/) in your Play project that use Ebean as an ORM.
*****

## Build the library

```shell
$> mvn compile
$> mvn package
```

### Install or deploy

To install in your local repository.

```shell
$> mvn install
```

To deploy to a remote repository.

```shell
$> mvn verify
$> mvn deploy -P release
```

## How to import the library

In your ```build.sbt``` file, you need to add a resolver to jitpack and the dependency for the module. This translate to
the following lines.

```scala
libraryDependencies += "com.jackson42.play" % "play-ebean-datatables" % "21.04"
```

## How to use the library

An example project with the common use cases is available on the sample folder of this repository.

### Important points

#### Your controller

For easier use, your controller will need to implement the interface `PlayEbeanDataTables`. This will allows you to
directly return the method `datatablesAjaxRequest` in your handling of the datatable query. The request will be
automatically parsed and analyzed. All you will need to do is to forge the answer by using the
object `EbeanDataTableQuery`.

A minimal controller would look like the following.

```java
import com.jackson42.play.ebeandatatables.EbeanDataTables;
import play.i18n.MessagesApi;

public class MyController extends Controller implements DataTablesHelper {
    private final FormFactory formFactory;

    private final EbeanDataTables<MyModel> edt;

    @Inject
    public MyController(final FormFactory formFactory, MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.edt = EbeanDataTables.create(MyModel.class, messagesApi);

        // Set a custom display suppliers.
        this.edt.setFieldDisplaySupplier("myField", (myModel, context) -> myModel.getMyDbField().trim());
        // Set a custom search handler to be able to match a partial search in the database.
        this.edt.setSearchHandler("myField", (query, search) -> query.ilike("myDbField", String.format("%%%s%%", search)));
        // Set a custom order handler.
        this.edt.setOrderHandler("myField", (query, orderEnum) -> query.orderBy(String.format("myDbField %s", orderEnum.name())));
    }

    public GET_MyPage(final Http.Request request) {
        return Results.ok(MyPage.render());
    }

    public POST_AjaxDatatable(final Http.Request request) {
        return this.dataTablesAjaxRequest(request, this.formFactory,
                boundForm -> {
                    // Error Callback
                    return Results.badRequest();
                },
                form -> {
                    // Success Callback
                    return Results.ok(edt.getAjaxResult(parameters));
                });
    }
}
```

#### The models

EbeanDataTableQuery will automatically analyze the parameters send from your page, and a query will be build accordingly
to your model and data in your view. It is important that your models have getters in order for the values to be
retrieved from them.

If you have a field called `foo`, the following getter name will be tried :

- `getFoo()`
- `isFoo()`
- `hasFoo()`
- `canFoo()`
- `foo()`

### Your webpage

Your webpage can be build using the scala template engine or anything else. The exemple bellow assume that you are using
the scala templates

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="//cdn.datatables.net/1.10.21/js/jquery.dataTables.js"></script>
</head>
<body>
<table id="my-list">
    <thead>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Email</th>
        <th>Actions</th>
    </tr>
    </thead>
</table>

<script>
    $(document).ready(function () {
        table = $('#my-list').DataTable({
            processing: true,
            serverSide: true,
            ajax: {
                type: "POST",
                contentType: "application/json; charset=utf-8",
                url: "@controllers.routes.MyController.POST_AjaxDatatable",
                data: function (d) {
                    return JSON.stringify({parameters: d});
                }
            },
            searching: true,
            dom: "ltipr",
            columns: [{
                name: "id",
                orderable: true,
                searchable: false
            }, {
                name: "name",
                orderable: true,
                searchable: true
            }, {
                name: "email",
                orderable: true,
                searchable: true
            }, {
                name: "actions",
                orderable: false,
                searchable: false
            }],
            order: [[0, "asc"]],
            columnDefs: []
        });
    });
</script>
</body>
</html>
```

## Versions

| Library Version | Play DataTable Core | Play Version | Ebean Version | Tested DataTables Version  |
|-----------------|---------------------|--------------|---------------|----------------------------|
| 21.04           | 21.04               | 2.8.x        | 12.7.x        | 1.10.x                     |
| 21.03           | 21.03               | 2.8.x        | 12.7.x        | 1.10.x                     |
| 20.11           | N/A                 | 2.8.x        | 12.6.x        | 1.10.x                     |
| 20.10           | N/A                 | 2.8.x        | 12.4.x        | 1.10.x                     |

### Changelog

#### 21.04

- Update Play DataTable Code library to 21.04.
- Compilation is now made to be done with Java 11 but compiled for Java 1.8.
- Update of the sample project to utilize the latest version

#### 21.03

- Complete refactor to utilise the new [play-datatables core library](https://github.com/PierreAdam/play-datatables).
- Remove deprecated function.
- API Breaking on the fields suppliers and on the result method.
- A payload can now be sent to the library allowing more context in the suppliers and the query.
- New naming
- New way to instantiate the `EbeanDataTables`.

#### 20.11

- Remove deprecated method `getPagedList`.
- Deprecate method `setInitialQuerySupplier`. Should only be set on the constructors to avoid race conditions when using
  the same instance for multiple requests.
- New constructors allowing more coherent behavior.

## License

This project is released under terms of
the [MIT license](https://raw.githubusercontent.com/PierreAdam/play-ebean-datatables/master/LICENSE).
