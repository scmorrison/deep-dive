package services

import models.Account
import models.AnormAccountRepository


object AccountService {

  def authenticate(email: String, password: String): Option[Account] = {
    AnormAccountRepository.findOneByEmailAndPassword(email, password)
  }
}
