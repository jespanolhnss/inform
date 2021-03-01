package es.sacyl.gsa.inform.util;

/**
 *
 * @author 06551256M
 */
import com.vaadin.data.validator.RegexpValidator;

@SuppressWarnings("serial")
public class ValidatorDni extends RegexpValidator {

    private static final String PATTERN = "(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])";

    public ValidatorDni(String errorMessage) {

        super(errorMessage, PATTERN, true);
    }
}
