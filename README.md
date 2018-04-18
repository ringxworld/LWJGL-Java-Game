# LWJGL-Java-Game
Following https://github.com/lwjglgamedev/lwjglbook Tutorials here was my game

![Alt Text](https://i.gyazo.com/28ecd66bbcb45bd53e790403572e8bd8.png)

## Getting Started

Since project was configured in a strange way relative to how I would normally approach it. Rather than entirely use maven to handle the external libraries I had manually added some to a lib folder and packaged them with the source here. As a result to build this you will need to configure the build path in your IDE to include these libraries.

### Prerequisites

To run have Java 8+ and have java on your environmental variables and path
Have maven 3.5+ and have it on your environmental variables and path
If maven is having trouble loading the referenced libraries open the pom xml and it will find them
Finally have either eclipse or intellij to set up the project

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [LWJGL](https://www.lwjgl.org/) - OpenGL bindings
* [JOML](https://github.com/JOML-CI/JOML) - A Java math library for OpenGL rendering calculations
* [pngdecoder](http://twl.l33tlabs.org/) - This is a very small (below 10KB) stand alone JAR which makes decoding PNG images very easy.


## Authors

* **Shawn Anderson** - [Wijkuy](https://github.com/Wijkuy)

## Acknowledgments

* [lwjglgamedev](https://github.com/lwjglgamedev/lwjglbook) - This project was almost entirely based off his book and tutorial series

