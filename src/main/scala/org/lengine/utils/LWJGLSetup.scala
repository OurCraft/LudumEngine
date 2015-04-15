package org.lengine.utils

import java.io.{File, FileOutputStream, InputStream}
import java.util.regex.Pattern
import java.util.{HashMap, Map}

object LWJGLSetup {

    def load(folder: File): Unit = {
        if (!folder.exists)
            folder.mkdirs
        if (folder.isDirectory) {
            val nativesMap: Map[String, Array[String]] = createNativesMap
            val arch: String = System.getProperty("os.arch")
            val is64bits: Boolean = !arch.equals("x86")
            val os: String = SystemUtils.getOS

            val arch64Variants: Array[String] = Array("_64", "64", "")
            val nativesList: Array[String] = nativesMap.get(os)
            if (nativesList != null) {
                for (f <- nativesList) {
                    for (variant <- arch64Variants) {
                        val fileName: String = if(is64bits) f.replace("32", "") else f
                        val parts: Array[String] = fileName.split(Pattern.quote("."))
                        var name: String = sum(parts, 0, parts.length - 1)
                        name += variant + "." + parts(parts.length - 1)
                        if (exists(name)) {
                            if (!new File(folder, name).exists) {
                                extractFromClasspath(name, folder)
                                println(s"Successfully extracted native from classpath: $name")
                            }
                        }
                    }
                }
            }
            System.setProperty("net.java.games.input.librarypath", folder.getAbsolutePath)
            System.setProperty("org.lwjgl.librarypath", folder.getAbsolutePath)
        }
    }

    def sum(parts: Array[String], offset: Int, length: Int): String = {
        val buffer: StringBuilder = new StringBuilder
        for (i <- offset until offset+length) {
            buffer.append(parts(i))
        }
        buffer.toString
    }

    def exists(fileName: String): Boolean = {
        val stream: InputStream = getClass.getResourceAsStream("/" + fileName)
        if (stream != null) {
            stream.close
            return true
        }
        return false
    }

    def createNativesMap: Map[String, Array[String]] = {
        val nativesMap: Map[String, Array[String]] = new HashMap[String, Array[String]]
        val win: Array[String] = Array("jinput-dx8.dll", "jinput-raw.dll", "lwjgl.dll", "OpenAL32.dll")
        nativesMap.put(OperatingSystem.WINDOWS, win)

        val macosx: Array[String] = Array("liblwjgl.jnilib", "liblwjgl-osx.jnilib", "openal.dylib")
        nativesMap.put(OperatingSystem.MACOSX, macosx)

        val unix: Array[String] =  Array("liblwjgl.so", "libopenal.so")
        nativesMap.put(OperatingSystem.LINUX, unix)
        nativesMap.put(OperatingSystem.SOLARIS, unix)
        nativesMap
    }

    /**
     * Extract given file from classpath into given folder
     */
    def extractFromClasspath(fileName: String, folder: File): Unit = {
        val out: FileOutputStream = new FileOutputStream(new File(folder, fileName))
        IOUtils.copy(getClass.getResourceAsStream("/" + fileName), out)
        out.flush
        out.close
    }
}
