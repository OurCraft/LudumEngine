#version 330

in vec2 texCoords0;
in vec4 baseColor;

uniform sampler2D diffuse;
uniform float time; // 1 second = 1f
uniform vec2 screenSize;

out vec4 color;

const int iterations = 5;
const float TAU = 6.2831853071f;

void main() {
    float angle = mod(time, TAU);
    float intensity = 1f;

    vec4 finalColor = vec4(0,0,0,1);
    for(int i = 0;i<iterations;i++) {

    }
    color = texture2D(diffuse, texCoords0) * baseColor;
}