/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.jersey.server.internal.scanning;

import org.glassfish.jersey.server.ResourceFinder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Not used yet - preparations for OSGi support.
 *
 * @author Jakub Podlesak (jakub.podlesak at oracle.com)
 */
class BundleSchemeResourceFinderFactory implements UriSchemeResourceFinderFactory {

    @Override
    public Set<String> getSchemes() {
        return new HashSet<String>(Arrays.asList("bundle"));
    }

    BundleSchemeResourceFinderFactory() {
    }

    @Override
    public BundleSchemeScanner create(URI uri) {
        return new BundleSchemeScanner(uri);
    }

    private class BundleSchemeScanner implements ResourceFinder {

        private BundleSchemeScanner(URI uri) {
            this.uri = uri;
        }

        private URI uri;
        private boolean accessed = false;

        @Override
        public boolean hasNext() {
            return !accessed;
        }

        @Override
        public String next() {
            if(!accessed) {
                accessed = true;
                return uri.getPath();
            }

            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public InputStream open() {
            if(!accessed) {
                try {
                    return uri.toURL().openStream();
                } catch (MalformedURLException e) {
                    throw new ResourceFinderException(e);
                } catch (IOException e) {
                    throw new ResourceFinderException(e);
                }
            }

            return null;
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException();
        }
    }

}