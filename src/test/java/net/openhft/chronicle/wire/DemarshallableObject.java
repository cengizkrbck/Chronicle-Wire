/*
 *
 *  *     Copyright (C) ${YEAR}  higherfrequencytrading.com
 *  *
 *  *     This program is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU Lesser General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License.
 *  *
 *  *     This program is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU Lesser General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU Lesser General Public License
 *  *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.openhft.chronicle.wire;

import org.jetbrains.annotations.NotNull;

/**
 * Created by peter on 27/01/16.
 */
public class DemarshallableObject implements Demarshallable, WriteMarshallable {
    final String name;
    final int value;

    public DemarshallableObject(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public DemarshallableObject(WireIn wire) {
        this.name = wire.read(() -> "name").text();
        this.value = wire.read(() -> "value").int32();
    }

    @Override
    public void writeMarshallable(@NotNull WireOut wire) {
        wire.write(() -> "name").text(name)
                .write(() -> "value").int32(value);
    }
}
