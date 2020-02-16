/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dream.codec.tlv.spring;

import com.dream.codec.tlv.TLVContext;
import com.dream.codec.tlv.codec.RawTLVCodec;
import com.dream.codec.tlv.codec.TLVCodec;
import com.dream.codec.tlv.header.IHeaderCodec;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link ImportBeanDefinitionRegistrar} to allow annotation configuration of TLV bean scanning
 *
 * @author DreamJM
 */
public class TLVBeanScannerRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

    public static final String BEAN_TLV_CONTEXT = "tlvContext";

    public static final String BEAN_TLV_CODEC = "tlvCodec";

    private BeanFactory beanFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes tlvScanAttrs =
                AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(TLVBeanScan.class.getName()));
        if (tlvScanAttrs != null) {
            registerContextDefinition(tlvScanAttrs, registry);
            ObjectProvider<IHeaderCodec> headerCodecProvider = beanFactory.getBeanProvider(IHeaderCodec.class);
            IHeaderCodec<?> headerCodec = headerCodecProvider.getIfAvailable();
            if (headerCodec != null) {
                registerCodecDefinition(registry, headerCodec);
            } else {
                registerDefaultCodecDefinition(registry);
            }
        }
    }

    private void registerContextDefinition(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(TLVContext.class);
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
                .collect(Collectors.toList()));
        builder.addConstructorArgValue(basePackages.toArray(new String[]{}));
        registry.registerBeanDefinition(BEAN_TLV_CONTEXT, builder.getBeanDefinition());
    }

    private void registerDefaultCodecDefinition(BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(TLVCodec.class);
        builder.addConstructorArgReference(BEAN_TLV_CONTEXT);
        registry.registerBeanDefinition(BEAN_TLV_CODEC, builder.getBeanDefinition());
    }

    private void registerCodecDefinition(BeanDefinitionRegistry registry, IHeaderCodec<?> headerCodec) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RawTLVCodec.class);
        builder.addConstructorArgReference(BEAN_TLV_CONTEXT);
        builder.addConstructorArgValue(headerCodec);
        registry.registerBeanDefinition(BEAN_TLV_CODEC, builder.getBeanDefinition());
    }
}
