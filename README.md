### Nebula
![alt text](https://github.com/Ben-Bingham/Nebula/raw/main/gallery/Terrain.png "Repeating terrain generation.")

This was one of the first projects I developed, it is a basic voxel world that is proceduraly generated using simplex noise, it is very much incomplete in its current state, but I feel as though its a good indicator of how far I've come as a programmer.

The most complex feature of this project is the terrain generation, in its current state it can generate a landscape that repeats at a specified rate, but it dosent only repeat the same thing over and over again, the repeating sections are seamless meaning there are no jagged edges between sections.

![alt text](https://github.com/Ben-Bingham/Nebula/raw/main/gallery/Repetition.png "Repeating terrain generation.")

The reason I designed it this way is that it allowa for the simulation of walking around a curved planet, if I could have made a shader, or some other kind of post processing filter that curves the voxels that are further away from you downwards, it would be an aproximate emulation of local flatness (because the radius of the earth is so large, it appears flat near you, but obviously it is still round) you would be able to entirely walk around a planet by simply walking in a straight line, with no need for any kind of teleportation back around to the start of the planet.