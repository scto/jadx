package jadx.plugins.input.dex.utils;

import java.nio.ByteBuffer;
import java.util.zip.Adler32;

import jadx.plugins.input.dex.DexException;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class DexCheckSum {

	public static void verify(String fileName, byte[] content, int offset) {
		int len = content.length;
		if (len < 12) {
			throw new DexException("Dex file truncated, length: " + len + ", file: " + fileName);
		}
		int checksum = ByteBuffer.wrap(content, offset + 8, 4).order(LITTLE_ENDIAN).getInt();
		Adler32 adler32 = new Adler32();
		adler32.update(content, 12, len - 12);
		int fileChecksum = (int) adler32.getValue();
		if (checksum != fileChecksum) {
			throw new DexException(String.format("Bad dex file checksum: 0x%08x, expected: 0x%08x, file: %s",
					fileChecksum, checksum, fileName));
		}
	}
}
