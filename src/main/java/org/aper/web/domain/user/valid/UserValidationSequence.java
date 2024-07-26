package org.aper.web.domain.user.valid;

import jakarta.validation.GroupSequence;

import static org.aper.web.domain.user.valid.UserValidationGroup.*;


@GroupSequence({
        UserValidationGroup.NotBlankGroup.class,
        EmailBlankGroup.class,
        EmailGroup.class,
        PasswordBlankGroup.class,
        PasswordPatternGroup.class,
        NicknameBlankGroup.class,
        NicknamePatternGroup.class,
})
public interface UserValidationSequence {

}
