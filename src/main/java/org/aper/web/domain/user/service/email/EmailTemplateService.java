package org.aper.web.domain.user.service.email;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EmailTemplateService {

    public String loadHtmlTemplate(String certificationNumber) {
        try {
            // HTML 파일을 ClassPath에서 로드
            ClassPathResource resource = new ClassPathResource("templates/email.html");
            String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            
            // 플레이스홀더 대체
            return template.replace("{{certificationNumber}}", certificationNumber);
        } catch (IOException e) {
            throw new ServiceException(ErrorCode.EMAIL_SEND_FAILURE);
        }
    }
}
