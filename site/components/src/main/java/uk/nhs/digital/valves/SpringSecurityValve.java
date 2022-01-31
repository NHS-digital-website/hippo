/*
 *  Copyright 2011 Hippo.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package uk.nhs.digital.valves;

import org.hippoecm.hst.container.valves.AbstractOrderableValve;
import org.hippoecm.hst.core.container.ContainerConstants;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;


import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * HST-2 request processing valve for integration with Spring Security Framework.
 * <p>
 * This is responsible for reading the <code>org.springframework.security.core.Authentication</code> instance if exists,
 * and establishing <code>javax.security.auth.Subject</code> for the whole HST-2 request processing
 * by converting <code>org.springframework.security.core.userdetails.UserDetails</code> into a set of
 * <code>java.security.Principal</code>s (user principal and role principals from the collection of
 * <code>org.springframework.security.core.GrantedAuthority</code>).
 * </P>
 */
public class SpringSecurityValve extends AbstractOrderableValve {

    private static Logger log = LoggerFactory.getLogger(SpringSecurityValve.class);

    /**
     * Flag whether or not to store JCR credentials as Subject's private credentials
     * simply by converting username/password to <code>javax.jcr.SimpleCredentials</code> object.
     */
    private boolean storeSubjectRepositoryCredentials = true;

    /**
     * Returns true if the option to store JCR credentials as Subject's private credentials is turned on.
     *
     * @return true if the option to store JCR credentials as Subject's private credentials is turned on
     */
    public boolean isStoreSubjectRepositoryCredentials() {
        return storeSubjectRepositoryCredentials;
    }

    /**
     * Sets the flag whether or not to store JCR credentials as Subject's private credentials.
     *
     * @param storeSubjectRepositoryCredentials flag whether or not to store subject repository credentials
     */
    public void setStoreSubjectRepositoryCredentials(boolean storeSubjectRepositoryCredentials) {
        this.storeSubjectRepositoryCredentials = storeSubjectRepositoryCredentials;
    }

    @Override
    public void invoke(ValveContext context) throws ContainerException {
        HttpServletRequest request = context.getServletRequest();
        Principal userPrincipal = request.getUserPrincipal();

        // If user has not been authenticated yet by any mechanism, then simply move to the next valve chain.
        if (userPrincipal == null) {
            if (log.isDebugEnabled()) {
                log.debug("No user principal found. Skipping SpringSecurityValve...");
            }
            context.invokeNext();
            return;
        }

        // Get the current subject from http session if exists.
        HttpSession session = request.getSession(false);
        Subject subject = (session != null ? (Subject) session.getAttribute(ContainerConstants.SUBJECT_ATTR_NAME)
            : null);

        // If a subject has been established already (normally by HST-2's SecurityValve), then simply move to the next valve chain.
        if (subject == null) {
            if (log.isDebugEnabled()) {
                log.debug("Already subject has been created somewhere before. Skipping SpringSecurityValve...");
            }
            context.invokeNext();
            return;
        }

        // Get Spring Security Context object from thread local.
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // If there's no Spring Security Context object, then just move to next valve chain.
        if (securityContext == null) {
            if (log.isDebugEnabled()) {
                log.debug("Spring Security hasn't established security context. Skipping SpringSecurityValve...");
            }
            context.invokeNext();
            return;
        }

        // Get the Authentication object from the Spring Security context object.
        Authentication authentication = securityContext.getAuthentication();

        // If there's no Authentication object, it's really weird, so leave warning logs, and move to next valve chain.
        if (authentication == null) {
            if (log.isWarnEnabled()) {
                log.warn("Spring Security hasn't establish security context with authentication object. Skipping SpringSecurityValve...");
            }
            context.invokeNext();
            return;
        }
        log.error("Value of authentication " + authentication);
        // Get principal object from the Spring Security authentication object.
        Object springSecurityPrincipal = authentication.getPrincipal();
        log.error("Value of springSecurityPrincipal " + springSecurityPrincipal);
        // We expect the principal is instance of UserDetails. Otherwise, let's skip it and leave warning logs.
        if (springSecurityPrincipal instanceof UserDetails) {
            if (log.isWarnEnabled()) {
                log.warn("Spring Security hasn't establish security context with UserDetails object. "
                    + "We don't support non UserDetails authentication. Skipping SpringSecurityValve...");
            }
            context.invokeNext();
            return;
        }

        // Cast principal instance to UserDetails
        //UserDetails userDetails = (UserDetails) springSecurityPrincipal;

        // Add both the existing user principal and new HST-2 user transient user principal
        // just for the case when HST-2 can inspect the user principals for some reasons.
        Set<Principal> principals = new HashSet<Principal>();
        principals.add(userPrincipal);

        Set<Object> pubCred = new HashSet<Object>();
        Set<Object> privCred = new HashSet<Object>();

        // If the flag is turned on, then store JCR credentials as well
        // just for the case the site is expected to use session stateful JCR sessions per authentication.

        subject = new Subject(true, principals, pubCred, privCred);

        // Save the created subject as http session attribute which can be read by HST-2 SecurityValve in the next valve chain.
        request.getSession(true).setAttribute(ContainerConstants.SUBJECT_ATTR_NAME, subject);

        context.invokeNext();
    }
}
