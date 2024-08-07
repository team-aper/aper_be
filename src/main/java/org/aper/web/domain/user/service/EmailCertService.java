package org.aper.web.domain.user.service;

import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.repository.EmailRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@Slf4j
public class EmailCertService {

    private final EmailRepository emailRepository;
    private final MailSendService mailSendService;

    public EmailCertService(EmailRepository emailRepository, MailSendService mailSendService) {
        this.emailRepository = emailRepository;
        this.mailSendService = mailSendService;
    }

    @Transactional
    public void emailSend(EmailSendDto emailSendDto) {
        String certificationNumber = createCertificationNumber();
        if (emailRepository.hashKey(emailSendDto.email())) {
            emailRepository.removeCertificationNumber(emailSendDto.email());
        }
        emailRepository.saveCertificationNumber(emailSendDto.email(), certificationNumber);

        String subject = "Aper : 이메일 인증번호 확인";

        mailSendService.sendEmail(emailSendDto.email(), subject, certificationNumber);
    }

    @Transactional
    public void emailAuthCheck(EmailAuthDto emailAuthDto) {
        String certificationNumber = emailRepository.getCertificationNumber(emailAuthDto.email());

        if (!certificationNumber.equals(emailAuthDto.authCode())) {
            throw new ServiceException(ErrorCode.EMAIL_AUTH_FAILED);
        }
        emailRepository.removeCertificationNumber(emailAuthDto.email());
    }

    private String createCertificationNumber()  {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(888888) + 111111;
        return String.valueOf(num);
    }
}
