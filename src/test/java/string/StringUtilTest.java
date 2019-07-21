package string;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilTest {


    @Test
    public void should_truncate_value_value_base_on_len() {
        String expected = BetterStringUtil.truncate("i love testing", 6);
        assertEquals(expected, "i love...");
    }
}
