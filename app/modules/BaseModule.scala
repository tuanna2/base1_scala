package modules

import com.google.inject.AbstractModule
import models.daos._
import models.services._
import net.codingwell.scalaguice.ScalaModule
import play.api.ApplicationLoader

/**
 * The base Guice module.
 */
class BaseModule extends AbstractModule with ScalaModule {

  /**
   * Configures the module.
   */
  def configure(): Unit = {
    ApplicationLoader

    bind[AuthTokenDAO].to[AuthTokenDAOImpl]
    bind[AuthTokenService].to[AuthTokenServiceImpl]
    bind[TermDAO].to[TermDAOImpl]
    bind[TermService].to[TermServiceImpl]
    bind[CodeGenDAO].to[CodeGenDAOImpl]
    bind[CodeGenService].to[CodeGenServiceImpl]
  }
}
