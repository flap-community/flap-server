package com.flap.flap.repository

import com.flap.flap.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {
   fun findByEmail(email:String): User?
}