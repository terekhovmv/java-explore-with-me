package ru.practicum.ewm.stats.dto.annotations;

import inet.ipaddr.IPAddressString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IpAddressValidator implements ConstraintValidator<IpAddress, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return new IPAddressString(s).isValid();
    }
}
