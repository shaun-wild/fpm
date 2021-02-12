package com.qardz.fpm.util

import com.qardz.fpm.exception.FPMException
import com.qardz.fpm.io.FileManager
import java.nio.file.Path

fun assertInfoExists(path: Path) {
    if (!FileManager.infoExists(path)) {
        throw FPMException("No project found, run fpm init")
    }
}

fun assertInfoNotExists(path: Path) {
    if(FileManager.infoExists(path)) {
        throw FPMException("A mod-info.json file already exists!")
    }
}