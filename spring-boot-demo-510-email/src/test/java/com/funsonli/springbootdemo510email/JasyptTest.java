package com.funsonli.springbootdemo510email;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/16
 */

public class JasyptTest extends SpringBootDemo510EmailApplicationTests {
    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void password() {
        String str = "W2008naya";

        System.out.println("encode: " + encryptor.encrypt(str));
    }
}
