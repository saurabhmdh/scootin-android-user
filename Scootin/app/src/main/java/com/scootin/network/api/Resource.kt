package com.scootin.network.api

data class Resource<out T> (val status: Status, val code: Int, val data: T?, val message: String?) {
    companion object {
        @JvmStatic
        fun <T> success(code: Int, data: T?): Resource<T> {
            return Resource(Status.SUCCESS, code, data, null)
        }
        @JvmStatic
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, 1, data, null)
        }
        @JvmStatic
        fun <T> error(code: Int, msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, code, data, msg)
        }
        @JvmStatic
        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, -1, data, msg)
        }
        @JvmStatic
        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, 0, data, null)
        }

    }

    fun isDataAvailable(): Boolean {
        return data != null
    }
}