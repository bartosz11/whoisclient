# whoisclient

A simple, configurable and dependency-less Java implementation of the WHOIS protocol.  
Inspired by: https://github.com/bestchanges/whoisclient

## ⚠ Caution

Before proceeding, it's important to note that the WHOIS protocol serves various purposes beyond domain owner lookups.
This library specifically implements the WHOIS protocol as defined in RFC 3912. While it provides a robust foundation
for protocol implementation, it may not seamlessly handle certain use cases, such as retrieving detailed domain
information due to the commonly adopted "thin WHOIS model." The "thin WHOIS model" requires additional steps, navigating
down the hierarchy of servers, to obtain the most accurate information, which is currently not covered by this library.
If your use case involves such scenarios, you may need to implement those aspects yourself. I may include an
implementation in the library itself in the future.

## Example code

```java
public class Main {

    public static void main(String[] args) throws WhoisIOException {
        WhoisClient whoisClient = new WhoisClient(null);
        WhoisResponse response = whoisClient.query("bartosz.one", "whois.iana.org");
        List<String> nameServers = response.getFields().get("nserver");
        System.out.println(String.join(", ", nameServers));
    }

}
```

I'll explain what's going on here:

1. class declarations, psvm, you probably know that stuff already. All exceptions the query method throws extend ``WhoisIOException``, so it's enough for this example snippet.
2. I created a new instance of ``WhoisClient`` with a ``null`` config parameter. Don't worry, it'll just use the default
   settings.
3. I queried IANA's WHOIS server (``whois.iana.org:43``) for information about my domain - ``bartosz.one``. You can skip
   the WHOIS host declaration in this case, because IANA's server is the default one. Port 43 is also the default, so
   it's skipped here.
4. I selected the ``nserver`` key's value out of the ``fields`` map. The .get() method on this map will always return a
   list, since WHOIS returns nothing else except these key:value pairs, but keys don't have to be unique, so that's what
   I settled with as a solution.
5. Lastly, I joined the list into a readable string and printed it out. The result is a list of .one TLD nameservers,
   and that's what you'll usually get when you query IANA's server. However, you can get the key ``refer`` or ``whois``
   from the map to get the hostname of the next server to query to get more accurate information about the domain.

## Usage

[![](https://repo.bartosz.one/api/badge/latest/releases/one/bartosz/whoisclient?color=40c14a&name=whoisclient%20version)](https://repo.bartosz.one/#/releases/one/bartosz/whoisclient)
<details>
  <summary>Maven instructions</summary>

### Maven

1. Add my repo to your Maven repositories:
  ```xml
    <repository>
      <id>bartoszs-repo-releases</id>
      <name>bartosz's repo</name>
      <url>https://repo.bartosz.one/releases</url>
    </repository>
  ```
2. Add whoisclient to your Maven dependencies: (Remember to replace VERSION with actual version you can see above!)
  ```xml
    <dependencies>
      <dependency>
        <groupId>one.bartosz</groupId>
    	<artifactId>whoisclient</artifactId>
    	<version>VERSION</version>
      </dependency>
    </dependencies>
  ```
</details>

<details>
  <summary>Gradle instructions</summary>

### Gradle

1. Add my repo to your Gradle repositories:

  ```groovy
    maven {
      url "https://repo.bartosz.one/releases"
    }
  ```

2. Add whoisclient to your Gradle dependencies: (Remember to replace VERSION with actual version you can see above!)

  ```groovy
    dependencies { 
        implementation 'one.bartosz:whoisclient:VERSION'
    }
  ```
</details>

Javadocs are available [here](https://bartosz11.github.io/whoisclient/).  
Minimum Java version: Java 8.

## Support/Contact

The preferred way to get support from me/contact me is joining [my Discord server](https://discord.gg/FcFebUD9wG).  
Or... email: [contact@bartosz.one](mailto:contact@bartosz.one)   
Feel free to open issues or PR's.

## License

"whoisclient" is licensed under the [MIT License](https://github.com/bartosz11/whoisclient/blob/master/LICENSE).