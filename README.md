Fork of More Red, adding a few more bits and bobs!

I plan for arrays to have top/bottom to work as a cable, with a similar attributes for left/right.  The top/bottoms value is treated as an input for left/right.  Inspired by [Project Red's array logic](https://projectredwiki.com/wiki/Invert_Cell)

- [x] Arithmetic 
	- [x] Add 
	- [x] Bit Shift 
	- [x] Multiply 
	- [x] Divide 
	- [x] Modulo 
	- [x] Power 
	- [x] Square root 
	- [x] Equal 
	- [x] Not Equal 
	- [x] Greater 
	- [x] Less 
- [ ] Arrays (Useful for memory) 
	- [ ] Null / Passthrough 
	- [ ] Buffer 
	- [ ] Invert 
 - [x] Inter-bit logic / Bit combination to single output 
	- [x] OR 
	- [x] AND 
	- [x] XOR 
- [ ] New single and network cable gates 
	- [ ] Controlled Diode 
	- [ ] D Latch 
	- [ ] JK Flip flop 
- [ ] Analog gates 
	- [ ] Diode 
	- [ ] Add 
	- [ ] Subtract 
	- [ ] To Digital / Network cable 
	- [ ] From Digital / Network cable 
	- [ ] potentiometer 
- [ ] Network cable switch board block 
- [ ] Network cable light matrix array block 
- [ ] Add crafting recipes

---

More Red is a minecraft mod that uses the forge modloader.

Built jars are available here

* https://www.curseforge.com/minecraft/mc-mods/more-red
* https://modrinth.com/mod/more-red

## Using the API

Mods can use More Red's API in a dev environment by including the following in their build.gradle

```
repositories {
	// java repo to get More Red jars from
	maven { url "https://cubicinterpolation.net/maven/" }
}

dependencies {
	compileOnly fg.deobf("commoble.morered:${morered_branch}:${morered_version}:api")
	runtimeOnly fg.deobf("commoble.morered:${morered_branch}:${morered_version}")
}
```

Where
* `${morered_branch}` is e.g. morered-1.16.5
* `${morered_version}` is e.g. 2.1.0.0

You may need to add this to your run configs to use More Red in a dev environment:

```
property 'mixin.env.remapRefMap', 'true'
property 'mixin.env.refMapRemappingFile', "${projectDir}/build/createSrgToMcp/output.srg"
```

The API is only available on branches `morered-1.16.5` or newer and versions `2.1.0.0` or newer.

A debug jar is available with full sources for easier debugging, which can be used by compiling against :debug instead of :api. It is not recommended to compile against the debug jar when building production jars, as binary-breaking changes are more likely to occur with it.

More documentation of the More Red API is available on the More Red github wiki:

https://github.com/Commoble/morered/wiki/API-Reference
