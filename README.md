# Dragoon

The Bit Goblin video transcoder.

## Installing from RPM

Installing from the Bit Goblin repository is easy! Add the following repo file to `/etc/yum.repos.d/bitgoblin.repo`:

```
[bitgoblin]
name=Bit Goblin repository
baseurl=http://repo.metaunix.net/dnf
enabled=1
gpgcheck=0
```

Update your package sources just to make sure all was added properly:

```
dnf updateinfo
```

Then install dragoon! Use the command below if you DON'T want DNF to install a bunch of unnecessary stuff to meet OpenJDK's weak dependencies; otherwise a regular `dnf install dragoon` is fine:

```
dnf --setopt=install_weak_deps=False --best install dragoon
```

## Building

Currently this project is targeting Java 17 LTS and uses Maven to manage the software lifecycle. Thus, you must have a Java 17 JDK and Maven installed to build this project.

### Ubuntu

`sudo apt install openjdk-17-jdk maven`

### Red Hat/Almalinux

`sudo dnf install java-17-openjdk-devel maven`

### Actually Building

Now that the needed tools are installed, you should be able to build this project. To build a JAR file with it's dependencies included:

`mvn clean compile assembly:single`

Then you can run the transcoder:

`java -jar target/Dragon-VERSION-jar-with-dependencies.jar`

## Configuration

If you were paying attention to Dragoon's output, you would have noticed that it failed with a complaint about not finding a configuration file. The location might move in the future or even be configurable, but for now you need to have a TOML file located at `~/.config/dragoon.toml` with at minimum the following contents:

```toml
# This example transcodes footage to DNxHD 1080p60 for use in video editors like DaVinci Resolve.
[transcoder]
repo_path = '~/videos' # location of the videos to transcode
video_format = 'mov' # video container format
video_codec = 'dnxhd' # video codec to use
video_parameters = 'scale=1920x1080,fps=60,format=yuv422p' # video extra format parameters flag - this will be broken later into separate attributes
video_profile = 'dnxhr_hq' # DNxHD has multiple presets for various video qualities
audio_codec = 'pcm_s16le' # audio codec to use
```
