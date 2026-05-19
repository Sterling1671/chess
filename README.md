# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Phase 2 diagram URL
[![Sequence Diagram](phase-2.png)]
(https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afJCIJqTsXxXCqwYagSRLMNkaAAOTMFQJpIAxOnwuJNT1AAQiGVlqGAUYxnGhRaUmlSiWmeFZssOaqHm8zQUWJb1Ho64ooS-lLHRTZuYKb5djACBCeKGKCcJfkktuhi7jS+4MkyU7KVyN61XeS7CmKEpujKcplu8SqYOZ6obv1cxGBAaj2cwVpoi1vJtR5SIwD2fZVSB1T+sO-KBSgsYKeh7nhcgqYwOmACMBGxfFBZjEl0D1D40yXtASAAF4oLs2UMVti6LS6I2vs61W+kD9QgPEKAgGqTT6H8OwYt12hrGV4oVWApJDRqMNbDsMC2Q5MAgNAKLgGtYUdp5Mi3tt0a7cFB2Ycd2GnU4F2jGMV35olxb3QqT16i972fY2DH5YKP0HhOKDPvEGIy9eEv3sKj4BpegOdu2bn1CjaAZKoAGYLl5OgS8hH6fMJGod8FFUfWlu0Qz5MRTAuH4ezTU0WRYw24hdukQ29GMd4fj+F4KDoDEcSJGHEdlb4WCiXlG2lg00gRvxEbtBG3Q9HJqgKcMPtIY7lRa8al624UmMwGjeOqFNMBOcgrmPMBxvJ95vkoBlOQ7Xt8ahcmTNgJFbsjDF-LXdzyWyiGMDpdZWUi4brcYX9BVFfY8elUJ8do6SYs1fN-L1WAMvnhXvtoHOrUOu1orik+avaLK8pF+gg2qhqMuV+Nk0EzNHIc0FwCnbp2bsvZ+yHw8ptam6g+700HkdEoI8WZs2zJPLmhYealkehRQWH0A5NkVmAqgS15ZbkPlSOBo4jAoG4MeS8ctn7yBvsfRclRlwyHoUyQwFFX6qxfJQoGmtV6ljjqePWBsjYwLAppZOjMUE4SiqML6QdmL+BROufw2BxQan4miGAABxJUGhE6yPqA0Ixmcc72CVIXS+xdQqlzEfUd+Vcv4127tZOuDcm4uRXrCNeFiYA+Q4GjBB+0kHwGHqPaKHNMEJWwTPVK89vGZRgGoo2INwGFTRCYnMpV8mmP3mTaht96QwEZGfJh7i2EgKVg-CUFD5ACPcZ-CygjKJXz-vXAB1ogEkPXhA1a0CcmUwlpEgeCinaxLQZdRJN07q4P5vEAhwtA5DPGeQlhwAylUwqbQ5AOQClqAxPU-cjSYCdXXCaBAVTTHAMuaQpaBicguAgAAMwxKc68YyHhBPEcUnMUiECATEetCSdRx5jDsTmAsDRxhwpQAASWkAWM64RgiBBBJseIuoUBuk5Hsb4yRQBqiJZBRY3xkUADklTUouDATo8ioWKJOq7eJyLVAIqRUqNFGKsU4uWHigllKDJjFJQgcl4qEqSpBHShlkqmUsuXp4YOAQOAAHY3BOBQE4GIEZghwC4gANngFLYxFYijDyTlCyxrQOi2PsasyuWZFVzFZYmFxgK3GOPQO6pU9LPUdOGrXfGjlnIt0BZCymYSIm037iFGZQ8lEszHhPXMWDbo4JSnPBeGSskQuGYTKWpyMRwDLUqUpVCDnsNodU8+dSnltU4R1R+XSepv39R4zpP8ekcAmn06aAysBbIpktFaUCRGkNgYcqZya2WzLTedBZWakk5pnng56UA3qELUeO-K9QWl7NrRLMGVa5g-KVArGhVyVanLJh+VxFqjwoFOaC8FMbZ1gWWMigV9RMXYsyY7VNHKVEwv-eiwDQqQOB3VRoyw9CiqbEjkgBIYAkN9ggKhgAUhAcUVqxr+DJVDG1KC7WU2aMyGSPRkUOPglfLM2BpVIagHACARUoBrCg16tePqvx+sY0hZjrHKAca4zx-l0gzKePDcOxuUbAlfljf6eN6Te6JsQSm5BJ10wZoSeupZubZ5pQ02AJegdskTv+gAKwI2gct+HxQfvMwfGd5T62nybT2i5rahRNKfkI1pvV2nV37UhXpDdAFjrvS8-6U6n3jLnfWhdoHdPM1XezTmG7lkPVWesoh304slpPfs89VSmTlqg35u+99rkdt+S-XqUHQ3fx7VF-ps1D2g2WpApLFMUsgLS9E52WWMFGenrzbdAtd1CyKy2ur2z-pNfkPso9hMIZQxRZ89owYZhhiQteuYPJVJifY5x6AGM5PmY+oTS7sATRmhrOGRdZD3zJdLIGZ7h3IxaaiTpmJK6MzxJy8ZmeT3gzmhgLWO2aiz00IetgLQ6Jy2reALVjhAXJTI7kIYdHMAvkw4hlUpANA0APLmGigbZdnOOerf+MFym24hJGHxw6QPwNjzUQhkOXhgDyliOh6OUABcxODI97ALHCB5AKDAcj5hKOSTThnLOOdjAlwBYJmAbPmf8ZswVEA3A8DSC8hW437ovI1o83WkBYMLem-OYtrHXCjcS8rNOjW5My6IAl6bz9euOcmx1+z9lzNOWqOXkAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
