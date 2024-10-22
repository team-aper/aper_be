package org.aper.web.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageEncoder {
    public MultipartFile Base64ToMultipartFile(String base64) {
        try {
            String[] base64Parts = base64.split(",");
            String base64Data = base64Parts[1];
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

            String fileName = UUID.randomUUID() + ".jpg";
            return new Base64DecodedMultipartFile(decodedBytes, fileName);
        } catch (IllegalArgumentException e) {
            throw new ServiceException(ErrorCode.BASE64_DECODE_FAILED);
        }
    }

}
