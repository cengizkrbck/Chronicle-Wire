package net.openhft.chronicle.wire.methodwriter;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.bytes.UpdateInterceptor;
import net.openhft.chronicle.core.Mocker;
import net.openhft.chronicle.wire.Wire;
import net.openhft.chronicle.wire.WireType;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class MethodWriterTest {

    static {
        System.setProperty("dumpCode", "true");
    }

    @Test
    public void allowThrough() {
        check(true);
    }

    @Test
    public void block() {
        check(false);
    }

    public void check(boolean allowThrough) {
        Wire w = WireType.BINARY.apply(Bytes.allocateElasticOnHeap());
        // checks that no exceptions are thrown here
        UpdateInterceptor ui = (methodName, t) -> allowThrough;
        FundingListener fundingListener = w.methodWriterBuilder(FundingOut.class).updateInterceptor(ui).build();
        fundingListener.funding(new Funding());

        List<String> output = new ArrayList<>();
        FundingListener listener = Mocker.intercepting(FundingListener.class, "", output::add);
        @NotNull MethodReader mr = w.methodReader(listener);

        if (allowThrough) {
            assertTrue(mr.readOne());
            assertEquals(1, output.size());
            assertEquals("[funding[!net.openhft.chronicle.wire.methodwriter.Funding {\n" +
                    "  symbol: 0,\n" +
                    "  fr: NaN,\n" +
                    "  mins: 0\n" +
                    "}\n" +
                    "]]", output.toString());
            assertFalse(mr.readOne());
        } else {
            assertFalse(mr.readOne());
            assertEquals(0, output.size());
        }
    }
}
