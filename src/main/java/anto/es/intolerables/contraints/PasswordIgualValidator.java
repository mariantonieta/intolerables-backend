package anto.es.intolerables.contraints;

import anto.es.intolerables.dto.UsuarioDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordIgualValidator implements ConstraintValidator<PasswordIgual, Object> {
    @Override
    public void initialize(PasswordIgual constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UsuarioDTO usuario = (UsuarioDTO) obj;

        boolean isValid = usuario.getContrasena().equals(usuario.getContrasenaConfirm());

        return isValid;
    }

}
