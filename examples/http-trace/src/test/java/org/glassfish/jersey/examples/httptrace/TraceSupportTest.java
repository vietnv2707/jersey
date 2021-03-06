/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.jersey.examples.httptrace;

import javax.ws.rs.client.Target;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TraceSupportTest extends JerseyTest {

    @Override
    protected ResourceConfig configure() {
        return App.create();
    }
    private String[] expectedFragmentsProgrammatic = new String[]{
        "TRACE http://localhost:" + this.getPort() + "/tracing/programmatic"
    };
    private String[] expectedFragmentsAnnotated = new String[]{
        "TRACE http://localhost:" + this.getPort() + "/tracing/annotated"
    };

    private Target prepareTarget(String path) {
        final Target target = target();
        target.configuration().register(LoggingFilter.class);
        return target.path(path);
    }

    @Test
    public void testProgrammaticApp() throws Exception {
        Response response = prepareTarget(App.ROOT_PATH_PROGRAMMATIC).request("text/plain").method(TRACE.NAME);

        assertEquals(Response.Status.OK, response.getStatusEnum());

        String responseEntity = response.readEntity(String.class);
        for (String expectedFragment : expectedFragmentsProgrammatic) {
            assertTrue("Expected fragment '" + expectedFragment + "' not found in response:\n" + responseEntity,
                    // toLowerCase - http header field names are case insensitive
                    responseEntity.contains(expectedFragment));
        }
    }

    @Test
    public void testAnnotatedApp() throws Exception {
        Response response = prepareTarget(App.ROOT_PATH_ANNOTATED).request("text/plain").method(TRACE.NAME);

        assertEquals(Response.Status.OK, response.getStatusEnum());

        String responseEntity = response.readEntity(String.class);
        for (String expectedFragment : expectedFragmentsAnnotated) {
            assertTrue("Expected fragment '" + expectedFragment + "' not found in response:\n" + responseEntity,
                    // toLowerCase - http header field names are case insensitive
                    responseEntity.contains(expectedFragment));
        }
    }
}
