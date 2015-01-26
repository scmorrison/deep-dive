package services

import models.User
import models.UserRepository


class UserService(userRepository: UserRepository) {

  def authenticate(email: String, password: String): Option[User] = {
    userRepository.findOneByEmailAndPassword(email, password)
  }

  def findOneById(id: Long): Option[User] = {
    userRepository.findOneById(id)
  }
}
