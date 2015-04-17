#version 330

in vec2 texCoords0;
in vec4 baseColor;

uniform sampler2D diffuse;
uniform float time; // 1 second = 1f
uniform vec2 screenSize;

out vec4 color;

const int iterations = 15;
const float TAU = 6.2831853071f;

void main() {
    float angle = mod(time/5f, TAU);
    float intensity = 2f;

    vec4 finalColor = vec4(0,0,0,1);
    float xoffset = cos(angle);
    float yoffset = sin(angle);
    for(int i = 0;i<iterations;i++) {
        float len = i*intensity;
        float indexf = i;
        float iterationsf = iterations;
        float avancment = indexf / iterationsf;
        float nx = len * xoffset * (1f/screenSize.x) + texCoords0.x;
        float ny = len * yoffset * (1f/screenSize.y) + texCoords0.y;
        vec2 coords = vec2(nx, ny);
        vec4 lookup = texture2D(diffuse, coords);
        finalColor = mix(finalColor, lookup, 1f-avancment);
    }
    color = finalColor * baseColor;
}