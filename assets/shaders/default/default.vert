#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uv;

out vec2 textureCoordinates;

uniform mat4 model; //TODO multply these on CPU
uniform mat4 view;
uniform mat4 projection;

uniform int curvedWorld;
uniform float worldCurve;

float WorldCurvature(vec2 pos){
    return dot(pos, pos) / worldCurve;
}

void main() {
    vec4 vertexPos = projection * view * model * vec4(position, 1.0);

    //round world
    if (curvedWorld == 1) {
        vec4 worldPostion = vertexPos;
        worldPostion.y -= WorldCurvature(vertexPos.xz);
        gl_Position = worldPostion;
    }
    else {
        // Flat world
        gl_Position = vertexPos;
    }

    textureCoordinates = uv;
}
