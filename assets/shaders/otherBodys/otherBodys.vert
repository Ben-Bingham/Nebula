#version 330

layout (location = 0) in vec3 position;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

// for fragment shader
uniform vec4 colour;
out vec4 Colour;

void main() {
    vec4 vertexPos = projection * view * model * vec4(position, 1.0);

    gl_Position = vertexPos;

    Colour = colour;
}
