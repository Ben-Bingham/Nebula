#version 330 core

in vec2 textureCoordinates;

in vec3 FragPos;
in vec3 Normal;
in vec3 LightPos;
in vec3 LightColor;

out vec4 FragColor;

uniform sampler2D texture1;

void main() {
    // Ambient
    float ambientValue = 1;
    vec3 ambient = ambientValue * texture(texture1, textureCoordinates).rgb;

    vec3 normalVector = normalize(Normal);

    vec3 lightDirection = vec3( 128f, -256f, 0f);
    lightDirection = normalize(lightDirection);

    float diff = max(dot(normalVector, lightDirection), 0.0);
    float diffuseAmount = 0.3;

    vec3 diffuse = vec3(diffuseAmount, diffuseAmount, diffuseAmount) * diff;

    // Everything Together
    vec3 result = ambient + diffuse;
    FragColor = vec4(result, 1.0);
}
