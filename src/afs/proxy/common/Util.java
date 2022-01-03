package afs.proxy.common;

import java.math.BigInteger;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Util
{
	// Constants and variables
	// -------------------------------------------------------------------------

  // Hex charset
  private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

  // Base-64 charset
  private static final String BASE64_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./";

  private static final char[] BASE64_CHARSET = BASE64_CHARS.toCharArray();

  // Constructor(s)
  // -------------------------------------------------------------------------

  /** Trivial constructor to enforce Singleton pattern. */
  private Util()
  {
    super();
  }

  // Class methods
  // -------------------------------------------------------------------------

  /**
   * <p>Returns a string of hexadecimal digits from a byte array. Each byte is
   * converted to 2 hex symbols; zero(es) included.</p>
   *
   * <p>This method calls the method with same name and three arguments as:</p>
   *
   * <pre>
   *    toString(ba, 0, ba.length);
   * </pre>
   *
   * @param ba the byte array to convert.
   * @return a string of hexadecimal characters (two for each byte)
   * representing the designated input byte array.
   */
  public static String toString(byte[] ba)
  {
    return toString(ba, 0, ba.length);
  }

  /**
   * <p>Returns a string of hexadecimal digits from a byte array, starting at
   * <code>offset</code> and consisting of <code>length</code> bytes. Each byte
   * is converted to 2 hex symbols; zero(es) included.</p>
   *
   * @param ba the byte array to convert.
   * @param offset the index from which to start considering the bytes to
   * convert.
   * @param length the count of bytes, starting from the designated offset to
   * convert.
   * @return a string of hexadecimal characters (two for each byte)
   * representing the designated input byte sub-array.
   */
  public static final String toString(byte[] ba, int offset, int length)
  {
    char[] buf = new char[length * 2];
    for (int i = 0, j = 0, k; i < length;)
      {
        k = ba[offset + i++];
        buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
        buf[j++] = HEX_DIGITS[k & 0x0F];
      }
    return new String(buf);
  }

  /**
   * <p>Returns a string of hexadecimal digits from a byte array. Each byte is
   * converted to 2 hex symbols; zero(es) included. The argument is
   * treated as a large little-endian integer and is returned as a
   * large big-endian integer.</p>
   *
   * <p>This method calls the method with same name and three arguments as:</p>
   *
   * <pre>
   *    toReversedString(ba, 0, ba.length);
   * </pre>
   *
   * @param ba the byte array to convert.
   * @return a string of hexadecimal characters (two for each byte)
   * representing the designated input byte array.
   */
  public static String toReversedString(byte[] ba)
  {
    return toReversedString(ba, 0, ba.length);
  }

  /**
   * <p>Returns a string of hexadecimal digits from a byte array, starting at
   * <code>offset</code> and consisting of <code>length</code> bytes. Each byte
   * is converted to 2 hex symbols; zero(es) included.</p>
   *
   * <p>The byte array is treated as a large little-endian integer, and
   * is returned as a large big-endian integer.</p>
   *
   * @param ba the byte array to convert.
   * @param offset the index from which to start considering the bytes to
   * convert.
   * @param length the count of bytes, starting from the designated offset to
   * convert.
   * @return a string of hexadecimal characters (two for each byte)
   * representing the designated input byte sub-array.
   */
  public static final String toReversedString(byte[] ba, int offset, int length)
  {
    char[] buf = new char[length * 2];
    for (int i = offset + length - 1, j = 0, k; i >= offset;)
      {
        k = ba[offset + i--];
        buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
        buf[j++] = HEX_DIGITS[k & 0x0F];
      }
    return new String(buf);
  }

  /**
   * <p>Returns a byte array from a string of hexadecimal digits.</p>
   *
   * @param s a string of hexadecimal ASCII characters
   * @return the decoded byte array from the input hexadecimal string.
   */
  public static byte[] toBytesFromString(String s)
  {
    int limit = s.length();
    byte[] result = new byte[((limit + 1) / 2)];
    int i = 0, j = 0;
    if ((limit % 2) == 1)
      {
        result[j++] = (byte) fromDigit(s.charAt(i++));
      }
    while (i < limit)
      {
        result[j] = (byte) (fromDigit(s.charAt(i++)) << 4);
        result[j++] |= (byte) fromDigit(s.charAt(i++));
      }
    return result;
  }

  /**
   * <p>Returns a byte array from a string of hexadecimal digits, interpreting
   * them as a large big-endian integer and returning it as a large
   * little-endian integer.</p>
   *
   * @param s a string of hexadecimal ASCII characters
   * @return the decoded byte array from the input hexadecimal string.
   */
  public static byte[] toReversedBytesFromString(String s)
  {
    int limit = s.length();
    byte[] result = new byte[((limit + 1) / 2)];
    int i = 0;
    if ((limit % 2) == 1)
      {
        result[i++] = (byte) fromDigit(s.charAt(--limit));
      }
    while (limit > 0)
      {
        result[i] = (byte) fromDigit(s.charAt(--limit));
        result[i++] |= (byte) (fromDigit(s.charAt(--limit)) << 4);
      }
    return result;
  }

  /**
   * <p>Returns a number from <code>0</code> to <code>15</code> corresponding
   * to the designated hexadecimal digit.</p>
   *
   * @param c a hexadecimal ASCII symbol.
   */
  public static int fromDigit(char c)
  {
    if (c >= '0' && c <= '9')
      {
        return c - '0';
      }
    else if (c >= 'A' && c <= 'F')
      {
        return c - 'A' + 10;
      }
    else if (c >= 'a' && c <= 'f')
      {
        return c - 'a' + 10;
      }
    else
      throw new IllegalArgumentException("Invalid hexadecimal digit: " + c);
  }

  /**
   * <p>Returns a string of 8 hexadecimal digits (most significant digit first)
   * corresponding to the unsigned integer <code>n</code>.</p>
   *
   * @param n the unsigned integer to convert.
   * @return a hexadecimal string 8-character long.
   */
  public static String toString(int n)
  {
    char[] buf = new char[8];
    for (int i = 7; i >= 0; i--)
      {
        buf[i] = HEX_DIGITS[n & 0x0F];
        n >>>= 4;
      }
    return new String(buf);
  }

  /**
   * <p>Returns a string of hexadecimal digits from an integer array. Each int
   * is converted to 4 hex symbols.</p>
   */
  public static String toString(int[] ia)
  {
    int length = ia.length;
    char[] buf = new char[length * 8];
    for (int i = 0, j = 0, k; i < length; i++)
      {
        k = ia[i];
        buf[j++] = HEX_DIGITS[(k >>> 28) & 0x0F];
        buf[j++] = HEX_DIGITS[(k >>> 24) & 0x0F];
        buf[j++] = HEX_DIGITS[(k >>> 20) & 0x0F];
        buf[j++] = HEX_DIGITS[(k >>> 16) & 0x0F];
        buf[j++] = HEX_DIGITS[(k >>> 12) & 0x0F];
        buf[j++] = HEX_DIGITS[(k >>> 8) & 0x0F];
        buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
        buf[j++] = HEX_DIGITS[k & 0x0F];
      }
    return new String(buf);
  }

  /**
   * <p>Returns a string of 16 hexadecimal digits (most significant digit first)
   * corresponding to the unsigned long <code>n</code>.</p>
   *
   * @param n the unsigned long to convert.
   * @return a hexadecimal string 16-character long.
   */
  public static String toString(long n)
  {
    char[] b = new char[16];
    for (int i = 15; i >= 0; i--)
      {
        b[i] = HEX_DIGITS[(int) (n & 0x0FL)];
        n >>>= 4;
      }
    return new String(b);
  }

  /**
   * <p>Similar to the <code>toString()</code> method except that the Unicode
   * escape character is inserted before every pair of bytes. Useful to
   * externalise byte arrays that will be constructed later from such strings;
   * eg. s-box values.</p>
   *
   * @throws ArrayIndexOutOfBoundsException if the length is odd.
   */
  public static String toUnicodeString(byte[] ba)
  {
    return toUnicodeString(ba, 0, ba.length);
  }

  /**
   * <p>Similar to the <code>toString()</code> method except that the Unicode
   * escape character is inserted before every pair of bytes. Useful to
   * externalise byte arrays that will be constructed later from such strings;
   * eg. s-box values.</p>
   *
   * @throws ArrayIndexOutOfBoundsException if the length is odd.
   */
  public static final String toUnicodeString(byte[] ba, int offset, int length)
  {
    StringBuffer sb = new StringBuffer();
    int i = 0;
    int j = 0;
    int k;
    sb.append('\n').append("\"");
    while (i < length)
      {
        sb.append("\\u");

        k = ba[offset + i++];
        sb.append(HEX_DIGITS[(k >>> 4) & 0x0F]);
        sb.append(HEX_DIGITS[k & 0x0F]);

        k = ba[offset + i++];
        sb.append(HEX_DIGITS[(k >>> 4) & 0x0F]);
        sb.append(HEX_DIGITS[k & 0x0F]);

        if ((++j % 8) == 0)
          {
            sb.append("\"+").append('\n').append("\"");
          }
      }
    sb.append("\"").append('\n');
    return sb.toString();
  }

  /**
   * <p>Similar to the <code>toString()</code> method except that the Unicode
   * escape character is inserted before every pair of bytes. Useful to
   * externalise integer arrays that will be constructed later from such
   * strings; eg. s-box values.</p>
   *
   * @throws ArrayIndexOutOfBoundsException if the length is not a multiple of 4.
   */
  public static String toUnicodeString(int[] ia)
  {
    StringBuffer sb = new StringBuffer();
    int i = 0;
    int j = 0;
    int k;
    sb.append('\n').append("\"");
    while (i < ia.length)
      {
        k = ia[i++];
        sb.append("\\u");
        sb.append(HEX_DIGITS[(k >>> 28) & 0x0F]);
        sb.append(HEX_DIGITS[(k >>> 24) & 0x0F]);
        sb.append(HEX_DIGITS[(k >>> 20) & 0x0F]);
        sb.append(HEX_DIGITS[(k >>> 16) & 0x0F]);
        sb.append("\\u");
        sb.append(HEX_DIGITS[(k >>> 12) & 0x0F]);
        sb.append(HEX_DIGITS[(k >>> 8) & 0x0F]);
        sb.append(HEX_DIGITS[(k >>> 4) & 0x0F]);
        sb.append(HEX_DIGITS[k & 0x0F]);

        if ((++j % 4) == 0)
          {
            sb.append("\"+").append('\n').append("\"");
          }
      }
    sb.append("\"").append('\n');
    return sb.toString();
  }

  public static byte[] toBytesFromUnicode(String s)
  {
    int limit = s.length() * 2;
    byte[] result = new byte[limit];
    char c;
    for (int i = 0; i < limit; i++)
      {
        c = s.charAt(i >>> 1);
        result[i] = (byte) (((i & 1) == 0) ? c >>> 8 : c);
      }
    return result;
  }

  /**
   * <p>Dumps a byte array as a string, in a format that is easy to read for
   * debugging. The string <code>m</code> is prepended to the start of each
   * line.</p>
   *
   * <p>If <code>offset</code> and <code>length</code> are omitted, the whole
   * array is used. If <code>m</code> is omitted, nothing is prepended to each
   * line.</p>
   *
   * @param data the byte array to be dumped.
   * @param offset the offset within <i>data</i> to start from.
   * @param length the number of bytes to dump.
   * @param m a string to be prepended to each line.
   * @return a string containing the result.
   */
  public static String dumpString(byte[] data, int offset, int length, String m)
  {
    if (data == null)
      {
        return m + "null\n";
      }
    StringBuffer sb = new StringBuffer(length * 3);
    if (length > 32)
      {
        sb.append(m).append("Hexadecimal dump of ").append(length).append(
                                                                          " bytes...\n");
      }
    // each line will list 32 bytes in 4 groups of 8 each
    int end = offset + length;
    String s;
    int l = Integer.toString(length).length();
    if (l < 4)
      {
        l = 4;
      }
    for (; offset < end; offset += 32)
      {
        if (length > 32)
          {
            s = "         " + offset;
            sb.append(m).append(s.substring(s.length() - l)).append(": ");
          }
        int i = 0;
        for (; i < 32 && offset + i + 7 < end; i += 8)
          {
            sb.append(toString(data, offset + i, 8)).append(' ');
          }
        if (i < 32)
          {
            for (; i < 32 && offset + i < end; i++)
              {
                sb.append(byteToString(data[offset + i]));
              }
          }
        sb.append('\n');
      }
    return sb.toString();
  }

  public static String dumpString(byte[] data)
  {
    return (data == null) ? "null\n" : dumpString(data, 0, data.length, "");
  }

  public static String dumpString(byte[] data, String m)
  {
    return (data == null) ? "null\n" : dumpString(data, 0, data.length, m);
  }

  public static String dumpString(byte[] data, int offset, int length)
  {
    return dumpString(data, offset, length, "");
  }

  /**
   * <p>Returns a string of 2 hexadecimal digits (most significant digit first)
   * corresponding to the lowest 8 bits of <code>n</code>.</p>
   *
   * @param n the byte value to convert.
   * @return a string of 2 hex characters representing the input.
   */
  public static String byteToString(int n)
  {
    char[] buf = { HEX_DIGITS[(n >>> 4) & 0x0F], HEX_DIGITS[n & 0x0F] };
    return new String(buf);
  }

  /**
   * <p>Converts a designated byte array to a Base-64 representation, with the
   * exceptions that (a) leading 0-byte(s) are ignored, and (b) the character
   * '.' (dot) shall be used instead of "+' (plus).</p>
   *
   * <p>Used by SASL password file manipulation primitives.</p>
   *
   * @param buffer an arbitrary sequence of bytes to represent in Base-64.
   * @return unpadded (without the '=' character(s)) Base-64 representation of
   * the input.
   */
  public static final String toBase64(byte[] buffer)
  {
    int len = buffer.length, pos = len % 3;
    byte b0 = 0, b1 = 0, b2 = 0;
    switch (pos)
      {
      case 1:
        b2 = buffer[0];
        break;
      case 2:
        b1 = buffer[0];
        b2 = buffer[1];
        break;
      }
    StringBuffer sb = new StringBuffer();
    int c;
    boolean notleading = false;
    do
      {
        c = (b0 & 0xFC) >>> 2;
        if (notleading || c != 0)
          {
            sb.append(BASE64_CHARSET[c]);
            notleading = true;
          }
        c = ((b0 & 0x03) << 4) | ((b1 & 0xF0) >>> 4);
        if (notleading || c != 0)
          {
            sb.append(BASE64_CHARSET[c]);
            notleading = true;
          }
        c = ((b1 & 0x0F) << 2) | ((b2 & 0xC0) >>> 6);
        if (notleading || c != 0)
          {
            sb.append(BASE64_CHARSET[c]);
            notleading = true;
          }
        c = b2 & 0x3F;
        if (notleading || c != 0)
          {
            sb.append(BASE64_CHARSET[c]);
            notleading = true;
          }
        if (pos >= len)
          {
            break;
          }
        else
          {
            try
              {
                b0 = buffer[pos++];
                b1 = buffer[pos++];
                b2 = buffer[pos++];
              }
            catch (ArrayIndexOutOfBoundsException x)
              {
                break;
              }
          }
      }
    while (true);

    if (notleading)
      {
        return sb.toString();
      }
    return "0";
  }

  /**
   * <p>The inverse function of the above.</p>
   *
   * <p>Converts a string representing the encoding of some bytes in Base-64
   * to their original form.</p>
   *
   * @param str the Base-64 encoded representation of some byte(s).
   * @return the bytes represented by the <code>str</code>.
   * @throws NumberFormatException if <code>str</code> is <code>null</code>, or
   * <code>str</code> contains an illegal Base-64 character.
   * @see #toBase64(byte[])
   */
  public static final byte[] fromBase64(String str)
  {
    int len = str.length();
    if (len == 0)
      {
        throw new NumberFormatException("Empty string");
      }
    byte[] a = new byte[len + 1];
    int i, j;
    for (i = 0; i < len; i++)
      {
        try
          {
            a[i] = (byte) BASE64_CHARS.indexOf(str.charAt(i));
          }
        catch (ArrayIndexOutOfBoundsException x)
          {
            throw new NumberFormatException("Illegal character at #" + i);
          }
      }
    i = len - 1;
    j = len;
    try
      {
        while (true)
          {
            a[j] = a[i];
            if (--i < 0)
              {
                break;
              }
            a[j] |= (a[i] & 0x03) << 6;
            j--;
            a[j] = (byte) ((a[i] & 0x3C) >>> 2);
            if (--i < 0)
              {
                break;
              }
            a[j] |= (a[i] & 0x0F) << 4;
            j--;
            a[j] = (byte) ((a[i] & 0x30) >>> 4);
            if (--i < 0)
              {
                break;
              }
            a[j] |= (a[i] << 2);
            j--;
            a[j] = 0;
            if (--i < 0)
              {
                break;
              }
          }
      }
    catch (Exception ignored)
      {
      }

    try
      { // ignore leading 0-bytes
        while (a[j] == 0)
          {
            j++;
          }
      }
    catch (Exception x)
      {
        return new byte[1]; // one 0-byte
      }
    byte[] result = new byte[len - j + 1];
    System.arraycopy(a, j, result, 0, len - j + 1);
    return result;
  }

  private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();

  private static final int BASE_58 = ALPHABET.length;
  private static final int BASE_256 = 256;

  private static final int[] INDEXES = new int[128];
  
  static {
    for (int i = 0; i < INDEXES.length; i++) {
      INDEXES[i] = -1;
    }
    for (int i = 0; i < ALPHABET.length; i++) {
      INDEXES[ALPHABET[i]] = i;
    }
  }

  public static String toBase58(byte[] input)
  {
    if (input.length == 0) {
      // paying with the same coin
      return "";
    }

    //
    // Make a copy of the input since we are going to modify it.
    //
    input = copyOfRange(input, 0, input.length);

    //
    // Count leading zeroes
    //
    int zeroCount = 0;
    while (zeroCount < input.length && input[zeroCount] == 0) {
      ++zeroCount;
    }

    //
    // The actual encoding
    //
    byte[] temp = new byte[input.length * 2];
    int j = temp.length;

    int startAt = zeroCount;
    while (startAt < input.length) {
      byte mod = divmod58(input, startAt);
      if (input[startAt] == 0) {
        ++startAt;
      }

      temp[--j] = (byte) ALPHABET[mod];
    }

    //
    // Strip extra '1' if any
    //
    while (j < temp.length && temp[j] == ALPHABET[0]) {
      ++j;
    }

    //
    // Add as many leading '1' as there were leading zeros.
    //
    while (--zeroCount >= 0) {
      temp[--j] = (byte) ALPHABET[0];
    }

    byte[] output = copyOfRange(temp, j, temp.length);
    return new String(output);
  }

  public static byte[] fromBase58(String input)
  {
    if (input.length() == 0) {
      // paying with the same coin
      return new byte[0];
    }

    byte[] input58 = new byte[input.length()];
    //
    // Transform the String to a base58 byte sequence
    //
    for (int i = 0; i < input.length(); ++i) {
      char c = input.charAt(i);

      int digit58 = -1;
      if (c >= 0 && c < 128) {
        digit58 = INDEXES[c];
      }
      if (digit58 < 0) {
        throw new RuntimeException("Not a Base58 input: " + input);
      }

      input58[i] = (byte) digit58;
    }

    //
    // Count leading zeroes
    //
    int zeroCount = 0;
    while (zeroCount < input58.length && input58[zeroCount] == 0) {
      ++zeroCount;
    }

    //
    // The encoding
    //
    byte[] temp = new byte[input.length()];
    int j = temp.length;

    int startAt = zeroCount;
    while (startAt < input58.length) {
      byte mod = divmod256(input58, startAt);
      if (input58[startAt] == 0) {
        ++startAt;
      }

      temp[--j] = mod;
    }

    //
    // Do no add extra leading zeroes, move j to first non null byte.
    //
    while (j < temp.length && temp[j] == 0) {
      ++j;
    }

    return copyOfRange(temp, j - zeroCount, temp.length);
  }

  private static byte divmod58(byte[] number, int startAt)
  {
    int remainder = 0;
    for (int i = startAt; i < number.length; i++) {
      int digit256 = (int) number[i] & 0xFF;
      int temp = remainder * BASE_256 + digit256;

      number[i] = (byte) (temp / BASE_58);

      remainder = temp % BASE_58;
    }

    return (byte) remainder;
  }

  private static byte divmod256(byte[] number58, int startAt)
  {
    int remainder = 0;
    for (int i = startAt; i < number58.length; i++) {
      int digit58 = (int) number58[i] & 0xFF;
      int temp = remainder * BASE_58 + digit58;

      number58[i] = (byte) (temp / BASE_256);

      remainder = temp % BASE_256;
    }

    return (byte) remainder;
  }

  private static byte[] copyOfRange(byte[] source, int from, int to)
  {
    byte[] range = new byte[to - from];
    System.arraycopy(source, from, range, 0, range.length);

    return range;
  }

  // BigInteger utilities ----------------------------------------------------

  /**
   * <p>Treats the input as the MSB representation of a number, and discards
   * leading zero elements. For efficiency, the input is simply returned if no
   * leading zeroes are found.</p>
   *
   * @param n the {@link BigInteger} to trim.
   * @return the byte array representation of the designated {@link BigInteger}
   * with no leading 0-bytes.
   */
  public static final byte[] trim(BigInteger n)
  {
    byte[] in = n.toByteArray();
    if (in.length == 0 || in[0] != 0)
      {
        return in;
      }
    int len = in.length;
    int i = 1;
    while (in[i] == 0 && i < len)
      {
        ++i;
      }
    byte[] result = new byte[len - i];
    System.arraycopy(in, i, result, 0, len - i);
    return result;
  }

  /**
   * <p>Returns a hexadecimal dump of the trimmed bytes of a {@link BigInteger}.
   * </p>
   *
   * @param x the {@link BigInteger} to display.
   * @return the string representation of the designated {@link BigInteger}.
   */
  public static final String dump(BigInteger x)
  {
    return dumpString(trim(x));
  }

	public static String jsonBuild (HashMap<String, String> map)
	{
		String data = "{";
		int i = 0;

		Set<Map.Entry<String, String>> set = map.entrySet ();

		for (Map.Entry<String, String> element : set)
		{
			if (i > 0) data += ",";
			data += "\"" + element.getKey () + "\":\"" + element.getValue () + "\"";
			i++;
		}

		data += "}";
		return data;
	}

	public static String jsonBuild (HashMap<String, String> map, HashMap<String, HashMap<String, String>> table)
	{
		String data = "{";
		int i = 0;

		Set<Map.Entry<String, String>> set = map.entrySet ();

		for (Map.Entry<String, String> element : set)
		{
			if (i > 0) data += ",";
			data += "\"" + element.getKey () + "\":\"" + element.getValue () + "\"";
			i++;
		}

		int j = 0;
		Set<Map.Entry<String, HashMap<String, String>>> set1 = table.entrySet ();
    int len1 = set1.size ();
    if (i > 0 && len1 > 0) data += ",";

		for (Map.Entry<String, HashMap<String, String>> element1 : set1)
		{
			data += "\"" + element1.getKey () + "\":{";

			HashMap<String, String> map1 = element1.getValue ();
			Set<Map.Entry<String, String>> set2 = map1.entrySet ();
      int len2 = set2.size ();

			i = 0;
			for (Map.Entry<String, String> element2 : set2)
			{
				data += "\"" + element2.getKey () + "\":\"" + element2.getValue () + "\"";
        if (i < len2 - 1) data += ",";
				i++;
			}

      if (j < len1 - 1) data += "},";
      else data += "}";

			j++;
		}

		data += "}";
		return data;
	}

	public static boolean jsonParse (String data, HashMap<String, String> map, HashMap<String, HashMap<String, String>> table)
	{
		if (map != null) map.clear ();
		if (table != null) table.clear ();

    if (data == null || data.equals ("")) return false;

		String s = data.substring (0, 1);
		if (!s.equals ("{")) return false;

		s = data.substring (data.length () - 1, data.length ());
		if (!s.equals ("}")) return false;

		s = data.substring (1, data.length () - 1);
		String [] t = jsonSplit (s, ',');

		for (int i = 0; i < t.length; i++)
		{
			String[] t1 = jsonSplit (t[i], ':');

			if (t1.length < 2) return false;

			if (t1[1].charAt(0) == '{')
			{
				// table
				s = t1[0].substring (0, 1);
				if (!s.equals ("\"")) return false;

				s = t1[0].substring (t1[0].length () - 1, t1[0].length ());
				if (!s.equals ("\"")) return false;

				String key = t1[0].substring (1, t1[0].length () - 1);
				//System.out.println (key);

				s = t1[1].substring (0, 1);
				if (!s.equals ("{")) return false;

				s = t1[1].substring (t1[1].length () - 1, t1[1].length ());
				
				if (!s.equals ("}")) return false;

				s = t1[1].substring (1, t1[1].length () - 1);

				HashMap<String, String> tableLine = new HashMap<String, String> ();
				String key1;
                String value;
				String[] t2 = s.split (",");
				for (int i1 = 0; i1 < t2.length; i1++)
				{
					String[] t3 = t2[i1].split (":");

					if (t3.length < 2) return false;

					s = t3[0].substring (0, 1);
					if (!s.equals ("\"")) return false;

					s = t3[0].substring (t3[0].length () - 1, t3[0].length ());
					if (!s.equals ("\"")) return false;

					key1 = t3[0].substring (1, t3[0].length () - 1);

					s = t3[1].substring (0, 1);
					if (!s.equals ("\"")) return false;

					s = t3[1].substring (t3[1].length () - 1, t3[1].length ());
					if (!s.equals ("\"")) return false;

					value = t3[1].substring (1, t3[1].length () - 1);

					//System.out.println (key1 + " " + value);
					tableLine.put (key1, value);
				}

				if (table != null) table.put (key, tableLine);
			}
			else
			{
				// map
				s = t1[0].substring (0, 1);
				if (!s.equals ("\"")) return false;

				s = t1[0].substring (t1[0].length () - 1, t1[0].length ());
				if (!s.equals ("\"")) return false;

				String key = t1[0].substring (1, t1[0].length () - 1);

				s = t1[1].substring (0, 1);
				if (!s.equals ("\"")) return false;

				s = t1[1].substring (t1[1].length () - 1, t1[1].length ());
				if (!s.equals ("\"")) return false;

				String value = t1[1].substring (1, t1[1].length () - 1);

				//System.out.println (key + " " + value);
				if (map != null) map.put (key, value);
			}
		}
    return true;
	}

	private static String[] jsonSplit (String string, char splitter)
	{
		//Неэффективный алгорим с конкатенацей строк
		ArrayList<String> strings = new ArrayList<String> ();
		String s = new String ();
		boolean insideBrackets = false;
		int i = 0;
		while (i < string.length ())
		{
			
			char ch = string.charAt (i);
			if (ch == splitter)
			{
				if (insideBrackets) s += ch;
				else
				{
					strings.add (s);
					s = "";
				}
			}
			else if (ch == '{')
			{
				insideBrackets = true;
				s += ch;
			}
			else if (ch == '}')
			{
				insideBrackets = false;
				s += ch;
			}
			else
			{
				s += ch;
			}
			 
			i++;
		}
		if (!s.equals ("")) strings.add (s);

		return strings.toArray (new String[0]);
	}
}