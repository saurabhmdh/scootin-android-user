package com.scootin.util.constants

import java.util.regex.Pattern

object Validation {

    val REGEX_VALID_OTP = Pattern.compile("\\d{4}")
    val REGEX_VALID_MOBILE_NUMBER = Pattern.compile("^[897][0-9]{9}")

}