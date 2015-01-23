import collection.mutable.Stack
import org.scalatest._
import org.scalatestplus.play._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mock.MockitoSugar
import models.{AccountRepository, Account}
import services.AccountService
import models.Role.NormalUser



class StackSpec extends PlaySpec {

  "A Stack" must {
    "pop values in last-in-first-out order" in {
      val stack = new Stack[Int]
      stack.push(1)
      stack.push(2)
      stack.pop() mustBe 2
      stack.pop() mustBe 1
    }
    "throw NoSuchElementException if an empty stack is popped" in {
      val emptyStack = new Stack[Int]
      a [NoSuchElementException] must be thrownBy {
        emptyStack.pop()
      }
    }
  }
}

class AccountServiceSpec extends PlaySpec with MockitoSugar {

  "AccountService#authenticate" should {
    "be true when the proper credentials are provided" in {
      val accountService = new AccountService(mock[AccountRepository])
      val account = new Account(Some(1), "test@example.com",Some("secret"),"tester tester", NormalUser)
      when(accountService.authenticate(any[String], any[String])) thenReturn Some(account)


      val actual = accountService.authenticate("test@example.com", "secret")

      actual mustBe Some(Account)
    }
  }
}
