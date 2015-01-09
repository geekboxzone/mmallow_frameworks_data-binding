/*
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.databinding.renderer2

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.Delegates
import com.android.databinding.ext.joinToCamelCase
import com.android.databinding.ext.joinToCamelCaseAsVar

private class LazyExt<K, T>(private val initializer: (k : K) -> T) : ReadOnlyProperty<K, T> {
    private val mapping = hashMapOf<K, T>()
    override fun get(thisRef: K, desc: PropertyMetadata): T {
        val t = mapping.get(thisRef)
        if (t != null) {
            return t
        }
        val result = initializer(thisRef)
        mapping.put(thisRef, result)
        return result
    }
}

fun Delegates.lazy<K, T>(initializer: (k : K) -> T): ReadOnlyProperty<K, T> = LazyExt(initializer)

public fun Class<*>.toJavaCode() : String = getName().replace("$", ".")

public fun String.androidId() : String = this.split("/")[1]

public fun String.toCamelCase() : String {
    val split = this.split("_")
    if (split.size == 0) return ""
    if (split.size == 1) return split[0].capitalize()
    return split.joinToCamelCase()
}

public fun String.toCamelCaseAsVar() : String {
    val split = this.split("_")
    if (split.size == 0) return ""
    if (split.size == 1) return split[0]
    return split.joinToCamelCaseAsVar()
}