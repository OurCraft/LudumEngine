package org.jglrxavpok;

import java.io.{InputStream, OutputStream, ByteArrayOutputStream, FileNotFoundException};

object IOUtils {

    def copy(in: InputStream, out: OutputStream): Unit = {
        val buffer: Array[Byte] = new Array[Byte](4096)
        var i: Int = 0
        while (i != -1) {
            i = in.read(buffer)
            if(i != -1)
                out.write(buffer, 0, i)
        }
        out.flush()
    }

    def read(classpathLoc: String, charset: String): String = {
        val input: InputStream = getClass.getResourceAsStream("/" + classpathLoc)
        if (input == null)
            throw new FileNotFoundException("No file found in classpath: /" + classpathLoc)
        val out: ByteArrayOutputStream = new ByteArrayOutputStream()
        copy(input, out)
        input.close()
        out.close()
        new String(out.toByteArray(), charset)
    }
}
