package com.scootin.network.manager

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


class TokenAuthenticator : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.i("Saurabh -> route ${route}")
        Timber.i("Saurabh -> response ${response}")

        return null
    }
}