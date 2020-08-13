package models

import repository.UserDB

case class User(userDB: UserDB, role: UserRole, addresses: List[Address])
