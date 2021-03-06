package test.setup

import slatekit.apis.Api
import slatekit.apis.ApiAction
import slatekit.apis.security.AuthMode
import slatekit.apis.security.AuthModes
import slatekit.apis.security.Protocols
import slatekit.apis.security.Verbs
import slatekit.common.auth.Roles

/**
 * REST Sample
 * This example shows a REST compliant API.
 * The Slate Kit API Container comes with middle-ware. One of the
 * middle-ware components is the Rewrite which can convert 1 request
 * to another request. Using this Rewrite component, we can customize
 * and enforce conventions.
 *
 *
 * NOTES:
 * 1. REST        : REST Support for methods with 0 or 1 parameters.
 * 2. Conventions : Create your own conventions using the Rewrite component
 *
 * HTTP:
 *      Method   Route
 *      GET      /SampleREST/                =>   getAll
 *      GET      /SampleREST/1               =>   getById   ( 1 )
 *      POST     /SampleREST/{item}          =>   create    ( item )
 *      PUT      /SampleREST/{item}          =>   update    ( item )
 *      DELETE   /SampleREST/{item}          =>   delete    ( item )
 *      DELETE   /SampleREST/1               =>   deleteById( 1 )
 *      PATCH    /SampleREST?id=1&title=abc  =>   patch     ( id, title )
 *
 *
 * CLI:
 *      SampleREST.getAll
 *      SampleREST.getById    -id=1
 *      SampleREST.create     -title="abc" -category="action"
 *      SampleREST.update     -id=1 -title="abc" -category="action"
 *      SampleREST.delete     -id=1 -title="abc" -category="action"
 *      SampleREST.deleteById -id=1
 *      SampleREST.patch      -id=1 -title="abc"
 */
class SampleRESTApi {

    fun getAll(): List<Movie> = Movie.samples()


    fun getById(id:Long): Movie = Movie.samples().first { it.id == id }


    fun create(item: Movie): Long = item.copy(id = Movie.samples().last().id + 1).id


    fun update(item: Movie): String = "updated ${item.id}"


    fun patch(id:Long, title:String): String = "patched $id with $title"


    fun delete(item: Movie): String = "deleted ${item.id}"


    fun deleteById(id:Long): String = "deleteById $id"


    fun activateById(id:Long): String = "activateById $id"
}


@Api(area = "samples", name = "restVerbAuto", desc = "sample api for testing verb mode with auto",
        auth = AuthModes.token, roles = Roles.all, verb = Verbs.auto, protocol = Protocols.all)
class SampleRESTVerbModeAutoApi {

    @ApiAction()
    fun getAll(): List<Movie> = Movie.samples()


    @ApiAction()
    fun getById(id:Long): Movie = Movie.samples().first { it.id == id }


    @ApiAction()
    fun create(item: Movie): Long = item.copy(id = Movie.samples().last().id + 1).id


    @ApiAction()
    fun update(item: Movie): String = "updated ${item.id}"


    @ApiAction()
    fun patch(id:Long, title:String): String = "patched $id with $title"


    @ApiAction()
    fun delete(item: Movie): String = "deleted ${item.id}"


    @ApiAction()
    fun deleteById(id:Long): String = "deleteById $id"


    @ApiAction()
    fun activateById(id:Long): String = "activateById $id"
}



@Api(area = "samples", name = "restVerbRest", desc = "sample api for testing verb mode with auto",
        auth = AuthModes.token, roles = Roles.all, verb = Verbs.rest, protocol = Protocols.all)
class SampleRESTVerbModeRestApi {

    @ApiAction()
    fun getAll(): List<Movie> = Movie.samples()


    @ApiAction()
    fun getById(id:Long): Movie = Movie.samples().first { it.id == id }


    @ApiAction()
    fun create(item: Movie): Long = item.copy(id = Movie.samples().last().id + 1).id


    @ApiAction()
    fun update(item: Movie): String = "updated ${item.id}"


    @ApiAction()
    fun patch(id:Long, title:String): String = "patched $id with $title"


    @ApiAction()
    fun delete(item: Movie): String = "deleted ${item.id}"


    @ApiAction()
    fun deleteById(id:Long): String = "deleteById $id"


    @ApiAction()
    fun activateById(id:Long): String = "activateById $id"
}