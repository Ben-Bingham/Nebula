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
out mat4 planetRotation;

uniform vec3 lightPos;
uniform vec3 lightColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat4 rotation;

uniform int curvedWorld;
uniform float worldCurve;
uniform vec3 playerPosition;


float WorldCurvature(vec2 pos){
    return dot(pos, pos) / worldCurve;
}

void main() {
//    float distance = sqrt(abs(pow(position.x - playerPosition.x, 2.0) + pow(position.y - playerPosition.y, 2.0) + pow(position.z - playerPosition.z, 2.0)));
//    vec3 pos = position;
//    pos.y += tan(3.0) * distance;
//    pos.x += tan(3.0) * distance;

    vec4 vertexPos = projection * view * model * vec4(position, 1.0);

    gl_Position = vertexPos;

    textureCoordinates = uv;
    Normal = normals;
    FragPos = vec3(model * vec4(position, 1.0));
    LightPos = lightPos;
    planetRotation = rotation;
}
