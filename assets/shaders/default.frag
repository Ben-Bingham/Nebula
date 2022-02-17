#version 330 core

in vec2 textureCoordinates;

out vec4 FragColor;

uniform sampler2D texture1;

void main() {
    FragColor = texture(texture1, textureCoordinates);
    //FragColor = vec4(1.0);
}
