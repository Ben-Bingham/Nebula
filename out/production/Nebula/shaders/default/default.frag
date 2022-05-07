#version 330 core

in vec2 textureCoordinates;

in vec3 FragPos;
in vec3 Normal;
in vec3 LightPos;
in vec3 LightColor;
in mat4 planetRotation;

out vec4 FragColor;

uniform sampler2D texture1;

void main() {
    // Ambient
    float ambientValue = 0.8; //default is 0.8
    vec3 ambient = ambientValue * texture(texture1, textureCoordinates).rgb;

    vec3 normalVector = normalize(Normal);

//    vec3 lightDirection = vec3( 128f, -256f, 0f);
//    lightDirection = (vec4(lightDirection, 1.0) * planetRotation).xyz;
    vec3 lightDirection = normalize(LightPos - FragPos);
    lightDirection = (vec4(lightDirection, 1.0) * planetRotation).xyz;
//    lightDirection = normalize(lightDirection);

    float diff = max(dot(normalVector, lightDirection), 0.0);
    float diffuseAmount = 0.3; //default is 0.3

    vec3 diffuse = vec3(diffuseAmount, diffuseAmount, diffuseAmount) * diff;

    // Everything Together
    vec3 result = ambient + diffuse;
    FragColor = vec4(result, 1.0);
}
