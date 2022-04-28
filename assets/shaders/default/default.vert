#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uv;
layout (location = 2) in vec3 normals;

out vec2 textureCoordinates;

// for fragment shader
out vec3 FragPos;
out vec3 Normal;
out vec3 LightPos;
out vec3 LightColor;

uniform vec3 lightPos;
uniform vec3 lightColor;

uniform mat4 model;
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
    Normal = normals;
    FragPos = vec3(model * vec4(position, 1.0));
    LightPos = lightPos;
}
