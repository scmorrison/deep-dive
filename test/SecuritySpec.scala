import collection.mutable.Stack
import org.scalatest._
import org.scalatestplus.play._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mock.MockitoSugar
import models.{UserRepository, User}
import services.UserService
import models.Role.NormalUser



/**
  example test
  */
class UserServiceSpec extends PlaySpec with MockitoSugar {

  "UserService#authenticate" should {
    "be true when the proper credentials are provided" in {
      val userService = new UserService(mock[UserRepository])
      val user = new User(Some(1), "test@example.com",Some("secret"),"tester tester", "NormalUser")
      when(userService.authenticate(any[String], any[String])) thenReturn Some(user)


      val actual = userService.authenticate("test@example.com", "secret")

      actual mustBe Some(user)
    }
  }
}
