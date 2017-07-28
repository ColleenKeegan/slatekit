package slatekit.sampleapp.core.apis

import slatekit.core.common.AppContext
import slatekit.entities.core.EntityService
import slatekit.integration.common.ApiBaseEntity
import slatekit.sampleapp.core.models.Movie

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
class SampleEntityApi(ctx: AppContext) : ApiBaseEntity<Movie, EntityService<Movie>>(ctx, Movie::class) {

    fun patch(id:Long, title:String): String = "patched $id with $title"
}
