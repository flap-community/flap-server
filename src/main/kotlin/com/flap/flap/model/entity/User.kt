package com.flap.flap.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "Users")
class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0

        @Column
        var password = ""
                @JsonIgnore
                get() = field
                set(value) {
                        val passwordEncoder = BCryptPasswordEncoder()
                        field = passwordEncoder.encode(value)
                }

        var nickname: String? = null

        @Column(unique = true)
        var email: String =""

        var profileMessage: String? = null
        var profileImagePath: String? = null

        @Column(nullable = false)
        var activateAt: LocalDateTime? = null
        @Column
        var createdAt: LocalDateTime? = null
        @Column
        var modifiedAt: LocalDateTime? = null

        @PrePersist
        fun onPrePersist() {
                createdAt = LocalDateTime.now()
                modifiedAt = LocalDateTime.now()
        }
        @PreUpdate
        fun onPreUpdate() {
                modifiedAt = LocalDateTime.now()
        }


        fun comparePassword(password: String): Boolean {
                return BCryptPasswordEncoder().matches(password, this.password)
        }
}