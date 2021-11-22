------------------------------------------------------------------------------
This is a github mirror for the esoteric_emu sourcecode (with some minor modifications),
a java based Sega Dreamcast emulator.

I've created this repository as a reference, I'm not intending to mantain/develop it.

------------------------------------------------------------------------------

Introduction
Hello. My name is Rui Caridade and i'm a student at the University of Evora in Portugal. This project aims to study the implementation of a Sh4 (1) software simulator in the Java programming language and is the basis for my master thesis. The final goal will be to use that simulator in a dreamcast emulator, capable of running complex software like NetBSD or Linux at acceptable speeds.

As shown by other projects

http://www-jpc.physics.ox.ac.uk/
http://sourceforge.net/projects/jpsx/
Emulation of complex systems is possible in Java, using dynamic recompilation techniques, having the JVM as the compilation backend. I will study several approaches to how this can be done in Java,present the benefits of each approach, and its performance in several JVM implementations. As such Sh4 to JVM assembler is the main focus of my thesis, and will be the area which will receive the most work. The aim of this project is not to create a full working dreamcast emulator in Java (2), but to use the dreamcast's environment as a testing ground for my implementation of a Sh4 simulator.

(1) - The Sh4 is a 32 bit RISC cpu developed by Renesas( result of Hitachi and Mitsubishi semiconductor groups merging) used in several embedded systems besides the dreamcast. The dreamcast environment was chosen as there is more documentation for it (KallitiOS) and is a system i am familiarised with.

(2) - The PowerVR graphics chip present on the dreamcast is a rather complex piece of hardware and a complete implementation of it falls out of the scope of this project, at least till i present my thesis to evaluation.For other projects which aim to fully emulate the Sega Dreamcast console, look at the Links section below

Why Java?
I am sure most of you are probably asking why would someone consider Java for such a project? Well the JVM has one of the worlds most advanced just in time compilers, which can very likely take better advantage of the underlying platform than a hand written just in time compiler. An amazing result of this approach was already shown by the projects stated above.So how can this project innovate and have some academic importance? At the time this document is being written the Dreamcast and the Sh4 is the most advanced platform which has been attempted to emulate on a java environment.Moreover, it will study several dynamic recompilation approaches which can target different systems, and hopefully will be used in projects which dynamic recompilation is a must have feature.

Details
There is no source available at this time as i'm trying to have a more or less working prototype, without dynamic recompilation, before i upload the source.Also there will not be a source release with the dynamic recompilation engine before i present my thesis for reasons i believe can be easily understandable.However i plan to do be binary releases meanwhile, so more people can test this software, and contribute to a more complete performance analysis.

Current Status
Below is a percentage of what is already done in Esoteric

CPU Modules

Sh4 Cpu Interpreter - 80%. All the opcodes are emulated, but there may be some bugs and the MMU, necessary to run Linux, is not yet implemented. Sh4 Timer Unit - 80%. mostly done Sh4 Interrupt Unit - 60% still trying to figure out to correct implement this one. Sh4 DMAC (DMA Controller) - 10%. Needs alot more work.

The remaining sub systems of the Sh4 processor are not emulated at all.

PowerVR Modules

A framebuffer implementation, using opengl is rather complete, but there are some timing issues to consider.

AICA Modules

At the moment there is no sound chip and ARM emulation. This is not an area i'm really focusing, as it is rather complex and beyond the scope of this project.

Additional Considerations

To be able to access and read cd images(.iso,.bin,.nrg) and cds there is a native library,written in C, which uses libcdio to provide a portable way of accessing those image formats.

Acknowledgements
Below is a list of people i would like to thank and without which this project could not exist:

Ivan Toledo - creator of the opensource dreamcast emulator dcemu on which worked for a while some time ago. This project couldn't exist without all of your great work.

Prof. Salvador Pinto Abreu - for having accepted mentoring this project

Alexandre - for being my friend and for passing on to me this little bug

Drk||Raziel - creator of the amazing dreamcast emulator NullDC for some really nice talks and explaining me how some areas of the dreamcast work.

KallistoOS - amazing simply amazing documentation.THANK YOU

Fackue - for is Binary checker.

Marcus Comsted - for is great website

Anders Gavare - i know i've never talked to you,but the first time i saw Linux booting inside of a dreamcast emulator was on your project GXemul.So thank you.

Stephan Dittrich - thank you for your input and great talks

Links
http://gavare.se/gxemul/ - GXemul Multi CPU Emulator. Amazing work.

http://forums.ngemu.com/nulldc-discussion/ - NullDC discussion forums

http://www.lxdream.org/ - a one man project to emulate the Dreamcast on Linux systems.

http://www.chanka.org/ - the worlds first sega dreamcast emulator, which at release could run commercial games

http://gamedev.allusion.net/softprj/kos/ - The KallistiOS main page.

http://fabrice.bellard.free.fr/qemu/ - The famous QEMU also has a Sh4 simulator

http://www.ludd.ltu.se/~jlo/dc/ - Useful information on some parts of the dreamcast.

http://www.workingdesign.de/projects/jenesis.php - a Sega Mega Drive and 32x emulator in java