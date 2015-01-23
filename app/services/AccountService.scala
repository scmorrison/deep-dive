package services

import models.Account
import models.AccountRepository


class AccountService(accountRepository: AccountRepository) {

  def authenticate(email: String, password: String): Option[Account] = {
    accountRepository.findOneByEmailAndPassword(email, password)
  }

  def findOneById(id: Long): Option[Account] = {
    accountRepository.findOneById(id)
  }
}
