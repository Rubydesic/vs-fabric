package org.valkyrienskies.loader.fabric.util.cqengine

import com.googlecode.cqengine.attribute.Attribute
import com.googlecode.cqengine.attribute.SimpleAttribute
import com.googlecode.cqengine.query.option.QueryOptions
import com.googlecode.cqengine.query.simple.SimpleQuery
import com.googlecode.cqengine.query.support.QueryValidation

class ReferentiallyEqual<O, A>(attribute: Attribute<O, A>?, value: A) : SimpleQuery<O, A>(attribute) {
    val value: A = QueryValidation.checkQueryValueNotNull(value)

    override fun toString(): String {
        return "referentiallyEqual(" + asLiteral(super.getAttributeName()) +
                ", " + asLiteral(value) +
                ")"
    }

    override fun matchesSimpleAttribute(
        attribute: SimpleAttribute<O, A>,
        obj: O,
        queryOptions: QueryOptions
    ): Boolean {
        return value === attribute.getValue(obj, queryOptions)
    }

    override fun matchesNonSimpleAttribute(
        attribute: Attribute<O, A>,
        obj: O,
        queryOptions: QueryOptions
    ): Boolean {
        for (attributeValue in attribute.getValues(obj, queryOptions))
            if (value === attributeValue)
                return true
        return false
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is ReferentiallyEqual<*, *>) return false
        if (attribute !== o.attribute) return false
        return value === o.value
    }

    override fun calcHashCode(): Int {
        var result = attribute.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

}