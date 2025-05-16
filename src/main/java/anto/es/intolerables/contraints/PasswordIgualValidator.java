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
        // Si queremos que el mensaje de error esté asociado a un campo y no a toda la clase
        //if (!isValid) {
        //    context.disableDefaultConstraintViolation();
        //    context.buildConstraintViolationWithTemplate("No coinciden las passwords")
        //            .addPropertyNode("passwordConfirm").addConstraintViolation();
        //}
        return isValid;
    }

}
