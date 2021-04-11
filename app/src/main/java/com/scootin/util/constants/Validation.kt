package com.scootin.util.constants

import java.util.regex.Pattern

object Validation {

    val REGEX_VALID_OTP = Pattern.compile("\\d{6}")
    val REGEX_VALID_MOBILE_NUMBER = Pattern.compile("^[6789][0-9]{9}")

    val REGEX_VALID_EMAIL = Pattern.compile("(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$)")
}