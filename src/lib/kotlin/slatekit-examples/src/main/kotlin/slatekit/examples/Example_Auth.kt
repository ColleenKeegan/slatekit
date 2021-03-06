/**
<slate_header>
  author: Kishore Reddy
  url: https://github.com/kishorereddy/scala-slate
  copyright: 2015 Kishore Reddy
  license: https://github.com/kishorereddy/scala-slate/blob/master/LICENSE.md
  desc: a scala micro-framework
  usage: Please refer to license on github for more info.
</slate_header>
  */


package slatekit.examples


//<doc:import_required>
import slatekit.common.auth.AuthConsole
import slatekit.common.auth.User

//</doc:import_required>

//<doc:import_examples>
import slatekit.core.cmds.Cmd
import slatekit.common.ResultEx
import slatekit.common.Success

//</doc:import_examples>

class Example_Auth  : Cmd("auth") {

  override fun executeInternal(args: Array<String>?) : ResultEx<Any>
  {
    //<doc:setup>
    // Setup: Setup the Auth wrapper with the user to inspect info about the user
    // NOTES:
    // * 1. This component does NOT handle any actual login/logout/authorization features.
    // * 2. This set of classes are only used to inspect information about a user.
    // * 3. Since authorization is a fairly complex feature with implementations such as
    // *    OAuth, Social Auth, Slate Kit has purposely left out the Authentication to more reliable
    // *    libraries and frameworks.
    // * 4. The SlateKit.Api component, while supporting basic api "Keys" based authentication,
    // *    and a roles based authentication, it leaves the login/logout and actual generating
    // *    of tokens to libraries such as OAuth.
    val user2 = User( "2", "john doe", "john", "doe", "jdoe@gmail.com", "123-456-7890", false, false, true)
    val auth = AuthConsole(isAuthenticated = true, user = user2, roles = "admin")
    //</doc:setup>

    //<doc:examples>
    // CASE 1: Use the auth to check user info
    println ("Checking auth info in desktop/local mode" )
    println ( "user info         : " + auth.user                   )
    println ( "user id           : " + auth.userId                 )
    println ( "is authenticated  : " + auth.isAuthenticated        )
    println ( "is email verified : " + auth.isEmailVerified        )
    println ( "is phone verified : " + auth.isPhoneVerified        )
    println ( "is a moderator    : " + auth.isInRole( "moderator") )
    println ( "is an admin       : " + auth.isInRole( "admin" )    )
    //</doc:examples>

    return Success("")
  }
}
