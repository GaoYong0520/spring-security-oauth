/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
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

package com.gaoyong.springsecurityoauth.oauth.crowd;

import lombok.Getter;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * A holder of selected HTTP details related to a web authentication request.
 *
 * @author Ben Alex
 * @author Luke Taylor
 */
public class CrowdAuthenticationDetails extends WebAuthenticationDetails implements Serializable {
    @Getter
    private HttpServletRequest request;
    public CrowdAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.request = request;
       
    }
}
