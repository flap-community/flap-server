package com.flap.flap.service

import com.flap.flap.common.exception.InvalidUserException
import com.flap.flap.model.entity.User
import com.flap.flap.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {
    fun saveUser(user: User):User {
        return userRepository.save(user)
    }
    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }
    fun getById(id: Int): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun checkValidUser(user: User) {
        val findUser: User = findByEmail(user.email) ?:
        throw InvalidUserException("User not found")

        if (!findUser.comparePassword(user.password)) {
            throw InvalidUserException("User not found")
        }

    }
    fun generateJwt(user:User): String?{
        val issuer = user.id.toString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
            .signWith(SignatureAlgorithm.HS512, "secret").compact()

        return jwt
    }
}