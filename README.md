# Cursed-Interpolator (as Intellij IDEA plugin)

A small GUI for viewing the mappings from Minecraft obfuscated code names to MCP code names that can also be used to compare MCP to Cursed/Plasma/BIN mappings.

THIS IS NOT RELATED TO MCP-MAPPINGS-VIEWER AT ALL. ANY ISSUES YOU HAVE WITH THIS SHOULD BE REPORTED HERE!

Plugin is based on [cursed-interpolator by calmilamsy](https://github.com/calmilamsy/cursed-interpolator). All additional information and links can be found there.

## Building

Warning! Having `Intellij IDEA` is highly recommended getting much more abilities on editing plugin code.

The way without `Intellij IDEA` (for building and getting .jar file):
1. Clone this repository.
2. Run `./gradlew buildPlugin` on *nix or `gradlew.bat buildPlugin` on Windows.
3. The jar file is created in the `/build/distributions` folder. Use the -all file unless you know what you are doing.

## License

The original code is based on [MIT License](https://raw.githubusercontent.com/WaterfallFlower/CursedInterpolatorPlugin/local/LICENSE_ORIGINAL).

The plugin code is based on [MIT License](https://raw.githubusercontent.com/WaterfallFlower/CursedInterpolatorPlugin/local/LICENSE).

Feel free to look at them.

***

### Raw Description

Cursed-Interpolator, but as IntelliJ IDEA plugin.<br/><br/>

**Original program:**<br/>
Copyright (C) 2013 bspkrs<br/>
Portions Copyright (C) 2013 Alex "immibis" Campbell<br/><br/>

**Fabric Mappings adaptation:**<br/>
calmilamsy<br/><br/>

**Intellij IDEA integration:**<br/>
WaterfallFlower<br/><br/>

**Credits (original application):**
- bspkrs (<a href="https://github.com/bspkrs/MCPMappingViewer">MCPMappingsViewer</a>);<br/>
- immibis (<a href="https://github.com/immibis/bearded-octo-nemesis">BON</a>);<br/>
- Searge et al (<a href="http://mcp.ocean-labs.de">MCP</a>);<br/>
- Fabric (<a href="https://fabricmc.net">Enigma</a>);<br/>
- Cursed Fabric (<a href="https://minecraft-cursed-legacy.github.io/">Unified b1.7.3 JAR</a>);<br/>

<em>Should work with versions higher than 2021.1.3<em/>