package com.github.cneftali.job.commons;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstantsTest {

    @Test
    public void testConstructorIsPrivate() throws Exception {
        final Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}