package com.qardz.fpm.exception

import java.lang.RuntimeException

class FPMException(override val message: String): RuntimeException(message)
