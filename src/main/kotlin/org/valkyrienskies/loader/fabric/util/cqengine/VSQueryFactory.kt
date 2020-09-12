package org.valkyrienskies.loader.fabric.util.cqengine

import com.googlecode.cqengine.attribute.Attribute
import com.googlecode.cqengine.attribute.support.FunctionalSimpleAttribute
import com.googlecode.cqengine.attribute.support.FunctionalSimpleNullableAttribute
import com.googlecode.cqengine.query.simple.Equal

inline fun <reified O, reified A> attributek(noinline accessor: (O) -> A) =
    FunctionalSimpleAttribute(O::class.java, A::class.java, accessor.javaClass.simpleName) { accessor(it) }

inline fun <reified O, reified A> nullableAttributek(noinline accessor: (O) -> A) =
    FunctionalSimpleNullableAttribute(O::class.java, A::class.java, accessor.javaClass.simpleName) { accessor(it) }

fun <O, A> refEqual(attribute: Attribute<O, A>, attributeValue: A) =
    ReferentiallyEqual(attribute, attributeValue)