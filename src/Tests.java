/**
 * @author POO team 2023/24
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class Tests {
	/**
	 * Use the following lines to specify the tests you are going to carry out.
	 * For example, the expected result for the test input01.txt is output01.txt.
	 * You don't have to do anything else in the rest of the class.
	 * Just set up this sequence of tests!
	 */


	@Test public void test01() { test("test01-in.txt","test01-out.txt"); }
	@Test public void test02() { test("test02-in.txt","test02-out.txt"); }
	@Test public void test03() { test("test03-in.txt","test03-out.txt"); }
	@Test public void test04() { test("test04-in.txt","test04-out.txt"); }
	@Test public void test05() { test("test05-in.txt","test05-out.txt"); }
	@Test public void test06() { test("test06-in.txt","test06-out.txt"); }
	@Test public void test07() { test("test07-in.txt","test07-out.txt"); }
	@Test public void test08() { test("test08-in.txt","test08-out.txt"); }
	@Test public void test09() { test("test09-in.txt","test09-out.txt"); }
	@Test public void test10() { test("test10-in.txt","test10-out.txt"); }
	@Test public void test11() { test("test11-in.txt","test11-out.txt"); }
	@Test public void test12() { test("test12-in.txt","test12-out.txt"); }
	@Test public void test13() { test("test13-in.txt","test13-out.txt"); }
	@Test public void test14() { test("test14-in.txt","test14-out.txt"); }
	@Test public void test15() { test("test15-in.txt","test15-out.txt"); }


	private static final File BASE = new File("tests");

	private PrintStream consoleStream;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void setup() {
		consoleStream = System.out;
		System.setOut(new PrintStream(outContent));
	}

	public void test(String intput, String output) {
		test(new File(BASE, intput), new File(BASE, output));
	}

	public void test(File input, File output) {
		consoleStream.println("Testing!");
		consoleStream.println("Input: " + input.getAbsolutePath());
		consoleStream.println("Output: " + output.getAbsolutePath());

		String fullInput = "", fullOutput = "";
		try {
			fullInput = new String(Files.readAllBytes(input.toPath()));
			fullOutput = new String(Files.readAllBytes(output.toPath()));
			consoleStream.println("INPUT ============");
			consoleStream.println(new String(fullInput));
			consoleStream.println("OUTPUT ESPERADO =============");
			consoleStream.println(new String(fullOutput));
			consoleStream.println("OUTPUT =============");
		} catch(Exception e) {
			e.printStackTrace();
			fail("Erro a ler o ficheiro");
		}

		try {
			Locale.setDefault(Locale.US);
			System.setIn(new FileInputStream(input));
			Class<?> mainClass = Class.forName("Main");
			mainClass.getMethod("main", String[].class).invoke(null, new Object[] { new String[0] });
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro no programa");
		} finally {
			byte[] outPrintBytes = outContent.toByteArray();
			consoleStream.println(new String(outPrintBytes));

			assertEquals(removeCarriages(fullOutput), removeCarriages(new String(outContent.toByteArray())));
		}
	}

	private static String removeCarriages(String s) {
		return s.replaceAll("\r\n", "\n");
	}

}