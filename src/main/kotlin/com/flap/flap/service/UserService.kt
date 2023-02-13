package com.flap.flap.service

import com.flap.flap.common.exception.InvalidUserException
import com.flap.flap.common.exception.UnauthenticatedException
import com.flap.flap.model.dto.auth.LoginDto
import com.flap.flap.model.entity.User
import com.flap.flap.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
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

    fun checkValidUser(body: LoginDto) {
        val findUser: User = findByEmail(body.email) ?:
        throw InvalidUserException("User not found")
        if (!findUser.comparePassword(body.password)) {
            throw InvalidUserException("password is not correct")
        }

    }
    fun generateJwt(body: LoginDto): String?{
        val issuer = body.email
        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
            .signWith(SignatureAlgorithm.HS512, "secret").compact()

        return jwt
    }

    fun getValidUser(jwt: String?): User? {
        if (jwt == null) {
            throw UnauthenticatedException("unauthenticated")
        }

        val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body

        return getById(body.issuer.toInt())
    }
}