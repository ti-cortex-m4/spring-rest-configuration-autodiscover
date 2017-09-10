package demo.service.core;

import demo.domain.Property;
import demo.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract public class AbstractPropertyService<T> implements PropertyService<T> {

    private Optional<T> valueOpt = Optional.empty();

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Property update(String value) {
        valueOpt = Optional.of(fromString(value));
        return save(value);
    }

    @Override
    public void init() {
        Optional<Property> propertyOpt = findPropertyByCode(getCode());
        if (propertyOpt.isPresent()) {
            String value = propertyOpt.get().getValue();
            valueOpt = Optional.of(fromString(value));
        } else {
            T value = getDefaultValue();
            valueOpt = Optional.of(value);
            save(toString(value));
        }
    }

    public T get() {
        if (valueOpt.isPresent()) {
            return valueOpt.get();
        } else {
            Optional<Property> propertyOpt = findPropertyByCode(getCode());
            if (propertyOpt.isPresent()) {
                valueOpt = Optional.of(fromString(propertyOpt.get().getValue()));
                return valueOpt.get();
            } else {
                return getDefaultValue();
            }
        }
    }

    protected abstract Type getType();

    protected abstract T fromString(String input);

    protected abstract String toString(T input);

    protected abstract T getDefaultValue();

    public List<String> getPossibleValues() {
        return new ArrayList<>();
    }

    private Optional<Property> findPropertyByCode(Code code) {
        return propertyRepository.findByCode(code);
    }

    private Property save(String value) {
        Property property = new Property();
        property.setCode(getCode());
        property.setType(getType());
        property.setValue(value);

        return propertyRepository.save(property);
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "getCode()=" + getCode() +
                ", getType()=" + getType() +
                '}';
    }
}
