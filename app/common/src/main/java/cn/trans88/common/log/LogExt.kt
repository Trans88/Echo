package com.bennyhuo.common.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory


val loggerMap = HashMap<Class<*>, org.slf4j.Logger>()

inline val <reified T> T.logger: org.slf4j.Logger
    get() {
        return loggerMap[T::class.java]?: LoggerFactory.getLogger(T::class.java).apply { loggerMap[T::class.java] = this }
    }