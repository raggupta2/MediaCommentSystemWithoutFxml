Firstly, you should install jdk from jdk-17.0.3.1_windows-x64_bin.exe(you can download this on https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and  javafx by extracting the openjfx-17.0.8_windows-x64_bin-sdk.zip(you can download it on https://gluonhq.com/products/javafx/).
And then, you should download vlc player(for 64 bit) and install it(https://www.videolan.org/vlc/download-windows.html).

1. Unzip the zip file "MediaCommentSystem_jar.zip".
2. Move to MediaCommentSystem_jar directory.
3. Open cmd in this path and then please run the following command line.

java -jar  --module-path "Your_javafx_root_path\javafx-sdk-17.0.8\lib" --add-modules javafx.controls,javafx.fxml mcs.jar

4. that's all.
Thanks.