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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for TLV Message
 *
 * @author DreamJM
 */
@ConfigurationProperties(prefix = "dream.tlv")
public class DreamTLVProperties {

    /**
     * TLV bean packages for scanning
     */
    private String[] beanPackages;

    /**
     * @return TLV bean packages for scanning
     */
    public String[] getBeanPackages() {
        return beanPackages;
    }

    /**
     * Configure bean packages for TLVContext initialization
     *
     * @param beanPackages TLV bean packages for scanning to set
     */
    public void setBeanPackages(String[] beanPackages) {
        this.beanPackages = beanPackages;
    }
}
