package jadx.tests.integration.debuginfo;

import java.lang.ref.WeakReference;

import org.junit.jupiter.api.Test;

import jadx.core.dex.nodes.ClassNode;
import jadx.tests.api.IntegrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLineNumbers2 extends IntegrationTest {

	public static class TestCls {
		private WeakReference<TestCls> f;

		// ------
		// keep constructor at line 18
		public TestCls(TestCls s) {
		}

		public TestCls test(TestCls s) {
			TestCls store = f != null ? f.get() : null;
			if (store == null) {
				store = new TestCls(s);
				f = new WeakReference<>(store);
			}
			return store;
		}

		public Object test2() {
			return new Object();
		}
	}

	@Test
	public void test() {
		printLineNumbers();

		ClassNode cls = getClassNode(TestCls.class);
		String linesMapStr = cls.getCode().getLineMapping().toString();
		if (isJavaInput()) {
			assertEquals("{5=16, 8=17, 11=21, 12=22, 13=23, 14=24, 15=25, 17=27, 20=30, 21=31}", linesMapStr);
		} else {
			// TODO: invert condition to match source lines
			assertEquals("{5=16, 8=17, 11=21, 12=22, 13=23, 14=27, 16=24, 17=25, 18=27, 21=30, 22=31}", linesMapStr);
		}
	}
}
