package by.itacademy.javaenterprise.goralchuk.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

public enum PetType {
    CAT("cat"),
    DOG("dog"),
    FROG("frog"),
    PARROT("parrot"),
    COW("cow");
    private final String code;

    PetType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Converter(autoApply = true)
    public static class PetTypeConverter
            implements AttributeConverter<PetType, String> {
        private static final Logger logger = LoggerFactory.getLogger(PetTypeConverter.class);

        @Override
        public String convertToDatabaseColumn(PetType petType) {
            if (petType == null) {
                return null;
            }
            return petType.getCode();
        }

        @Override
        public PetType convertToEntityAttribute(String string) {
            if (string == null) {
                return null;
            }
            try {
                return Stream.of(PetType.values())
                        .filter(el -> el.getCode().equals(string))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Bad argument converter"));
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }
    }
}
