/*
 * Copyright (c) 2026 tutosrive. All rights reserved.
 *
 * Author: tutosrive
 * GitHub: https://github.com/tutosrive
 *
 * This source code is PROPRIETARY and CONFIDENTIAL.
 * Unauthorized copying, modification, or distribution of this file,
 * via any medium, is strictly prohibited.
 *
 * This software is provided "as is", without warranty of any kind.
 * In no event shall the author be liable for any claim or damages.
 */

package com.srm.whysudo.enums

enum class DataFileName(val filename: String) {
    DB_COMMANDS("data-linux.enc.srm"),
    ABOUT("about.srm"),
    LICENSE_MARKWON("license_markwon"),
    LICENSE_SQLCIPHER("license_sqlcipher"),
    LICENSE_ANDROIDX("license_androidx"),
    SHARED_FILE_NAME("preferences");

    operator fun invoke() = filename
}