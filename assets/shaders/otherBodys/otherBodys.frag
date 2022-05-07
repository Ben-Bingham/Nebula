#version 330

in vec4 Colour;

void main() {
    gl_FragColor = vec4(normalize(Colour));
}
