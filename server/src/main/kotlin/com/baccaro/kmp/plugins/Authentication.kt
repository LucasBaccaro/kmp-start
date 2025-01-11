package com.baccaro.kmp.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.HoconApplicationConfig
import java.util.*


fun Application.configureAuthentication() {}
