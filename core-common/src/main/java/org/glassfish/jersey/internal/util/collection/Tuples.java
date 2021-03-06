/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2012 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.jersey.internal.util.collection;

import com.google.common.base.Objects;

/**
 * Utility class with methods for working with tuples, such as {@link Pair} and
 * {@link Ref}.
 *
 * @author Paul Sandoz
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public final class Tuples {

    private Tuples() {
        // preventing istantiation
    }

    /**
     * Default {@link Pair} implementation.
     */
    private static final class PairImpl<L, R> implements Pair<L, R> {

        private final L left;
        private final R right;

        public PairImpl(final L left, final R right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public L left() {
            return left;
        }

        @Override
        public R right() {
            return right;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).
                    add("left", left).
                    add("right", right).toString();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            @SuppressWarnings("unchecked")
            final PairImpl<L, R> other = (PairImpl<L, R>) obj;
            if (this.left != other.left && (this.left == null || !this.left.equals(other.left))) {
                return false;
            }
            if (this.right != other.right && (this.right == null || !this.right.equals(other.right))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + (this.left != null ? this.left.hashCode() : 0);
            hash = 83 * hash + (this.right != null ? this.right.hashCode() : 0);
            return hash;
        }
    }

    /**
     * Constructs {@link Pair} of the given values.
     *
     * @param <L> left-hand reference type
     * @param <R> right-hand reference type
     * @param left left-hand reference in the pair
     * @param right right-hand reference in the pair
     *
     * @return {@link Pair} of input values
     *
     * @see #swap(Pair)
     */
    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new PairImpl<L, R>(left, right);
    }

    /**
     * Constructs a new {@link Pair} instance with values of the input pair instance
     * swapped.
     *
     * @param <L> left-hand reference type
     * @param <R> right-hand reference type
     * @param pair instance whose values are to be swapped
     *
     * @return a new {@link Pair} instance with values of the input pair instance
     *         swapped.
     *
     * @see #of(java.lang.Object, java.lang.Object)
     */
    public static <L, R> Pair<R, L> swap(final Pair<L, R> pair) {
        return new PairImpl<R, L>(pair.right(), pair.left());
    }

    /**
     * Immutable {@link Ref} implementation.
     */
    private static final class ImmutableRefImpl<T> implements Ref<T> {

        private final T reference;

        public ImmutableRefImpl(final T value) {
            this.reference = value;
        }

        @Override
        public T get() {
            return reference;
        }

        @Override
        public void set(final T value) throws IllegalStateException {
            throw new IllegalStateException("This implementation of Ref interface is immutable.");
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("reference", reference).toString();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            @SuppressWarnings("unchecked")
            final ImmutableRefImpl<T> other = (ImmutableRefImpl<T>) obj;

            return reference == other.reference || (reference != null && reference.equals(other.reference));
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + (this.reference != null ? this.reference.hashCode() : 0);
            return hash;
        }
    }

    /**
     * Default (mutable) {@link Ref} implementation.
     */
    private static final class DefaultRefImpl<T> implements Ref<T> {

        private T reference;

        public DefaultRefImpl() {
            this.reference = null;
        }

        public DefaultRefImpl(final T value) {
            this.reference = value;
        }

        @Override
        public T get() {
            return reference;
        }

        @Override
        public void set(final T value) throws IllegalStateException {
            this.reference = value;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("reference", reference).toString();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            @SuppressWarnings("unchecked")
            final DefaultRefImpl<T> other = (DefaultRefImpl<T>) obj;

            return reference == other.reference || (reference != null && reference.equals(other.reference));
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 47 * hash + (this.reference != null ? this.reference.hashCode() : 0);
            return hash;
        }
    }

    /**
     * Constructs a new mutable {@link Ref} instance referencing the given
     * input reference.
     *
     * @param <T> type of the referenced instance
     * @param value reference of the newly constructed reference
     *
     * @return a new mutable {@link Ref} instance referencing the given
     *         input reference.
     *
     * @see #immutableRef(java.lang.Object)
     * @see #emptyRef()
     */
    public static <T> Ref<T> of(final T value) {
        return new DefaultRefImpl<T>(value);
    }

    /**
     * Constructs a new empty mutable {@link Ref} instance.
     *
     * @param <T> type of the referenced instance
     * @return a new mutable empty {@link Ref} instance
     *
     * @see #immutableRef(java.lang.Object)
     * @see #of(java.lang.Object)
     */
    public static <T> Ref<T> emptyRef() {
        return new DefaultRefImpl<T>();
    }

    /**
     * Constructs a new immutable {@link Ref} instance referencing the given
     * input reference.
     * <p/>
     * Invoking a {@link Ref#set(java.lang.Object)} on the returned instance
     * will result in a {@link IllegalStateException} being thrown.
     *
     * @param <T> type of the referenced instance
     * @param value reference of the newly constructed reference
     *
     * @return a new immutable {@link Ref} instance referencing the given
     *         input reference.
     *
     * @see #emptyRef()
     * @see #of(java.lang.Object)
     */
    public static <T> Ref<T> immutableRef(final T value) {
        return new ImmutableRefImpl<T>(value);
    }
}
