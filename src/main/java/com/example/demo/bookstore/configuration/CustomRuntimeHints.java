/* (C) 2025 */ 

package com.example.demo.bookstore.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(CustomRuntimeHints.class)
@Slf4j
public class CustomRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        log.info("registerHints");
        TypeReference typeReference = TypeReference.of("com.github.benmanes.caffeine.cache.SSLMSA");
        hints.reflection().registerType(typeReference, builder -> {
            builder.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
            builder.withMembers(MemberCategory.INVOKE_PUBLIC_METHODS);
            builder.withMembers(MemberCategory.DECLARED_FIELDS);
        });
    }
}
