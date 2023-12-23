/*
 * Copyright (c) 2023, Project-K
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package jp.mydns.projectk.vfs.ram;

import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import org.junit.jupiter.api.Test;
import test.RamConfigUtils;

/**
 * Test of class RamMaxSize.
 *
 * @author riru
 * @version 1.0.0
 * @since 1.0.0
 */
class RamMaxSizeTest {

    /**
     * Test constructor. If argument is valid {@code JsonValue}.
     *
     * @since 1.0.0
     */
    @Test
    void testConstructor_JsonValue() {

        assertThat(new RamMaxSize(Json.createValue(Long.MIN_VALUE)).getValue()).isEqualTo(Json.createValue(Long.MIN_VALUE));

    }

    /**
     * Test constructor. If argument is illegal {@code JsonValue}.
     *
     * @since 1.0.0
     */
    @Test
    void testConstructor_IllegalJsonValue() {

        assertThatIllegalArgumentException().isThrownBy(() -> new RamMaxSize(JsonValue.NULL))
                .withMessage("FileOption value of [%s] must be long.", "ram:maxSize");

    }

    /**
     * Test constructor. If argument is valid {@code long}.
     *
     * @since 1.0.0
     */
    @Test
    void testConstructor_long() {

        assertThat(new RamMaxSize(Long.MAX_VALUE).getValue()).isEqualTo(Json.createValue(Long.MAX_VALUE));

    }

    /**
     * Test apply method.
     *
     * @since 1.0.0
     */
    @Test
    void testApply() throws FileSystemException {

        FileSystemOptions opts = new FileSystemOptions();

        new RamMaxSize.Resolver().newInstance(Json.createValue(Long.MIN_VALUE)).apply(opts);

        assertThat(extractValue(opts)).isEqualTo(Long.MIN_VALUE);

        new RamMaxSize.Resolver().newInstance(Json.createValue(Long.MAX_VALUE)).apply(opts);

        assertThat(extractValue(opts)).isEqualTo(Long.MAX_VALUE);

    }

    /**
     * Test {@code equals} method and {@code hashCode} method.
     *
     * @since 1.0.0
     */
    @Test
    void testEqualsHashCode() {

        RamMaxSize base = new RamMaxSize(Long.MAX_VALUE);
        RamMaxSize same = new RamMaxSize(Long.MAX_VALUE);
        RamMaxSize another = new RamMaxSize(Long.MIN_VALUE);

        assertThat(base).hasSameHashCodeAs(same).isEqualTo(same)
                .doesNotHaveSameHashCodeAs(another).isNotEqualTo(another);

    }

    /**
     * Test of toString method.
     *
     * @since 1.0.0
     */
    @Test
    void testToString() {

        var result = new RamMaxSize(777L).toString();

        assertThat(result).isEqualTo("{\"ram:maxSize\":777}");

    }

    long extractValue(FileSystemOptions opts) {

        return new RamConfigUtils().getParam(opts, "maxsize");

    }
}
