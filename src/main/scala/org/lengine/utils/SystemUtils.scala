package org.lengine.utils

import java.io.File
;

object SystemUtils {

    private var gameFolder: File = _

    def getOS: String = {
        val os: String = System.getProperty("os.name").toLowerCase
        if (os.contains("win")) {
            return OperatingSystem.WINDOWS
        } else if (os.contains("sunos") || os.contains("solaris")) {
            return OperatingSystem.SOLARIS
        } else if (os.contains("unix") || os.contains("linux")) {
            return OperatingSystem.LINUX
        } else if (os.contains("mac")) {
            return OperatingSystem.MACOSX
        }
        OperatingSystem.UNKNOWN
    }

    /**
     * Returns the folder where game data is saved
     */
    def getGameFolder(gameID: String): File =  {
        if (gameFolder == null) {
            val appdata: String = System.getenv("APPDATA")
            if (appdata != null)
                gameFolder = new File(appdata, gameID)
            else
                gameFolder = new File(System.getProperty("user.home"), gameID)

            if(!gameFolder.exists)
                gameFolder.mkdirs
        }
        gameFolder
    }

}
