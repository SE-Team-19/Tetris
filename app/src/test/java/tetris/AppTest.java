package tetris;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import tetris.controller.*;

public class AppTest {
    @Test
    void testMain() {
        TestAllView testAllView = new TestAllView();
        ViewController frame = new ViewController();
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getMainView().getStartBtn());
    }
}
