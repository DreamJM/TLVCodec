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

package com.dream.codec.tlv;

import com.dream.codec.tlv.codec.RawTLVCodec;
import com.dream.codec.tlv.codec.TLVCodec;
import com.dream.codec.tlv.exception.TLVInitException;
import com.dream.codec.tlv.header.IHeaderCodec;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TLV Auto Configuration
 *
 * @author DreamJM
 */
@Configuration
@EnableConfigurationProperties(DreamTLVProperties.class)
public class DreamTLVAutoConfiguration {

    private DreamTLVProperties properties;

    public DreamTLVAutoConfiguration(DreamTLVProperties properties) {
        this.properties = properties;
    }

    @ConditionalOnMissingBean
    @Bean
    public TLVContext tlvContext() throws TLVInitException {
        return new TLVContext(properties.getBeanPackages());
    }

    @ConditionalOnMissingBean({TLVCodec.class, IHeaderCodec.class})
    @Bean("tlvCodec")
    public TLVCodec defaultTLVCodec() throws TLVInitException {
        return new TLVCodec(tlvContext());
    }

    @ConditionalOnBean(IHeaderCodec.class)
    @Bean("tlvCodec")
    public RawTLVCodec<?> customTLVCodec(IHeaderCodec<?> headerCodec) throws TLVInitException {
        return new RawTLVCodec<>(tlvContext(), headerCodec);
    }

}
