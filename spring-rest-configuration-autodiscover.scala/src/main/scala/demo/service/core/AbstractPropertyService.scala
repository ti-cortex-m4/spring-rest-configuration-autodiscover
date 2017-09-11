package demo.service.core

import demo.domain.Property
import demo.repository.PropertyRepository
import org.springframework.beans.factory.annotation.Autowired
import java.util._

abstract class AbstractPropertyService[T] extends PropertyService[T] {

    private var valueOpt = Optional.empty[T]()

    @Autowired
    private val propertyRepository: PropertyRepository = _

    override def update(value: String): Property = {
        valueOpt = Optional.of(fromString(value))
        save(value)
    }

    override def init() = {
        val propertyOpt = findPropertyByCode(getCode())
        if (propertyOpt.isPresent) {
            val value = propertyOpt.get().value
            valueOpt = Optional.of(fromString(value))
        } else {
            val value = getDefaultValue()
            valueOpt = Optional.of(value)
            save(toString(value))
        }
    }

    def get(): T = {
        if (valueOpt.isPresent) {
            valueOpt.get()
        } else {
            val propertyOpt = findPropertyByCode(getCode())
            if (propertyOpt.isPresent) {
                valueOpt = Optional.of(fromString(propertyOpt.get().value))
                valueOpt.get()
            } else {
                getDefaultValue()
            }
        }
    }

    protected abstract def getType: Type.Value

    protected abstract def fromString(input: String): T

    protected abstract def toString(input: T): String

    protected abstract def getDefaultValue: T

    override def getPossibleValues: java.util.List[String] = new java.util.ArrayList()

    private def findPropertyByCode(code: Code.Value): Optional[Property] = {
        propertyRepository.findByCode(code)
    }

    private def save(value: String): Property = {
        val property = new Property()
        property.code = getCode
        property.type = getType
        property.value = value

        propertyRepository.save(property)
    }

    override def toString: String = {
        super.toString + "{" +
                "getCode()=" + getCode +
                ", getType()=" + getType +
                '}'
    }
}
