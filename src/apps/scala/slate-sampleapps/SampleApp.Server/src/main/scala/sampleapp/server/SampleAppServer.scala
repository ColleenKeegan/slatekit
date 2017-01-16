/**
<slate_header>
  author: Kishore Reddy
  url: https://github.com/kishorereddy/scala-slate
  copyright: 2016 Kishore Reddy
  license: https://github.com/kishorereddy/scala-slate/blob/master/LICENSE.md
  desc: a scala micro-framework
  usage: Please refer to license on github for more info.
</slate_header>
  */

package sampleapp.server

// Sample services
import sampleapp.core.common.{AppApiKeys, AppAuth, AppEncryptor}
import sampleapp.core.models.{Movie, User}
import sampleapp.core.services.{MovieApi, MovieService, UserService, UserApi}
import slate.common.args.ArgsSchema
import slate.core.apis.ApiReg

// Slate Result Monad + database/logger/application metadata
import slate.common.{Result}
import slate.common.logging.LoggerConsole

// Slate Base Application ( to support command line args, environments, config etc )
import slate.core.app.{AppOptions, AppRunner, AppProcess}
import slate.core.common.AppContext

// Slate entities ( mini-ORM ) - Optional
import slate.entities.core.Entities

import slate.integration.{VersionApi, AppApi}
import slate.server.core.Server
import scala.reflect.runtime.universe.{typeOf}

object SampleAppServer
{

  /**
    * Entry point into the sample console application.
    *
    * java -jar sample_app.jar -env=dev -log.level=info -config.location = "jars"
    * java -jar sample_app.jar -env=dev -log.level=info -config.location = "conf"
    * java -jar sample_app.jar -env=dev -log.level=info -config.location = "file://./conf-sample-batch"
    * java -jar sample_app.jar -env=dev -log.level=info -config.location = "file://./conf-sample-shell"
    * java -jar sample_app.jar -env=dev -log.level=info -config.location = "file://./conf-sample-server"
    * java -jar sample_app.jar --version
    * java -jar sample_app.jar --about
    * java -jar sample_app.jar ?
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    // Supply the args passed into app to runner.
    // NOTE: The args format is -key=value.
    AppRunner.run(new SampleAppServer(), Option(args))
  }
}



/**
  * Sample Server application.
  *
  * IMPORTANT
  * 1. You can further extend the slate AppProcess ( refer to AppBase in SampleApp.Core )
  * 2. The onInit method is ONLY provided here to show how the context can be set up
  * 3. The AppBase class ( in SampleApp.Core ) can be used to have a common base class with
  *    the onInit method already implemented for your specific needs.
  *
  * NOTE(s):
  * 1. you can extend from AppBase ( SampleApp.Core ) to avoid initializing context in onInit here
  * 2. command line arguments are optional but set up here for demo purposes
  */
class SampleAppServer extends AppProcess
{
  // setup the command line arguments.
  // NOTE(s):
  // 1. These values can can be setup in the env.conf file
  // 2. If supplied on command line, they override the values in .conf file
  // 3. If any of these are required and not supplied, then an error is display and program exists
  // 4. Help text can be easily built from this schema.
  override lazy val argsSchema = new ArgsSchema()
            .text("env"        , "the environment to run in", false, "dev"  , "dev"  , "dev1|qa1|stg1|pro" )
            .text("region"     , "the region linked to app" , false, "us"   , "us"   , "us|europe|india|*" )
            .text("port"       , "the port to run on"       , false, "5000" , "5000" , "5000|80")
            .text("domain"     , "domain association"       , false, "::0"  , "::0"  , "::0|mycompany.com")
            .text("log.level"  , "the log level for logging", false, "info" , "info" , "debug|info|warn|error")


  // For server, print all info at startup
  override lazy val options = new AppOptions(printSummaryBeforeExec = true)

  /**
    * initialize app context, database and ORM / entities.
    *
    * NOTE: If you extend this class from AppBase ( see SampleApp.Core project ),
    * which contains this init code. That way you don't have to duplicate if for the app types
    * below. This approach works in the initialization of app context is same for all the app types.
    * 1. console
    * 2. cli
    * 3. server
    */
  override def onInit(): Unit =
  {
    // Initialize the context with common app info
    // The database can be set up in the "env.conf" shared inherited config or
    // overridden in the environment specific e.g. "env.qa.conf"
    ctx = new AppContext (
      env  = env,
      cfg  = conf,
      log  = new LoggerConsole(getLogLevel()),
      ent  = new Entities(Option(dbs())),
      inf  = aboutApp(),
      dbs  = Option(dbs()),
      enc  = Some(AppEncryptor),
      dirs = Some(folders())
    )

    // 4. Setup the User entity services
    // NOTE(s):
    // 1. See the ORM documentation for more info.
    // 2. The entity services uses a Generic Service/Repository pattern for ORM functionality.
    // 3. The services support CRUD operations out of the box for single-table mapped entities.
    // 4. This uses an In-Memory repository for demo but you can use EntityRepoMySql for MySql

    // =========================================================================
    // NOTE: Uncomment below to use MySql based Repositories
    // =========================================================================
    // ctx.ent.register[User](isSqlRepo= true, entityType = typeOf[User],
    //   serviceType= typeOf[UserService], repository= new EntityRepoMySql[User](typeOf[User]))
    // ctx.ent.register[Movie](isSqlRepo= true, entityType = typeOf[Movie],
    //   serviceType= typeOf[MovieService], repository= new EntityRepoMySql[Movie](typeOf[Movie]))

    // =========================================================================
    // NOTE: Comment below to use MySql based Repositories
    // =========================================================================
    ctx.ent.register[User](isSqlRepo= false, entityType = typeOf[User], serviceType= typeOf[UserService])
    ctx.ent.register[Movie](isSqlRepo= false, entityType = typeOf[Movie], serviceType= typeOf[MovieService])
  }


  /**
   * You implement this method to executes the app
   *
   * @return
   */
  override def onExecute():Result[Any] =
  {
    info("server starting")

    // 1. Build the auth provider
    val sampleKeys = AppApiKeys.fetch()
    val selectedKey = sampleKeys(5)
    val auth = new AppAuth("test-mode", "slatekit", "johndoe", selectedKey, Some(sampleKeys))

    // 2. Initialize server with port, domain, context (see above) and auth provider
    val server = new Server( args.getIntOrElse("port", 5000),
                             args.getStringOrElse("domain", "::0"),
                             ctx, auth,
                             apiItems = Some(
                                List[ApiReg](
                                    new ApiReg(new AppApi()    , true  ),
                                    new ApiReg(new VersionApi(), true  ),
                                    new ApiReg(new UserApi()   , false ),
                                    new ApiReg(new MovieApi()  , false )
                                  )
                              )
                            )
    // 3. Init the APIs within the api container
    server.apis.init()

    // 4. Run the server ( this starts the life-cycle init, execute, shutdown )
    server.run()

    info("server stopped")

    success(true)
  }


  /**
   * HOOK for when app is shutting down
   */
  override def onShutdown(): Unit =
  {
    info("app shutting down")
  }


  /**
   * HOOK for adding items to the summary of data shown at the end of app execution
   */
  override def collectSummaryExtra(): Option[List[(String,String)]] =
  {
    Some(List[(String,String)](
      ("region", args.getStringOrElse("region", "n/a"))
    ))
  }
}